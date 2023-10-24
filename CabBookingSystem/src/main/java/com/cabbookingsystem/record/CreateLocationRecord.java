package com.cabbookingsystem.record;

import jakarta.validation.constraints.NotNull;

public record CreateLocationRecord(@NotNull(message = "Location name cannot be null.") String name) {

}
