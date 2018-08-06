package com.simple.restful.simplerestful.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.simple.restful.simplerestful.persistence.model.DataEntity;

@Repository
public interface DataRepository extends JpaRepository<DataEntity, Integer>, JpaSpecificationExecutor<DataEntity> {

}
