package com.arielzarate.infraestructure.persistence.model;


import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlSchemaTypes;
import lombok.Getter;
import lombok.Setter;


@MappedSuperclass
@Setter
@Getter
public abstract class AddressBaseEntity extends BaseEntity {
    @Column(name = "street")
    private String street;
    @Column(name = "city")
    private String city;
    @Column(name = "state")
    private String state;
    @Column(name = "zip_code")
    private String zipCode;
    @Column(name = "country")
    private String country;

}
