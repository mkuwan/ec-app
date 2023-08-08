package dev.mkuwan.customer.catalog.api;

import dev.mkuwan.customer.catalog.usecase.ICatalogUseCase;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CatalogController {
    private final ICatalogUseCase catalogUseCase;

    public CatalogController(ICatalogUseCase catalogUseCase) {
        this.catalogUseCase = catalogUseCase;
    }
}
