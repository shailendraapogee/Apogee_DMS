package dms.security;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

//	Start generate Token
	private static String secretKey;

	JwtUtil() {
		SecureRandom random = new SecureRandom();
		byte[] key = new byte[32]; // 256 bits
		random.nextBytes(key);
		secretKey = Base64.getEncoder().encodeToString(key);
	}

	public String generateToken(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10)) // 10 minutes
				.signWith(getSignedKey(), SignatureAlgorithm.HS256).compact();
	}
	
//	public String generateToken(UserDetails userDetails) {
//	    Map<String, Object> claims = new HashMap<>();
//
//	    // Get role(s) from authorities
//	    List<String> roles = userDetails.getAuthorities()
//	                                    .stream()
//	                                    .map(GrantedAuthority::getAuthority)
//	                                    .collect(Collectors.toList());
//
//	    claims.put("roles", roles);
//
//	    return Jwts.builder()
//	            .setClaims(claims)
//	            .setSubject(userDetails.getUsername())
//	            .setIssuedAt(new Date(System.currentTimeMillis()))
//	            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
//	            .signWith(getSignedKey(), SignatureAlgorithm.HS256)
//	            .compact();
//	}


	private Key getSignedKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

//	End generate Token

//	When come any request so check token is correct or not  --> JwtRequestFilter
	public Boolean validToken(String token, String username) {
		return (extractUsername(token).equals(username) && !isTokenExpired(token));
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

//	public List<String> extractRole(String token) {
//		return extractClaim(token, claims -> claims.get("roles", List.class));
//	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = Jwts.parserBuilder().setSigningKey(getSignedKey()).build().parseClaimsJws(token)
				.getBody();
		return claimsResolver.apply(claims);
	}

}
