package org.prathame.malavi.entity;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDetails {

    private String orderId;
    private String currency;
    private Integer amount;
    private String key;

}
