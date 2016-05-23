package group03.itsmap.groupshare.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import group03.itsmap.groupshare.R;
import group03.itsmap.groupshare.models.ToDoItem;

public class ToDoListFragmentAdapter extends ArrayAdapter<ToDoItem> {

    public ToDoListFragmentAdapter(Context context, int resource) {
        super(context, resource);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.todo_list_item, null);
        }

        final ToDoItem item = getItem(position);

        if (item != null) {
            final TextView descriptionTextView = (TextView) v.findViewById(R.id.todo_item_description);
            if (descriptionTextView != null) {
                descriptionTextView.setText(item.getDescription());
                if (item.getChecked()) {
                    descriptionTextView.setPaintFlags(descriptionTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    descriptionTextView.setPaintFlags(descriptionTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
                final CheckBox itemChecked = (CheckBox) v.findViewById(R.id.todo_item_checkBox);
                if (itemChecked != null) {
                    itemChecked.setChecked(item.getChecked());
                    itemChecked.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (itemChecked.isChecked()) {
                                descriptionTextView.setPaintFlags(descriptionTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                item.setChecked(true);
                                notifyDataSetChanged();
                            } else {
                                descriptionTextView.setPaintFlags(descriptionTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                                item.setChecked(false);
                                notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        }

        return v;
    }
}