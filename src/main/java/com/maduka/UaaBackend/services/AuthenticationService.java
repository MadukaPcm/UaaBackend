package com.maduka.UaaBackend.services;

import com.maduka.UaaBackend.models.ApplicationUser;
import com.maduka.UaaBackend.models.LoginResponseDTO;
import com.maduka.UaaBackend.models.Role;
import com.maduka.UaaBackend.repository.RoleRepository;
import com.maduka.UaaBackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    public TokenService tokenService;

    public ApplicationUser registerUser(String username, String password){
        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("USER").get();
        Set<Role> authority = new HashSet<>();
        authority.add(userRole);
        return userRepository.save(new ApplicationUser(0, username, encodedPassword, authority));
    }

    public LoginResponseDTO loginUser(String username, String password){
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username,password)
            );
            System.out.println("I have logged in bro.");
            String token = tokenService.generateJwt(auth);

            return new LoginResponseDTO(userRepository.findByUsername(username).get(), token);
        } catch (AuthenticationException e) { // Catch the broader category
            System.out.println("Imezingua kutengeneza token");
            return new LoginResponseDTO(null, "");
        }

    }
}
