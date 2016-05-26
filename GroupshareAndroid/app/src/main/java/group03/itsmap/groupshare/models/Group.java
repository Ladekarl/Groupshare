package group03.itsmap.groupshare.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Group implements Parcelable {

    private String name;
    private ArrayList<Friend> friends;
    private ArrayList<ToDoList> toDoLists;
    private long id;
    private ArrayList<GroupShareCalendar> calendars;

    public Group() {
    }

    public Group(long id, String name) {
        this.id = id;
        this.name = name;
        friends = new ArrayList<>();
        toDoLists = new ArrayList<>();
    }

    public Group(long id, String name, ArrayList<Friend> friends) {
        this(id, name);
        this.friends = friends;
    }

    public Group(long id, String name, ArrayList<Friend> friends, ArrayList<ToDoList> toDoLists) {
        this(id, name, friends);
        this.toDoLists = toDoLists;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Friend> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
    }

    public void addFriends(List<Friend> friendsToBeAdded) {
        this.friends.addAll(friendsToBeAdded);
    }

    public List<ToDoList> getToDoLists() {
        return toDoLists;
    }

    public void setToDoLists(ArrayList<ToDoList> toDoLists) {
        this.toDoLists = toDoLists;
    }

    public void addToDoList(ToDoList toDoList) {
        this.toDoLists.add(toDoList);
    }

    public ArrayList<GroupShareCalendar> getCalendars() {
        return calendars;
    }

    public void setCalendars(ArrayList<GroupShareCalendar> calendars) {
        this.calendars = calendars;
    }

    public Group(Parcel parcel) {
        name = parcel.readString();
        id = parcel.readLong();
        friends = new ArrayList<>();
        toDoLists = new ArrayList<>();
        calendars = new ArrayList<>();
        parcel.readTypedList(friends, Friend.CREATOR);
        parcel.readTypedList(toDoLists, ToDoList.CREATOR);
        parcel.readTypedList(calendars, GroupShareCalendar.CREATOR);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(id);
        dest.writeTypedList(friends);
        dest.writeTypedList(toDoLists);
        dest.writeTypedList(calendars);
    }

    public static Creator<Group> CREATOR = new Creator<Group>() {

        @Override
        public Group createFromParcel(Parcel source) {
            return new Group(source);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }

    };
}
