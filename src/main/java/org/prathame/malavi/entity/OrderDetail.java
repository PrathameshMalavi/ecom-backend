package org.prathame.malavi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "order_detail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer orderId;

    private String orderFullName;
    private String orderFullOrder;
    private String orderContactNumber;
    private String orderAlternateContactNumber;
    private String orderStatus;
    private Double orderAmount;

    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "productId", unique = false, nullable = false)
    private Product product;

    @OneToOne
//    @JoinColumn(name = "user_name", referencedColumnName = "userName")
    @JoinColumn(name = "user_name", referencedColumnName = "user_name", unique = false, nullable = false) // Changed
    private User user;

    private String transactionId;

    @Column(name = "date_field")
    private LocalDate orderDate;

    public OrderDetail(String orderFullName, String orderFullOrder, String orderContactNumber, String orderAlternateContactNumber, String orderStatus, Double orderAmount, Product product, User user, String transactionId, LocalDate orderDate) {
        this.orderFullName = orderFullName;
        this.orderFullOrder = orderFullOrder;
        this.orderContactNumber = orderContactNumber;
        this.orderAlternateContactNumber = orderAlternateContactNumber;
        this.orderStatus = orderStatus;
        this.orderAmount = orderAmount;
        this.product = product;
        this.user = user;
        this.transactionId = transactionId;
    }

}