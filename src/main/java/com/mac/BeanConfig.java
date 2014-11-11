/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac;

import com.mac.budgetmanager.pojo.entities.Payment;
import com.mac.budgetmanager.pojo.entities.dao.impl.BillRepository;
import com.mac.budgetmanager.processes.PaymentFiler;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.function.Supplier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 *
 * @author Mac
 */
@Configuration
public class BeanConfig {
    
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ArrayList arrayList(){
        return new ArrayList(1);
    }
    
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public HashMap hashMap(){
        return new HashMap(1);
    }
    
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Date date(){
        return new Date();
    }
    
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor tpte = new ThreadPoolTaskExecutor();
        tpte.setCorePoolSize(2);
        tpte.setWaitForTasksToCompleteOnShutdown(true);
        return tpte;
    }
    
    @Bean
    public PaymentFiler paymentFiler(){
        return new PaymentFiler();
    }
    
    @Bean
    public MailSender gmailMailSender(){
        JavaMailSenderImpl ms = new JavaMailSenderImpl();
        ms.setHost("smtp.gmail.com");
        ms.setUsername("noreply.mybudget@gmail.com");
        ms.setPassword("Notorious05261982**");
        ms.setJavaMailProperties(gmailProps());
        return ms;
    }
    
    Properties gmailProps(){
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.mime.charset", "UTF-8");
        props.setProperty("mail.transport.protocol", "smtp");
        return props;
    }
    
    @Bean
    public Supplier<Payment> paymentSupplier(){
        return () -> new Payment();
    }
}
