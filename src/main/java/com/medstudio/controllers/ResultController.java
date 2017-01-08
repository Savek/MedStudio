package com.medstudio.controllers;

import com.medstudio.models.crud.ResultRepository;
import com.medstudio.models.crud.UserRepository;
import com.medstudio.models.entity.QResult;
import com.medstudio.models.entity.Result;
import com.mysema.query.Tuple;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.NumberExpression;
import org.hibernate.jpa.criteria.expression.function.AggregationFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

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
                        @RequestParam(value="date") @DateTimeFormat(pattern="yyyy-MM-dd") Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        QResult result = QResult.result;
        JPQLQuery query = new JPAQuery (entityManager);

        List<Result> results = query
                .from(result)
                .where(result.user.eq(userId)
                        .and(result.date.month().eq(calendar.get(Calendar.MONTH) + 1))
                        .and(result.date.year().eq(calendar.get(Calendar.YEAR)))
                        .and(result.result_type.eq(resultType)))
                .orderBy(result.date.asc())
                .list(result);

        LinkedHashSet<Date> dateList = results.stream()
                .map(Result::getDate)
                .sorted((d1, d2) -> d1.compareTo(d2))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        LinkedHashMap<Date, Long> finalResults = new LinkedHashMap<>();
        dateList.forEach(v -> {
            int sum = results.stream()
                    .filter(o -> o.getDate().equals(v))
                    .mapToInt(o -> Integer.valueOf(o.getValue())).sum();

            long count = results.stream()
                    .filter(o -> o.getDate().equals(v)).count();

            finalResults.put(v, sum / count);
        });

        return finalResults;
    }

    @RequestMapping("/getResultsDetails/{userId}/{resultType}")
    @ResponseBody
    public List resultsType(@PathVariable Long userId, @PathVariable Long resultType,
                            @RequestParam(value="date") @DateTimeFormat(pattern="yyyy-M-dd") Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        QResult result = QResult.result;
        JPQLQuery query = new JPAQuery (entityManager);

        List<Result> results = query
                .from(result)
                .where(result.user.eq(userId)
                        .and(result.date.dayOfMonth().eq(Calendar.DATE))
                        .and(result.result_type.eq(resultType)))
                .orderBy(result.date.asc())
                .list(result);

        return results;
    }
}
