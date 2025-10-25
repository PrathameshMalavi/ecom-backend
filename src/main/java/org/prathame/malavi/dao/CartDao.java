package org.prathame.malavi.dao;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.prathame.malavi.entity.Cart;
import org.prathame.malavi.entity.User;

import java.util.List;

@ApplicationScoped
public class CartDao implements PanacheRepository<Cart> {

    public Cart save(Cart cart) {
        persist(cart);
        return findById(cart.getCartId().longValue());
    }

    /**
     * Find all cart items by user
     */
    public List<Cart> findByUser(User user) {
        return list("user", user);
    }

    /**
     * Delete cart item by ID
     */
    public void deleteById(Integer cartId) {
        delete("id", cartId);
    }
}