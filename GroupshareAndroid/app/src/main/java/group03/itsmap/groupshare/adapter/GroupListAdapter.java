package group03.itsmap.groupshare.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import group03.itsmap.groupshare.R;
import group03.itsmap.groupshare.model.Group;

public class GroupListAdapter extends ArrayAdapter<Group> {
    public GroupListAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.group_list_row, null);
        }

        Group group = getItem(position);

        if (group != null) {
            Button groupButton = (Button) v.findViewById(R.id.group_list_button);
            if (groupButton != null) {
                groupButton.setText(group.getName());
                // Alternating colors
//                if ((position % 2) == 0) {
//                    groupButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.groupListEven));
//                } else {
//                    groupButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.groupListOdd));
//                }

            }
        }
        return v;
    }
}
