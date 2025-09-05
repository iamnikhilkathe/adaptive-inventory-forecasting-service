package com.nikhil.inventory.adaptiveinventoryforecastingservice.models;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.nikhil.inventory.adaptiveinventoryforecastingservice.entity.User;
import com.nikhil.inventory.adaptiveinventoryforecastingservice.entity.UserRoles;

public class MainUserPrincipal  implements UserDetails{
	
	 private User mainUser;
	
	 

	public MainUserPrincipal(User mainUser) {
		this.mainUser = mainUser;
	}
	
	
	public MainUserPrincipal(Long userId, String username,List<String> role) {
		mainUser=new User();
		Set<UserRoles> roles=new HashSet<UserRoles>();
		role.forEach(x ->{   roles.add(UserRoles.builder().roleName(x).build());});
		mainUser.setId(userId.intValue());
		mainUser.setUserName(username);
		mainUser.setRole(roles);
	}


	public Long getid() {
		return Long.valueOf(mainUser.getId());
	}
	
	
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		
		return mainUser.getRole().stream().map(role-> new SimpleGrantedAuthority("ROLE_"+ role.getRoleName()) ).collect(Collectors.toList());
	}
	
	
	
	@Override
	public String getPassword() {
		return mainUser.getPassword();
	}

	@Override
	public String getUsername() {
		return mainUser.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return mainUser.isActive();
	}

	


	



}
