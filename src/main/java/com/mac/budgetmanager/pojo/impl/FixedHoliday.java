/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.budgetmanager.pojo.impl;

import com.google.common.primitives.Ints;
import com.mac.budgetmanager.enums.Ordinal;
import com.mac.budgetmanager.enums.Status;
import com.mac.budgetmanager.enums.Type;
import com.mac.budgetmanager.pojo.Holiday;
import com.mac.common.utilities.Utility;
import com.mac.common.utilities.dates.DateManipulator;
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
public class FixedHoliday implements Holiday {

    private String holidayName;
    private String comments;
    private Temporal date;
    private List<Holiday> subHolidays;
    private Set<String> aliases;
    private Type type;
    private Status status;
    private Month month;
    private int dayOfMonth;

    public FixedHoliday() {
    }

    public FixedHoliday(Month month, int dayOfMonth) {
        date = DateManipulator.initDate(month, Ordinal.getValue(dayOfMonth));
    }

    public FixedHoliday(LocalDate date) {
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
        if (Objects.isNull(date)) {
            Utility.checkNotNull(month, dayOfMonth);
            date = DateManipulator.initDate(month, Ordinal.getValue(dayOfMonth));
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
        switch (key) {
            case "-name": {
                this.holidayName = value;
                return;
            }
            case "-status": {
                this.status = Status.getValue(value);
                return;
            }
            case "type": {
                this.type = Type.getValue(value);
                return;
            }
            case "month": {
                this.month = Month.valueOf(value);
                return;
            }
            case "day": {
                this.dayOfMonth = Ints.stringConverter().convert(value);
                return;
            }
            case "comment": {
                comments = value;
            }
        }
    }

}
