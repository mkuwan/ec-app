package dev.mkuwan.adminstock.infrastructure.jpa;

import dev.mkuwan.adminstock.infrastructure.entity.WareHouseTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IWareHouseTableJpaRepository extends JpaRepository<WareHouseTable, String> {
    Optional<List<WareHouseTable>> findWareHouseTablesByWareHouseName(String name);
}
