package org.prathame.malavi.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId;


    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "productId", unique = false)
    private Product product;

    @OneToOne
//    @JoinColumn(name = "user_name", referencedColumnName = "userName")
    @JoinColumn(name = "user_name", referencedColumnName = "user_name", unique = false, nullable = false) // Changed
    private User user;


    public Cart(Product product, User user) {
        this.product = product;
        this.user = user;
    }
}