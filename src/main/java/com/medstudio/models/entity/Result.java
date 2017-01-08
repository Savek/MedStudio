package com.medstudio.models.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Savek on 2016-12-21.
 */

@Entity
@Table(name = "results")
public @Data class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long user;
    private long result_type;
    private String value;
    private Date date;
}
