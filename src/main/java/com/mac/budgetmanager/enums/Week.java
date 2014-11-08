/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.budgettracker.newpackage.enums;

/**
 *
 * @author Mac
 */
public enum Week {
    FIRST(1), SECOND(2), THIRD(3), FOURTH(4), LAST(5);
    
    public static Week getValue(String value){
        for(Week week : Week.class.getEnumConstants()){
            if(week.toString().equalsIgnoreCase(value)){
                return week;
            }
        }
        return null;
    }
    
    private int week;
    Week(int week){
        this.week = week;
    }
    
    public int week(){
        return week;
    }
}
