/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.budgetmanager.enums;

/**
 *
 * @author MacDerson
 */
public enum Status {
    ACTIVE, INACTIVE, ABOLISHED;
    
    public static Status getValue(String value){
        for(Status status : Status.class.getEnumConstants()){
            if(status.toString().equalsIgnoreCase(value)){
                return status;
            }
        }
        return null;
    }
}
