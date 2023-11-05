package com.cabbookingsystem.record;

import com.cabbookingsystem.enums.OtpStatus;

public record OtpRequestRecord(OtpStatus status, String message) {

}
