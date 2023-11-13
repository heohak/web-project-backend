package com.taldate.backend.auth.dto;

import java.sql.Date;

public record RegisterDTO(String firstName, String lastName, String email, String password, Date dateOfBirth, boolean genderMale) {
}
