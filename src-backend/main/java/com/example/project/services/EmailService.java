package com.example.project.services;

import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import java.util.Map;

@Service
public interface EmailService {
    void sendSimpleMessage(String[] to, String subject, String text);
    void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException;
    void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel) throws MessagingException;
}
