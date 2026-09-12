package com.banking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService{

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String fromEmail;

	@Async
	@Override
	public void sendEmail(String to, String subject, String body) {
		try {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
		helper.setFrom(fromEmail,"Banking System");
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(body,true);
		javaMailSender.send(message);
		}catch(Exception e) {
			throw new RuntimeException("Failed to send email to " + to + ": " + e.getMessage(), e);
		}
		
	}

	@Async
	@Override
	public void sendBulkEmail(List<String> recipients, String subject, String body) {
		for(String email : recipients) {
			sendEmail(email, subject, body);
		}
		
	}

}
