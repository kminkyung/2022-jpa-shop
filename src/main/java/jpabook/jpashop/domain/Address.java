package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Embeddable
@Getter
public class Address {

    protected Address() {} // JPA 스펙에 맞추기 위해

    private String city;
    private String street;
    private String zipcode;
}
