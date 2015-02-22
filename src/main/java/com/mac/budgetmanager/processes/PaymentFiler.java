/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.budgetmanager.processes;

import com.mac.abstractrepository.AbstractRepository;
import com.mac.abstractrepository.budgetrepo.PaymentDao;
import com.mac.budgetentities.pojos.Payment;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mac
 */
@Component
public class PaymentFiler implements Runnable{

    private List<Payment> paymentsToFile;
    private AbstractRepository<Payment> repo;
    
    public PaymentFiler(){}
    
    public PaymentFiler(AbstractRepository<Payment> repo, List<Payment> payments){
        paymentsToFile = payments;
        this.repo = repo;
    }
    
    public void setup(PaymentDao repo, List<Payment> payments){
        paymentsToFile = payments;
        this.repo = repo;
    }
    
    public void clear(){
        paymentsToFile = null;
        this.repo = null;
    }
            
    @Override
    public void run() {
        if(Objects.nonNull(repo) && Objects.nonNull(paymentsToFile)){
            for(Payment p : paymentsToFile){
                repo.create(p);
            }
            clear();
        }
    }
    
}
