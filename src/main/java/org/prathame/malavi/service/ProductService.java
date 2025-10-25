package org.prathame.malavi.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.prathame.malavi.configuration.JwtRequestFilter;
import org.prathame.malavi.dao.CartDao;
import org.prathame.malavi.dao.ProductDao;
import org.prathame.malavi.dao.UserDao;
import org.prathame.malavi.entity.Cart;
import org.prathame.malavi.entity.Product;
import org.prathame.malavi.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class ProductService {

    @Inject
    ProductDao productDao;

    @Inject
    UserDao userDao;

    @Inject
    CartDao cartDao;

    public Product addNewProduct(Product product) {
        productDao.persist(product);
        return product;
    }

    public List<Product> getAllProducts(int pageNumber, String searchKey) {
        int pageSize = 12;

        if (searchKey == null || searchKey.trim().isEmpty()) {
            return productDao.findAll()
                    .page(pageNumber, pageSize)
                    .list();
        } else {
            return productDao.find("LOWER(productName) LIKE ?1 OR LOWER(productDescription) LIKE ?2",
                            "%" + searchKey.toLowerCase() + "%", "%" + searchKey.toLowerCase() + "%")
                    .page(pageNumber, pageSize)
                    .list();
        }
    }

    public Product getProductDetailsById(Integer productId) {
        return productDao.findById(productId);
    }

    public void deleteProductDetails(Integer productId) {
        productDao.delete("id", productId);
    }

    public List<Product> getProductDetails(boolean isSingleProductCheckout, Integer productId) {
        if (isSingleProductCheckout && productId != 0) {
            List<Product> list = new ArrayList<>();
            Product product = productDao.findById(productId);
            if (product != null) list.add(product);
            return list;
        } else {
            String username = JwtRequestFilter.CURRENT_USER;
            User user = userDao.findById(username);
            if (user == null) return new ArrayList<>();

            List<Cart> carts = cartDao.findByUser(user);
            return carts.stream()
                    .map(Cart::getProduct)
                    .collect(Collectors.toList());
        }
    }
}