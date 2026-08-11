package com.banking.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.dto.UserRegistrationDto;
import com.banking.model.User;
import com.banking.security.JwtUtil;
import com.banking.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto){
		try {
			User user = userService.registerUser(registrationDto);
			Map<String, Object> response = new HashMap<>();
			response.put("message", "User Registered Successfully");
			response.put("userId", 	user.getId());
			return new ResponseEntity<>(HttpStatus.OK);
		}catch(RuntimeException ex) {
			return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody Map<String,String> loginRequest){
		try {
			String username = loginRequest.get("username");
			String password = loginRequest.get("password");
			
			Authentication auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(username, password));
			
			User user = (User)auth.getPrincipal();
			String token = jwtUtil.generateToken(user.getUsername());
			
			userService.updateLastLogin(username);
			
			Map<String, Object> response = new HashMap<>();
			response.put("token", token);
			response.put("user", Map.of(
					"id", user.getId(),
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "firstName", user.getFirstName(),
                    "lastName", user.getLastName(),
                    "role", user.getRole()));
			
			return ResponseEntity.ok(response);
		}catch(Exception ex) {
			return ResponseEntity.badRequest().body(Map.of("error","Invalid Credentials"));
		}
	}
}
