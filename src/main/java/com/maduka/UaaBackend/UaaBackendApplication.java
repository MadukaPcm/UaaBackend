package com.maduka.UaaBackend;

import com.maduka.UaaBackend.models.ApplicationUser;
import com.maduka.UaaBackend.models.Role;
import com.maduka.UaaBackend.repository.RoleRepository;
import com.maduka.UaaBackend.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
//@ComponentScan("com.maduka.UaaBackend.configuration")
public class UaaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(UaaBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder){
		return args -> {
			if(roleRepository.findByAuthority("ADMIN").isPresent()) return;
			Role adminRole = roleRepository.save(new Role("ADMIN"));
			roleRepository.save(new Role("USER"));

			Set<Role> roles = new HashSet<>();
			roles.add(adminRole);

			ApplicationUser admin = new ApplicationUser(1,"admin",passwordEncoder.encode("password"), roles);
			userRepository.save(admin);
		};
	}

}
