package group03.itsmap.groupshare.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Group implements Serializable {

    private String name;
    private List<Friend> friends;

    public Group() {
    }

    public Group(String name) {
        this.name = name;
        friends = new ArrayList<>();
    }

    public Group(String name, Friend friend) {
        this.name = name;
        friends = new ArrayList<>();
        friends.add(friend);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }
}
