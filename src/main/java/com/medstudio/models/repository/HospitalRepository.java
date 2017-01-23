package com.medstudio.models.repository;

import com.medstudio.models.entity.Hospital;
import com.medstudio.models.entity.Result;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Savek on 2017-01-21.
 */
@Repository
public interface HospitalRepository extends CrudRepository<Hospital, Long> {
}
