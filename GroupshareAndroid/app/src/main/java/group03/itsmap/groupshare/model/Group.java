package group03.itsmap.groupshare.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Group implements Serializable {

    private String name;
    private List<Long> members;

    public Group() {
    }

    public Group(String name) {
        this.name = name;
        members = new ArrayList<>();
    }

    public Group(String name, long member) {
        this.name = name;
        members = new ArrayList<>();
        members.add(member);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getMembers() {
        return members;
    }

    public void setMembers(List<Long> members) {
        this.members = members;
    }
}
