package group03.itsmap.groupshare;

import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
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
import group03.itsmap.groupshare.model.Group;
import group03.itsmap.groupshare.model.Friend;
import group03.itsmap.groupshare.utils.IntentKey;

public class GroupActivity extends AppCompatActivity {

    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Toolbar groupToolbar = (Toolbar) findViewById(R.id.group_toolbar);
        group = (Group) getIntent().getSerializableExtra(IntentKey.GroupActivityIntent);
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
                                Friend friend = new Friend();
                                friend.setFacebookId(Long.valueOf((String) object.get("id")));
                                friend.setName((String) object.get("name"));
                                JSONObject picture = (JSONObject) object.get("picture");
                                JSONObject data = (JSONObject) picture.get("data");
                                friend.setPictureUrl((String) data.get("url"));
                                friends.add(friend);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(GroupActivity.this)
                .setTitle(R.string.invite_friends)
                .setAdapter(adapter, null)
                .setPositiveButton("Invite friends", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();

        dialog.getListView().setAdapter(new ArrayAdapter<Friend>(this,
                R.layout.invite_friends, friends));
        dialog.getListView().setItemsCanFocus(false);
        dialog.getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        dialog.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(GroupActivity.this, "clicked " + position, Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();


    }
}
