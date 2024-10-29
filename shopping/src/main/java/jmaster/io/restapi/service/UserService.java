package jmaster.io.restapi.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jmaster.io.restapi.converter.CustomUserDetails;
import jmaster.io.restapi.model.Role;
import jmaster.io.restapi.model.User;
import jmaster.io.restapi.repository.RoleRepository;
import jmaster.io.restapi.repository.UserRepository;

@Service
public class UserService implements UserDetailsService{
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
//	Vòng lặp autowire (phụ thuộc)
//	SecurityConfig phụ thuộc vào UserService để cấu hình phương thức xác thực.
//	UserService lại phụ thuộc vào PasswordEncoder (được cung cấp bởi SecurityConfig).
//	Nhưng PasswordEncoder lại được tạo ra từ SecurityConfig.
	

	public boolean exitsByUsername(String username) {
		System.out.println(userRepository.existsByUsername(username)+"33");
		return userRepository.existsByUsername(username);//vãi ò tự ptrien thêm trg repo mà nó tự hiểu, lại còn sinh thêm mấy exits biến thể
	}
	
	public User createUser(String userName, String passWord, Set<Integer> roleId) {
		Set<Role> roles = new HashSet<>();
		for(int roleid:roleId) {											//Lúc đầu ta truyền vào 1 user có n quyền, tạo roles ms và thêm vào các
			Role role = roleRepository.findById(roleid).orElseThrow(()->new UsernameNotFoundException("48"));			//Role hợp lệ cho riêng user đó
			roles.add(role);									//<=> roles.add(role) vì role là Optional
		}
		User user = new User(userName, passwordEncoder.encode(passWord), roles);
		System.out.println(user);
		System.out.println(53);
		return userRepository.save(user);
	}
	
	public User findAUser(String[] unAndPw) {
		Optional<User> user = userRepository.findByUsername(unAndPw[0]);
		boolean checkSame = user.filter(value->value.getPassword().equals(unAndPw[1])).isPresent();
		if(checkSame)  return userRepository.save(user.get());			//Optional có get để convert sang User =)), đọc gợi ý code ms bt =))
		else return null;
	}
	
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	public List<User> delAUser(int user_id) {
		userRepository.deleteById(user_id);
		return userRepository.findAll();
	}
	
	public User updateAUser(String userName, String passWord, Set<Integer> roleId) {
		if(passWord==null||passWord.equals("")) passWord = userRepository.findByUsername(userName).orElseThrow(()->new UsernameNotFoundException("74")).getPassword();
		Set<Role> roleSet = new HashSet<>();
		for(int roleid:roleId) {								
			Optional<Role> role = roleRepository.findById(roleid);			
			role.ifPresent(value->roleSet.add(value));
		}
		return userRepository.save(new User(userName,passwordEncoder.encode(passWord), roleSet));
	}
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//Tìm username trong database, sau đó chuyển đổi thuộc tính thành kiểu UserDetails
		// TODO Auto-generated method stub
		User user = userRepository.findByUsername(username).orElseGet(null);
		System.out.println("91 US "+user);
		return new CustomUserDetails(user);
}
}
