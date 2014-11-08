/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.budgetmanager.processes;

import com.mac.budgetmanager.pojo.entities.Bill;
import com.mac.budgetmanager.pojo.entities.Payment;
import com.mac.budgetmanager.pojo.entities.dao.impl.BillRepository;
import com.mac.budgettracker.utilities.Utility;
import com.mac.common.utilities.dates.DateManipulator;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
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

    //@Scheduled(cron = "0 0 6,12,16 * * *")
    @Scheduled(fixedDelay=10000)
    public void processBills() {
        BillRepository billDAO = ctx.getBean(BillRepository.class);
        List<Bill> allBills = billDAO.findAll();
        if (Objects.nonNull(allBills) && !allBills.isEmpty()) {
            List<Payment> paymentsToFile = ctx.getBean(ArrayList.class);
            Map<Payment, Bill> billsToNotifyAbout = ctx.getBean(HashMap.class);

            allBills.stream().map((bill) -> {
                System.out.println(bill);
                System.out.println(bill.getBillName());
                System.out.println(bill.getBillSource());
                if (isBillComingDue(bill)) {
                    Payment payment = fileNewPayment(bill);
                    if (Objects.nonNull(payment.getPaymentId())
                            && Objects.nonNull(payment.getPaymentId()) && !payment.getPaymentId().isEmpty()) {
                        paymentsToFile.add(payment);
                        billsToNotifyAbout.put(payment, bill);
                        System.out.println("filed payment for: " + bill);
                        System.out.println("filed payment for: " + payment);
                        System.out.println("filed payment for: " + payment.getPaymentDueDate());
                    }
                }
                return bill;
            }).forEach((bill) -> {
                List<Payment> pendingPayments = getPendingPayments(bill);
                if (Objects.nonNull(pendingPayments) && !pendingPayments.isEmpty()) {
                    pendingPayments.stream().filter((pending)
                            -> (shouldNofitfy(pending))).forEach((pending) -> {
                                billsToNotifyAbout.put(pending, bill);
                            });
                }
            });
        }
    }

    private boolean shouldNofitfy(Payment payment) {
        if (Objects.isNull(payment)) {
            return false;
        }
        Date notifiedDate = payment.getPaymentLastNotificationDate();
        if (Objects.isNull(notifiedDate)) {
            return true;
        }
        Instant notificationInstance = Instant.ofEpochMilli(notifiedDate.getTime());
        long daysPassed = ChronoUnit.DAYS.between(notificationInstance, Instant.now());
        return daysPassed >= NOTIFICATION_GAP;
    }

    private Payment fileNewPayment(Bill bill) {
        if (Objects.isNull(bill)) {
            return null;
        }
        LocalDate dueDate = getPSBMDate(bill);
        Payment singlePayment = ctx.getBean(Payment.class);

        if (Objects.nonNull(dueDate)) {
            Date fileDate = ctx.getBean(Date.class);

            UUID paymentId = Utility.generateUUIDFrom(bill.getBillId(),
                    bill.getBillOwner().getUserId(),
                    String.valueOf(Instant.now().toEpochMilli()),
                    String.valueOf(dueDate.toEpochDay()));
            singlePayment.setPaymentId(paymentId.toString());
            singlePayment.setPaymentBillId(bill);
            singlePayment.setPaymentUserId(bill.getBillOwner());
            singlePayment.setPaymentFilingDate(fileDate);
            singlePayment.setPaymentDueDate(new Date(dueDate.toEpochDay()));
        }
        return singlePayment;
    }

    private LocalDate getPSBMDate(Bill bill) {
        LocalDate dueDate = DateManipulator.formMostAccurateFromDay(bill.getBillDueDate());
        return DateManipulator.businessDaysPriorTo(DAYSNOTICE_PRIOR_TO_PAYMENT, dueDate);
    }

    private boolean isBillComingDue(Bill bill) {
        LocalDate dueDate = DateManipulator.formMostAccurateFromDay(bill.getBillDueDate());
        int daysTill = DateManipulator.daysUntilDate(dueDate);
        return daysTill <= COMING_DUE_TIMEFRAME;
    }

    private List<Payment> getPendingPayments(Bill bill) {
        List<Payment> pendingPmts = new ArrayList(0);
        if (Objects.nonNull(bill)) {
            List<Payment> payments = bill.getPaymentList();
            if (Objects.nonNull(payments)) {
                payments.stream().filter((pay)
                        -> (Objects.isNull(pay.getPaymentPaidDate())))
                        .forEach((pay) -> {
                            pendingPmts.add(pay);
                        });
            }
        }
        return pendingPmts;
    }
}
