package dev.mkuwan.catalog.api;

import dev.mkuwan.catalog.usecase.ICatalogUseCase;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CatalogController {
    private final ICatalogUseCase catalogUseCase;

    public CatalogController(ICatalogUseCase catalogUseCase) {
        this.catalogUseCase = catalogUseCase;
    }
}
