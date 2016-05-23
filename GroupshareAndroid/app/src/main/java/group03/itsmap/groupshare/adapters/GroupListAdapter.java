package group03.itsmap.groupshare.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import group03.itsmap.groupshare.activities.GroupActivity;
import group03.itsmap.groupshare.R;
import group03.itsmap.groupshare.models.Group;
import group03.itsmap.groupshare.utils.IntentKey;

public class GroupListAdapter extends ArrayAdapter<Group> {

    private List<Drawable> colorList;

    public GroupListAdapter(Context context, int resource) {
        super(context, resource);
        initColorList();
    }

    private void initColorList() {
        colorList = new ArrayList<>();
        colorList.add(ContextCompat.getDrawable(getContext(), R.drawable.group_1));
        colorList.add(ContextCompat.getDrawable(getContext(), R.drawable.group_2));
        colorList.add(ContextCompat.getDrawable(getContext(), R.drawable.group_3));
        colorList.add(ContextCompat.getDrawable(getContext(), R.drawable.group_4));
        colorList.add(ContextCompat.getDrawable(getContext(), R.drawable.group_5));
        colorList.add(ContextCompat.getDrawable(getContext(), R.drawable.group_6));
        colorList.add(ContextCompat.getDrawable(getContext(), R.drawable.group_7));
        colorList.add(ContextCompat.getDrawable(getContext(), R.drawable.group_8));
        colorList.add(ContextCompat.getDrawable(getContext(), R.drawable.group_9));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.group_list_row, null);
        }

        final Group group = getItem(position);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackground(colorList.get((position % (colorList.size()))));
        } else {
            v.setBackgroundDrawable(colorList.get((position % (colorList.size()))));
        }

        if (group != null) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), GroupActivity.class);
                    // TODO: Create class to for shared Intent keys
                    intent.putExtra(IntentKey.GroupActivityIntent, group);
                    v.getContext().startActivity(intent);
                }
            });

            TextView nameTextView = (TextView) v.findViewById(R.id.group_row_name);
            if (nameTextView != null) {
                nameTextView.setText(group.getName());
            }
        }
        return v;
    }
}
