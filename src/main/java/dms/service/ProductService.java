package dms.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dms.entity.Product;
import dms.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public String uploadImageInDirectory(String path, MultipartFile file) {
		String originalFileName = file.getOriginalFilename();

		try {
			// Ensure the directory exists
			File folder = new File(path);
			if (!folder.exists()) {
				folder.mkdirs(); // mkdirs handles nested dirs
			}

			// Full path to save the file
			Path fullPath = Paths.get(path, originalFileName);

			// Save or overwrite the file
			Files.copy(file.getInputStream(), fullPath, StandardCopyOption.REPLACE_EXISTING);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return originalFileName;
	}

	public Product saveProduct(Product product) {
		Product savedProduct = productRepository.save(product);
		return savedProduct;
	}
	
	
	 public List<Product> getAllProducts(String baseImageUrl) {
	        List<Product> products = productRepository.findAll();
	        for (Product product : products) {
	            product.setImageUrl(baseImageUrl + "/" + product.getFileName());
	        }
	        return products;
	    }
}
