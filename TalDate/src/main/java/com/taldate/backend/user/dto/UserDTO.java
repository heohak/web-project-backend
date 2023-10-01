package com.taldate.backend.user.dto;

import java.sql.Date;
import java.time.LocalDate;

public record UserDTO(String firstName, String lastName, String email, String passwordHash, Date dateOfBirth) {
}
