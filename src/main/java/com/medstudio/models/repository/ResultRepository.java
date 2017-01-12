package com.medstudio.models.repository;

import com.medstudio.models.entity.Result;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Savek on 2016-12-21.
 */

@Repository
public interface ResultRepository extends CrudRepository<Result, Long> {

    List<Result> findByUser(Long user_id);
}
