/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.budgetmanager.pojo.entities.dao.impl;

import com.mac.budgetmanager.pojo.entities.Bill;
import com.mac.budgettracker.entities.dao.AbstractRepository;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mac
 */
@Component
public class BillRepository extends AbstractRepository<Bill>  {    
    public BillRepository() {
        super(Bill.class);
    }    
}