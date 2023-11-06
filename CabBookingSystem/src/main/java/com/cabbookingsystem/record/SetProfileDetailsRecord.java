package com.cabbookingsystem.record;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SetProfileDetailsRecord(@NotBlank(message = "First name can't be null or blank.") String firstName,
		@NotBlank(message = "Last name can't be null or blank.") String lastName,
		@NotBlank(message = "Gender can't be null or blank.") String gender) {
}
