package group03.itsmap.groupshare.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ToDoList implements Parcelable {
    private long id;
    private String title;
    private ArrayList<ToDoItem> items;

    public ToDoList() {
    }

    public ToDoList(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public ToDoList(long id, String title, ArrayList<ToDoItem> items) {
        this.id = id;
        this.title = title;
        this.items = items;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<ToDoItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<ToDoItem> items) {
        this.items = items;
    }

    public ToDoList(Parcel parcel) {
        title = parcel.readString();
        id = parcel.readLong();
        items = new ArrayList<>();
        parcel.readTypedList(items, ToDoItem.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeLong(id);
        dest.writeTypedList(items);
    }

    public static Creator<ToDoList> CREATOR = new Creator<ToDoList>() {

        @Override
        public ToDoList createFromParcel(Parcel source) {
            return new ToDoList(source);
        }

        @Override
        public ToDoList[] newArray(int size) {
            return new ToDoList[size];
        }

    };

}
