package com.zhacky.ninjapos.repository;

import com.zhacky.ninjapos.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
