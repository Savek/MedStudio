package com.medstudio.controllers;

import com.medstudio.models.entity.Config;
import com.medstudio.models.entity.Hospital;
import com.medstudio.models.entity.QConfig;
import com.medstudio.models.entity.QHospital;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.hibernate.HibernateUpdateClause;
import com.mysema.query.jpa.impl.JPAQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Savek on 2017-01-21.
 */
@Controller
public class ConfigController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SessionFactory sessionFactory;

    @RequestMapping("/getConfig/{userId}")
    @ResponseBody
    public Config getConfigByUserId(@PathVariable Long userId) {

        QConfig config = QConfig.config;
        JPQLQuery query = new JPAQuery(entityManager);

        return query
                .from(config)
                .where(config.user.id.eq(userId))
                .list(config)
                .get(0);
    }

    @RequestMapping("/updateConfig")
    @ResponseBody
    public void updateConfig(@RequestBody Config config) {

        QConfig queryConfig = QConfig.config;

        new HibernateUpdateClause(sessionFactory.openSession(), queryConfig)
                .where(queryConfig.id.eq(config.getId()))
                .set(queryConfig.preasure_interval, config.getPreasure_interval())
                .set(queryConfig.pulse_interval, config.getPulse_interval())
                .set(queryConfig.temperature_interval, config.getTemperature_interval())
                .set(queryConfig.start_time, config.getStart_time())
                .set(queryConfig.end_time, config.getEnd_time())
                .execute();
    }
}
