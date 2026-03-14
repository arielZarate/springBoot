package com.arielzarate.infraestructure.persistence.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "phone")
    private String phone;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")//fk client
    private AddressEntity address;

//    @OneToMany(mappedBy = "clientOrders", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<OrderEntity> orders;
}

/**
 *
 * cascade = CascadeType.ALL → si guardás o borrás un cliente, se aplican automáticamente los mismos cambios a sus direcciones.
 *
 *
 */