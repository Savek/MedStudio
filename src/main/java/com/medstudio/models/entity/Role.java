package com.medstudio.models.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Savek on 2017-01-12.
 */
@Entity
@Table(name = "role")
@Data public class Role {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @NotNull
    String role;
}
