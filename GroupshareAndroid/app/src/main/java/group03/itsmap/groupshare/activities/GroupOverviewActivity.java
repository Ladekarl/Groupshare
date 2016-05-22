package group03.itsmap.groupshare.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

import group03.itsmap.groupshare.R;
import group03.itsmap.groupshare.adapters.GroupListAdapter;
import group03.itsmap.groupshare.models.Friend;
import group03.itsmap.groupshare.models.Group;
import group03.itsmap.groupshare.utils.FacebookUtil;

public class GroupOverviewActivity extends AppCompatActivity {

    private GridView groupListView;
    private GroupListAdapter groupListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_overview);

        groupListView = (GridView) findViewById(R.id.group_gridView);
        groupListAdapter = new GroupListAdapter(this, R.layout.group_list_row);
        groupListView.setAdapter(groupListAdapter);

        Toolbar groupOverviewToolbar = (Toolbar) findViewById(R.id.group_overview_toolbar);
        setSupportActionBar(groupOverviewToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_overview_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_group:
                createGroupCommand();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void createGroupCommand() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.group_name));

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setGravity(Gravity.CENTER);
        builder.setView(input);

        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String groupName = input.getText().toString();
                createGroup(groupName);
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void createGroup(final String groupName) {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        saveGroup(groupName, object);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void saveGroup(String groupName, JSONObject object) {
        Friend groupMaker = null;
        try {
            groupMaker = FacebookUtil.jsonObjectToFriend(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (groupMaker != null) {
            // TODO ADD GROUP TO SERVER
            groupListAdapter.add(new Group(groupName, groupMaker));
        } else {
            Toast.makeText(GroupOverviewActivity.this, R.string.save_group_error, Toast.LENGTH_SHORT).show();
        }

    }
}
