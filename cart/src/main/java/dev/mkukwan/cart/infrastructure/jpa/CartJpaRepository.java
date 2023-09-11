package dev.mkukwan.cart.infrastructure.jpa;


import dev.mkukwan.cart.infrastructure.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartJpaRepository extends JpaRepository<CartEntity, String> {

    Optional<CartEntity> findByCartId(String cartId);
    Optional<CartEntity> findByCartIdAndBuyerId(String cartId, String buyerId);

    void deleteByCartId(String cartId);
}
