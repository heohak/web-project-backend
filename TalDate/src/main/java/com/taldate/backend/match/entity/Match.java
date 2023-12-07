package com.taldate.backend.match.entity;

import com.taldate.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

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
    @JoinColumn(name = "user_id")
    private User userID1;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userID2;

    @Column
    private boolean matchedByBoth;
}
