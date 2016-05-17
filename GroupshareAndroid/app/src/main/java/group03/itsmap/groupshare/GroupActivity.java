package group03.itsmap.groupshare;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import group03.itsmap.groupshare.model.Group;

public class GroupActivity extends AppCompatActivity {

    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Toolbar groupToolbar = (Toolbar) findViewById(R.id.group_toolbar);
        group = (Group) getIntent().getSerializableExtra("group");
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
}
