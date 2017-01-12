package com.medstudio.controllers;

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
    @ResponseBody
    public Map results(@PathVariable Long userId, @PathVariable Long resultType,
                        @RequestParam(value="date")
                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {

        QResult result = QResult.result;
        JPQLQuery query = new JPAQuery (entityManager);

        List<Result> results = query
                .from(result)
                .where(result.user.eq(userId)
                        .and(result.date.month().eq(date.getMonthValue()))
                        .and(result.date.year().eq(date.getYear()))
                        .and(result.resultType.eq(resultType)))
                .orderBy(result.date.asc())
                .list(result);

        Map finalResults = results.stream()
                .collect(groupingBy(Result::getDate,
                        TreeMap::new,
                        averagingLong(Result::getValue)
                        ));

        return finalResults;
    }

    @RequestMapping("/getResultsDetails/{userId}/{resultType}")
    @ResponseBody
    public List resultsType(@PathVariable Long userId, @PathVariable Long resultType,
                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {

        QResult result = QResult.result;
        JPQLQuery query = new JPAQuery (entityManager);

        List<Result> results = query
                .from(result)
                .where(result.user.eq(userId)
                        .and(result.date.dayOfMonth().eq(date.getDayOfMonth()))
                        .and(result.resultType.eq(resultType)))
                .orderBy(result.date.asc())
                .list(result);

        return results;
    }
}
