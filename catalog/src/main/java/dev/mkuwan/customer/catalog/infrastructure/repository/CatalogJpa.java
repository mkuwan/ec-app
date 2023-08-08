package dev.mkuwan.customer.catalog.infrastructure.repository;

import dev.mkuwan.customer.catalog.infrastructure.entity.Catalog;
import org.springframework.data.repository.CrudRepository;

public interface CatalogJpa extends CrudRepository<Catalog, String> {
}
