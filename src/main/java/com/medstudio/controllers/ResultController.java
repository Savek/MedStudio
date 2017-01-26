package com.medstudio.controllers;

import com.medstudio.models.entity.User;
import com.medstudio.models.repository.ResultRepository;
import com.medstudio.models.entity.QResult;
import com.medstudio.models.entity.Result;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.*;

/**
 * Created by Savek on 2016-12-21.
 */

@RestController
public class ResultController {

    @Autowired
    ResultRepository repo;

    @PersistenceContext
    private EntityManager entityManager;

    @RequestMapping("/getResultsMonthAndYear/{userId}/{resultType}")
    public List results(@PathVariable Long userId, @PathVariable Long resultType,
                        @RequestParam(value="date")
                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {

        QResult result = QResult.result;
        JPQLQuery query = new JPAQuery (entityManager);

        return query
                .from(result)
                .where(result.user.id.eq(userId)
                        .and(result.date.month().eq(date.getMonthValue()))
                        .and(result.date.year().eq(date.getYear()))
                        .and(result.resultType.eq(resultType)))
                .orderBy(result.date.asc())
                .groupBy(result.date.dayOfMonth())
                .list(result.date)
                .stream()
                .map(localDateTime -> Timestamp.valueOf(localDateTime))
                .collect(toList());
    }

    @RequestMapping("/getResultsDetails/{userId}/{resultType}")
    public Map resultsType(@PathVariable Long userId, @PathVariable Long resultType,
                            @RequestParam(value="date")
                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {

        QResult result = QResult.result;
        JPQLQuery query = new JPAQuery (entityManager);

        return query
                .from(result)
                .where(result.user.id.eq(userId)
                        .and(result.date.dayOfMonth().eq(date.getDayOfMonth()))
                        .and(result.resultType.eq(resultType)))
                .orderBy(result.date.asc())
                .list(result)
                .stream().collect(
                    toMap(x -> Timestamp.valueOf(x.getDate()), Result::getValue, (v1, v2) -> v1, TreeMap::new));
    }

    @RequestMapping("/getResultsCount")
    public Long resultsCount() {

        QResult result = QResult.result;
        JPQLQuery query = new JPAQuery(entityManager);

        return query
                .from(result)
                .count();
    }

    @RequestMapping("/setResult")
    public void resultsCount(@RequestBody Result result) {

        repo.save(result);
    }
}
