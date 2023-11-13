package com.taldate.backend.user.dto;

import java.sql.Date;

public record UserDTO(String firstName, String lastName, String email, String passwordHash, Date dateOfBirth, boolean genderMale) {
}
