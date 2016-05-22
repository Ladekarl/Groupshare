package group03.itsmap.groupshare.utils;


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
}
