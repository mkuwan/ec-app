package dev.mkukwan.cart.usecase.query;

import dev.mkukwan.cart.infrastructure.entity.CatalogueItemEntity;
import dev.mkukwan.cart.infrastructure.jpa.CatalogueItemJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CatalogueQueryServiceTest {
    @Autowired
    private CatalogueItemJpaRepository catalogueItemJpaRepository;

    @Autowired
    private ICatalogueQueryService catalogueQueryService;

    @BeforeEach
    void setUp(){
        // サンプルデータ作成
        for (int i = 0; i < 20; i++) {
            CatalogueItemEntity catalogueItem = new CatalogueItemEntity("item-id-" + (i + 1),
                    "商品" + (i + 1), 100 * (i + 1), i + 1, 5);
            catalogueItemJpaRepository.save(catalogueItem);
        }
    }

    @AfterEach
    void tearDown(){
        catalogueItemJpaRepository.deleteAll();
    }


    @Test
    void getAll(){
        // arrange

        // act
        var catalogueItems = catalogueQueryService.getAll(1);

        // assertion
        assertEquals(10, catalogueItems.size());
//        catalogueItems.forEach(x -> {
//            System.out.println(x.getCatalogItemName());
//        });
    }
}