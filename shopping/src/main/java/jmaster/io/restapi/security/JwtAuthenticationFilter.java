package jmaster.io.restapi.security;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jmaster.io.restapi.converter.CustomUserDetails;
import jmaster.io.restapi.cotroller.CartController;

public class JwtAuthenticationFilter extends OncePerRequestFilter{
		@Autowired
		private UserDetailsService customUserDetailsService;
		@Autowired
		private JwtTokenProvider jwtTokenProvider;
		
		private Map<String, String> getHeadersInfo(HttpServletRequest request) {

	        Map<String, String> map = new HashMap<String, String>();

	        Enumeration<String> headerNames = request.getHeaderNames();
	        while (headerNames.hasMoreElements()) {
	            String key = (String) headerNames.nextElement();
	            String value = request.getHeader(key);
	            map.put(key, value);
	        }

	        return map;
	    }
		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
				throws ServletException, IOException {
			// TODO Auto-generated method stub
			try {
				//Lấy jwt từ request, nma chx có sẵn nên ta lại tự tạo
//				String jwt = getJwtFromRequest(request);Tính năng này thực hiện trên postman nhưng khó khăn với browser
				String jwt = jwtTokenProvider.getCurrentBearerToken();//Dùng bearer token hiện tại, là bearer từ login
				System.out.println("JAF 36");
				System.out.println(getHeadersInfo(request)+" JAT 53");
				if(StringUtils.hasText(jwt)) {// && jwtTokenProvider.validateToken(jwt)) { KO CẦN VÌ DƯỚI CÓ .GET ĐỂ VALIDATE R
				// Lấy thông tin người dùng từ name... thế thì ta sẽ override loadByUsername của UserDetailsService
				String username = jwtTokenProvider.getUsernameFromJwt(jwt);
				//Vấn đề là mình muốn dùng tham số username để dùng repo tìm, rồi xuất ra 1 đối tượng customUserDetails (là đtg chuyển đổi giữa
				//user (từ repo) sang userDetails
				//Dựa vào vấn đề trên ta ko nên cho overide này vào converter, vậy thử cho vào service
				System.out.println("JAF 42");
				UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
				
				if (userDetails!=null) {
				// Nếu người dùng hợp lệ, set thông tin cho Security Context
					UsernamePasswordAuthenticationToken authtoken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());//tạo token xác thực
					// thiết lập các chi tiết bổ sung lquan đến qtrình xác thực (đc IP của người dùng or session ID) từ httpreq	
					authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					//SecurityContextHolder là kho lưu trữ thông tin xác thực của người dùng, .get để lấy ra context hiện tại, rồi set xác thực để Spring Security biết là đã xác thực (có thể là dk bắt buộc để lm các vc tiếp theo)
					SecurityContextHolder.getContext().setAuthentication(authtoken);
				}
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("JAF 56");
				e.printStackTrace();
			}
			System.out.println("JAF 60");
			filterChain.doFilter(request, response); //Vãi ò thiếu cái này thành ra code đ chạy, bao lâu mà đ để ý, RÚT KINH NGHIỆM SÂU SẮC
			//PHẢI XEM ĐOẠN MÃ HẾT CHƯA RỒI MỚI VIẾT TIẾP HUHUHU MẤT BAO NHIÊU TGIAN
		}
//		private String getJwtFromRequest(HttpServletRequest req) { ko sd vì có currentBearerToken
//			String bearerToken = req.getHeader("Authorization");
//			// Kiểm tra xem header Authorization có chứa thông tin jwt không
//			if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//				return bearerToken.substring(7);
//			}
//			return null;
//		}
}
