package dms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dms.entity.User;
import dms.security.JwtUtil;
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

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody User user) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

		if (passwordEncoder.matches(user.getPassword(), userDetails.getPassword())) {
			String token = jwtUtil.generateToken(userDetails.getUsername());
//			String token = jwtUtil.generateToken(userDetails);
			return ResponseEntity.ok(token);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
		}
	}

}
