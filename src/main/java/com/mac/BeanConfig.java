/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac;

import com.mac.abstractrepository.budgetrepo.AddressDao;
import com.mac.abstractrepository.budgetrepo.BillDao;
import com.mac.abstractrepository.budgetrepo.PaymentDao;
import com.mac.abstractrepository.budgetrepo.UserDao;
import com.mac.budgetentities.pojos.Address;
import com.mac.budgetentities.pojos.Bill;
import com.mac.budgetentities.pojos.Income;
import com.mac.budgetentities.pojos.Paycheck;
import com.mac.budgetentities.pojos.Payment;
import com.mac.budgetentities.pojos.User;
import com.mac.budgetmanager.pojo.PaymentEnvelope;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.function.Supplier;
import javax.mail.internet.MimeMessage;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 *
 * @author Mac
 */
@Configuration
public class BeanConfig {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ArrayList arrayList() {
        return new ArrayList(1);
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public HashMap hashMap() {
        return new HashMap(1);
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Date date() {
        return new Date();
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor tpte = new ThreadPoolTaskExecutor();
        tpte.setCorePoolSize(2);
        tpte.setWaitForTasksToCompleteOnShutdown(true);
        return tpte;
    }

    @Bean
    public MailSender gmailMailSender() {
        JavaMailSenderImpl ms = new JavaMailSenderImpl();
        ms.setHost("smtp.gmail.com");
        ms.setPort(587);
        ms.setUsername("noreply.mybudget@gmail.com");
        ms.setPassword("Notorious05261982**");
        ms.setJavaMailProperties(gmailProps());
        return ms;
    }
    
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MimeMessageHelper gmailMimeMessageHelper() {
        MimeMessage mm = ((JavaMailSenderImpl) gmailMailSender()).createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mm, "UTF-8");
        return message;
    }

    Properties gmailProps() {
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.mime.charset", "UTF-8");
        props.setProperty("mail.transport.protocol", "smtp");
        return props;
    }

    @Bean
    public Supplier<Payment> paymentSupplier() {
        return () -> new Payment();
    }

    @Bean
    public VelocityEngine velocityEngine() {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty("resource.loader", "class");
        ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        return ve;
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public VelocityContext velocityContext() {
        return new VelocityContext();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public StringWriter stringWriter() {
        return new StringWriter();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public PaymentEnvelope paymentEnvelope() {
        return new PaymentEnvelope();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public User user(){
        return new User();
    }
    
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Bill bill(){
        return new Bill();
    }
    
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Address address(){
        return new Address();
    }
    
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Income income(){
        return new Income();
    }
    
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Paycheck paycheck(){
        return new Paycheck();
    }
    
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Payment payment(){
        return new Payment();
    }
    
    @Bean
    public UserDao userDao(){
        return new UserDao();
    }
    
    @Bean
    public BillDao billDao(){
        return new BillDao();
    }
    
    @Bean
    public AddressDao addressDao(){
        return new AddressDao();
    }
    
    @Bean
    public PaymentDao paymentDao(){
        return new PaymentDao();
    }

}
