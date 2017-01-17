package com.medstudio.controllers;

/**
 * Created by Savek on 2016-12-17.
 */

import com.medstudio.models.entity.QUser;
import com.medstudio.models.entity.Role;
import com.medstudio.models.entity.User;
import com.medstudio.models.repository.UserRepository;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.hibernate.HibernateSubQuery;
import com.mysema.query.jpa.hibernate.HibernateUpdateClause;
import com.mysema.query.jpa.impl.JPAQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
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

    @RequestMapping("/userInfo/{userId}")
    @ResponseBody
    public User userWithId(@PathVariable Long userId) {

        AbstractAuthenticationToken auth = (AbstractAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();

        Boolean isAdminOrDoc = ((UserDetails) auth.getPrincipal()).getAuthorities()
                .stream().map(GrantedAuthority::toString).reduce("", String::concat).matches("ROLE_DOC|ROLE_ADMIN");
        System.out.println("isAdminOrDoc " + isAdminOrDoc.toString());
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
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        QUser queryUser = QUser.user;

        new HibernateUpdateClause(sessionFactory.openSession(), queryUser)
                .where(queryUser.id.eq(user.getId()))
                .set(queryUser.name, user.getName())
                .set(queryUser.surname, user.getSurname())
                //.set(queryUser.image, user.getImage())
                .set(queryUser.email, user.getEmail())
                .execute();

        return repo.findByLogin(user.getLogin());
    }

    @RequestMapping("/getPatients/{userId}")
    @ResponseBody
    public List<User> patients(@PathVariable Long userId) {

        QUser user = QUser.user;
        JPQLQuery query = new JPAQuery (entityManager);

        List<User> patients = query
                .from(user)
                .where(user.hospital.in(new HibernateSubQuery().from(user).where(user.id.eq(userId)).list(user.hospital)))
                .list(user)
                .stream()
                .filter(user1 -> user1.getRoles().stream().map(Role::toString).reduce("", String::concat).contains("ROLE_PATIENT"))
                .collect(Collectors.toList());

        return patients;
    }
}

