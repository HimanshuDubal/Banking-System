package com.banking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.banking.dto.AuthResponse;
import com.banking.dto.LoginRequest;
import com.banking.dto.UserRegistrationDto;
import com.banking.model.User;
import com.banking.repository.UserRepository;
import com.banking.security.JwtUtil;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	public AuthResponse login(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String token = jwtUtil.generateToken(userDetails.getUsername());
		User user = userRepository.findByUsername(loginRequest.getUsername());
		
		AuthResponse response = new AuthResponse();
		response.setToken(token);
		response.setRole(user.getRole());
		response.setUserId(user.getId());
		
		return response;
	}
	
	public String register(UserRegistrationDto registrationDto) {
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
		userRepository.save(user);
		
		return "User registered successfully";
	}

	public AuthResponse refresh(String oldToken) {
		String username = jwtUtil.extractUsername(oldToken);
		User user = userRepository.findByUsername(username);
		UserDetails userDetails = userRepository.findByUsername(username);
		String newToken = jwtUtil.generateToken(username);
		
		AuthResponse response = new AuthResponse();
		response.setToken(newToken);
		response.setRole(user.getRole());
		response.setUserId(user.getId());
		return response;
		
	}
}
