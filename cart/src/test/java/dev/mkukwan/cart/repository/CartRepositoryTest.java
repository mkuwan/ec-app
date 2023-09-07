package dev.mkukwan.cart.repository;

import dev.mkukwan.cart.domain.model.CartModel;
import dev.mkukwan.cart.domain.repository.ICartRepository;
import dev.mkukwan.cart.domain.valueobject.Buyer;
import dev.mkukwan.cart.domain.valueobject.CartItem;
import dev.mkukwan.cart.infrastructure.entity.CartEntity;
import dev.mkukwan.cart.infrastructure.entity.CartItemEntity;
import dev.mkukwan.cart.infrastructure.entity.CatalogueItemEntity;
import dev.mkukwan.cart.infrastructure.jpa.CartItemJpaRepository;
import dev.mkukwan.cart.infrastructure.jpa.CartJpaRepository;
import dev.mkukwan.cart.infrastructure.jpa.CatalogueItemJpaRepository;
import dev.mkukwan.cart.infrastructure.repository.CartRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
class CartRepositoryTest {

    @Autowired
    private CatalogueItemJpaRepository catalogueItemJpaRepository;

    @Autowired
    private CartJpaRepository cartJpaRepository;

    @Autowired
    private CartItemJpaRepository cartItemJpaRepository;

    @Autowired
    private ICartRepository cartRepository;

    @Mock
    private CartJpaRepository cartJpaRepositoryMock;

    @Mock
    private CartItemJpaRepository cartItemJpaRepositoryMock;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setup(){
        autoCloseable = MockitoAnnotations.openMocks(this);

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

        cartJpaRepository.save(new CartEntity("testCartId", "testBuyerId",null));
    }

    @AfterEach
    void tearDown() throws Exception {
        for (int i = 0; i < 20; i++) {
            String catalogueId = "item-id-" + (i + 1);
            catalogueItemJpaRepository.deleteById(catalogueId);
        }

        cartJpaRepository.deleteById("testCartId");

        autoCloseable.close();
    }

    @Test
    void getCartById_ReturnNull(){
        // arrange

        // act
        var cartModel = cartRepository.getCartByCartId("testId");

        // assertion
        assertNull(cartModel);
    }

    @Test
    void getExistedCartById_Success(){
        // arrange
        cartJpaRepository.save(new CartEntity("cartId", "buyerId", null));

        // act
        var cartModel = cartRepository.getCartByCartId("cartId");

        // assertion
        assertNotNull(cartModel);
        assertEquals("cartId", cartModel.CartId());
        assertEquals("buyerId", cartModel.Buyer().buyerId());
        assertEquals(0, cartModel.CartItems().size());
    }

    @Test
    void getCartWithItems_Success(){
        // arrange
        List<CartItemEntity> catalogueItemEntityList = new ArrayList<>();
        var item1 = catalogueItemJpaRepository.findById("item-id-1").get();
        var item2 = catalogueItemJpaRepository.findById("item-id-2").get();
        CartItemEntity cartItem1 = new CartItemEntity(UUID.randomUUID().toString(),
                "cartHasItemsId",
                item1.getCatalogItemId(),
                item1.getCatalogItemName(), item1.getSalesPrice(),
                1, item1.getPurchaseLimit());
        CartItemEntity cartItem2 = new CartItemEntity(UUID.randomUUID().toString(),
                "cartHasItemsId",
                item2.getCatalogItemId(),
                item2.getCatalogItemName(), item2.getSalesPrice(),
                3, item2.getPurchaseLimit());
        cartItemJpaRepository.save(cartItem1);
        cartItemJpaRepository.save(cartItem2);
        catalogueItemEntityList.add(cartItem1);
        catalogueItemEntityList.add(cartItem2);
        cartJpaRepository.save(new CartEntity("cartHasItemsId", "buyerId", catalogueItemEntityList));

        // act
        var cartModel = cartRepository.getCartByCartId("cartHasItemsId");

        // assertion
        assertEquals(2, cartModel.CartItems().size());
        var gotCartItem1 = cartModel.CartItems()
                .stream()
                .filter(x -> x.itemId().equals("item-id-1"))
                .findFirst()
                .get();
        assertEquals("商品1", gotCartItem1.itemName());

    }

    @CsvSource({
            "item-id-1, 商品1",
            "item-id-2, 商品2",
            "item-id-3, 商品3",
            "item-id-4, 商品4",
            "item-id-5, 商品5",
    })
    @ParameterizedTest
    void saveCart_Success(String itemId, String itemName){
        // arrange
        var catalogueItem = catalogueItemJpaRepository.findById(itemId).get();

        // act
        var cartModel = cartRepository.getCartByCartId("testCartId");
        cartModel.putItemIntoCart(new CartItem(UUID.randomUUID().toString(),
                catalogueItem.getCatalogItemId(), catalogueItem.getCatalogItemName(),
                catalogueItem.getSalesPrice(), 1, catalogueItem.getPurchaseLimit()));
        cartRepository.save(cartModel);
        var savedCartModel = cartRepository.getCartByCartId("testCartId");

        // assertion
        var cartItem = savedCartModel.CartItems()
                .stream().filter(x -> x.itemId().equals(itemId))
                .findFirst()
                .get();
        assertEquals(itemName, cartItem.itemName());

    }

    @Test
    void withMockJpaRepositoryTest(){
        // given
        var cartRepositoryWithMock = new CartRepository(cartJpaRepositoryMock, cartItemJpaRepositoryMock);
        var catalogueItem = catalogueItemJpaRepository.findById("item-id-1").get();

        // when
        var cartModel = new CartModel(UUID.randomUUID().toString(),
                new Buyer(UUID.randomUUID().toString()), null);
        cartModel.putItemIntoCart(new CartItem(UUID.randomUUID().toString(),
                catalogueItem.getCatalogItemId(), catalogueItem.getCatalogItemName(),
                catalogueItem.getSalesPrice(), 1, catalogueItem.getPurchaseLimit()));

        cartRepositoryWithMock.save(cartModel);

        // then
        ArgumentCaptor<CartEntity> cartEntityArgumentCaptor =
                ArgumentCaptor.forClass(CartEntity.class);
        verify(cartJpaRepositoryMock).save(cartEntityArgumentCaptor.capture());

        var capturedCart = cartEntityArgumentCaptor.getValue();
        assertEquals(cartModel.CartId(), capturedCart.getCartId());

        ArgumentCaptor<CartItemEntity> cartItemEntityArgumentCaptor =
                ArgumentCaptor.forClass(CartItemEntity.class);
        verify(cartItemJpaRepositoryMock).save(cartItemEntityArgumentCaptor.capture());

        var capturedItem = cartItemEntityArgumentCaptor.getValue();
        assertEquals(catalogueItem.getCatalogItemId(), capturedItem.getItemId());
    }

    @Test
    void changeCartItem_Success(){
        // arrange
        var item1 = catalogueItemJpaRepository.findById("item-id-11").get();
        var item2 = catalogueItemJpaRepository.findById("item-id-12").get();
        CartEntity cart = new CartEntity("changeCartId", "buyerId",null);
        cartJpaRepository.save(cart);

        // act & assertion
        var cartModel = cartRepository.getCartByCartId("changeCartId");
        // one item add to cart
        cartModel.putItemIntoCart(new CartItem(UUID.randomUUID().toString(),
                item1.getCatalogItemId(), item1.getCatalogItemName(),
                item1.getSalesPrice(), 1, item1.getPurchaseLimit()));
        cartRepository.save(cartModel);
        cartModel = cartRepository.getCartByCartId("changeCartId");
        assertEquals(1, cartModel.CartItems().size());

        // second item add to cart
        cartModel.putItemIntoCart(new CartItem(UUID.randomUUID().toString(),
                item2.getCatalogItemId(), item2.getCatalogItemName(),
                item2.getSalesPrice(), 1, item2.getPurchaseLimit()));
        cartRepository.save(cartModel);
        cartModel = cartRepository.getCartByCartId("changeCartId");
        assertEquals(2, cartModel.CartItems().size());

        // remove first item from cart
        cartModel.modifyItemInCart(new CartItem(UUID.randomUUID().toString(),
                item1.getCatalogItemId(), item1.getCatalogItemName(),
                item1.getSalesPrice(), 0, item1.getPurchaseLimit()));
        cartRepository.save(cartModel);
        cartModel = cartRepository.getCartByCartId("changeCartId");
        assertEquals(1, cartModel.CartItems().size());
        assertEquals(item2.getCatalogItemId(), cartModel.CartItems().stream().findFirst().get().itemId());

        // add amount second item in cart
        cartModel.modifyItemInCart(new CartItem(UUID.randomUUID().toString(),
                item2.getCatalogItemId(), item2.getCatalogItemName(),
                item2.getSalesPrice(), 3, item2.getPurchaseLimit()));
        cartRepository.save(cartModel);
        cartModel = cartRepository.getCartByCartId("changeCartId");
        assertEquals(1, cartModel.CartItems().size());
        var inCartItem = cartModel.CartItems()
                .stream().filter(x -> x.itemId().equals(item2.getCatalogItemId()))
                .findFirst()
                .get();
        assertEquals(3, inCartItem.expectedAmount());
    }

}