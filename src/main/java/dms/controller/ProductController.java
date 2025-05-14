package dms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

	@PreAuthorize("hasRole('DEALER')")
	@GetMapping("/getDealerProducts")
	public ResponseEntity<String> getDealerProducts() {
		return new ResponseEntity<>("find Dealer products", HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/getAdminProducts")
	public ResponseEntity<String> getAdminProducts() {
		return new ResponseEntity<>("find Admin products", HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'DEALER', 'SELLER')")
	@GetMapping("/getProducts")
	public ResponseEntity<String> getProducts() {
		return new ResponseEntity<>("find All products", HttpStatus.OK);
	}

}
