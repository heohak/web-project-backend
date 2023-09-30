package com.taldate.backend.user.dto;

import java.sql.Date;

public record UserDTO(String firstName, String lastName, String email, String password_hash, Date birthDate) {
}
