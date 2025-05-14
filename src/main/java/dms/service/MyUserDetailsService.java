package dms.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import dms.entity.User;
import dms.repository.UserRepository;
import dms.security.UserPrincipal;

@Service
public class MyUserDetailsService implements UserDetailsService {
//public class MyUserDetailsService {

	@Autowired
	private UserRepository userRepository;

//	public User createUser(User user) {
//        user.setPassword(new BCryptPasswordEncoder(12).encode(user.getPassword()));
//		return userRepository.save(user);
//	}

	public User createUser(User user) {
		user.setPassword(new BCryptPasswordEncoder(12).encode(user.getPassword()));

		// Ensure role starts with "ROLE_"
		if (!user.getRole().startsWith("ROLE_")) {
			user.setRole("ROLE_" + user.getRole().toUpperCase());
		}

		return userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isEmpty())
			throw new UsernameNotFoundException("User not found!");
		return new UserPrincipal(user.get());
	}
}
