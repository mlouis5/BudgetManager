/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.common.utilities.dates;

import com.mac.budgetmanager.enums.Ordinal;
import com.mac.budgetmanager.enums.Week;
import com.mac.budgetmanager.pojo.Holiday;
import com.mac.budgetmanager.pojo.Year;
import com.mac.budgetmanager.pojo.impl.FixedHoliday;
import com.mac.budgetmanager.pojo.impl.FloatingHoliday;
import com.mac.budgetmanager.pojo.impl.SubHoliday;
import com.mac.common.utilities.Utility;
import java.io.IOException;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Mac
 */
public class DateManipulator {

    private static final String[] fixedKeys = {"-name", "-status", "type",
        "month", "day", "comment"};
    private static final String[] floatingKeys = {"-name", "-status", "type",
        "ordinal", "dayOfWeek", "month", "comment"};
    private static final String[] subKeys = {"-name", "-status", "type", "start", "numDays", "comment"};
    private static final String FIXED_HOLIDAYS = "fixedHoliday";
    private static final String FLOATING_HOLIDAYS = "nonFixedHoliday";
    private static final String SUB_HOLIDAYS = "subHolidays";
    private static final String SUB_HOLIDAY = "subHoliday";
    private static final Map<String, Holiday> holidays;

    static {
        holidays = new HashMap();
        try {
            InputStream is = DateManipulator.class
                    .getResourceAsStream("/holidays/holidays.json");

            String jsonText = IOUtils.toString(is);
            JSONObject json = new JSONObject(jsonText);
            JSONObject holiday = json.getJSONObject("holidays");
            JSONObject country = holiday.getJSONObject("country");
            JSONObject observedHolidays = country.getJSONObject("observedHolidays");

            //System.out.println(jsonText);
            setHolidays(observedHolidays, FIXED_HOLIDAYS, FixedHoliday.class, fixedKeys);
            setHolidays(observedHolidays, FLOATING_HOLIDAYS, FloatingHoliday.class, floatingKeys);
        } catch (IOException | JSONException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(DateManipulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static String formatDate(LocalDate date, DateTimeFormatter formatter){
        return date.format(formatter);
    }
    
    public static boolean isKnownHoliday(LocalDate date){
        if(Objects.nonNull(date)){
            if (holidays.values().stream().anyMatch((hol) -> 
                    (date.compareTo((LocalDate) hol.getHolidayDate()) == 0))) {
                return true;
            }
        }
        return false;
    }
    
    public static LocalDate businessDaysPriorTo(int businessDays, LocalDate priorTo){
        LocalDate date = priorTo;
        for(int i = 0; i < businessDays; ){
            date = date.minusDays(1);
            DayOfWeek dow = date.getDayOfWeek();
            if(!isKnownHoliday(date) && (dow != DayOfWeek.SATURDAY 
                    || dow != DayOfWeek.SUNDAY)){
                i++;
            }
        }
        return date;
    }
    
    public LocalDate businessDaysAfter(int businessDays, LocalDate after){
        LocalDate date = after;
        for(int i = 0; i < businessDays; ){
            date = date.plusDays(1);
            DayOfWeek dow = date.getDayOfWeek();
            if(!isKnownHoliday(date) && (dow != DayOfWeek.SATURDAY 
                    || dow != DayOfWeek.SUNDAY)){
                i++;
            }
        }
        return date;
    }
    
    public LocalDate calendarDaysPriorTo(int calendarDays, LocalDate priorTo){
        LocalDate date = priorTo;
        for(int i = 0; i < calendarDays; i++){
            date = date.minusDays(1);
            DayOfWeek dow = date.getDayOfWeek();            
        }
        return date;
    }
    
    public LocalDate calendarDaysAfter(int calendarDays, LocalDate after){
        LocalDate date = after;
        for(int i = 0; i < calendarDays; i++){
            date = date.plusDays(1);
            DayOfWeek dow = date.getDayOfWeek();            
        }
        return date;
    }
    
    public static int daysUntilDate(LocalDate date){
        return LocalDate.now().until(date).getDays();
    }
    
    public static LocalDate formMostAccurateFromDay(int day){
        Month month = LocalDate.now().getMonth();
        LocalDate date = createFixedDateFrom(LocalDate.now().getMonth(), day);
        
        if(date.getMonth() != month){
            date = date.minusMonths(1);
        }
        return date;
    }

    public static LocalDate initDate(Month month, Ordinal ordinal) {
        return initDate(new Year(LocalDate.now().getYear()), month, ordinal);
    }

    public static LocalDate initDate(Year year, Month month, Ordinal ordinal) {
        Utility.checkNotNull(year, month, ordinal);
        return LocalDate.of(year.year(), month, ordinal.order());
    }

    public static LocalDate of(Month month, Week week, DayOfWeek day) {
        return of(new Year(LocalDate.now().getYear()), month, week, day);
    }
    
    public static LocalDate toLocalDate(Date date){
        if(Objects.nonNull(date)){
            Instant instant = Instant.ofEpochMilli(date.getTime());
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
        }
        return null;
    }

    public static LocalDate of(Year year, Month month, Week week, DayOfWeek day) {
        Utility.checkNotNull(year, month, week, day);
        LocalDate date = LocalDate.of(year.year(), month, 1);
        date = date.with(ChronoField.ALIGNED_WEEK_OF_MONTH, week.week());
        int certainWeek = week.week();
        while (date.getMonthValue() > month.getValue() && certainWeek >= 1) {
            certainWeek = certainWeek - 1;
            date = LocalDate.of(year.year(), month, 1);
            date = date.with(ChronoField.ALIGNED_WEEK_OF_MONTH, certainWeek);
        }
        while (date.getDayOfWeek() != day && date.getDayOfMonth() < date.lengthOfMonth()) {
            date = date.plusDays(1);
        }
        if (date.getDayOfWeek() != day) {
            while (date.getDayOfWeek() != day) {
                date = date.minusDays(1);
            }
        }
        return date;
    }

    public static LocalDate dayBeforeDate(LocalDate date, DayOfWeek day) {
        Utility.checkNotNull(date, day);
        int dayOfMonth = date.getDayOfMonth();
        LocalDate newDate = date;

        while (newDate.getDayOfMonth() == dayOfMonth || newDate.getDayOfWeek() != day) {
            newDate = newDate.minusDays(1);
        }
        return newDate;
    }

    public static LocalDate dayAfterDate(LocalDate date, DayOfWeek day) {
        Utility.checkNotNull(date, day);
        int dayOfMonth = date.getDayOfMonth();
        LocalDate newDate = date;

        while (newDate.getDayOfMonth() == dayOfMonth || newDate.getDayOfWeek() != day) {
            newDate = newDate.plusDays(1);
        }
        return newDate;
    }
    
    public static LocalDate dateAwayFromDate(LocalDate date, int numDaysAway){
        Utility.checkNotNull(date, numDaysAway);
        if(numDaysAway < 0){
            return date.minusDays(Math.abs(numDaysAway));
        }else if(numDaysAway > 0){
            return date.plusDays(Math.abs(numDaysAway));
        }        
        return date;
    }

    private static LocalDate getEasterDate() {
        return getEasterDate(new Year(LocalDate.now().getYear()));
    }

    private static LocalDate getEasterDate(Year yearObj) {
        Utility.checkNotNull(yearObj);
        int year = yearObj.year();
        int a = year % 19;
        int b = year / 100;
        int c = year % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int n = (h + l - 7 * m + 114) / 31;
        int p = (h + l - 7 * m + 114) % 31;
        return LocalDate.of(year, n, p + 1);
    }

    private static void setHolidays(JSONObject json, String arrKey, Class<? extends Holiday> holiday, String[] keys) throws JSONException, InstantiationException, IllegalAccessException {
        Utility.checkNotNull(json, arrKey, holiday, keys);

        JSONArray arr = json.getJSONArray(arrKey);
        Utility.checkNotNull(arr);
        int c, i;
        for (i = 0, c = arr.length(); i < c; i++) {
            JSONObject holidayJson = arr.getJSONObject(i);
            Utility.checkNotNull(holidayJson);
            Holiday holidayInstance = holiday.newInstance();
            setHolidayValues(holidayInstance, holidayJson, keys);
            findSubHolidays(holidayJson, holidayInstance);
//            System.out.println(holidayInstance.getHolidayName() + "\t" + holidayInstance.getHolidayDate());

            holidays.put(holidayInstance.getHolidayName(), holidayInstance);
        }
    }

    private static void setHolidayValues(Holiday holidayInstance, JSONObject holidayJson, String[] keys) throws JSONException {
        int j, k;
        for (j = 0, k = keys.length; j < k; j++) {
            String key = keys[j];
            Utility.checkNotNull(key);
            holidayInstance.set(key, holidayJson.getString(key));
//            System.out.println(holidayJson.getString(key));
        }
    }

    private static void findSubHolidays(JSONObject holidayJson, Holiday parentHoliday) {
        try {
            JSONObject sub = holidayJson.getJSONObject(SUB_HOLIDAYS);
            if (Objects.nonNull(sub)) {
                try {
                    JSONArray jArr = sub.getJSONArray(SUB_HOLIDAY);
                    if (Objects.nonNull(jArr)) {
                        int l, m;
                        for (l = 0, m = jArr.length(); l < m; l++) {
                            JSONObject jOb = jArr.getJSONObject(l);
                            Holiday subHol = new SubHoliday(parentHoliday);
                            setHolidayValues(subHol, jOb, subKeys);
                            System.out.println(subHol.getHolidayDate());
                            holidays.put(subHol.getHolidayName(), subHol);
//                            System.out.println(subHol.getHolidayName() + "\t" + subHol.getHolidayDate());
                        }
                    }
                } catch (JSONException ex) {
                    JSONObject jOb = sub.getJSONObject(SUB_HOLIDAY);
                    if (Objects.nonNull(jOb)) {
                        Holiday subHol = new SubHoliday(parentHoliday);
                        setHolidayValues(subHol, jOb, subKeys);
                        System.out.println(subHol.getHolidayDate());
                        holidays.put(subHol.getHolidayName(), subHol);
//                        System.out.println(subHol.getHolidayName() + "\t" + subHol.getHolidayDate());
                    }
                }
            }
        } catch (JSONException ex) {
//            System.out.println(ex.getMessage());
        }
    }

    private static LocalDate createFixedDateFrom(Month month, int dayInMonth) {
        return initDate(new Year(LocalDate.now().getYear()), month, getBestMatch(month, dayInMonth));
    }
    
    private static Ordinal getBestMatch(Month month, int dayOfMonth){
        int minDaysInMonth = month.minLength();
        if(minDaysInMonth < dayOfMonth){
            return Ordinal.getValue(minDaysInMonth);
        }else if(dayOfMonth < 1){
            return Ordinal.first;
        }
        return Ordinal.getValue(dayOfMonth);
    }

//    public static void main(String[] args) {
//        System.out.println(DateManipulator.dayBeforeDate(DateManipulator.of(new Year(2014), Month.NOVEMBER, Week.FIRST, DayOfWeek.SUNDAY), DayOfWeek.MONDAY));
//        System.out.println(DateManipulator.getEasterDate());
//        System.out.println(DateManipulator.getEasterDate(new Year(2015)));
//        System.out.println(DateManipulator.getEasterDate(new Year(2016)));
//        System.out.println(DateManipulator.getEasterDate(new Year(2017)));
//        System.out.println(DateManipulator.getEasterDate(new Year(2018)));
//    }

}
