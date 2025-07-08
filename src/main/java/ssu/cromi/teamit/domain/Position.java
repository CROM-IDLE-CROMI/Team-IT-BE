package ssu.cromi.teamit.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;


@Data
@Entity
@Table(name = "position_table")
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "position_tag", nullable = false, unique = true)
    private String tag;
    @Column(name = "position_icon", length = 2083)
    private String icon;

    @ManyToMany(mappedBy = "positions")
    private Set<User> users = new HashSet<>();

    protected Position(){}
}
