package com.medstudio.models.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Savek on 2017-01-12.
 */
@Entity
@Table(name = "hospital")
@Data public class Hospital {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    @NotNull
    private String name;
    @NotNull
    private String country;
    @NotNull
    private String city;
    @NotNull
    private String adress;
    @NotNull
    private String passCode;

}
