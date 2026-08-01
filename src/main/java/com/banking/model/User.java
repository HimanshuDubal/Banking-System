package com.banking.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "users")
public class User implements UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotBlank
	@Column(unique = true)
	private String username;
	
	@NotBlank
	private String password;
	
	@Email
	@Column(unique = true)
	private String email;
	
	@NotBlank
	private String firstName;
	
	@NotBlank
	private String lastName;
	
	@Pattern(regexp = "\\d{10}")
	private String phoneNumber;
	
	@Enumerated(EnumType.STRING)
	private Role role = Role.CUSTOMER;
	
	private boolean enabled = true;
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	
	private LocalDateTime createdAt = LocalDateTime.now();
	private LocalDateTime lastLoginAt;
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private Set<Account> accounts;
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private Set<LoanApplication> loanApplications;
	
	public User(){
		
	}
	
	public User(String username, String password, String email, String firstName, String lastName, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
	    this.lastName = lastName;
	    this.phoneNumber = phoneNumber;
	}
	
	//UserDetils implementation
	 @Override
	 public Collection<? extends GrantedAuthority> getAuthorities() {
		 return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
	 }

	 @Override
	 public boolean isAccountNonExpired() {
        return accountNonExpired;
     }

	 @Override
	 public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
    	return enabled;
	}

	@Override
	public @Nullable String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	//Getter and Setter
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getLastLoginAt() {
		return lastLoginAt;
	}

	public void setLastLoginAt(LocalDateTime lastLoginAt) {
		this.lastLoginAt = lastLoginAt;
	}

	public Set<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}

	public Set<LoanApplication> getLoanApplications() {
		return loanApplications;
	}

	public void setLoanApplications(Set<LoanApplication> loanApplications) {
		this.loanApplications = loanApplications;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}
	
}
