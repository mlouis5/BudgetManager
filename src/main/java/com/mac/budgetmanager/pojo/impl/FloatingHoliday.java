/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.budgetmanager.pojo.impl;

import com.mac.budgettracker.newpackage.enums.Week;
import com.mac.common.utilities.dates.DateManipulator;
import com.mac.budgettracker.utilities.Utility;
import com.mac.budgettracker.utilities.xml.Holiday;
import com.mac.budgettracker.utilities.xml.enums.Status;
import com.mac.budgettracker.utilities.xml.enums.Type;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author MacDerson
 */
public class FloatingHoliday implements Holiday {

    private String holidayName;
    private String comments;
    private Temporal date;
    private List<Holiday> subHolidays;
    private Set<String> aliases;
    private Type type;
    private Status status;
    private Week week;
    private Month month;
    private DayOfWeek dayOfWeek;

    public FloatingHoliday() {
    }

    public FloatingHoliday(Week week, DayOfWeek day, Month month) {
        date = DateManipulator.of(month, week, day);
    }

    public FloatingHoliday(LocalDate date) {
        this.date = date;
    }

    @Override
    public void setHolidayName(String name) {
        holidayName = name;
    }

    @Override
    public String getHolidayName() {
        return holidayName;
    }

    @Override
    public Temporal getHolidayDate() {
        if(Objects.isNull(date)){
            Utility.checkNotNull(week, month, dayOfWeek);
            date = DateManipulator.of(month, week, dayOfWeek);
        }
        return date;
    }

    @Override
    public boolean hasSubHolidays() {
        return Objects.nonNull(subHolidays) && subHolidays.size() > 0;
    }

    @Override
    public List<Holiday> getSubHolidays() {
        return subHolidays;
    }

    @Override
    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String getComments() {
        return comments;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setHolidayType(Type type) {
        this.type = type;
    }

    @Override
    public Type getHolidayType() {
        return type;
    }

    @Override
    public void setAliases(String... aliases) {
        if (Objects.nonNull(aliases)) {
            this.aliases = new HashSet(Arrays.asList(aliases));
        }
    }

    @Override
    public Set<String> getAliases() {
        return aliases;
    }

    @Override
    public void setHolidayDate(Temporal date) {
        if (Objects.isNull(this.date)) {
            this.date = date;
        }
    }

    @Override
    public void set(String key, String value) {
        Utility.checkNotNull(key, value);
        
        switch (key) {
            case "-name":{
                this.holidayName = value;
                return;
            }
            case "-status":{
                this.status = Status.getValue(value);
                return;
            }
            case "type":{
                this.type = Type.getValue(value);
                return;
            }
            case "ordinal":{
                week = Week.getValue(value);
                return;
            }
            case "dayOfWeek":{
                dayOfWeek = DayOfWeek.valueOf(value);
                return;
            }
            case "month":{
                month = Month.valueOf(value);
                return;
            }
            case "comment":{
                comments = value;
            }
        }
    }

}
