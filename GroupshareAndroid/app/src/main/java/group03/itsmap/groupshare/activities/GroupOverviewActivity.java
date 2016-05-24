package group03.itsmap.groupshare.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import group03.itsmap.groupshare.R;
import group03.itsmap.groupshare.adapters.GroupListAdapter;
import group03.itsmap.groupshare.models.Group;
import group03.itsmap.groupshare.services.GroupService;
import group03.itsmap.groupshare.utils.FacebookUtil;

public class GroupOverviewActivity extends AppCompatActivity {

    private GroupListAdapter groupListAdapter;
    private ArrayList<Group> groupList;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_overview);
        userId = FacebookUtil.getFacebookUserId(getApplicationContext());
        groupList = new ArrayList<>();
        GridView groupListView = (GridView) findViewById(R.id.group_gridView);
        groupListAdapter = new GroupListAdapter(this, R.layout.group_list_row);
        if (groupListView != null) {
            groupListView.setAdapter(groupListAdapter);
        }

        Toolbar groupOverviewToolbar = (Toolbar) findViewById(R.id.group_overview_toolbar);
        setSupportActionBar(groupOverviewToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        IntentFilter groupIntentFilter = new IntentFilter(
                GroupService.GET_GROUPS_BROADCAST_INTENT + userId);

        GroupReceiver groupReceiver = new GroupReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                groupReceiver,
                groupIntentFilter);
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

    @Override
    public void onStart() {
        super.onStart();
        getGroupsFromService();
    }

    @Override
    public void onPause() {
        saveGroups();
        super.onPause();
    }


    private void saveGroups() {
        GroupService.startActionSaveGroups(this, groupList, userId);
    }

    private void getGroupsFromService() {
        GroupService.startActionGetGroups(this, userId);
    }


    private void createGroupCommand() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.GroupshareTheme_AlertDialog);
        builder.setTitle(getString(R.string.group_name));

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setGravity(Gravity.CENTER);
        builder.setView(input);

        builder.setPositiveButton(getString(R.string.create_group), new DialogInterface.OnClickListener() {
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
        long largestId = 0;
        if (groupList != null) {
            for (Group group : groupList) {
                if (group.getId() > largestId) {
                    largestId = group.getId();
                }
            }
        }
        Group newGroup = new Group(largestId + 1, groupName);
        groupList.add(newGroup);
        refreshAdapter();
    }

    private class GroupReceiver extends BroadcastReceiver {
        private GroupReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            ArrayList<Group> groups = intent.getParcelableArrayListExtra(GroupService.EXTRA_GROUPS);
            if (groups == null) return;
            if (groupList.containsAll(groups)) return;
            groupList = groups;
            if (groupListAdapter == null) return;
            refreshAdapter();
        }
    }

    private void refreshAdapter() {
        groupListAdapter.clear();
        Collections.sort(groupList, new Comparator<Group>() {
            @Override
            public int compare(Group lhs, Group rhs) {
                return (int) (lhs.getId() - rhs.getId());
            }
        });
        groupListAdapter.addAll(groupList);
        groupListAdapter.notifyDataSetChanged();
    }
}
