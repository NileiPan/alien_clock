package com.example.alien.system.Entity;

import java.util.ArrayList;
import java.util.Map;

public class AlienClock {
    private long year;
    private long month;
    private long day;
    private long hour;
    private long minute;
    private long second;
    private final int[] daysInMonth = new int[]{
            44, 42, 48, 40, 48, 44, 40, 44, 42, 40, 40, 42, 44, 48, 42, 40, 44, 38
    };
    private final int daysInYear = 770;

    public AlienClock(long year, long month, long day, long hour, long minute, long second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;

    }

    public long getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public long getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public long getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public long getHour() {
        return hour;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }
    public long getMinute() {
        return minute;
    }
    public void setMinute(int minute) {
        this.minute = minute;
    }
    public long getSecond() {
        return second;
    }
    public void setSecond(int second) {
        this.second = second;
    }

    public long calculateSeconds(AlienClock time){
        long totalSeconds = 0;
        totalSeconds += time.getYear() * 770 * 36 * 90 * 90;
        for(int i = 0; i < time.getMonth() - 1; i++){
            totalSeconds += (long) time.daysInMonth[i] * 36 * 90 * 90;
        }
        totalSeconds += (time.getDay()) * 36 * 90 * 90;
        totalSeconds += time.getHour() * 90 * 90;
        totalSeconds += time.getMinute() * 90;
        totalSeconds += time.getSecond();
        return totalSeconds;
    }
    public long getSeconds(){
        return this.calculateSeconds(this);
    }

    public long getSecondsSinceEpoch(){
        return this.calculateSeconds(this) - this.calculateSeconds(new AlienClock(2804, 18, 31, 2, 2, 88));
    }

    public void incrementSecond(){
        this.second++;
        if(this.second >= 90){
            this.second = 0;
            this.incrementMinute();
        }
    }
    public void decrementSecond(){
        this.second--;
        if(this.second < 0){
            this.second = 89;
            this.decrementMinute();
        }
    }
    public void incrementMinute(){
        this.minute++;
        if(this.minute >= 90){
            this.minute = 0;
            this.incrementHour();
        }
    }
    public void decrementMinute(){
        this.minute--;
        if(this.minute < 0){
            this.minute = 89;
            this.decrementHour();
        }
    }
    public void incrementHour(){
        this.hour++;
        if(this.hour >= 36){
            this.hour = 0;
            this.incrementDay();
        }
    }
    public void decrementHour(){
        this.hour--;
        if(this.hour < 0){
            this.hour = 35;
            this.decrementDay();
        }
    }
    public void incrementDay(){
        this.day++;
        if(this.day >= this.daysInMonth[(int)this.month-1]){
            this.day = 1;
            this.incrementMonth();
        }
    }
    public void decrementDay(){
        this.day--;
        if(this.day < 1){
            this.day = this.daysInMonth[(int)this.month-1];
            this.decrementMonth();
        }
    }
    public void incrementMonth(){
        this.month++;
        if(this.month >= 18){
            this.month = 1;
            this.incrementYear();
        }
    }
    public void decrementMonth(){
        this.month--;
        if(this.month < 1){
            this.month = 17;
            this.decrementYear();
        }
    }
    public void incrementYear(){
        this.year++;
    }
    public void decrementYear(){
        this.year--;
    }


    public void addSeconds(long alienSeconds){
        long calculateSeconds = getSeconds();
        long totalSeconds = calculateSeconds + alienSeconds;
        this.second = totalSeconds % 90;
        totalSeconds /= 90;
        this.minute = totalSeconds % 90;
        totalSeconds /= 90;
        this.hour = totalSeconds % 36;
        totalSeconds /= 36;

        this.year = totalSeconds / this.daysInYear;
        long day = totalSeconds % this.daysInYear;
        int month = 1;
        while(day >= this.daysInMonth[month-1]){
            day -= this.daysInMonth[month-1];
            month++;
        }

        this.month = month;
        this.day = day;
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "AlienClock{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", hour=" + hour +
                ", minute=" + minute +
                ", second=" + second +
                '}';
    }

    public Map<String, Long> getAlienTime(){
        return Map.of("year", year, "month", month, "day", day, "hour", hour, "minute", minute, "second", second);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AlienClock){
            AlienClock other = (AlienClock) obj;
            return this.year == other.year && this.month == other.month && this.day == other.day && this.hour == other.hour && this.minute == other.minute && this.second == other.second;
        }
        return false;
    }
}
