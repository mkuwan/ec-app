package dev.mkuwan.adminstock.infrastructure.jpa;

import dev.mkuwan.adminstock.infrastructure.entity.StockItemTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IStockItemTableJpaRepository extends JpaRepository<StockItemTable, String> {
}
