package com.medstudio.models.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Savek on 2016-12-25.
 */
@Entity
@Table(name = "users")
@Data public class User {

    @Id
    private long id;

    private String name;
    private String surname;
    private String login;
    private String password;
    private String email;
    private Boolean enabled;
    private Byte[] image;
}