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
import java.io.StringWriter;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.mail.MessagingException;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author MacDerson
 */
@Component
public class PaymentNotifier {

    private static final int NOTIFICATION_GAP = 2;
    private static final String EMAIL = "MyBudget@gmail.com";
    private static final String EMAIL_SUBJECT = "MyBudget Payment Notification";

    @Autowired
    private ApplicationContext ctx;

    //@Scheduled(cron = "0 2 6,12,16 * * *")
    @Scheduled(fixedDelay = 13000)
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
            
            notify(notifications, paymentDAO);
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

    private void notify(Map<Bill, List<Payment>> notifications, PaymentRepository paymentDAO) {
        if (Objects.nonNull(notifications)) {
            try {
                VelocityEngine ve = ctx.getBean("velocityEngine", VelocityEngine.class);
                ve.init();

                for (Bill bill : notifications.keySet()) {
                    PaymentEnvelope envelope = ctx.getBean(PaymentEnvelope.class);
                    envelope.setBill(bill);
                    List<Payment> payments = notifications.get(bill);
                    
                    envelope.setPayments(payments);

                    VelocityContext context = ctx.getBean(VelocityContext.class);
                    context.put("envelope", envelope);

                    //ve.g
                    Template template = ve.getTemplate("/templates/paymentReminder.vm", "UTF-8");
                    /*  now render the template into a Writer  */
                    StringWriter writer = ctx.getBean(StringWriter.class);
                    template.merge(context, writer);

                    boolean isSent = sendMail(bill, writer.toString());
                    if(isSent){
                        Date notificationDate = ctx.getBean(Date.class);
                        for(Payment p : payments){
                            p.setPaymentLastNotificationDate(notificationDate);
                            paymentDAO.edit(p);
                        }
                    }
                }
            } catch (BeansException | ResourceNotFoundException | ParseErrorException | MethodInvocationException ex) {
                Logger.getLogger(PaymentNotifier.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private boolean sendMail(Bill bill, String emailContent) {
        System.out.println(emailContent);
        try {            
            MimeMessageHelper message = ctx.getBean("gmailMimeMessageHelper", MimeMessageHelper.class);
            message.setFrom(EMAIL);
            message.setTo(bill.getBillOwner().getUserEmail());
            message.setSubject(EMAIL_SUBJECT);
            message.setText(emailContent, true);
//        message.addInline("myLogo", new ClassPathResource("img/mylogo.gif"));
//        message.addAttachment("myDocument.pdf", new ClassPathResource("doc/myDocument.pdf"));
            JavaMailSenderImpl mailSender = ctx.getBean("gmailMailSender", JavaMailSenderImpl.class);
            mailSender.send(message.getMimeMessage());
            return true;
        } catch (MessagingException ex) {
            Logger.getLogger(PaymentNotifier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
