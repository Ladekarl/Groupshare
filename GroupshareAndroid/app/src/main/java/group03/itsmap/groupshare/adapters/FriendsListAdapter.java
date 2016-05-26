package group03.itsmap.groupshare.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import group03.itsmap.groupshare.R;
import group03.itsmap.groupshare.models.Friend;
import group03.itsmap.groupshare.utils.FacebookUtil;

public class FriendsListAdapter extends ArrayAdapter<Friend> {

    public FriendsListAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.friends_list_item, null);
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Empty
            }
        });

        // CircleImageView from https://github.com/vinc3m1/RoundedImageView
        CircleImageView friendImage = (CircleImageView) v.findViewById(R.id.friends_list_image);
        TextView friendName = (TextView) v.findViewById(R.id.friends_list_name);

        final Friend friend = getItem(position);

        if (friendName != null) {
            String firstName = friend.getName().split("\\s+")[0];
            if (firstName != null) {
                friendName.setText(firstName);
            }
        }

        if (friendImage != null) {
            new FacebookUtil.DownloadFacebookPicture(friendImage).execute(friend.getPictureUrl());
        }
        return v;
    }
}
