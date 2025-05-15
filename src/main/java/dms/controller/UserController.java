package dms.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dms.entity.User;
import dms.security.JwtUtil;
import dms.security.UserPrincipal;
import dms.service.MyUserDetailsService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private JwtUtil jwtUtil;

//	@Autowired
//	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MyUserDetailsService userDetailsService;

//	http://localhost:8082/user/register
//		
//	{
//	    "username" : "shailu@gmail.com",
//	    "password" : "shailu@123",
//	    "role":"ROLE_ADMIN"
//	}
	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody User user) {
		User createdUser = userDetailsService.createUser(user);
		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
	}

//	@PostMapping("/login")
//	public String login(@RequestBody User user) {
////		Authentication authentication = authenticationManager
////				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
////		SecurityContextHolder.getContext().setAuthentication(authentication);
////
////		List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
//
//		UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
//		return jwtUtil.generateToken(userDetails.getUsername(), roles);
//		return "Hii";
//
//	}
	

//	http://localhost:8082/user/login
//		
//	{
//	    "username" : "ram@gmail.com",
//	    "password" : "ram@123"
//	}
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody User user) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
		
		 if (!(userDetails instanceof UserPrincipal)) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid user details type");
	        }

	        UserPrincipal principal = (UserPrincipal) userDetails;

	        if (!principal.isApproved()) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                    .body("Your account is not approved by the admin yet.");
	        }

		if (passwordEncoder.matches(user.getPassword(), userDetails.getPassword())) {
			List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
					.collect(Collectors.toList());

			String token = jwtUtil.generateToken(userDetails.getUsername(), roles);
//			String token = jwtUtil.generateToken(userDetails.getUsername());
			return ResponseEntity.ok(token);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
		}
	}

}
