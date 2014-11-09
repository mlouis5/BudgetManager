/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.budgetmanager.pojo;

/**
 *
 * @author MacDerson
 * @param <U>
 * @param <P>
 */
public interface Notifier<U extends Notifiee, P extends Subject> {
    
    boolean notify(U u, P p);
}
