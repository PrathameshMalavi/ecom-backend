package org.prathame.malavi.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.prathame.malavi.common.KeycloakService;
import org.prathame.malavi.dao.CartDao;
import org.prathame.malavi.dao.ProductDao;
import org.prathame.malavi.dao.UserDao;
import org.prathame.malavi.entity.Cart;
import org.prathame.malavi.entity.Product;
import org.prathame.malavi.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CartService {

    @Inject
    CartDao cartDao;

    @Inject
    ProductDao productDao;

    @Inject
    KeycloakService keycloak;

    @Inject
    UserDao userDao;

    @Transactional
    public void deleteCartItem(Integer cartId) {
        cartDao.deleteById(cartId);
    }

    @Transactional
    public Cart addToCart(Integer productId) {
        Product product = productDao.findById(productId);

        String username = keycloak.getUsername();

        User user = null;
        if (username != null) {
            user = userDao.findById(username);
        }

        List<Cart> cartList = cartDao.findByUser(user);
        List<Cart> filteredList = cartList.stream()
                .filter(x -> x.getProduct().getProductId().equals(productId))
                .collect(Collectors.toList());

        if (!filteredList.isEmpty()) {
            return null;
        }

        if (product != null && user != null) {
            Cart cart = new Cart(product, user);
            return cartDao.save(cart);
        }

        return null;
    }


    public List<Cart> getCartDetails() {
        String username = keycloak.getUsername();
        User user = userDao.findById(username);
        return cartDao.findByUser(user);
    }
}