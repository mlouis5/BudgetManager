/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.budgetmanager.processes;

import com.mac.budgetmanager.pojo.entities.Bill;
import com.mac.budgetmanager.pojo.entities.Payment;
import java.util.Map;

/**
 *
 * @author MacDerson
 */
public class PaymentNotifier implements Runnable{
    
    Map<Payment, Bill> notifications;
    
    public PaymentNotifier(){}
    
    public PaymentNotifier(Map<Payment, Bill> noticeMap){
        this.notifications = noticeMap;
    }
    
    public void setup(Map<Payment, Bill> noticeMap){
        this.notifications = noticeMap;
    }
    
    public void clear(){
        this.notifications = null;
    }

    @Override
    public void run() {
        
    }
    
    
}
