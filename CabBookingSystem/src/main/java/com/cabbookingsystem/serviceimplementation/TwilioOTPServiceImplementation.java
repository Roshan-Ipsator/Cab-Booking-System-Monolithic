package com.cabbookingsystem.serviceimplementation;

import java.text.DecimalFormat;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cabbookingsystem.config.TwilioConfig;
import com.cabbookingsystem.enums.OtpStatus;
import com.cabbookingsystem.record.OtpResponseRecord;
import com.twilio.type.PhoneNumber;
import com.twilio.rest.api.v2010.account.Message;

@Service
public class TwilioOTPServiceImplementation {
	@Autowired
	private TwilioConfig twilioConfig;

	public OtpResponseRecord sendSMSToVerifyPhoneOtp(String phoneNumber, String otp) {
		OtpResponseRecord otpResponseRecord = null;
		try {
			PhoneNumber to = new PhoneNumber(phoneNumber);// to
			PhoneNumber from = new PhoneNumber(twilioConfig.getPhoneNumber()); // from
			String otpMessage = "Dear user , Your OTP is  " + otp + " for verifying your phone number. Thank You.";
			Message message = Message.creator(to, from, otpMessage).create();
			otpResponseRecord = new OtpResponseRecord(OtpStatus.DELIVERED, otpMessage);
		} catch (Exception e) {
			e.printStackTrace();
			otpResponseRecord = new OtpResponseRecord(OtpStatus.FAILED, e.getMessage());
		}
		return otpResponseRecord;
	}

	public OtpResponseRecord sendSMSToVerifyRideOtp(String phoneNumber, String otp) {
		OtpResponseRecord otpResponseRecord = null;
		try {
			PhoneNumber to = new PhoneNumber(phoneNumber);// to
			PhoneNumber from = new PhoneNumber(twilioConfig.getPhoneNumber()); // from
			String otpMessage = "Dear Passenger , Your OTP is  " + otp + " for sharing with your driver. Thank You.";
			Message message = Message.creator(to, from, otpMessage).create();
			otpResponseRecord = new OtpResponseRecord(OtpStatus.DELIVERED, otpMessage);
		} catch (Exception e) {
			e.printStackTrace();
			otpResponseRecord = new OtpResponseRecord(OtpStatus.FAILED, e.getMessage());
		}
		return otpResponseRecord;
	}

}
