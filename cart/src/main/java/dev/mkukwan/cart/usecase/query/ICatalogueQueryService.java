package dev.mkukwan.cart.usecase.query;


import dev.mkukwan.cart.infrastructure.entity.CatalogueItemEntity;

import java.util.List;

public interface ICatalogueQueryService {
    List<CatalogueItemEntity> getAll(int pageNumber);

    CatalogueItemEntity get(String id);
}
