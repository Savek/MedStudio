package com.medstudio.models.entity;

import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Created by Savek on 2017-01-18.
 */
@Entity
@Table(name = "config")
@Data
public class Config {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName="id")
    @NotNull
    private User user;
    @NotNull
    private Long temperature_interval;
    @NotNull
    private Long temperature_counter;
    @NotNull
    private Long pressure_interval;
    @NotNull
    private Long pressure_counter;
    @NotNull
    private Long pulse_interval;
    @NotNull
    private Long pulse_counter;
}
