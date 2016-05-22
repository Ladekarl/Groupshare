package group03.itsmap.groupshare;

import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
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
import android.widget.CompoundButton;
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

import group03.itsmap.groupshare.adapter.InviteFriendsAdapter;
import group03.itsmap.groupshare.model.Friend;
import group03.itsmap.groupshare.model.Group;
import group03.itsmap.groupshare.utils.FacebookUtil;
import group03.itsmap.groupshare.utils.IntentKey;

public class GroupActivity extends AppCompatActivity {

    private Group group;
    private List<Friend> friendsToBeInvited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Toolbar groupToolbar = (Toolbar) findViewById(R.id.group_toolbar);
        group = (Group) getIntent().getSerializableExtra(IntentKey.GroupActivityIntent);
        friendsToBeInvited = new ArrayList<>();
        setSupportActionBar(groupToolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(true);
            if (group != null) {
                supportActionBar.setTitle(group.getName());
            }
        }

        if (groupToolbar != null) {
            groupToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        // TODO: Create Calendar and Todo for chosen group
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
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void inviteFriends() {
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
}
