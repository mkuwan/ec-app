package dev.mkukwan.cart;

import dev.mkukwan.cart.domain.repository.ICartRepository;
import dev.mkukwan.cart.domain.valueobject.CartItem;
import dev.mkukwan.cart.infrastructure.entity.CartEntity;
import dev.mkukwan.cart.infrastructure.entity.CatalogueItemEntity;
import dev.mkukwan.cart.infrastructure.jpa.CartJpaRepository;
import dev.mkukwan.cart.infrastructure.jpa.CatalogueItemJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class CartApplicationCommandLineRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(CartApplicationCommandLineRunner.class);

    private final CatalogueItemJpaRepository catalogueItemJpaRepository;
    private final CartJpaRepository cartJpaRepository;
    private final ICartRepository cartRepository;

    public CartApplicationCommandLineRunner(CatalogueItemJpaRepository catalogueItemJpaRepository,
                                            CartJpaRepository cartJpaRepository,
                                            ICartRepository cartRepository) {
        this.catalogueItemJpaRepository = catalogueItemJpaRepository;
        this.cartJpaRepository = cartJpaRepository;
        this.cartRepository = cartRepository;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        // サンプルデータ作成
        try {
            for (int i = 0; i < 100; i++) {
                CatalogueItemEntity catalogueItem = new CatalogueItemEntity("item-id-" + (i + 1),
                        "商品" + (i + 1), 100 * (i + 1), i + 1, 5);
                catalogueItemJpaRepository.save(catalogueItem);
            }

            cartJpaRepository.save(new CartEntity(UUID.randomUUID().toString(), "sampleCartAId", "sampleUserA",null));
            cartJpaRepository.save(new CartEntity(UUID.randomUUID().toString(), "sampleCartBId", "sampleUserB",null));

            var catalogueItem10 = catalogueItemJpaRepository.findById("item-id-10").get();
            var catalogueItem11 = catalogueItemJpaRepository.findById("item-id-12").get();
            var cartModelA = cartRepository.getCartByCartId("sampleCartAId");
            cartModelA.putItemIntoCart(new CartItem(cartModelA.CartId(),
                    catalogueItem10.getCatalogItemId(), catalogueItem10.getCatalogItemName(),
                    catalogueItem10.getSalesPrice(), 1, catalogueItem10.getPurchaseLimit()));
            cartModelA.putItemIntoCart(new CartItem(cartModelA.CartId(),
                    catalogueItem11.getCatalogItemId(), catalogueItem11.getCatalogItemName(),
                    catalogueItem11.getSalesPrice(), 2, catalogueItem11.getPurchaseLimit()));
            cartRepository.save(cartModelA);

            var catalogueItem21 = catalogueItemJpaRepository.findById("item-id-21").get();
            var cartModelB = cartRepository.getCartByCartId("sampleCartBId");
            cartModelB.putItemIntoCart(new CartItem(cartModelB.CartId(),
                    catalogueItem21.getCatalogItemId(), catalogueItem21.getCatalogItemName(),
                    catalogueItem21.getSalesPrice(), 4, catalogueItem21.getPurchaseLimit()));
            cartRepository.save(cartModelB);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
