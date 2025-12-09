package africanroyals.service;

import africanroyals.model.Product;
import africanroyals.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void addProduct(String name, int price, String imagePath) {
        Product product = new Product(name, price, imagePath);
        productRepository.save(product);
    }

    public Product getById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}

