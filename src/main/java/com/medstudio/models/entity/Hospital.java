package com.medstudio.models.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by Savek on 2017-01-12.
 */
@Entity
@Table(name = "hospital")
@Data public class Hospital {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String name;
    private String country;
    private String city;
    private String adress;
    private String passCode;
}
