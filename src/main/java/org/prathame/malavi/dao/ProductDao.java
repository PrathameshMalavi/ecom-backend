package org.prathame.malavi.dao;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.prathame.malavi.entity.Product;


import java.util.List;

@ApplicationScoped
public class ProductDao implements PanacheRepository<Product> {

    /**
     * Get all products with pagination
     * @param start starting index (0-based)
     * @param size number of items to return
     */
    public List<Product> findAll(int start, int size) {
        return findAll().range(start, start + size - 1).list();
    }

    /**
     * Search products by name or description (case-insensitive) with pagination
     * @param key1 search keyword for product name
     * @param key2 search keyword for product description
     * @param start starting index (0-based)
     * @param size number of items to return
     */
    public List<Product> findByProductNameContainingIgnoreCaseOrProductDescriptionContainingIgnoreCase(
            String key1, String key2, int start, int size
    ) {
        return find(
                "lower(productName) like ?1 or lower(productDescription) like ?2",
                "%" + key1.toLowerCase() + "%", "%" + key2.toLowerCase() + "%"
        ).range(start, start + size - 1).list();
    }

    /**
     * Find product by ID
     */
    public Product findById(Integer productId) {
        return find("id", productId).firstResult();
    }
}