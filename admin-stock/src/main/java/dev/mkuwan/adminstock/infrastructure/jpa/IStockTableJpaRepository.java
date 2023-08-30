package dev.mkuwan.adminstock.infrastructure.jpa;

import dev.mkuwan.adminstock.infrastructure.entity.StockItemTable;
import dev.mkuwan.adminstock.infrastructure.entity.StockTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IStockTableJpaRepository extends JpaRepository<StockTable, String> {
}
