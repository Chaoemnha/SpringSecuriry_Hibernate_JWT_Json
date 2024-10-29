package jmaster.io.restapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jmaster.io.restapi.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{
	Optional<Product> findByPname(String pname);
	boolean existsByPname(String pname);
}
