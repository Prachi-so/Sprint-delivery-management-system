package com.lpu.auth_service.service;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.lpu.auth_service.entity.User;
import com.lpu.auth_service.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

	    @Mock
	    private UserRepository repo;

	    @InjectMocks
	    private CustomUserDetailsService service;

	    // ================= SUCCESS =================

	    @Test
	    void testLoadUserByUsernameSuccess() {

	        String email = "amit@lpu.com";

	        User user = new User();
	        user.setEmail(email);
	        user.setPassword("encodedPass");
	        user.setRole("ROLE_USER");

	        Mockito.when(repo.findByEmail(email))
	                .thenReturn(Optional.of(user));

	        UserDetails response = service.loadUserByUsername(email);

	        Assertions.assertEquals(email, response.getUsername());
	        Assertions.assertEquals("encodedPass", response.getPassword());

	        Assertions.assertTrue(
	                response.getAuthorities().stream()
	                        .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))
	        );
	    }

	    // ================= FAILURE =================

	    @Test
	    void testLoadUserByUsername_UserNotFound() {

	        String email = "notfound@lpu.com";

	        Mockito.when(repo.findByEmail(email))
	                .thenReturn(Optional.empty());

	        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
	            service.loadUserByUsername(email);
	        });
	    }
}
