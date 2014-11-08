/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.budgettracker.utilities.xml.enums;

/**
 *
 * @author MacDerson
 */
public enum Type {
    FEDERAL, TRADITIONAL, RELIGIOUS, INFORMAL;
    
    public static Type getValue(String value){
        for(Type type : Type.class.getEnumConstants()){
            if(type.toString().equalsIgnoreCase(value)){
                return type;
            }
        }
        return null;
    }
}
