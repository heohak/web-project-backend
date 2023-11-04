package com.taldate.backend.auth.validator;

import com.taldate.backend.auth.dto.RegisterDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegisterValidator {
    public static boolean isValidDto(RegisterDTO dto) {
        return isValidEmail(dto.email()) &&
                isValidPassword(dto.password()) &&
                isValidName(dto.firstName()) &&
                isValidName(dto.lastName()) &&
                isValidDateOfBirth(dto.dateOfBirth());
    }

    private static boolean isValidEmail(String email) {
        int maxEmailLength = 255;
        String validEmailRegex = "^[A-Za-z0-9._-]+@[A-Za-z0-9-]+\\.[A-Za-z]{2,}$";
        if (email != null && email.length() <= maxEmailLength) {
            Pattern pattern = Pattern.compile(validEmailRegex);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
        return false;

    }

    private static boolean isValidPassword(String password) {
        int minPasswordLength = 3;
        int maxPasswordLength = 255;
        return password != null &&
                password.length() >= minPasswordLength &&
                password.length() <= maxPasswordLength;
    }

    private static boolean isValidName(String name) {
        int minNameLength = 1;
        int maxNameLength = 50;
        return name != null &&
                name.length() >= minNameLength &&
                name.length() <= maxNameLength;
    }

    private static boolean isValidDateOfBirth(Date dateOfBirth) {
        int minAge = 18;
        if (dateOfBirth != null) {
            LocalDate birthDate = dateOfBirth.toLocalDate();
            LocalDate now = LocalDate.now();
            Period p = Period.between(birthDate, now);
            return p.getYears() >= minAge;
        }

        return false;
    }
}