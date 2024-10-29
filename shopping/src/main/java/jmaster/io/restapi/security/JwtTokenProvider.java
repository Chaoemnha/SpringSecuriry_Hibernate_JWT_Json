package jmaster.io.restapi.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jmaster.io.restapi.converter.CustomUserDetails;

@Component
public class JwtTokenProvider {
	private String currentBearerToken;
	public String getCurrentBearerToken() {
		return currentBearerToken;
	}
	public void setCurrentBearerToken(String currentBearerToken) {
		this.currentBearerToken = currentBearerToken;
	}

	// Đoạn JWT_SECRET này là bí mật, chỉ có phía server biết, kích thước tối thiểu 32 kí tự cho hệ mật HS256
	private final String JWT_SECRET = "abcdefghijklmnopqrstuvwxyz0123456789";
	//Thời gian có hiệu lực của chuỗi jwt
	private final long JWT_EXPIRATION = 604800000L;
	// Tạo ra jwt từ thông tin user
	public String generateToken(CustomUserDetails userDetails) {
		//Khoi tao thoi gian luc thuc hien phuong thuc;
		Date now = new Date();
		Date expiry_date = new Date(now.getTime()+JWT_EXPIRATION);
		//Tạo token hay mã hóa thông tin id của ng dùng, nma ở đây t sẽ lấy username để phù hợp vs loadByUsername sau này
		return Jwts.builder()
					.setSubject(userDetails.getUsername())
					.setIssuedAt(now)
					.setExpiration(expiry_date)
					.signWith(getSigningKey(), SignatureAlgorithm.HS256)
					.compact();
					//.signWith(SignatureAlgorithm.HS512, JWT_SECRET)//bị deprecated
	}
	private Key getSigningKey() {
        byte[] keyBytes = this.JWT_SECRET.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

	// Lấy thông tin user từ jwt hay còn gọi là giải mã jwt để lấy nd jwt
	public String getUsernameFromJwt(String token) {
		try {
			System.out.println("JTP 49");
		Claims claims = Jwts.parserBuilder()
							.setSigningKey(getSigningKey())
							.build()
							.parseClaimsJws(token)
							.getBody();
		System.out.println("JTP 55");
		return claims.getSubject();//Chuyển String thành long
//		getSubject là lấy sub(nội dung) từ setSubject của p mã hóa thôi
		} catch (MalformedJwtException ex) {//còn ko thì ...
          	throw new IllegalArgumentException("claimsJws string is not a valid JWS");
		} catch (ExpiredJwtException ex) {
			throw new IllegalArgumentException("Claims has an expiration timebefore");
		} catch (UnsupportedJwtException ex) {
			throw new IllegalArgumentException("claimsJws argument does not represent an Claims JWS");
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException("claimsJws string is null or empty or only whitespace");
		} catch (SignatureException ex) {
			throw new SignatureException("claimsJws string is null or empty or only whitespace");
		}
		//Phải liệt kê hết catch ngle do parseClaimsJws ném ra ko ct báo lỗi thiếu trường hợp trả về (return)
	}
	//Xác thực token hợp lệ hay không (loại bỏ vì h .parseClaimsJws(token) có thể ném ra ng lệ tg tự r
//	public boolean validateToken(String token) {
//		try {
//			Jwts.parserBuilder().setSigningKey(getSigningKey()).parseClaimsJws(token);//parser: trình biên dịch, .set để set khóa bí mật nhằm đảm bảo thông tin
//			//parseClaims để dịch ra claims của tham số (của token) nếu hợp lệ
//			return true;
//		} catch (MalformedJwtException ex) {//còn ko thì ...
//            ex.printStackTrace();
//        } catch (ExpiredJwtException ex) {
//            ex.printStackTrace();
//        } catch (UnsupportedJwtException ex) {
//        	ex.printStackTrace();
//        } catch (IllegalArgumentException ex) {
//        	ex.printStackTrace();
//        }
//		return false;
//	}
}
