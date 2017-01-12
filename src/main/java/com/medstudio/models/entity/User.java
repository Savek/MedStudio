package com.medstudio.models.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Created by Savek on 2016-12-25.
 */
@Entity
@Table(name = "user")
@Data public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    private String name;
    private String surname;
    private String login;
    private String password;
    private String email;
    private Boolean enabled;
    private Byte[] image;

    @OneToMany
    private Collection<Hospital> hospitals = new ArrayList<>();

    @OneToMany
    private List<Role> roles = new ArrayList<>();
}