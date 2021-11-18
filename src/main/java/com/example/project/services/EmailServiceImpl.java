package com.example.project.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;
@Service

public class EmailServiceImpl implements EmailService{

    private static final String SERVICE_ADDRESS = "elidonpixel3@gmail.com";
    private final JavaMailSender emailSender;
    private final SpringTemplateEngine thymeLeafTemplateEngine;

    public EmailServiceImpl(JavaMailSender emailSender,SpringTemplateEngine thymeLeafTemplateEngine) {
        this.emailSender = emailSender;
        this.thymeLeafTemplateEngine = thymeLeafTemplateEngine;

    }

    @Override
    public void sendSimpleMessage(String[] to, String subject, String text) {
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(SERVICE_ADDRESS);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message=emailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,true,"UTF-8");
        helper.setFrom(SERVICE_ADDRESS);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody,true);
        emailSender.send(message);

    }

    @Override
    public void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel) throws MessagingException {
    Context thymeLeafContext=new Context();
        thymeLeafContext.setVariables(templateModel);
        String htmlBody=thymeLeafTemplateEngine.process("notification-email.html", thymeLeafContext);
        sendHtmlMessage(to, subject, htmlBody);
    }

}
