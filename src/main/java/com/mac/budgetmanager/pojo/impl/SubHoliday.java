/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.budgetmanager.pojo.impl;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
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
 * @author Mac
 */
public class SubHoliday implements Holiday{
    
    private String holidayName;
    private String comments;
    private Temporal date;
    private Holiday parentHoliday;
    private Set<String> aliases;
    private Type type;
    private Status status;
    private int start;
    private int numDays;
    private Set<Temporal> dateRange;
    
    public SubHoliday() {
    }

    public SubHoliday(Week week, DayOfWeek day, Month month) {
        date = DateManipulator.of(month, week, day);
    }

    public SubHoliday(LocalDate date) {
        this.date = date;
    }
    
    public SubHoliday(Holiday parent) {
        this.parentHoliday = parent;
    }

    @Override
    public void set(String key, String value) {
        switch(key){
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
            case "start":{
                this.start = Ints.stringConverter().convert(value);
                return;
            }
            case "numDays":{
                this.numDays = Ints.stringConverter().convert(value);
                return;
            }
            case "comment":{
                this.comments = value;
            }
        }
    }
    
    public void setParentHoliday(Holiday parent){
        parentHoliday = parent;
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
        Preconditions.checkNotNull(parentHoliday);
        if(Objects.isNull(date)){
            Utility.checkNotNull(start);
            date = DateManipulator.dateAwayFromDate((LocalDate) parentHoliday.getHolidayDate(), start);
//            System.out.println("SUBDATE: " + date);
        }
        return date;
    }

    @Override
    public boolean hasSubHolidays() {
        return false;
    }

    @Override
    public List<Holiday> getSubHolidays() {
        return null;
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
    
}
