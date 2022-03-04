package com.wenance.workshop.functional.handson.repository;

import com.wenance.workshop.functional.handson.model.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends CrudRepository<Order, Long> {

    List<Order> findAll();
}
