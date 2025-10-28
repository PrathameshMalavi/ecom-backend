package org.prathame.malavi.service;

import com.razorpay.Order;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.prathame.malavi.common.KeycloakService;
import org.prathame.malavi.common.MailService;
import org.prathame.malavi.dao.CartDao;
import org.prathame.malavi.dao.OrderDetailDao;
import org.prathame.malavi.dao.ProductDao;
import org.prathame.malavi.dao.UserDao;
import org.prathame.malavi.entity.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import com.razorpay.RazorpayClient;
import org.prathame.malavi.util.UserUtilty;

@ApplicationScoped
public class OrderDetailService {

    private static final String ORDER_PLACED = "Placed";
    private static final String RAZOR_PAY_KEY = "rzp_test_RYQrZWZnEz52RQ";
    private static final String RAZOR_PAY_KEY_SECRET = "4hq0ISZTg0t6YuMGB7cLrEHN";
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

    @Inject
    MailService mailService;

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
        System.out.println("Get Order Requested :: ");
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
                    orderInput.getTransactionId(),
                    LocalDate.now()
            );

            // empty the cart if not single product checkout
            if (!isSingleProductCheckout) {
                List<Cart> carts = cartDao.findByUser(user);
                carts.forEach(x -> cartDao.deleteById(x.getCartId()));
            }


            mailService.sendOrderDeliveredMail(user.getUserName() , UserUtilty.getUserFullName(user.getUserFirstName() , user.getUserLastName()), orderDetail.getTransactionId());
            orderDetailDao.persist(orderDetail);
        }
    }

    @Transactional
    public void markOrderAsDelivered(Integer orderId) {
        OrderDetail orderDetail = orderDetailDao.findById(orderId.longValue());
        User user = orderDetail.getUser();

        if (orderDetail != null) {
            orderDetail.setOrderStatus("Delivered");
            mailService.sendOrderDeliveredMail(user.getUserName() , UserUtilty.getUserFullName(user.getUserFirstName() , user.getUserLastName()), orderDetail.getTransactionId());
            orderDetailDao.persist(orderDetail);
//            orderDetailDao.save(orderDetail);
        }
    }

    public TransactionDetails createTransaction(Double amount) {
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject();
            jsonObject.put("amount", (amount * 100));
            jsonObject.put("currency", CURRENCY);

            RazorpayClient razorpayClient = new RazorpayClient(RAZOR_PAY_KEY, RAZOR_PAY_KEY_SECRET);
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

        return new TransactionDetails(orderId, currency, amount, RAZOR_PAY_KEY);
    }
}
