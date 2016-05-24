package group03.itsmap.groupshare.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

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

        ImageView friendImage = (ImageView) v.findViewById(R.id.friends_list_image);

        final Friend friend = getItem(position);

        if (friendImage != null) {
            new FacebookUtil.DownloadFacebookPicture(friendImage).execute(friend.getPictureUrl());
        }
        return v;
    }
}
