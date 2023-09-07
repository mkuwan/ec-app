package dev.mkukwan.cart;

import dev.mkukwan.cart.infrastructure.jpa.CatalogueItemJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

public class CartApplicationCommandLineRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(CartApplicationCommandLineRunner.class);

    private final CatalogueItemJpaRepository catalogueItemJpaRepository;

    public CartApplicationCommandLineRunner(CatalogueItemJpaRepository catalogueItemJpaRepository) {
        this.catalogueItemJpaRepository = catalogueItemJpaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // サンプルデータ作成
//        for (int i = 0; i < 100; i++) {
//            CatalogueItemEntity catalogueItem = new CatalogueItemEntity("item-id-" + (i + 1),
//                    "商品" + (i + 1), 100 * (i + 1), i + 1, 5);
//            catalogueItemJpaRepository.save(catalogueItem);
//        }

    }
}
