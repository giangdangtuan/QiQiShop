package com.app85soft.qiqishop.repositories.model;

import com.app85soft.qiqishop.entities.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelRepository extends JpaRepository<Model, Integer>, ModelRepositoryCustom {
    List<Model> findAllByProductId(int productId);
    Model getById(int id);
}
