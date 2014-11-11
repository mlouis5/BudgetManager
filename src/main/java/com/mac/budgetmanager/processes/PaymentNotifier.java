/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.budgetmanager.processes;

import com.mac.budgetmanager.pojo.PaymentEnvelope;
import com.mac.budgetmanager.pojo.entities.Bill;
import com.mac.budgetmanager.pojo.entities.Payment;
import com.mac.budgetmanager.pojo.entities.dao.impl.PaymentRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author MacDerson
 */
@Component
public class PaymentNotifier {

    private static final int NOTIFICATION_GAP = 2;

    @Autowired
    private ApplicationContext ctx;

    public PaymentNotifier() {
    }

    //@Scheduled(cron = "0 2 6,12,16 * * *")
    @Scheduled(fixedDelay = 12000)
    public void notifyUsers() {
        PaymentRepository paymentDAO = ctx.getBean(PaymentRepository.class);
        List<Payment> allPayments = paymentDAO.findAll();

        if (Objects.nonNull(allPayments)) {
            Map<Bill, List<Payment>> notifications = new HashMap();

            allPayments.stream()
                    .filter((payment) -> (Objects.nonNull(payment)
                            && shouldNofitfy(payment)))
                    .collect(Collectors.toList())
                    .forEach((payment)
                            -> addToNotifications(notifications, payment));
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

    private void addToNotifications(Map<Bill, List<Payment>> payments, Payment payment) {
        if (Objects.nonNull(payment)) {
            Bill bill = payment.getPaymentBillId();
            if (Objects.nonNull(bill)) {
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
    }

    @Autowired
    private void notify(Map<Bill, List<Payment>> notifications) {
        if (Objects.nonNull(notifications)) {
            MailSender mailSender = ctx.getBean("gmailMailSender", MailSender.class);

            //VelocityEngine ve = new VelocityEngine();
            //ve.init();
            for (Bill bill : notifications.keySet()) {
                PaymentEnvelope envelope = ctx.getBean("paymentEnvelope", PaymentEnvelope.class);
                envelope.setBill(bill);
                envelope.setPayments(notifications.get(bill));
            }
        }

    }

}
