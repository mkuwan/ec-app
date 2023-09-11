package dev.mkukwan.cart.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "CartTable")
public class CartEntity {
    @Id
    private String Id;

    private String cartId;

    private String buyerId;

    @OneToMany
    private List<CartItemEntity> cartItemEntities;


}
