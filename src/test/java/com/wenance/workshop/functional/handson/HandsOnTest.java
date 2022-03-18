package com.wenance.workshop.functional.handson;

import com.wenance.workshop.functional.handson.model.Customer;
import com.wenance.workshop.functional.handson.model.Order;
import com.wenance.workshop.functional.handson.model.Product;
import com.wenance.workshop.functional.handson.repository.CustomerRepo;
import com.wenance.workshop.functional.handson.repository.OrderRepo;
import com.wenance.workshop.functional.handson.repository.ProductRepo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@DataJpaTest
class HandsOnTest {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ProductRepo productRepo;

    @BeforeEach
    void before() {
        log.info("");
        log.info("--------------------------------------------------------------------------------");
    }

    //
    // Obtener una lista de productos que tengan como categoría "Books" y su precio sea mayor a 100
    //
    @Test
    void exercise1() {
        List<Product> result = productRepo.findAll().stream()
                .filter(product -> product.getCategory().equalsIgnoreCase("Books") && product.getPrice() > 100)
                .collect(Collectors.toList());

        // ...

        result.forEach(o -> log.info(o.toString()));

        Assertions.assertEquals(5, result.size());
        // 7, 9, 16, 17, 24
    }

    //
    // Obtener una lista de órdenes con productos de la categoría = "Baby"
    //
    @Test
    void exercise2() {
        List<Order> result = orderRepo.findAll().stream()
                .filter(order ->
                        order.getProducts().stream()
                                .anyMatch(
                                        product -> product.getCategory().equalsIgnoreCase("Baby")
                                )
                )
                .collect(Collectors.toList());

        result.forEach(o -> log.info(o.toString()));

        Assertions.assertEquals(26, result.size());
    }

    //
    // Obtener una lista de productos de la categoría = “Toys” y aplicarles un 10% de descuento
    //
    @Test
    void exercise3() {
        List<Product> result = productRepo.findAll().stream()
                .filter(product -> product.getCategory().equals("Toys"))
                .map(product -> product.withPrice(product.getPrice() * 0.9))
                .collect(Collectors.toList());

        // ...

        result.forEach(o -> log.info(o.toString()));

        Assertions.assertEquals(11, result.size());
    }

    //
    // Obtener una lista de productos ordenados por clientes de tier 2 entre las fechas n 01-Feb-2021 y 01-Apr-2021
    //
    @Test
    void exercise4() {
        List<Product> result = orderRepo.findAll().stream()
                .filter(order -> order.getCustomer().getTier() == 2)
                .filter(order -> order.getOrderDate().compareTo(LocalDate.of(2021, 2, 1)) >= 0)
                .filter(order -> order.getOrderDate().compareTo(LocalDate.of(2021, 4, 1)) <= 0)
                .flatMap(order -> order.getProducts().stream())
                .distinct()
                .collect(Collectors.toList());

        // ...

        result.forEach(o -> log.info(o.toString()));

        Assertions.assertEquals(19, result.size());
    }

    //
    // Buscar el producto de la categoría “Books” más barato
    //
    @Test
    void exercise5() {

        Product result = productRepo.findAll().stream()
                .filter(product -> product.getCategory().equals("Books"))
                .min(Comparator.comparing(Product::getPrice))
                .orElse(new Product());

        //

        log.info("Product: " + result);

        Assertions.assertEquals(17, result.getId());
    }

    //
    // Buscar las 3 órdenes más recientes
    //
    @Test
    void exercise6() {
        List<Order> result = orderRepo.findAll().stream()
                .sorted(Comparator.comparing(Order::getOrderDate).reversed())
                .limit(3)
                .collect(Collectors.toList());

        // ...

        result.forEach(o -> log.info(o.toString()));

        Assertions.assertEquals(3, result.size());
    }

    //
    // Mostrar por pantalla las órdenes con la fecha de orden 15-03-2021 y obtener la lista de productos
    //
    @Test
    void exercise7() {
        List<Product> result = orderRepo.findAll().stream()
                .filter(order -> order.getOrderDate().isEqual(LocalDate.of(2021, 3, 15)))
                .peek(System.out::println)
                .flatMap(order -> order.getProducts().stream())
                .distinct()
                .collect(Collectors.toList());

        // ...

        result.forEach(o -> log.info(o.toString()));

        Assertions.assertEquals(7, result.size());
    }

    //
    // Calcular el precio total de todas la ordenes hechas en la fecha 14-Feb-2021
    //
    @Test
    void exercise8() {
        Double result = orderRepo.findAll().stream()
                .filter(order -> order.getOrderDate().isEqual(LocalDate.of(2021, 3, 15)))
                .flatMap(order -> order.getProducts().stream())
                .mapToDouble(Product::getPrice)
                .sum();
        //

        log.info("Result: " + result);

        Assertions.assertEquals(3528.9, result);
    }

    //
    // Calcular el precio promedio de todas las órdenes hechas en la fecha 15-Mar-2021
    //
    @Test
    void exercise9() {
        Double result = orderRepo.findAll()
                .stream()
                .filter(o -> o.getOrderDate().isEqual(LocalDate.of(2021, 3, 15)))
                .flatMap(o -> o.getProducts().stream())
                .mapToDouble(Product::getPrice)
                .average()
                .orElse(0);

        log.info("Result: " + result);

        Assertions.assertEquals(352.89, result);
    }

    //
    // Obtener las estadísticas de todos los productos de categoría "Books"
    //
    @Test
    void exercise10() {
        DoubleSummaryStatistics result = productRepo.findAll()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase("Books"))
                .mapToDouble(Product::getPrice)
                .summaryStatistics();

        log.info("Result: " + result);

        Assertions.assertEquals(5, result.getCount());
    }

    //
    // Obtener un mapa de los id de órdenes y la cantidad de productos que tienen esas órdenes
    //
    @Test
    void exercise11() {
        Map<Long, Integer> result = orderRepo.findAll().stream()
                .collect(
                        Collectors.toMap(
                                Order::getId,
                                order -> order.getProducts().size()
                        )
                );
        //

        log.info(result.toString());

        Assertions.assertEquals(50, result.size());
    }

    //
    // Obtener un mapa de clientes y la lista de sus ordenes
    //
    @Test
    void exercise12() {
        Map<Customer, List<Order>> result = orderRepo.findAll().stream()
                        .collect(
                            Collectors.groupingBy(order -> order.getCustomer())
                        );

        log.info(result.toString());

        Assertions.assertEquals(10, result.size());
    }

    //
    // Obtener un mapa con las órdenes y el precio total de las mismas
    //
    @Test
    void exercise13() {
        Map<Order, Double> result = orderRepo.findAll()
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                order -> order.getProducts().stream()
                                        .mapToDouble(Product::getPrice)
                                        .sum()
                        )
                );
        //

        log.info(result.toString());

        Assertions.assertEquals(50, result.size());
    }

    //
    // Obtener un mapa del nombre de los productos por categoría
    //
    @Test
    void exercise14() {
        Map<String, List<String>> result = productRepo.findAll().stream()
                .collect(
                        Collectors.groupingBy(
                                Product::getCategory,
                                Collectors.mapping(Product::getName, Collectors.toList())
                        )
                );
        //

        log.info(result.toString());

        Assertions.assertEquals(5, result.size());
    }

    //
    // Buscar el producto mas caro por categoria
    //
    @Test
    void exercise15() {
        Map<String, Optional<Product>> result = productRepo.findAll()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Product::getCategory,
                                Collectors.maxBy(Comparator.comparing(Product::getPrice)))
                );

        result.forEach((k, v) -> log.info("key: " + k + ", value: " + v.get()));

        Assertions.assertEquals(5, result.size());
    }
}
