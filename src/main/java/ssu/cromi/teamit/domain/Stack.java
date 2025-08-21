package ssu.cromi.teamit.domain;


import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "stack_table")
public class Stack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "stack_tag", nullable = false, unique = true)
    private String tag;
    @Column(name = "stack_icon", length = 2083)
    private String icon;
    @ManyToMany(mappedBy = "stacks")
    private Set<User> users = new HashSet<>();

    protected Stack(){}
}