package group03.itsmap.groupshare.utils;


import android.content.Context;
import android.net.ConnectivityManager;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import org.json.JSONException;
import org.json.JSONObject;

import group03.itsmap.groupshare.models.Friend;

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
}
