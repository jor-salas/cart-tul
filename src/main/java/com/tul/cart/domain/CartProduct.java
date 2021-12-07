package com.tul.cart.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartProduct {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;
    @Column
    private UUID productId;
    @Column
    private int quantity;
}
