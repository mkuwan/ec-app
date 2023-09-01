package dev.mkuwan.adminstock.infrastructure.jpa;

import dev.mkuwan.adminstock.infrastructure.entity.UserTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserTableJpaRepository extends JpaRepository<UserTable, String> {
}
