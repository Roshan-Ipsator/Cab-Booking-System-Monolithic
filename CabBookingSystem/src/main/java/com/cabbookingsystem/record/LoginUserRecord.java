package com.cabbookingsystem.record;

import jakarta.validation.constraints.NotNull;

public record LoginUserRecord(@NotNull(message = "Email id cannot be null.") String email) {

}
