package jmaster.io.restapi.cotroller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jmaster.io.restapi.converter.CustomUserDetails;
import jmaster.io.restapi.model.User;
import jmaster.io.restapi.security.JwtTokenProvider;
import jmaster.io.restapi.service.UserService;

@Controller
public class HtmlController {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserService userService;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	private Authentication authentication;
	@RequestMapping("/home")
	public String home() {
		return "home";
	}
	@RequestMapping("/hello")
	public String hello(Model model) {
		model.addAttribute("token",jwtTokenProvider.getCurrentBearerToken());
		return "hello";
	}
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	@PostMapping("/login")
	public String log(HttpServletRequest req) {
		System.out.println(38+req.getParameter("username")+req.getParameter("password"));
		try {
		authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    req.getParameter("username"),
                    req.getParameter("password")
            ));
		System.out.println("auth OK");
		SecurityContextHolder.getContext().setAuthentication(authentication);
		HttpSession session = req.getSession(true);
		session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());//session giữ context lưu lại xác thực đc tạo huhu
		String jwt = jwtTokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
		jwtTokenProvider.setCurrentBearerToken(jwt);
		System.out.println("45"+SecurityContextHolder.getContext());
		System.out.println(jwt);
		return "redirect:/hello";
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return "redirect:/login?error";
		}
	}
	@PostMapping("/register")
	public String createUser(HttpServletRequest requser){//Cũ chx ktra trùng tên, h lm hàm ktra trùng => để trg service, ko thể sdung @RequestBody
		//vì nó chỉ chấp nhận dl post dạng json, ta có serverlet chấp nhận dl post dạng form
		if(!userService.exitsByUsername(requser.getParameter("username"))) {	//vì tồn tại thì ko thêm, ko tồn tại thì thêm =))
		Set<Integer> roleId = new HashSet<>();
		for (String role : requser.getParameterValues("role_id")) {
			roleId.add(Integer.parseInt(role));
		}
		User user = userService.createUser(requser.getParameter("username"), requser.getParameter("password"), roleId);
		System.out.println(user);
		return "redirect:/login";
		}
		else return "redirect:/register?error";
	}
	@GetMapping("/register")
	public String register() {
		return "register";
	}

	@DeleteMapping("/delete")
	public List<User> delete(@RequestParam int user_id) {
		return userService.delAUser(user_id);
	}
	
	@PostMapping("/update")	
	public String update(HttpServletRequest requser) {
		if(userService.exitsByUsername(requser.getParameter("username"))) {	//vì tồn tại thì ko thêm, ko tồn tại thì thêm =))
			Set<Integer> roleId = new HashSet<>();
			for (String role : requser.getParameterValues("role_id")) {
				roleId.add(Integer.parseInt(role));
			}
			User user = userService.updateAUser(requser.getParameter("username"), requser.getParameter("password"), roleId);
			System.out.println(user);
			return "redirect:/update?sucess";
			}
			else return "redirect:/update?error";					
	}
	
	@GetMapping("/update")
	public String respondU(){
		System.out.println("HT 123");
		return "update";
	}
	@RequestMapping("/list")
	public ResponseEntity<List<User>> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());	
	}
}