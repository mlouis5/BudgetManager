/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.budgetmanager.processes;

import com.mac.budgetmanager.pojo.entities.Bill;
import com.mac.budgetmanager.pojo.entities.Payment;
import com.mac.budgetmanager.pojo.entities.User;
import com.mac.budgetmanager.pojo.entities.dao.impl.BillRepository;
import com.mac.common.utilities.dates.DateManipulator;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author MacDerson
 */
@Component
public class BillProcessor {
    
    private static final int NOTIFICATION_GAP = 2;
    private static final int COMING_DUE_TIMEFRAME = 15;
    private static final int DAYSNOTICE_PRIOR_TO_PAYMENT = 10;
    
    @Autowired
    private ApplicationContext ctx;
    
    @Scheduled(cron="0 0 6,12,16 * * *")    
    public void processBills(){
        BillRepository billDAO = ctx.getBean(BillRepository.class);
        List<Bill> allBills = billDAO.findAll();
    }
    
    private boolean shouldNofitfy(Payment payment){
        if(Objects.isNull(payment)){
            return false;
        }    
        Date notifiedDate = payment.getPaymentLastNotificationDate();
        if(Objects.isNull(notifiedDate)){
            return true;
        }
        Instant notificationInstance = Instant.ofEpochMilli(notifiedDate.getTime());
        long daysPassed = ChronoUnit.DAYS.between(notificationInstance, Instant.now());
        return daysPassed >= NOTIFICATION_GAP;
    }
    
    private Payment generateNewPayment(User user, Bill bill){
        if(Objects.isNull(user) || Objects.isNull(bill)){
            return null;
        }
        Payment singlePayment = ctx.getBean(Payment.class);
        LocalDate dueDate = getPSBMDate(bill);
        
        if(Objects.nonNull(dueDate)){
            singlePayment.setPaymentBillId(bill);
            singlePayment.setPaymentUserId(user);
            singlePayment.setPaymentFilingDate(new Date(Instant.now().toEpochMilli()));
            singlePayment.setPaymentDueDate(new Date(dueDate.toEpochDay()));
        }
        return singlePayment;
    }
    
    private LocalDate getPSBMDate(Bill bill){
        LocalDate dueDate = DateManipulator.formMostAccurateFromDay(bill.getBillDueDate());        
        return DateManipulator.businessDaysPriorTo(DAYSNOTICE_PRIOR_TO_PAYMENT, dueDate);
    }
    
    private boolean isBillComingDue(Bill bill){
        LocalDate dueDate = DateManipulator.formMostAccurateFromDay(bill.getBillDueDate());
        int daysTill = DateManipulator.daysUntilDate(dueDate);
        return daysTill <= COMING_DUE_TIMEFRAME;
    }
    
    private List<Payment> getPendingPayments(Bill bill){
        List<Payment> pendingPmts = new ArrayList(0);
        if(Objects.nonNull(bill)){
            List<Payment> payments = bill.getPaymentList();
            if(Objects.nonNull(payments)){
                payments.stream().filter((pay) -> 
                        (Objects.isNull(pay.getPaymentPaidDate())))
                        .forEach((pay) -> {
                    pendingPmts.add(pay);
                });
            }
        }
        return pendingPmts;
    }
}