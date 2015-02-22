/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.budgetmanager.pojo;

import com.mac.budgetentities.pojos.Bill;
import com.mac.budgetentities.pojos.Payment;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Mac
 */
public class PaymentEnvelope {
    
    private Bill bill;
    private List<Payment> contents;
    
    public void setBill(Bill bill){
        this.bill = bill;
    }
    
    public void setPayments(List<Payment> contents){
        this.contents = contents;
    }
    
    public String recepient(){
        String recepient = "Recepient";
        if(Objects.nonNull(bill) && Objects.nonNull(bill.getBillOwner())){
            recepient = bill.getBillOwner().getUserFname() + " " + bill.getBillOwner().getUserLname();
        }
        
        return recepient;
    }
    
    public Bill getBill(){
        return bill;
    }
    
    public List<Payment> getPayments(){
        return contents;
    }
    
}
