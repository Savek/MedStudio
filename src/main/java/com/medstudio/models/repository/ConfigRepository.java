package com.medstudio.models.repository;

import com.medstudio.models.entity.Config;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Savek on 2017-01-21.
 */
@Repository
public interface ConfigRepository extends CrudRepository<Config, Long> {

}
