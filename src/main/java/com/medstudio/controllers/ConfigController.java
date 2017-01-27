package com.medstudio.controllers;

import com.medstudio.models.entity.Config;
import com.medstudio.models.entity.Hospital;
import com.medstudio.models.entity.QConfig;
import com.medstudio.models.entity.QHospital;
import com.medstudio.models.repository.ConfigRepository;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.hibernate.HibernateUpdateClause;
import com.mysema.query.jpa.impl.JPAQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Savek on 2017-01-21.
 */
@RestController
public class ConfigController {

    @Autowired
    ConfigRepository repo;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SessionFactory sessionFactory;

    @RequestMapping("/getConfig/{userId}")
    public List getConfigByUserId(@PathVariable Long userId) {

        QConfig config = QConfig.config;
        JPQLQuery query = new JPAQuery(entityManager);

        List res = query
                .from(config)
                .where(config.user.id.eq(userId))
                .list(config);

        if (res.size() > 0) {
            return res;
        } else {
            return new ArrayList<Config>() {{add(new Config());}};
        }
    }

    @RequestMapping("/updateConfig")
    public void updateConfig(@RequestBody Config config) {

        repo.save(config);
    }
}
