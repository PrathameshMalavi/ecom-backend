package org.prathame.malavi.service;

import com.razorpay.Order;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.transaction.Transactional;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.prathame.malavi.Common.KeycloakService;
import org.prathame.malavi.dao.CartDao;
import org.prathame.malavi.dao.OrderDetailDao;
import org.prathame.malavi.dao.ProductDao;
import org.prathame.malavi.dao.UserDao;
import org.prathame.malavi.entity.*;

import java.util.ArrayList;
import java.util.List;


import com.razorpay.RazorpayClient;

@ApplicationScoped
public class OrderDetailService {

    private static final String ORDER_PLACED = "Placed";
    private static final String KEY = "rzp_test_AXBzvN2fkD4ESK";
    private static final String KEY_SECRET = "bsZmiVD7p1GMo6hAWiy4SHSH";
    private static final String CURRENCY = "INR";

    @Inject
    OrderDetailDao orderDetailDao;

    @Inject
    ProductDao productDao;


    @Inject
    KeycloakService keycloak;


    @Inject
    UserDao userDao;

    @Inject
    CartDao cartDao;

    public List<OrderDetail> getAllOrderDetails(String status) {
        List<OrderDetail> orderDetails = new ArrayList<>();

        if ("All".equals(status)) {
            orderDetailDao.findAll().stream().forEach(orderDetails::add);
        } else {
            orderDetailDao.findByOrderStatus(status).forEach(orderDetails::add);
        }

        return orderDetails;
    }

    public List<OrderDetail> getOrderDetails() {
        String currentUser = keycloak.getUsername();
        User user = userDao.findById(currentUser);
        return orderDetailDao.findByUser(user);
    }

    @Transactional
    public void placeOrder(OrderInput orderInput, boolean isSingleProductCheckout) {
        List<OrderProductQuantity> productQuantityList = orderInput.getOrderProductQuantityList();

        for (OrderProductQuantity o : productQuantityList) {
            Product product = productDao.findById(o.getProductId());

            String currentUser = keycloak.getUsername();
            User user = userDao.findById(currentUser);

            OrderDetail orderDetail = new OrderDetail(
                    orderInput.getFullName(),
                    orderInput.getFullAddress(),
                    orderInput.getContactNumber(),
                    orderInput.getAlternateContactNumber(),
                    ORDER_PLACED,
                    product.getProductDiscountedPrice() * o.getQuantity(),
                    product,
                    user,
                    orderInput.getTransactionId()
            );

            // empty the cart if not single product checkout
            if (!isSingleProductCheckout) {
                List<Cart> carts = cartDao.findByUser(user);
                carts.forEach(x -> cartDao.deleteById(x.getCartId()));
            }

            orderDetailDao.persist(orderDetail);
        }
    }

    @Transactional
    public void markOrderAsDelivered(Integer orderId) {
        OrderDetail orderDetail = orderDetailDao.findById(orderId.longValue());

        if (orderDetail != null) {
            orderDetail.setOrderStatus("Delivered");
            orderDetailDao.persist(orderDetail);
//            orderDetailDao.save(orderDetail);
        }
    }

    public TransactionDetails createTransaction(Double amount) {
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject();
            jsonObject.put("amount", (amount * 100));
            jsonObject.put("currency", CURRENCY);

            RazorpayClient razorpayClient = new RazorpayClient(KEY, KEY_SECRET);
            Order order = razorpayClient.orders.create(jsonObject);

            return prepareTransactionDetails(order);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private TransactionDetails prepareTransactionDetails(Order order) {
        String orderId = order.get("id");
        String currency = order.get("currency");
        Integer amount = order.get("amount");

        return new TransactionDetails(orderId, currency, amount, KEY);
    }
}
