package com.taldate.backend.profile.dto;

import com.taldate.backend.picture.Picture;

public record ProfileDTO(String name,
                         int age,
                         boolean genderPreferenceMale,
                         String bio,
                         Picture picture,
                         boolean profileActive,
                         boolean genderMale) {
}
