package dev.mkukwan.cart.usecase.query;


import dev.mkukwan.cart.infrastructure.entity.CatalogueItemEntity;
import dev.mkukwan.cart.infrastructure.jpa.CatalogueItemJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogueQueryService implements ICatalogueQueryService {

    private final CatalogueItemJpaRepository catalogueItemJpaRepository;

    public CatalogueQueryService(CatalogueItemJpaRepository catalogueItemJpaRepository) {
        this.catalogueItemJpaRepository = catalogueItemJpaRepository;
    }


    @Override
    public List<CatalogueItemEntity> getAll(int pageNumber){
        Pageable sortedByName = PageRequest.of(pageNumber, 10, Sort.by("catalogItemName"));
        var resultPage = catalogueItemJpaRepository.findAll(sortedByName);
        return resultPage.stream().toList();
    }

    @Override
    public CatalogueItemEntity get(String id) {
        var entity = catalogueItemJpaRepository.findById(id);
        if(entity.isPresent())
            return entity.get();

        return null;
    }
}
