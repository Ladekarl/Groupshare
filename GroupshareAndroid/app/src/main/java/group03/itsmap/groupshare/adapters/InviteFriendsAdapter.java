package group03.itsmap.groupshare.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

import group03.itsmap.groupshare.R;
import group03.itsmap.groupshare.models.Friend;
import group03.itsmap.groupshare.utils.FacebookUtil;

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

            final CheckBox checkBox = (CheckBox) v.findViewById(R.id.invite_friends_checkbox);

            if (checkBox != null) {
               /* checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Toast.makeText(getContext(), "Checked:" + isChecked, Toast.LENGTH_SHORT).show();
                    }
                });

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkBox.setChecked(!checkBox.isChecked());
                    }
                });*/
            }
        }
        return v;
    }
}
