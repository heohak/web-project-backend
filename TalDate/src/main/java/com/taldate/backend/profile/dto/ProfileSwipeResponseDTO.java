package com.taldate.backend.profile.dto;

public record ProfileSwipeResponseDTO(int id,
                                      String name,
                                      int age,
                                      String bio,
                                      String picture) {
}
