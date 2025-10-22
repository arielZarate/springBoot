package com.arielzarate.infraestructure.persistence.model;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "product")
@Setter
@Getter
public class ProductEntity extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    // Relación bidireccional Many-to-Many
    @ManyToMany(mappedBy = "products" ,fetch = FetchType.LAZY) // Lado inverso de la relación
    private List<OrderEntity> orders;



    /**
     *✅ El lado inverso (no dueño) de la relación.
     *
     * ✅ mappedBy = "products" coincide con el nombre de la lista en OrderEntity.
     *
     * ✅ Sin @JoinTable, ya que se define del otro lado.
     * */
}


/**
 * Mucho cuidado al usar lombok en las entidades JPA, ya que puede generar problemas con los proxies y la carga perezosa (lazy loading).
 * En este caso, se ha utilizado @Data y @EqualsAndHashCode(callSuper = true) para incluir los campos de la clase base.
 * Asegúrate de probar bien las entidades y considerar usar @Getter y @Setter en lugar de @Data si encuentras problemas.
 */