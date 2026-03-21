package com.arielzarate.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Client {
    private Long clientId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Address address;
    private Boolean active;

    // Se eliminó List<Order> orders por arquitectura:
    // La relación debe ser unidireccional: Order -> Client (no al revés)
    // Reasons:
    // - Evita problemas de rendimiento (N+1 queries)
    // - Evita referencias circulares
    // - Para obtener orders de un cliente, usar consulta: orderRepository.findByClientId(id)

    // Address: Relación OneToOne
    // Optimización: findAll() no trae address (evita N+1), findById() sí trae detalles
    // Esto evita cargar direcciones cuando se listan muchos clientes
}
