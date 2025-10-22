package com.arielzarate.infraestructure.persistence.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "client")
@Setter
@Getter
public class ClientEntity extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "phone", nullable = false)
    private String phone;
    @OneToMany(mappedBy = "clientAddress" ,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AddressClientEntity> addresses  ;
    @OneToMany(mappedBy = "clientOrders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderEntity> orders;
}

/**
 *
 * cascade = CascadeType.ALL → si guardás o borrás un cliente, se aplican automáticamente los mismos cambios a sus direcciones.
 *
 * */