package com.medstudio.models.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Savek on 2016-12-21.
 */

@Entity
@Table(name = "results")
public @Data class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String user_name;
    private String result_type;
    private String value;
}
