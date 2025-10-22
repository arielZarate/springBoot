package com.arielzarate.domain.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Setter
@Getter
public class Order {
        private Long orderId;
        private Client client; // referencia al cliente
        private List<Product> products; // productos que tiene el pedido
        private LocalDateTime orderDate; // fecha del pedido

}
