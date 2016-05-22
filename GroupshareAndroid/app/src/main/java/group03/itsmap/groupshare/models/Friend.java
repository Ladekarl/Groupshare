package group03.itsmap.groupshare.models;

public class Friend {

    private long facebookId;
    private String Name;
    private String pictureUrl;

    public Friend() {

    }

    public Friend(long facebookId, String name, String pictureUrl) {
        this.facebookId = facebookId;
        Name = name;
        this.pictureUrl = pictureUrl;
    }

    public long getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(long facebookId) {
        this.facebookId = facebookId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
