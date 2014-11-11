/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.budgetmanager.processes;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.mac.budgetmanager.pojo.entities.Bill;
import com.mac.budgetmanager.pojo.entities.Payment;
import com.mac.budgetmanager.pojo.entities.dao.impl.BillRepository;
import com.mac.budgetmanager.pojo.entities.dao.impl.PaymentRepository;
import com.mac.common.utilities.dates.DateManipulator;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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
    @Scheduled(fixedDelay = 10000)
    public void processBills() {
        BillRepository billDAO = ctx.getBean(BillRepository.class);
        List<Bill> allBills = billDAO.findAll();
        if (Objects.nonNull(allBills) && !allBills.isEmpty()) {
            List<Payment> paymentsToFile = ctx.getBean(ArrayList.class);
            Map<Bill, List<Payment>> billNotifications = ctx.getBean(HashMap.class);

            allBills.stream().map((bill) -> {
                System.out.println(bill);
                System.out.println(bill.getBillName());
                System.out.println(bill.getBillSource());
                if (isBillComingDue(bill)) {
                    if (!isPaymentFiled(bill)) {
                        Payment payment = fileNewPayment(bill);
                        if (Objects.nonNull(payment.getPaymentId())
                                && Objects.nonNull(payment.getPaymentId()) && !payment.getPaymentId().isEmpty()) {

                            paymentsToFile.add(payment);
                            addToNotifications(billNotifications, bill, payment);
                            System.out.println("filed payment for: " + bill);
                            System.out.println("filed payment for: " + payment);
                            System.out.println("filed payment for: " + payment.getPaymentDueDate());
                            bill.getPaymentList().add(payment);
                        }
                    }
                }
                return bill;
            }).forEach((bill) -> {
                List<Payment> pendingPayments = getPendingPayments(bill);
                if (Objects.nonNull(pendingPayments) && !pendingPayments.isEmpty()) {
                    pendingPayments.stream().filter((pending)
                            -> (shouldNofitfy(pending))).forEach((pending) -> {
                                addToNotifications(billNotifications, bill, pending);
                            });
                }
            });
            PaymentFiler filer = ctx.getBean(PaymentFiler.class);
            PaymentRepository pr = ctx.getBean(PaymentRepository.class);
            filer.setup(pr, paymentsToFile);
            ThreadPoolTaskExecutor executor = ctx.getBean(ThreadPoolTaskExecutor.class);
            executor.execute(filer);
        }
    }

    private void addToNotifications(Map<Bill, List<Payment>> payments, Bill bill, Payment payment) {
        if (Objects.nonNull(bill) && Objects.nonNull(payment)) {
            List<Payment> paymentsForBill = payments.get(bill);
            if (Objects.nonNull(paymentsForBill)) {
                if (!paymentsForBill.contains(payment)) {
                    paymentsForBill.add(payment);
                }
            } else {
                List<Payment> newPayments = ctx.getBean(ArrayList.class);
                newPayments.add(payment);
                payments.put(bill, newPayments);
            }
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
            Instant instant = dueDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            Date due = Date.from(instant);

            HashFunction hf = Hashing.md5();
            HashCode hc = hf.newHasher()
                    .putLong(due.getTime())
                    .putString(bill.getBillId(), Charsets.UTF_8)
                    .putString(bill.getBillOwner().getUserId(), Charsets.UTF_8).hash();

            singlePayment.setPaymentId(hc.toString());
            singlePayment.setPaymentBillId(bill);
            singlePayment.setPaymentUserId(bill.getBillOwner());
            singlePayment.setPaymentFilingDate(fileDate);

            singlePayment.setPaymentDueDate(due);
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

    private boolean isPaymentFiled(Bill bill) {
        Payment aPmt = fileNewPayment(bill);
        List<Payment> payments = bill.getPaymentList();
        if (Objects.nonNull(payments) && !payments.isEmpty()) {
            if (payments.stream().filter((payment) -> (Objects.nonNull(payment)))
                    .anyMatch((pmtToCompare) -> (pmtToCompare.hashCode() == aPmt.hashCode()))) {
                return true;
            }
        }
        return false;
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
