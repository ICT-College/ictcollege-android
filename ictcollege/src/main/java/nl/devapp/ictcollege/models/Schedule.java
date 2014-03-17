package nl.devapp.ictcollege.models;

public class Schedule {

    String lesson;
    String to;
    String classRoom;
    int hour;
    int weekDay;
    boolean isProgressbar = false;
    long max;
    long start;

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public boolean isProgressbar() {
        return isProgressbar;
    }

    public void setProgressbar(boolean progressbar){
        this.isProgressbar = progressbar;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public long getMax(){
        return max;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getStart(){
        return start;
    }
}