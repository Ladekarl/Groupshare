package group03.itsmap.groupshare.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ToDoItem implements Parcelable {

    private String description;

    private Boolean isChecked;

    public ToDoItem() {
    }

    public ToDoItem(String description, boolean isChecked) {
        this.description = description;
        this.isChecked = isChecked;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ToDoItem(Parcel parcel) {
        description = parcel.readString();
        isChecked = (Boolean) parcel.readValue(getClass().getClassLoader());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeValue(isChecked);
    }

    public static Creator<ToDoItem> CREATOR = new Creator<ToDoItem>() {

        @Override
        public ToDoItem createFromParcel(Parcel source) {
            return new ToDoItem(source);
        }

        @Override
        public ToDoItem[] newArray(int size) {
            return new ToDoItem[size];
        }

    };
}
