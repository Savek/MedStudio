package com.medstudio.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medstudio.models.entity.*;
import com.medstudio.models.repository.HospitalRepository;
import com.medstudio.models.repository.UserRepository;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by krzysztof.mazur on 2017-01-19.
 */
@Controller
public class HospitalController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    HospitalRepository repo;

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

    @RequestMapping("/addHospitalToDB")
    @ResponseBody
    public void addHospital(@RequestBody Hospital hospital) {
        System.out.println(hospital.toString());
        repo.save(hospital);
    }

    @RequestMapping("/removeHospital/{hospId}")
    @ResponseBody
    public void deleteHospital(@PathVariable Long hospId) {

        repo.delete(hospId);
    }
}
