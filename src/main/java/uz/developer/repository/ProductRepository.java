package uz.developer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.developer.entity.product.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity,Integer> {
}
