package dms.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dms.entity.User;
import dms.repository.UserRepository;

@Service
public class AdminService {

	@Autowired
	private UserRepository userRepository;

	public User approveUser(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));

		if (user.isApproval()) {
			throw new IllegalStateException("User is already approved");
		}

		user.setApproval(true);
		return userRepository.save(user);
	}
}
