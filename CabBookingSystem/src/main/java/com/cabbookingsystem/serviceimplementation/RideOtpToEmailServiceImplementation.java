package com.cabbookingsystem.serviceimplementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.thymeleaf.context.Context;

@Service
public class RideOtpToEmailServiceImplementation {

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private TemplateEngine templateEngine;

	/**
	 * The method to send verify url to the user's email
	 * 
	 * @param to      the destination email id
	 * @param subject subject line for the email
	 * @param url     the url containing the login confirmation API
	 * 
	 * @return void
	 * 
	 * @throws MessagingException
	 */
	public void sendEmailWithOtp(String to, String subject, String otp) throws MessagingException {
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		// Creating a Thymeleaf context and set variables
		Context context = new Context();
		context.setVariable("otp", otp);

		// Processing the HTML email template
		String htmlContent = templateEngine.process("ride-otp-email-template", context);

		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(htmlContent, true);

		// Sending the email
		emailSender.send(message);
	}
}
