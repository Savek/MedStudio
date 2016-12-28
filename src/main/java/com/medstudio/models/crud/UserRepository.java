package com.medstudio.models.crud;

import com.medstudio.models.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Savek on 2016-12-17.
 */

@Repository
public interface UserRepository extends CrudRepository<User, Long>{

    User findByLogin(String login);
}
