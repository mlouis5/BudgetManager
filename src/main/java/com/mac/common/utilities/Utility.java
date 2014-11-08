/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.budgettracker.utilities;

import com.google.common.base.Preconditions;

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
}
