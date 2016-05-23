package group03.itsmap.groupshare.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import group03.itsmap.groupshare.R;
import group03.itsmap.groupshare.adapters.InviteFriendsAdapter;
import group03.itsmap.groupshare.fragments.ToDoFragment;
import group03.itsmap.groupshare.models.Friend;
import group03.itsmap.groupshare.models.Group;
import group03.itsmap.groupshare.models.ToDoList;
import group03.itsmap.groupshare.services.GroupService;
import group03.itsmap.groupshare.services.ToDoService;
import group03.itsmap.groupshare.utils.FacebookUtil;
import group03.itsmap.groupshare.utils.IntentKey;

public class GroupActivity extends AppCompatActivity {

    public final static String GROUP_KEY = "group03.itsmap.groupshare.activities.groupactivity.GroupId";
    public final static String TODOLIST_ID_KEY = "group03.itsmap.groupshare.activities.groupactivity.ToDoListId";
    private Boolean userDeletedGroup = false;
    private Group group;
    private List<Friend> friendsToBeInvited;
    private String userId;
    private ActionBar supportActionBar;
    private Long groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        userId = FacebookUtil.getFacebookUserId(getApplicationContext());
        Toolbar groupToolbar = (Toolbar) findViewById(R.id.group_toolbar);
        friendsToBeInvited = new ArrayList<>();
        setSupportActionBar(groupToolbar);

        groupId = getIntent().getLongExtra(IntentKey.GroupActivityIntent, 0);

        supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(true);
        }

        IntentFilter groupIntentFilter = new IntentFilter(
                GroupService.GET_SINGLE_GROUP_BROADCAST_INTENT + groupId + userId);

        GroupReceiver groupReceiver = new GroupReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                groupReceiver,
                groupIntentFilter);

        if (groupToolbar != null) {
            groupToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getGroupFromService();
    }

    @Override
    public void onPause() {
        if (!userDeletedGroup) {
            saveGroup();
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.invite_friends_menu_item:
                inviteFriends();
                break;
            case R.id.delete_group_menu_item:
                deleteGroup();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteGroup() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        GroupService.startActionDeleteSingleGroup(GroupActivity.this, group.getId(), userId);
                        for (ToDoList toDoList : group.getToDoLists()) {
                            ToDoService.startActionDeleteToDoList(GroupActivity.this, group.getId(), toDoList.getId(), userId);
                        }
                        userDeletedGroup = true;
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure)).setPositiveButton(getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getString(R.string.no), dialogClickListener).show();
    }

    private void getGroupFromService() {
        GroupService.startActionGetSingleGroup(this, groupId, userId);
    }

    private void saveGroup() {
        GroupService.startActionSaveSingleGroup(this, group, group.getId(), userId);
    }

    private void initGroupView() {
        if (isFinishing()) return;
        // ADDING TODOLIST TO GROUP Todo: Make this a manual decision
        Bundle bundle = new Bundle();
        if (group.getToDoLists().size() == 0) {
            ToDoList toDoList = new ToDoList(1, getString(R.string.todo_list_text));
            group.addToDoList(toDoList);
            ToDoService.startActionSaveToDoList(this, toDoList, group.getId(), toDoList.getId(), userId);
        }
        bundle.putParcelable(GROUP_KEY, group);
        bundle.putLong(TODOLIST_ID_KEY, group.getToDoLists().get(0).getId());
        ToDoFragment toDoFragment = new ToDoFragment();
        toDoFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.act_fragment_toDo, toDoFragment);
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
        // TODO: Create Calendar and Todo for chosen group
    }

    private void inviteFriends() {
        if (FacebookUtil.isNetworkAvailable(getApplicationContext())) {
            GraphRequest request = GraphRequest.newMyFriendsRequest(AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONArrayCallback() {
                        @Override
                        public void onCompleted(JSONArray objects, GraphResponse response) {
                            List<Friend> friends = new ArrayList<>();

                            for (int i = 0; i < objects.length(); i++) {
                                try {
                                    JSONObject object = objects.getJSONObject(i);
                                    friends.add(FacebookUtil.jsonObjectToFriend(object));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            showDialogForFriends(friends);
                        }
                    });
            Bundle params = new Bundle();
            params.putString("fields", "id, name, picture");
            request.setParameters(params);
            request.executeAsync();
        } else {
            Toast.makeText(GroupActivity.this, R.string.no_connection, Toast.LENGTH_SHORT).show();
        }
    }

    // Dialog based on http://stackoverflow.com/questions/10932832/multiple-choice-alertdialog-with-custom-adapter
    private void showDialogForFriends(List<Friend> friends) {

        ArrayAdapter adapter = new InviteFriendsAdapter(GroupActivity.this, R.layout.invite_friends, friends);
        AlertDialog dialog = new AlertDialog.Builder(GroupActivity.this)
                .setTitle(R.string.invite_friends)
                .setAdapter(adapter, null)
                .setPositiveButton("Invite friends", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        group.addFriends(friendsToBeInvited);

                        if (friendsToBeInvited.size() >= 1) {
                            Toast.makeText(GroupActivity.this, R.string.invite_friends_success, Toast.LENGTH_SHORT).show();
                        }

                        friendsToBeInvited.clear();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("LOG", "cancel");
                        friendsToBeInvited.clear();
                    }
                })
                .create();

        ListView listView = dialog.getListView();
        listView.setAdapter(new ArrayAdapter<>(this, R.layout.invite_friends, friends));
        listView.setItemsCanFocus(false);

        // Setting divider with gradient. Based on: http://stackoverflow.com/questions/2372415/how-to-change-color-of-android-listview-separator-line
        int[] colors = {0, 0xFF000000, 0};
        listView.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        listView.setDividerHeight(1);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setItemsCanFocus(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friend f = (Friend) parent.getItemAtPosition(position);
                CheckBox box = (CheckBox) parent.findViewById(R.id.invite_friends_checkbox);

                if (box != null) {
                    if (box.isChecked()) {
                        friendsToBeInvited.remove(f);
                        box.setChecked(false);
                    } else {
                        friendsToBeInvited.add(f);
                        box.setChecked(true);
                    }
                }
            }
        });
        dialog.show();
    }

    private class GroupReceiver extends BroadcastReceiver {
        private GroupReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            Group retrievedGroup = intent.getParcelableExtra(GroupService.EXTRA_SINGLE_GROUP);
            if (retrievedGroup == null) return;
            group = retrievedGroup;
            supportActionBar.setTitle(group.getName());
            initGroupView();
        }
    }
}
