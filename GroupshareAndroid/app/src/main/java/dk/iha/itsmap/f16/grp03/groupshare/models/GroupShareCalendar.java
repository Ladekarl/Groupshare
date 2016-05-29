package dk.iha.itsmap.f16.grp03.groupshare.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class GroupShareCalendar implements Parcelable {

    private long id;
    private String title;
    private ArrayList<CalendarEvent> calendarEvents;

    public GroupShareCalendar(long id, String title, ArrayList<CalendarEvent> calendarEvents) {
        this.id = id;
        this.title = title;
        this.calendarEvents = calendarEvents;
    }

    public GroupShareCalendar(Parcel parcel) {
        calendarEvents = new ArrayList<>();
        id = parcel.readLong();
        title = parcel.readString();
        parcel.readTypedList(calendarEvents, CalendarEvent.CREATOR);
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

    public List<CalendarEvent> getCalendarEvents() {
        return calendarEvents;
    }

    public void setCalendarEvents(ArrayList<CalendarEvent> calendarEvents) {
        this.calendarEvents = calendarEvents;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeTypedList(calendarEvents);
    }

    public static Creator<GroupShareCalendar> CREATOR = new Creator<GroupShareCalendar>() {
        @Override
        public GroupShareCalendar createFromParcel(Parcel source) {
            return new GroupShareCalendar(source);
        }

        @Override
        public GroupShareCalendar[] newArray(int size) {
            return new GroupShareCalendar[0];
        }
    };
}
