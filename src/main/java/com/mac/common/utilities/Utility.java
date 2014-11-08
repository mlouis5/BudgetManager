/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.budgettracker.utilities;

import com.google.common.base.Preconditions;
import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author MacDerson
 */
public class Utility {
    public static void checkNotNull(Object... obj) {
        Preconditions.checkNotNull(obj);
        for (Object ob : obj) {
            Preconditions.checkNotNull(ob);
        }
    }
    
    public static UUID generateUUIDFrom(String... values){
        if(Objects.nonNull(values) && values.length != 0){
            StringBuilder sb = new StringBuilder();
            for(String str : values){
                sb.append(str);
            }
            return UUID.fromString(sb.toString());
        }
        return UUID.randomUUID();
    }
}
