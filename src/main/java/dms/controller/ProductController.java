package dms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dms.entity.Product;
import dms.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Value("${product.path.images}")
	private String imagepath;

//	http://localhost:8082/product/getDealerProducts
	@PreAuthorize("hasRole('DEALER')")
	@GetMapping("/getDealerProducts")
	public ResponseEntity<String> getDealerProducts() {
		return new ResponseEntity<>("find Dealer products", HttpStatus.OK);
	}

//	http://localhost:8082/product/getAdminProducts
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/getAdminProducts")
	public ResponseEntity<String> getAdminProducts() {
		return new ResponseEntity<>("find Admin products", HttpStatus.OK);
	}

//	http://localhost:8082/product/getProducts
	@PreAuthorize("hasAnyRole('ADMIN', 'DEALER', 'SELLER')")
	@GetMapping("/getProducts")
	public ResponseEntity<String> getProducts() {
		return new ResponseEntity<>("find All products", HttpStatus.OK);
	}

//	http://localhost:8082/product/createProduct
	@PreAuthorize("hasAnyRole('ADMIN', 'DEALER', 'SELLER')")
	@PostMapping("createProduct")
//	public ResponseEntity<Product> createProduct(@RequestPart(value = "file") MultipartFile file,
//			@RequestPart(value = "product") Product product) {
	public ResponseEntity<Product> createProduct(@RequestPart(value = "file") MultipartFile file,
			@RequestParam String productName, @RequestParam Double price, @RequestParam Long quantity) {
		Product savedProduct = null;
		try {
			String uploadImageName = productService.uploadImageInDirectory(imagepath, file);

			Product product = new Product();
			product.setFileName(uploadImageName);
			product.setPrice(price);
			product.setQuantity(quantity);
			product.setProductName(productName);
			product.setFilePath(imagepath);
			savedProduct = productService.saveProduct(product);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(savedProduct, HttpStatus.ACCEPTED);
	}

}
