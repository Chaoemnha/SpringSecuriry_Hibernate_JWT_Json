package jmaster.io.restapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class SecurityConfig{//Cũ implements UserDetailsService=> gây lỗi vì gặp bải 1 bean overrideed=>ko rõ nguồn gốc và tham số, ý nói là username
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

    @Bean
    PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configure) throws Exception{
		return configure.getAuthenticationManager(); //Ko cần set service và encode vì nó có sẵn trg phthuc get này r
	}

//	Cơ chế HD của @Autowired: vd @Autowired private UserDetailsService customUserDetailsService; thì nó sẽ tiêm các phương thức ghi đè or 
//	mđinh của UserDetailsService, và lấy tên customUserDetailsService như là ID, để tránh nhầm lẫn vs các phthuc trùng tên của interface khác.
//	@Bean KHÔNG CẦN NX VÌ CÓ 1 BEAN TRONG USERSERVICE R, CÁI NÀY LÀ CŨ DÙNG SECU CƠ BẢN MS DÙNG, H DÙNG NÓ NHƯNG MODIFY NÓ THEO CUSTOMUSER...
//	public UserDetailsService userDetailsService() {
//		return new UserDetailsService() {
//			@Override
//			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http.cors(withDefaults())
	    		.csrf(csrf -> csrf.disable())	//Bảo vệ tấn công mạng, có khi chặn ta mà ko bt lý do
	    		.authorizeHttpRequests(
	            auth -> auth.requestMatchers("/home","/login","/register").permitAll()
	            .requestMatchers("/list").hasAuthority("ROLE_USER") 	//dòng này để chỉ cho request từ user
	            .requestMatchers("/update", "/delete","/addProduct","/deleteProduct","/updateProduct").hasAuthority("ROLE_ADMIN") //Ở hàm bên trên thì có .roles()<=>authorities(ROLE_) nên cẩn thận
	            .anyRequest().authenticated()					//mọi req khác permitAll đều cần xác thực
	           )
//	    		.rememberMe(withDefaults()) ko cần vì h có bearer token r
	    		.logout(logout->logout.logoutSuccessUrl("/login?logout").permitAll());
	    http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	    
	    		//.rememberMe(rememberMe -> rememberMe.key("uniqueAndSecret")); //<=>.rememberMe().key("uniqueAndSecret")
	    return http.build();
	}
}
