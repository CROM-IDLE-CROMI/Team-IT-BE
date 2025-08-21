package ssu.cromi.teamit.domain;


import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "organization_table")
public class Organization {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "organ_name", nullable = false, unique = true)
    private String name;

    @Column(name = "organ_icon", length = 2083)
    private String icon;

    @ManyToMany(mappedBy = "organizations")
    private Set<User> users = new HashSet<>();

    protected Organization() {}

}