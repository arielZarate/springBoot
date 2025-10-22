package com.arielzarate.infraestructure.persistence.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "address_client")
@Entity
public class AddressClientEntity extends AddressBaseEntity{

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)// Nombre de la columna FK en la DB
    private ClientEntity clientAddress;  // Relación ManyToOne con ClientAddress en la db pero clientAddress en mappedBy de ClientEntity

}


/**
 No podés usar @Column junto con @ManyToOne.
 Las relaciones usan @JoinColumn, no @Column.
 * */