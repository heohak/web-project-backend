package com.taldate.backend.match.entity;

import com.taldate.backend.profile.entity.Profile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "match", schema = "public")
@NoArgsConstructor
@Getter
@Setter
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "profile_id_1", referencedColumnName = "id")
    private Profile profile1;

    @ManyToOne
    @JoinColumn(name = "profile_id_2", referencedColumnName = "id")
    private Profile profile2;

    @Column
    private boolean matchedByBoth;
}
