package org.prathame.malavi.entity;


import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "order_track")
public class OrderTrack extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime date;

    private String address;

    @Column(name = "location", nullable = true)
    private Double[] location; // simple array (e.g., [18.5204, 73.8567])

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", referencedColumnName = "orderId", nullable = false)
    private OrderDetail order;

    // Constructors
    public OrderTrack() {}

    public OrderTrack(LocalDateTime date, String address, Double[] location, OrderDetail order) {
        this.date = date;
        this.address = address;
        this.location = location;
        this.order = order;
    }
}
