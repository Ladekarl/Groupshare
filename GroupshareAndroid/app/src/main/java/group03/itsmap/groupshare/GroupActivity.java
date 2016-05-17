package group03.itsmap.groupshare;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import group03.itsmap.groupshare.model.Group;

public class GroupActivity extends AppCompatActivity {

    private TextView textView;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        textView = (TextView) findViewById(R.id.group_name_textView);
        Toolbar groupToolbar = (Toolbar) findViewById(R.id.group_toolbar);
        group = (Group) getIntent().getSerializableExtra("group");
        setSupportActionBar(groupToolbar);
        if (group != null) {
            textView.setText("Group name: " + group.getName());
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            if (group != null) {
                getSupportActionBar().setTitle(group.getName());
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
}
