package dk.iha.itsmap.f16.grp03.groupshare.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dk.iha.itsmap.f16.grp03.groupshare.R;
import dk.iha.itsmap.f16.grp03.groupshare.models.Friend;
import dk.iha.itsmap.f16.grp03.groupshare.utils.FacebookUtil;

public class InviteFriendsAdapter extends ArrayAdapter<Friend> {

    public InviteFriendsAdapter(Context context, int resource, List<Friend> friends) {
        super(context, resource);
        addAll(friends);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.invite_friends, null);
        }

        final Friend friend = getItem(position);

        if (friend != null) {

            ImageView pictureImageView = (ImageView) v.findViewById(R.id.invite_friends_picture);
            if (pictureImageView != null) {
                new FacebookUtil.DownloadFacebookPicture(pictureImageView).execute(friend.getPictureUrl());
            }

            TextView nameTextView = (TextView) v.findViewById(R.id.invite_friends_name);
            if (nameTextView != null) {
                nameTextView.setText(friend.getName());
            }
        }
        return v;
    }
}
