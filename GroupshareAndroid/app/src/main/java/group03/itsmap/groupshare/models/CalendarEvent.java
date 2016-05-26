package group03.itsmap.groupshare.models;


import android.os.Parcel;
import android.os.Parcelable;

public class CalendarEvent implements Parcelable {
    private Long id;
    private String name, location;
    private int startYear, startMonth, startDay, startHour, startMinute, endYear, endMonth, endDay, endHour, endMinute;
    private int color;

    public CalendarEvent() {

    }

    public CalendarEvent(Long id, String name, String location, int startYear, int startMonth, int startDay, int startHour, int startMinute, int endYear, int endMonth, int endDay, int endHour, int endMinute, int color) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.startYear = startYear;
        this.startMonth = startMonth;
        this.startDay = startDay;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endYear = endYear;
        this.endMonth = endMonth;
        this.endDay = endDay;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.color = color;
    }

    public CalendarEvent(Parcel parcel) {
        id = parcel.readLong();
        name = parcel.readString();
        location = parcel.readString();
        startYear = parcel.readInt();
        startMonth = parcel.readInt();
        startDay = parcel.readInt();
        startHour = parcel.readInt();
        startMinute = parcel.readInt();
        endYear = parcel.readInt();
        endMonth = parcel.readInt();
        endDay = parcel.readInt();
        endHour = parcel.readInt();
        endMinute = parcel.readInt();
        color = parcel.readInt();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    public int getStartDay() {
        return startDay;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }

    public int getEndDay() {
        return endDay;
    }

    public void setEndDay(int endDay) {
        this.endDay = endDay;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);

        dest.writeString(name);
        dest.writeString(location);
        dest.writeInt(startYear);
        dest.writeInt(startMonth);
        dest.writeInt(startDay);
        dest.writeInt(startHour);
        dest.writeInt(startMinute);
        dest.writeInt(endYear);
        dest.writeInt(endMonth);
        dest.writeInt(endDay);
        dest.writeInt(endHour);
        dest.writeInt(endMinute);
        dest.writeInt(color);
    }

    public static Creator<CalendarEvent> CREATOR = new Creator<CalendarEvent>() {
        @Override
        public CalendarEvent createFromParcel(Parcel source) {
            return new CalendarEvent(source);
        }

        @Override
        public CalendarEvent[] newArray(int size) {
            return new CalendarEvent[0];
        }
    };
}
