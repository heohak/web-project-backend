package com.taldate.backend.user.dto;

import lombok.Builder;

import java.sql.Date;
@Builder
public record UserDTO(String firstName, String lastName, String email, String passwordHash, Date dateOfBirth, boolean genderMale) {
}
