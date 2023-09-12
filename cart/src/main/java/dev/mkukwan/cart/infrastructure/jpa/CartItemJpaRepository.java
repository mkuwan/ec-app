package dev.mkukwan.cart.infrastructure.jpa;

import dev.mkukwan.cart.infrastructure.entity.CartItemEntity;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemJpaRepository extends JpaRepository<CartItemEntity, String> {
    Optional<CartItemEntity> findByCartIdAndItemId(String cartId, String itemId);

    Optional<List<CartItemEntity>> searchCartItemEntitiesByCartId(String cartId);

    Optional<List<CartItemEntity>> findAllByCartId(String cartId);

}
