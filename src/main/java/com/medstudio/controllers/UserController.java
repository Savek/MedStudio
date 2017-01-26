package com.medstudio.controllers;

/**
 * Created by Savek on 2016-12-17.
 */

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medstudio.models.entity.*;
import com.medstudio.models.repository.UserRepository;
import com.mysema.query.group.GroupBy;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.hibernate.HibernateSubQuery;
import com.mysema.query.jpa.hibernate.HibernateUpdateClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Projections;
import com.mysema.query.types.QTuple;
import com.mysema.query.types.expr.Wildcard;
import org.hibernate.LobHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.SQLInsert;
import org.hibernate.criterion.Projection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.security.Principal;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    UserRepository repo;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SessionFactory sessionFactory;

    @RequestMapping("/userInfo")
    @ResponseBody
    public User user() {
        AbstractAuthenticationToken auth = (AbstractAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();

        String login = auth == null ?null:((UserDetails) auth.getPrincipal()).getUsername();

        if (login != null) {
            return repo.findByLogin(login);
        }
        return null;
    }

    @RequestMapping("/getUsersCounter")
    @ResponseBody
    public Long usersCounter() {

        QUser user = QUser.user;
        JPQLQuery query = new JPAQuery (entityManager);

        return query
                .from(user)
                .fetchAll()
                .count();
    }

    @RequestMapping("/getUsersLast7days")
    @ResponseBody
    public Map userPerDay() {

        QUser user = QUser.user;
        JPQLQuery query = new JPAQuery (entityManager);

        return query
                .from(user)
                .where(user.createDate.after(LocalDateTime.now().minusDays(7)))
                .groupBy(user.createDate.dayOfMonth())
                .transform(GroupBy.groupBy(user.createDate.dayOfMonth()).as(Wildcard.count));
    }

    @RequestMapping("/userInfo/{userId}")
    @ResponseBody
    public User userWithId(@PathVariable Long userId) {

        AbstractAuthenticationToken auth = (AbstractAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();

        Boolean isAdminOrDoc = ((UserDetails) auth.getPrincipal()).getAuthorities()
                .stream().map(GrantedAuthority::toString).reduce("", String::concat).matches("ROLE_DOC|ROLE_ADMIN");

        if (isAdminOrDoc) {
            QUser user = QUser.user;
            JPQLQuery query = new JPAQuery (entityManager);

            List<User> users = query
                    .from(user)
                    .where(user.id.eq(userId))
                    .list(user);

            return users.size() > 0?users.get(0):null;
        } else {
            return null;
        }

    }

    @RequestMapping("/user")
    @ResponseBody
    public Principal user(Principal user) {
        return user;
    }

    @RequestMapping("/updateUser")
    @ResponseBody
    public User updateUser(@RequestBody User user) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User us = repo.findOne(user.getId());
        if (!us.getPassword().equals(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return repo.save(user);
    }

    @RequestMapping("/getPatients/{userId}")
    @ResponseBody
    public List<User> patients(@PathVariable Long userId) {

        QUser user = QUser.user;
        JPQLQuery query = new JPAQuery (entityManager);

        return query
                .from(user)
                .where(user.hospital.in(new HibernateSubQuery().from(user).where(user.id.eq(userId)).list(user.hospital)))
                .list(user)
                .stream()
                .filter(user1 -> user1.getRole().getRole().equals("ROLE_PATIENT"))
                .collect(Collectors.toList());
    }

    @RequestMapping("/deleteUserFromDB/{userId}")
    @ResponseBody
    public void deleteUser(@PathVariable Long userId) {

        repo.delete(userId);
    }

    @RequestMapping("/getUsers")
    @ResponseBody
    public List<User> users() {

        QUser user = QUser.user;
        JPQLQuery query = new JPAQuery (entityManager);

        List<User> users = query
                .from(user)
                .fetchAll()
                .list(user);

        return users;
    }

    @RequestMapping("/getRoles")
    @ResponseBody
    public List<Role> roles() {

        QRole role = QRole.role1;
        JPQLQuery query = new JPAQuery (entityManager);

        List<Role> roles = query
                .from(role)
                .fetchAll()
                .list(role);

        return roles;
    }

    @RequestMapping("/addUserToDB")
    @ResponseBody
    public void addUser(@RequestBody String user) {

        ObjectMapper mapper = new ObjectMapper();
        JPQLQuery query = new JPAQuery (entityManager);

        QHospital hospital = QHospital.hospital;
        QRole role = QRole.role1;

        try {
            Map<String, String> jsonInfo = mapper.readValue(user, new TypeReference<Map<String, String>>(){});

            User newUser = new User();
            newUser.setName(jsonInfo.get("name"));
            newUser.setSurname(jsonInfo.get("surname"));
            newUser.setEmail(jsonInfo.get("email"));
            newUser.setLogin(jsonInfo.get("login"));

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            newUser.setPassword(passwordEncoder.encode(jsonInfo.get("password")));

            newUser.setEnabled(Boolean.valueOf(jsonInfo.get("enabled")));

            List<Hospital> hosp;
            if (jsonInfo.get("hospital_id") != null) {
                hosp = query
                        .from(hospital)
                        .where(hospital.id.eq(Long.valueOf(jsonInfo.get("hospital_id"))))
                        .limit(1)
                        .list(hospital);

                if (hosp.size() > 0) newUser.setHospital(hosp.get(0));
            }

            List<Role> rol;
            if (jsonInfo.get("role_id") != null) {

                rol = query
                        .from(role)
                        .where(role.id.eq(Long.valueOf(jsonInfo.get("role_id"))))
                        .limit(1)
                        .list(role);

                if (rol.size() > 0) newUser.setRole(rol.get(0));
            }

            repo.save(newUser);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

