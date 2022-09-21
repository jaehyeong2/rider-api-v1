package jjfactory.simpleapi.business.seller.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
public class Seller {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sellerCode;
    private String tell;



}
