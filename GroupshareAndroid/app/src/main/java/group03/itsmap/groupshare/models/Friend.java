package group03.itsmap.groupshare.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Friend implements Parcelable {

    private long facebookId;
    private String name;
    private String pictureUrl;

    public Friend() {

    }

    public Friend(long facebookId, String name, String pictureUrl) {
        this.facebookId = facebookId;
        this.name = name;
        this.pictureUrl = pictureUrl;
    }

    public long getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(long facebookId) {
        this.facebookId = facebookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Friend(Parcel parcel) {
        name = parcel.readString();
        facebookId = parcel.readLong();
        pictureUrl = parcel.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(facebookId);
        dest.writeString(pictureUrl);
    }

    public static Creator<Friend> CREATOR = new Creator<Friend>() {

        @Override
        public Friend createFromParcel(Parcel source) {
            return new Friend(source);
        }

        @Override
        public Friend[] newArray(int size) {
            return new Friend[size];
        }

    };
}
