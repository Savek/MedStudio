package com.medstudio.models.entity;

import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
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
    private User user;
    private Long temperature_interval;
    private Long preasure_interval;
    private Long pulse_interval;

    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime start_time;
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime end_time;
}
