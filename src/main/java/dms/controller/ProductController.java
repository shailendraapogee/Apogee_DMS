package dms.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dms.entity.Product;
import dms.service.ProductService;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Value("${product.path.images}")
	private String imagepath;

	// Base URL to serve image publicly (must match static resource config)
	private static final String BASE_IMAGE_URL = "http://localhost:8082/images";

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

////	http://localhost:8082/product/getProducts
//	@PreAuthorize("hasAnyRole('ADMIN', 'DEALER', 'SELLER')")
//	@GetMapping("/getProducts")
//	public ResponseEntity<String> getProducts() {
//		return new ResponseEntity<>("find All products", HttpStatus.OK);
//	}

//	http://localhost:8082/product/getProducts
	@PreAuthorize("hasAnyRole('ADMIN', 'DEALER', 'SELLER')")
	@GetMapping("/getProducts")
	public ResponseEntity<List<Product>> getAllProducts() {
		List<Product> products = productService.getAllProducts(BASE_IMAGE_URL);
		return new ResponseEntity<>(products, HttpStatus.OK);
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
	
	
//	@PostMapping("/upload")
//    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
//        Map<String, String> response = new HashMap<>();
//
//        if (file.isEmpty()) {
//            response.put("status", "failed");
//            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//        }
//
//        try {
//            // Save file to "uploads/" folder (you can change the path)
//            String uploadDir = "uploads/";
//            File dir = new File(uploadDir);
//            if (!dir.exists()) dir.mkdirs();
//
//            String filePath = uploadDir + file.getOriginalFilename();
//            file.transferTo(new File(filePath));
//
//            response.put("status", "success");
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.put("status", "failed");
//            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

}
