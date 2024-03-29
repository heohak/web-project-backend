package com.taldate.backend.profile.entity;


import com.taldate.backend.picture.Picture;
import com.taldate.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "profile", schema = "public")
@NoArgsConstructor
@Getter
@Setter
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(mappedBy = "profile")
    private User user;

    @Column
    private String name;

    @Column
    private int age;

    @Column
    private boolean genderPreferenceMale;

    @Column
    private String bio;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "picture_id")
    private Picture picture;

    @Column
    private boolean profileActive;

    @Column
    private boolean genderMale;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(id, profile.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
