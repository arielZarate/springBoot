package com.arielzarate.infraestructure.persistence.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "orders")
@Setter
@Getter
public class OrderEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)  // columna FK en la tabla order
    private ClientEntity clientOrders; // Relación ManyToOne con ClientEntity en el mappedBy

    private LocalDateTime orderDate; // fecha del pedido

    @ManyToMany
    @JoinTable(
            name = "order_product",     // tabla intermedia para la relación Many-to-Many
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<ProductEntity> products;

 /**
  ✅ El lado inverso (no dueño) de la relación.

  ✅ mappedBy = "products" coincide con el nombre de la lista en OrderEntity.

  ✅ Sin @JoinTable, ya que se define del otro lado.
  * */

}
