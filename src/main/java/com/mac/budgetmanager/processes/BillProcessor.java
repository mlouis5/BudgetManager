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
import com.mac.abstractrepository.budgetrepo.BillDao;
import com.mac.abstractrepository.budgetrepo.PaymentDao;
import com.mac.budgetentities.pojos.Bill;
import com.mac.budgetentities.pojos.Payment;
//import com.mac.budgetmanager.pojo.entities.Bill;
//import com.mac.budgetmanager.pojo.entities.Payment;
//import com.mac.budgetmanager.pojo.entities.dao.impl.BillRepository;
//import com.mac.budgetmanager.pojo.entities.dao.impl.PaymentRepository;
import com.mac.common.utilities.dates.DateManipulator;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * BillProcessor handles the task of filing payments based on information<br>
 * contained within a known bill, for all bills.
 *
 * @author MacDerson Louis
 * @version 1.0.0
 */
@Component
public class BillProcessor {

    private static final int COMING_DUE_TIMEFRAME = 12;
    private static final int DAYSNOTICE_PRIOR_TO_PAYMENT = 9;

    @Autowired
    private ApplicationContext ctx;

    /**
     * Processes the bill in the Bill table, files payments if necessary
     */
    //@Scheduled(fixedDelay = 10000)
//    @Scheduled(cron = "0 0 */2 * * *")
    @Scheduled(cron = "0 */1 * * * *")
    public void processBills() {
        BillDao billDAO = ctx.getBean(BillDao.class);
        List<Bill> allBills = billDAO.findAll();
        if (Objects.nonNull(allBills) && !allBills.isEmpty()) {
            List<Payment> paymentsToFile = new ArrayList();

            allBills.stream().forEach((bill) -> {
                if (isBillComingDue(bill)) {
                    if (!isPaymentFiled(bill)) {
                        Payment payment = generatePayment(bill);
                        if (Objects.nonNull(payment) && Objects.nonNull(payment.getPaymentId())
                                && !payment.getPaymentId().isEmpty()) {
                            paymentsToFile.add(payment);
                        }
                    }
                }
            });
            if (!paymentsToFile.isEmpty()) {
                PaymentFiler filer = ctx.getBean(PaymentFiler.class);
                PaymentDao pr = ctx.getBean(PaymentDao.class);
                filer.setup(pr, paymentsToFile);
                ThreadPoolTaskExecutor executor = ctx.getBean(ThreadPoolTaskExecutor.class);
                executor.execute(filer);
            }
        }
    }

    /**
     * Creates a new Payment to be filed (Added to the payment table in db).
     *
     * @param bill The Bill with which the created payment is to be associated.
     * @return a Payment
     */
    private Payment generatePayment(Bill bill) {
        if (Objects.isNull(bill)) {
            return null;
        }
        LocalDate dueDate = getPSBMDate(bill);
        Payment singlePayment = new Payment();//(Payment) ctx.getBean("payment");

        if (Objects.nonNull(dueDate)) {
            Date fileDate = ctx.getBean(Date.class);
            Instant instant = dueDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            Date due = Date.from(instant);
//            System.out.println("dueDate: " + dueDate + "\ttime: " + due.getTime());

            HashFunction hf = Hashing.md5();
            HashCode hc = hf.newHasher()
                    .putInt(dueDate.getYear())
                    .putInt(dueDate.getMonthValue())
                    .putInt(dueDate.getDayOfMonth())
                    .putString(bill.getBillId(), Charsets.UTF_8)
                    .putString(bill.getBillOwner().getUserId(), Charsets.UTF_8)
                    .putString(bill.getBillName(), Charsets.UTF_8)
                    .putString(bill.getBillSource(), Charsets.UTF_8).hash();

            singlePayment.setPaymentId(hc.toString());
            singlePayment.setPaymentBillId(bill);
            singlePayment.setPaymentUserId(bill.getBillOwner());
            singlePayment.setPaymentFilingDate(fileDate);
            singlePayment.setPaymentDueDate(due);
        }
        return singlePayment;
    }

    /**
     * Returns the LocalDate that this payment should be paid.<br>
     * Note: it's entirely possible that this date could be in the past.
     *
     * @param bill The bill for which to calculate the PSBM.<br>The date will be
     * calculated for the current month if possible.
     * @return The date that this bill should be due for the current month.
     */
    private LocalDate getPSBMDate(Bill bill) {
        LocalDate dueDate = DateManipulator.formMostAccurateFromDay(bill.getBillDueDate());
        dueDate = DateManipulator.businessDaysPriorTo(DAYSNOTICE_PRIOR_TO_PAYMENT, dueDate);

        LocalDate now = LocalDate.now().atStartOfDay()
                .atZone(ZoneId.systemDefault()).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();

        dueDate = dueDate.atStartOfDay().atZone(ZoneId.systemDefault())
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (dueDate.isBefore(now)) {
            dueDate = now;
        }
        return dueDate;
    }

    /**
     * Determines if the given bill is coming due, within the next<br>
     * <pre>
     *  COMING_DUE_TIMEFRAME
     * </pre>
     *
     * @param bill
     * @return true or false.
     */
    private boolean isBillComingDue(Bill bill) {
        LocalDate dueDate = DateManipulator.formMostAccurateFromDay(bill.getBillDueDate());
        int daysTill = DateManipulator.daysUntilDate(dueDate);
        return daysTill <= COMING_DUE_TIMEFRAME;
    }

    /**
     * Determines if a payment has already been filed for this payment,<br>
     * for the month in which it is due.
     *
     * @param bill the Bill in question.
     * @return true or false (false) will see a new payment generated, and <br>
     * added to the payment table).
     */
    private boolean isPaymentFiled(Bill bill) {
        Payment aPmt = generatePayment(bill);
        System.out.println("aPmt");
        aPmt.print();
        if (!aPmt.getPaymentId().isEmpty()) {
            List<Payment> payments = bill.getPaymentList();
            if (Objects.nonNull(payments) && !payments.isEmpty()) {
                for (Payment p : payments) {
                    System.out.println("Comparing:");
                    aPmt.print();
                    p.print();
                    if (aPmt.getPaymentId().equalsIgnoreCase(p.getPaymentId())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
