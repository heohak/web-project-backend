package com.taldate.backend.user.dto;

import java.time.LocalDate;

public record UserDTO(String firstName, String lastName, String email, String password, LocalDate birthDate) {
}
