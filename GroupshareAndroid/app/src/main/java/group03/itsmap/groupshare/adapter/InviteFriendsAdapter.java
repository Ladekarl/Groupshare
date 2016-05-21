package group03.itsmap.groupshare.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.List;

import group03.itsmap.groupshare.R;
import group03.itsmap.groupshare.model.Friend;

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
                new DownloadFacebookPicture(pictureImageView).execute(friend.getPictureUrl());
            }

            TextView nameTextView = (TextView) v.findViewById(R.id.invite_friends_name);
            if (nameTextView != null) {
                nameTextView.setText(friend.getName());
            }

            final CheckBox checkBox =  (CheckBox) v.findViewById(R.id.invite_friends_checkbox);

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

    // Getting Bitmap from URL based on: http://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
    private class DownloadFacebookPicture extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadFacebookPicture(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
