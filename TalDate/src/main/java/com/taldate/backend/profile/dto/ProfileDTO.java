package com.taldate.backend.profile.dto;

import lombok.Builder;

@Builder
public record ProfileDTO(String name,
                         int age,
                         boolean genderPreferenceMale,
                         String bio,
                         String picture,
                         boolean profileActive,
                         boolean genderMale) {
}
