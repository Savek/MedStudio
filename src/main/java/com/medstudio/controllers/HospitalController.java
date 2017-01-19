package com.medstudio.controllers;

import com.medstudio.models.entity.Hospital;
import com.medstudio.models.entity.QHospital;
import com.medstudio.models.entity.User;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by krzysztof.mazur on 2017-01-19.
 */
@Controller
public class HospitalController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SessionFactory sessionFactory;

    @RequestMapping("/getHospitals")
    @ResponseBody
    public List<Hospital> getAllHospitals() {

        QHospital hospital = QHospital.hospital;
        JPQLQuery query = new JPAQuery(entityManager);

        List<Hospital> hospitals = query
                .from(hospital)
                .fetchAll()
                .list(hospital);

        return hospitals;
    }
}
