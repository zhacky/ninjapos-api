package com.zhacky.ninjapos.repository;

import com.zhacky.ninjapos.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
