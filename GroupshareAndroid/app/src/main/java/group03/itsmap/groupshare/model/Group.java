package group03.itsmap.groupshare.model;

import java.io.Serializable;

public class Group implements Serializable {

    private String name;

    public Group() {
    }

    public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
