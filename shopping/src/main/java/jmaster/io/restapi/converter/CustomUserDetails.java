package jmaster.io.restapi.converter;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import jmaster.io.restapi.model.Cart;
import jmaster.io.restapi.model.User;
//class mới giúp chuyển các thông tin của User thành UserDetails
//@Component
public class CustomUserDetails implements UserDetails{
	private User user;
	private static final long serialVersionUID = 1L;
//	private RoleRepository roleRepository; ko cần nx vì giờ user có set role rồi
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getRoles().stream().map(role->new SimpleGrantedAuthority("ROLE_"+role.getRole())).collect(Collectors.toList());
	}
	
	public CustomUserDetails(User user) {//Giống setter, ưu tiên constructor
		this.user = user;
	}
	
//	Lần sau rút kinh nghiệm lm biến final của class đi, ta có thể extends nó và class=super đúng nghĩa luôn
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getUsername();
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
}
