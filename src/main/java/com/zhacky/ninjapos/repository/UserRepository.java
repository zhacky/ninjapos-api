package com.zhacky.ninjapos.repository;

import com.zhacky.ninjapos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    List<User> findByUsernameAndPin(String username, Integer pin);
}
