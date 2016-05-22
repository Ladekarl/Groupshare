package group03.itsmap.groupshare.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import group03.itsmap.groupshare.R;
import group03.itsmap.groupshare.models.ToDoItem;

public class ToDoListActivityAdapter extends ArrayAdapter<ToDoItem> {

    public ToDoListActivityAdapter(Context context, int resource) {
        super(context, resource);
    }

    public ToDoListActivityAdapter(Context context, int resource, List<ToDoItem> items) {
        super(context, resource, items);
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {

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

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < getCount(); i++) {
                        parent.getChildAt(i).findViewById(R.id.todo_item_delete).setVisibility(View.INVISIBLE);
                    }
                    view.findViewById(R.id.todo_item_delete).setVisibility(View.VISIBLE);
                }
            });

            ImageButton removeButton = (ImageButton) v.findViewById(R.id.todo_item_delete);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ListView) parent).performItemClick(v, position, 0);
                }
            });
            removeButton.setTag(item);
        }

        return v;
    }
}