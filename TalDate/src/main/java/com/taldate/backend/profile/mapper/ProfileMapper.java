package com.taldate.backend.profile.mapper;

import com.taldate.backend.picture.Picture;
import com.taldate.backend.profile.dto.ProfileDTO;
import com.taldate.backend.profile.dto.ProfileSwipeResponseDTO;
import com.taldate.backend.profile.entity.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileDTO profileToProfileDTO(Profile profile);
    ProfileSwipeResponseDTO profileToProfileSwipeResponseDTO(Profile profile);
    default String map(Picture picture) {
        if (picture == null) {
            return null;
        }
        return picture.getEncodedPicture();
    }
}
