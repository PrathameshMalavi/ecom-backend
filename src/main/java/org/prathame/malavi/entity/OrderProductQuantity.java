package org.prathame.malavi.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderProductQuantity {

    private Integer productId;
    private Integer quantity;

}
