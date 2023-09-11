package dev.mkukwan.cart.usecase.service;

import dev.mkukwan.cart.domain.model.CartModel;
import dev.mkukwan.cart.infrastructure.entity.CartEntity;
import dev.mkukwan.cart.infrastructure.entity.CartItemEntity;
import dev.mkukwan.cart.infrastructure.entity.CatalogueItemEntity;
import dev.mkukwan.cart.infrastructure.jpa.CartItemJpaRepository;
import dev.mkukwan.cart.infrastructure.jpa.CartJpaRepository;
import dev.mkukwan.cart.infrastructure.jpa.CatalogueItemJpaRepository;
import dev.mkukwan.cart.usecase.dto.CartItemDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class CartUseCaseServiceTest {
    @Autowired
    private CatalogueItemJpaRepository catalogueItemJpaRepository;

    @Autowired
    private CartJpaRepository cartJpaRepository;

    @Autowired
    private CartItemJpaRepository cartItemJpaRepository;

    @Autowired
    private ICartUseCaseService cartUseCaseService;

    @Mock
    private CatalogueItemJpaRepository catalogueItemJpaRepositoryMock;

    @BeforeEach
    void setup(){
        // サンプルデータ作成
        for (int i = 0; i < 20; i++) {
            CatalogueItemEntity catalogueItem = new CatalogueItemEntity("item-id-" + (i + 1),
                    "商品" + (i + 1), 100 * (i + 1), i + 1, 5);
            catalogueItemJpaRepository.save(catalogueItem);
        }
        // 作成されるカタログデータ
        // PURCHASE_LIMIT  	STOCK_AMOUNT  	SALES_PRICE  	CATALOG_ITEM_ID  	CATALOG_ITEM_NAME
        // 5                	1	            100	            item-id-1       	商品1
        // 5                	2	            200	            item-id-2       	商品2
        // 5                  	3	            300	            item-id-3       	商品3
        // 5                	4	            400	            item-id-4       	商品4
        // 5                	5	            500	            item-id-5       	商品5
        // 5                	6	            600	            item-id-6       	商品6
        // 5                	7	            700	            item-id-7       	商品7
        // 5                	8	            800	            item-id-8       	商品8
        // 5                	9	            900	            item-id-9       	商品9
        // 5                   10	           1000	           item-id-10       	商品10
        // 5	               11	           1100	           item-id-11       	商品11
        // 5	               12	           1200	           item-id-12       	商品12
        // 5	               13	           1300	           item-id-13       	商品13
        // 5	               14	           1400	           item-id-14       	商品14
        // 5	               15	           1500	           item-id-15       	商品15
        // 5	               16	           1600	           item-id-16       	商品16
        // 5	               17	           1700	           item-id-17       	商品17
        // 5	               18	           1800	           item-id-18       	商品18
        // 5	               19	           1900	           item-id-19       	商品19
        // 5	               20	           2000	           item-id-20       	商品20
    }

    @AfterEach
    void tearDown(){
        for (int i = 0; i < 20; i++) {
            String catalogueId = "item-id-" + (i + 1);
            catalogueItemJpaRepository.deleteById(catalogueId);
        }
    }

    // 商品をカートに入れた時に新規作成される
    // カートIDを不正に作成しても、実際にDBになければ新しいカートIDが割り振られる
    @Test
    void addFirstItemToCart_withDummyCartId_butReCreateCartId(){
        // arrange
        var cart = new CartModel(null, null, null);
        var cartId = cart.CartId();
        var buyerId = cart.Buyer().buyerId();

        // act
        var itemEntity = catalogueItemJpaRepository.findById("item-id-1").get();
        var cartItemDto = CartItemDto.builder()
                .cartId(cartId)
                .itemId(itemEntity.getCatalogItemId())
                .itemName(itemEntity.getCatalogItemName())
                .itemPrice(itemEntity.getSalesPrice())
                .itemAmount(1)
                .itemLimitedCount(itemEntity.getPurchaseLimit())
                .build();
        var cartDto = cartUseCaseService.putItemIntoCart(cartId, cartItemDto);

        // assertion
        assertNotEquals(cartId, cartDto.getCartId());
        assertNotEquals(buyerId, cartDto.getBuyerId());

        var dbCartDto = cartUseCaseService.getCartByCartId(cartDto.getCartId());
        assertEquals(1, dbCartDto.getCartItemDtoList().size());
    }

    @Test
    void modifyCartItem_Success(){
        // arrange
        var id = UUID.randomUUID().toString();
        var cartId = UUID.randomUUID().toString();
        var buyerId = UUID.randomUUID().toString();
        CartEntity cartEntity = new CartEntity(id, cartId, buyerId, null);
        cartJpaRepository.save(cartEntity);
        var catalogueItemEntity2 = catalogueItemJpaRepository.findById("item-id-2").get();
        var catalogueItemEntity3 = catalogueItemJpaRepository.findById("item-id-3").get();
        var itemEntity2 = new CartItemEntity(UUID.randomUUID().toString(),
                cartId,
                catalogueItemEntity2.getCatalogItemId(),
                catalogueItemEntity2.getCatalogItemName(),
                catalogueItemEntity2.getSalesPrice(),
                1,
                catalogueItemEntity2.getPurchaseLimit());
        var itemEntity3 = new CartItemEntity(UUID.randomUUID().toString(),
                cartId,
                catalogueItemEntity3.getCatalogItemId(),
                catalogueItemEntity3.getCatalogItemName(),
                catalogueItemEntity3.getSalesPrice(),
                1,
                catalogueItemEntity3.getPurchaseLimit());
        cartItemJpaRepository.save(itemEntity2);
        cartItemJpaRepository.save(itemEntity3);
        List<CartItemEntity> itemEntities = new ArrayList<>(List.of(itemEntity2, itemEntity3));
        cartEntity = new CartEntity(id, cartId, buyerId, itemEntities);
        cartJpaRepository.save(cartEntity);

        // act
        // 価格更新と限度数更新
        var catalogItem = catalogueItemJpaRepository.findById(itemEntity2.getItemId()).get();
        catalogItem.setSalesPrice(3000);
        catalogItem.setPurchaseLimit(10);
        catalogueItemJpaRepository.save(catalogItem);

        CartItemDto itemDto2 = CartItemDto
                .builder()
                .cartId(cartId)
                .itemId(itemEntity2.getItemId())
                .itemName(itemEntity2.getItemName())
                .itemPrice(200)  // 更新された価格ではなく以前の価格だが、更新時は最新価格となる
                .itemAmount(2)
                .itemLimitedCount(10)
                .build();
        cartUseCaseService.modifyCartItem(cartId, itemDto2);

        // assertion
        var resultItem = cartItemJpaRepository.findByCartIdAndItemId(cartId, itemDto2.getItemId()).get();
        assertEquals(2, resultItem.getItemAmount());
        assertEquals(2, resultItem.getItemAmount());
        assertEquals(3000, resultItem.getItemPrice());
        assertEquals(10, resultItem.getItemLimitedCount());


    }

}