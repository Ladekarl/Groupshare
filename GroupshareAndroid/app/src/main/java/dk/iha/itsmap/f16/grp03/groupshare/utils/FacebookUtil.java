package dk.iha.itsmap.f16.grp03.groupshare.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import dk.iha.itsmap.f16.grp03.groupshare.models.Friend;

public class FacebookUtil {
    public static Friend jsonObjectToFriend(JSONObject object) throws JSONException {
        Friend friend = new Friend();
        friend.setFacebookId(Long.valueOf((String) object.get("id")));
        friend.setName((String) object.get("name"));
        JSONObject picture = (JSONObject) object.get("picture");
        JSONObject data = (JSONObject) picture.get("data");
        friend.setPictureUrl((String) data.get("url"));
        return friend;
    }

    // Based on: http://stackoverflow.com/questions/9570237/android-check-internet-connection
    public static boolean isNetworkAvailable(final Context context) {
        initialiseFacebookSdk(context);
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static String getFacebookUserId(final Context context) {
        initialiseFacebookSdk(context);

        return AccessToken.getCurrentAccessToken().getUserId();
    }

    public static AccessToken getFacebookAccessToken(final Context context) {
        initialiseFacebookSdk(context);
        return AccessToken.getCurrentAccessToken();
    }

    public static void initialiseFacebookSdk(final Context context) {
        if (FacebookSdk.isInitialized()) return;
        FacebookSdk.sdkInitialize(context);
    }


    // Getting Bitmap from URL based on: http://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
    public static class DownloadFacebookPicture extends AsyncTask<String, Void, Bitmap> {
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

    public static class UserInfo {
        private static long id;
        private static String name;
        private static String pictureUrl;

        public static long getId() {
            return id;
        }

        public static void setId(long id) {
            UserInfo.id = id;
        }

        public static String getName() {
            return name;
        }

        public static void setName(String name) {
            UserInfo.name = name;
        }

        public static String getPictureUrl() {
            return pictureUrl;
        }

        public static void setPictureUrl(String pictureUrl) {
            UserInfo.pictureUrl = pictureUrl;
        }
    }
}
