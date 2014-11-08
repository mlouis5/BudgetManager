/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.budgettracker.utilities.xml;

import com.mac.budgettracker.utilities.xml.enums.Status;
import com.mac.budgettracker.utilities.xml.enums.Type;
import java.io.Serializable;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Set;

/**
 *
 * @author MacDerson
 */
public interface Holiday extends Serializable{
    
    void set(String key, String value);
    void setHolidayName(String name);
    String getHolidayName();
    void setHolidayDate(Temporal date);
    Temporal getHolidayDate();
    boolean hasSubHolidays();
    List<Holiday> getSubHolidays();
    void setComments(String comments);
    String getComments();
    void setStatus(Status status);
    Status getStatus();
    void setHolidayType(Type type);
    Type getHolidayType();
    void setAliases(String... aliases);
    Set<String> getAliases();
}
