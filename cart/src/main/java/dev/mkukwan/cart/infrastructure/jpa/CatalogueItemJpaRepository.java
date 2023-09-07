package dev.mkukwan.cart.infrastructure.jpa;


import dev.mkukwan.cart.infrastructure.entity.CatalogueItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogueItemJpaRepository extends JpaRepository<CatalogueItemEntity, String> {
//    List<CatalogueItemEntity> getAllAndOrderByCatalogItemName();

}
