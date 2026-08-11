package com.banking.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username not Found " + username));
	}

	public User registerUser(UserRegistrationDto registrationDto) {
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
		
		return userRepository.save(user);
	}
	
	public Optional<User> findByUsername(String username){
		return userRepository.findByUsername(username);
	}
	
	public void updateLastLogin(String username) {
		userRepository.findByUsername(username).ifPresent(user -> {
			user.setLastLoginAt(LocalDateTime.now());
			userRepository.save(user);
		});
	}
	
	public User findById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User Not Found"));
	}
}
