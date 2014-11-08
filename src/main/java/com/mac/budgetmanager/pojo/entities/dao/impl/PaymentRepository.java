/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.budgetmanager.pojo.entities.dao.impl;

import com.mac.budgetmanager.pojo.entities.Payment;
import com.mac.budgettracker.entities.dao.AbstractRepository;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mac
 */
@Component
public class PaymentRepository extends AbstractRepository<Payment>  {    
    public PaymentRepository() {
        super(Payment.class);
    }    
}