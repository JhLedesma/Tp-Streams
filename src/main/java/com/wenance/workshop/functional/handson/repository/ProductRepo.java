package com.wenance.workshop.functional.handson.repository;

import com.wenance.workshop.functional.handson.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends CrudRepository<Product, Long> {

    List<Product> findAll();
}
