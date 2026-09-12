package com.banking.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.dto.UserRegistrationDto;
import com.banking.model.User;
import com.banking.repository.UserRepository;

@Service
@Transactional
public class UserService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailServiceImpl emailServiceImpl;
	
	@Override
	@Cacheable(value = "userDetails", key = "#username")
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		
		if (user == null) {
	        throw new UsernameNotFoundException("Username not found: " + username);
	    }
		
		if (user.getUsername() == null) {

			throw new IllegalStateException(
		           "User row for '" + username + "' has a null username column — check registration.");
		}
		
		return org.springframework.security.core.userdetails.User.builder()
	            .username(user.getUsername())
	            .password(user.getPassword())
	            .roles(user.getRole().name())   // was .roles("") — must be the real role, e.g. "CUSTOMER"
	            .build();
	}


	public User registerUser(UserRegistrationDto registrationDto) {
		System.out.println("DTO username: " + registrationDto.getUsername());
		if(userRepository.existsByUsername(registrationDto.getUsername())) {
			throw new RuntimeException("Username Already Exists");
		}
		if(userRepository.existsByEmail(registrationDto.getEmail())) {
			throw new RuntimeException("Email Already Exists");
		}
		
		User user = new User();
		user.setUsername(registrationDto.getUsername());
		user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
		user.setEmail(registrationDto.getEmail());
		user.setFirstName(registrationDto.getFirstName());
		user.setLastName(registrationDto.getLastName());
		user.setPhoneNumber(registrationDto.getPhoneNumber());
		
		emailServiceImpl.sendEmail(user.getEmail(),
				"Welcome to The Banking System",
				"<h2>Welcome " + user.getUsername() + "!</h2>"
				+"<p>The User has been Registered successfully</p>"
						+"<p>Your username is "+user.getUsername()+"!</p>"
						+"<p>Your password is "+registrationDto.getPassword()+"!</p>"
						+"<p>Use this Credentials for login</p>"
						+"<p>Thank you</p>");
		
		return userRepository.save(user);
	}
	
	public User findByUsername(String username){
		return userRepository.findByUsername(username);
	}
	
/*	public void updateLastLogin(String username) {
		userRepository.findByUsername(username).ifPresent(user -> {
			user.setLastLoginAt(LocalDateTime.now());
			userRepository.save(user);
		});
	}
*/	
	public User findById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User Not Found"));
	}
	
}
