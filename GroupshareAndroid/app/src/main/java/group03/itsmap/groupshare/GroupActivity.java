package group03.itsmap.groupshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import group03.itsmap.groupshare.model.Group;

public class GroupActivity extends AppCompatActivity {

    private TextView textView;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        textView = (TextView) findViewById(R.id.textView);

        group = (Group) getIntent().getSerializableExtra("group");

        if (group != null) {
            textView.setText("Group name: " + group.getName());
        }

        // TODO: Create Calendar and Todo for chosen group
    }
}
