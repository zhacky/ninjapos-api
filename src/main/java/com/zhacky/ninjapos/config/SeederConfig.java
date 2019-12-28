package com.zhacky.ninjapos.config;

import com.zhacky.ninjapos.model.*;
import com.zhacky.ninjapos.repository.AuthorityRepository;
import com.zhacky.ninjapos.repository.ProductRepository;
import com.zhacky.ninjapos.repository.UnitRepository;
import com.zhacky.ninjapos.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;


@Configuration
public class SeederConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    ProductRepository productRepository;

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        logger.info("Seeding users table...");
        seedUsersTable();
        seedProductsTable();
    }

    private void seedProductsTable() {
        String sql = "SELECT name, code FROM products LIMIT 50";
        List<Product> p = jdbcTemplate.query(sql, (resultSet, rowNum) -> null);
        if (p == null || p.size() <= 0) {
            logger.info("Creating products ...");
            Product product = new Product();
            product.setName("FISH PATER");
            product.setDescription("Cup rice with fish toppings");
            product.setCode("200");
            product.setCost(10.50);
            product.setPrice(15.00);
            product.setLastUpdated(new Date());
            product.setOldPrice(12.00);
            // create unit
            Unit unit = new Unit();
            unit.setName(UnitName.PCK.toString());
            unit = unitRepository.saveAndFlush(unit);

            product.setUnit(unit);

            productRepository.saveAndFlush(product);
        }
    }

    private void seedUsersTable() {
        logger.info("Check if admin user exists ...");
//        String sql = "SELECT username, email FROM users U WHERE U.username = \"admin\" OR " +
//                "U.email = \"test@test.com\" LIMIT 1";
        String sql = "SELECT username, email FROM users WHERE username = 'admin' LIMIT 1";
        List<User> u = jdbcTemplate.query(sql, (resultSet, rowNum) -> null);
        if (u == null || u.size() <= 0) {
            logger.debug("Admin user not found, creating user ...");
            User user = new User();
            user.setUsername("admin");
            user.setEmail("test@test.com");
            user.setPassword(new BCryptPasswordEncoder().encode("password123"));
            user.setPin(123456);
            user.setEnabled(true);
            user.setLocked(false);
            logger.debug("Adding an admin authority ...");
            Authority adminAuthority = new Authority();
            adminAuthority.setName(AuthorityName.ROLE_ADMIN);
            authorityRepository.save(adminAuthority);

            logger.debug("Setting admin user authorities ...");
            List<Authority> authorities = new ArrayList<>();
            Optional<Authority> authority = authorityRepository.findById((long) 1);
            authority.get().setName(AuthorityName.ROLE_ADMIN);
            authorities.add(authority.get());
            user.setAuthorities(authorities);
            userRepository.save(user);
            logger.debug("Admin user added.");
        } else {
            logger.info("Admin user already exists!");
        }
    }
}
