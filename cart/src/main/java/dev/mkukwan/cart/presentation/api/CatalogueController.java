package dev.mkukwan.cart.presentation.api;


import dev.mkukwan.cart.presentation.viewmodel.CatalogueViewModel;
import dev.mkukwan.cart.usecase.query.ICatalogueQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/catalogue")
public class CatalogueController {

    private final ICatalogueQueryService catalogueQueryService;

    public CatalogueController(ICatalogueQueryService catalogueQueryService) {
        this.catalogueQueryService = catalogueQueryService;
    }

//    @GetMapping(path = "/all/{pageNumber}")
//    public List<CatalogueViewModel> getAll(@PathVariable(name = "pageNumber") int pageNumber){
//        List<CatalogueViewModel> resultList = new ArrayList<>();
//
//        var entities = catalogueQueryService.getAll(pageNumber - 1);
//        if(entities.isEmpty())
//            return resultList;
//
//
//        entities.forEach(x -> {
//            resultList.add(CatalogueViewModel.fromEntity(x));
//        });
//        return resultList;
//    }

    @GetMapping(path = "/all/{pageNumber}")
    public ResponseEntity<List<CatalogueViewModel>> getAll(@PathVariable(name = "pageNumber") int pageNumber){
        List<CatalogueViewModel> resultList = new ArrayList<>();

        var entities = catalogueQueryService.getAll(pageNumber - 1);
        if(entities.isEmpty())
            return ResponseEntity.noContent().build();


        entities.forEach(x -> {
            resultList.add(CatalogueViewModel.fromEntity(x));
        });
        return ResponseEntity.ok().body(resultList);
    }

    @GetMapping(path = "/item/{id}")
    public CatalogueViewModel getById(@PathVariable("id") String id){
        var entity = catalogueQueryService.get(id);

        return CatalogueViewModel.fromEntity(entity);
    }
}
