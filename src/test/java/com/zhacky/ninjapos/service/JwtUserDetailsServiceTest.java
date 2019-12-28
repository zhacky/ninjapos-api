package com.zhacky.ninjapos.service;

import com.zhacky.ninjapos.model.User;
import com.zhacky.ninjapos.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtUserDetailsServiceTest {

    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private JwtUserDetailsService userDetailsService;

    @Autowired
    UserRepository userRepository;

    private User user;

    @Before
    public void setUp() {
        // setup instances
        user = userRepository.findByUsername("admin");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testLoadUserByUsernameLoadsUserDetailsFromUsername() {
        // given

        String username = "admin";

        // when
        when(mockUserRepository.findByUsername(username)).thenReturn(user);
        userDetailsService = new JwtUserDetailsService(mockUserRepository);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("admin");
    }
}