package com.myitsolver.baseandroidapp.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Peter on 2016. 08. 17..
 */
public class OpeningHoursParser {

    private static final String OPEN_ALL_DAY = "OPEN";
    private static final String ClOSED_ALL_DAY = "CLOSED";
    private String openingHours;

    public OpeningHoursParser(String openingHours) {
        this.openingHours = openingHours;
    }

    public OpeningHoursParser(){

    }

    public OneDayOpeningHours getToday() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Budapest"));
        cal.setTimeInMillis(System.currentTimeMillis());

        int dayNumber = cal.get(Calendar.DAY_OF_WEEK);
        if (dayNumber == 1){
            dayNumber = 6;
        }else{
            dayNumber -= 2;
        }
        return getDay(dayNumber);
    }

    /**
     * zero based
     * first day: Monday
     */
    public OneDayOpeningHours getDay(int dayNumber){
        if (openingHours == null) {
            return null;
        }
        String[] days = openingHours.split("!");
        String day = days[dayNumber];

        OneDayOpeningHours oneDayOpeningHours = new OneDayOpeningHours();
        if (day.equals(ClOSED_ALL_DAY)){
            oneDayOpeningHours.setClosedAllDay(true);
            return oneDayOpeningHours;
        }
        if (day.equals(OPEN_ALL_DAY)){
            oneDayOpeningHours.setOpenAllDay(true);
            return oneDayOpeningHours;
        }

        String[] hours = day.split(":");
        oneDayOpeningHours.setStartHour(Integer.parseInt(hours[0]));
        oneDayOpeningHours.setStartMinute(Integer.parseInt(hours[1]));
        oneDayOpeningHours.setEndHour(Integer.parseInt(hours[2]));
        oneDayOpeningHours.setEndMinute(Integer.parseInt(hours[3]));

        return oneDayOpeningHours;
    }

    public List<OneDayOpeningHours> getAllDays(){
        List<OneDayOpeningHours> oneDayOpeningHoursList = new ArrayList<OneDayOpeningHours>();
        for (int  i=0; i<7; i++){
            oneDayOpeningHoursList.add(getDay(i));
        }
        return oneDayOpeningHoursList;
    }

    /**
     * zero based
     * first day: Monday
     * only if a correct formatted openingHours string is set
     */
    public void setDay(int dayNumber,OneDayOpeningHours oneDayOpeningHours){
        String[] days = openingHours.split("!");
        days[dayNumber] = getDayString(oneDayOpeningHours);
        buildString(days);
    }

    private String getDayString(OneDayOpeningHours oneDayOpeningHours){
        String day;

        if (oneDayOpeningHours.isClosedAllDay()){
            day = ClOSED_ALL_DAY;
            return day;
        }
        if(oneDayOpeningHours.isOpenAllDay()){
            day = OPEN_ALL_DAY;
            return day;
        }
        day = oneDayOpeningHours.getStartHour()+":"+oneDayOpeningHours.getStartMinute()+":"+oneDayOpeningHours.getEndHour()+":"+oneDayOpeningHours.getEndMinute();
        return day;
    }

    private void buildString(String[] days){
        openingHours = "";
        for (int i=0; i<7; i++){
            openingHours+=days[i]+"!";
        }
        openingHours = openingHours.replaceFirst(".$",""); //remove the last ! from the string
    }

    public void setAllDay(List<OneDayOpeningHours> oneDayOpeningHoursList){
        String[] days = new String[7];
        for (int i=0; i<7; i++){
            days[i] = getDayString(oneDayOpeningHoursList.get(i));
        }
        buildString(days);
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public static class OneDayOpeningHours{
        private int startHour = 0;
        private int startMinute = 0;
        private int endHour = 0;
        private int endMinute = 0;
        private boolean closedAllDay = false;
        private boolean openAllDay = false;

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

        public boolean isClosedAllDay() {
            return closedAllDay;
        }

        public void setClosedAllDay(boolean closedAllDay) {
            this.closedAllDay = closedAllDay;
        }

        public boolean isOpenAllDay() {
            return openAllDay;
        }

        public void setOpenAllDay(boolean openAllDay) {
            this.openAllDay = openAllDay;
        }

        public boolean isOpenNow(){
            return isOpenAtAGivenTime(System.currentTimeMillis());
        }

        public boolean isOpenAtAGivenTime(long time){
            if (closedAllDay) return false;
            if (openAllDay) return true;

            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Budapest"));
            cal.setTimeInMillis(time);

            int openTime = startHour *60+ startMinute;
            int closeTime = endHour *60+ endMinute;

            int currentTime = cal.get(Calendar.HOUR_OF_DAY)*60+cal.get(Calendar.MINUTE);

            return  (currentTime > openTime && currentTime<closeTime);
        }
    }
}
