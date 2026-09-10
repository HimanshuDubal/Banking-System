package com.banking.service;

import java.util.List;

public interface EmailService {

	void sendEmail(String to,String subject,String body);
	void sendBulkEmail(List<String> recipients,String subject,String body);
}
