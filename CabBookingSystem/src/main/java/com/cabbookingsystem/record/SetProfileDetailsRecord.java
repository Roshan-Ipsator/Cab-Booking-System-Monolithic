package com.cabbookingsystem.record;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SetProfileDetailsRecord(String email,
		@NotBlank(message = "First name can't be null or blank.") String firstName,
		@NotBlank(message = "Last name can't be null or blank.") String lastName,
		@NotBlank(message = "Gender can't be null or blank.") String gender,
		@NotBlank(message = "Phone number can't be null or blank.") @Pattern(regexp = "^[0-9]{10}", message = "Mobile number length must be 10 digits [0-9]") String phone) {
}
