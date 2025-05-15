package dms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dms.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
