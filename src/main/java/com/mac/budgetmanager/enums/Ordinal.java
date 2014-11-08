/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.budgettracker.newpackage.enums;

import com.google.common.base.Converter;
import com.google.common.base.Enums;

/**
 *
 * @author Mac
 */
public enum Ordinal {
    first(1), second(2), third(3), fourth(4), fifth(5), sixth(6), seventh(7), 
    eighth(8), ninth(9), tenth(10), eleventh(11), twelfth(12), thirteenth(13), 
    fourteenth(14), fifteenth(15), sixteenth(16), seventeenth(17), 
    eighteenth(18), nineteenth(19), twentieth(20), twenty_first(21), 
    twenty_second(22), twenty_third(23), twenty_fourth(24), twenty_fifth(25), 
    twenty_sixth(26), twenty_seventh(27), twenty_eight(28), twenty_ninth(29), 
    thirtieth(30), thirty_first(31);
    
    public static Ordinal getValue(int value){
        for(Ordinal ord : Ordinal.class.getEnumConstants()){
            if(ord.order == value){
                return ord;
            }
        }
        return null;
    }
    
    private int order;
    Ordinal(int order){
        this.order = order;
    }
    
    public int order(){
        return order;
    }
}