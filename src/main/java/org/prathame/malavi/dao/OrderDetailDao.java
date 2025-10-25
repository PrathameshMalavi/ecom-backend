package org.prathame.malavi.dao;


import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.prathame.malavi.entity.OrderDetail;
import org.prathame.malavi.entity.User;

import java.util.List;

@ApplicationScoped
public class OrderDetailDao implements PanacheRepository<OrderDetail> {

    /**
     * Find all order details for a specific user
     */
    public List<OrderDetail> findByUser(User user) {
        return list("user", user);
    }

    /**
     * Find all order details by order status
     */
    public List<OrderDetail> findByOrderStatus(String status) {
        return list("orderStatus", status);
    }

    /**
     * Delete order detail by ID
     */
    public void deleteById(Integer orderId) {
        delete("id", orderId);
    }
}
