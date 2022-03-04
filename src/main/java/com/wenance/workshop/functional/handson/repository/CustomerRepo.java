package com.wenance.workshop.functional.handson.repository;

import com.wenance.workshop.functional.handson.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepo extends CrudRepository<Customer, Long> {

    List<Customer> findAll();
}
