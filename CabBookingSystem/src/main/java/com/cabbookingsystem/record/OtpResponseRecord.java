package com.cabbookingsystem.record;

import com.cabbookingsystem.enums.OtpStatus;

public record OtpResponseRecord(OtpStatus otpStatus, String otpMessage) {

}
