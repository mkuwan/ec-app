package dev.mkukwan.cart.model;

import dev.mkukwan.cart.domain.model.CartModel;
import dev.mkukwan.cart.domain.valueobject.CartItem;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class CartModelTest {

    /**
     * 正常系
     * カートドメインモデルの新規作成
     */
    @Test
    void createNewCartModel_Success(){
        // arrange

        // act
        CartModel model = new CartModel(null, null, null);

        // assertion by Junit
        assertNotEquals(null, model.CartId());
        assertNotEquals(null, model.Buyer().buyerId());
        assertEquals(new ArrayList<CartItem>(), model.CartItems());
        // assertion by AssertJ
        assertThat(model.CartId()).isNotNull();
        assertThat(model.Buyer().buyerId()).isNotNull();
        assertThat(model.CartItems()).isEqualTo(new ArrayList<CartItem>());
    }

    /**
     * 正常系
     * 空のカートに商品を入れる
     */
    @Test
    void putItemIntoBlankCartModel_Success(){
        // arrange
        var cartModel = new CartModel(null, null,null);

        // act
        CartItem item = new CartItem(UUID.randomUUID().toString(),
                cartModel.CartId(),
                "商品A", 5000, 1,  5);
        cartModel.putItemIntoCart(item);

        // assertion
        assertEquals(1, cartModel.CartItems().size());

        assertThat(cartModel.CartItems().size()).isEqualTo(1);
        assertThat(cartModel).matches(p -> p.CartItems().size() == 1);
        var itemInCart = cartModel.CartItems().get(0);
        assertThat(itemInCart).matches(p -> p.itemName().equals("商品A"));
    }

    /**
     * 異常系
     * 空のカートに購入限度数以上の数の商品を入れる
     */
    @Test
    void putItemIntoBlankCartModel_overLimitedCount_throwError(){
        // arrange
        var cartModel = new CartModel(null, null,null);

        // act
        CartItem item = new CartItem(UUID.randomUUID().toString(),
                cartModel.CartId(),
                "商品A", 5000, 6,  5);

        // assertion throw
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> cartModel.putItemIntoCart(item));
        assertEquals("購入限度数5を超えてカートに入れることはできません", exception.getMessage());

        // assertion by AssertJ
        assertThat(exception.getMessage()).startsWith("購入限度数").endsWith("カートに入れることはできません");
        assertThat(exception.getMessage()).contains("5");

        Throwable thrown = catchThrowable(() -> cartModel.putItemIntoCart(item));
        then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("購入限度数5を超えてカートに入れることはできません");
    }

    /**
     * 異常系
     * 商品の入っているカートに購入限度数以上となるように商品を入れる
     */
    @Test
    void putItemIntoCart_thenOverLimitedCount_throwError(){
        // arrange
        List<CartItem> cartItems = new ArrayList<>();
        String cartItemId = UUID.randomUUID().toString();
        String itemId = UUID.randomUUID().toString();
        CartItem item = new CartItem(cartItemId, itemId,
                "商品A", 5000, 5,  5);
        cartItems.add(item);
        var cartModel = new CartModel(null, null,cartItems);

        // act
        var addItem = new CartItem(cartItemId,itemId,
                "商品A", 5000, 1,  5);

        // assertion throw
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> cartModel.putItemIntoCart(addItem));
        assertEquals("購入限度数5を超えてカートに入れることはできません", exception.getMessage());

    }

    /**
     * 異常系
     * カートに10商品入っている時に、さらに商品を入れる
     */
    @Test
    void putItemIntoCart_over10items_throwError(){
        // arrange
        List<CartItem> cartItems = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CartItem item = new CartItem(UUID.randomUUID().toString(),UUID.randomUUID().toString(),
                    "商品テスト" + i, 5000, 1,  5);
            cartItems.add(item);
        }
        var cartModel = new CartModel(null, null,cartItems);

        // act
        CartItem item = new CartItem(UUID.randomUUID().toString(),UUID.randomUUID().toString(),
                "商品過剰", 5000, 1,  5);

        // assertion throw
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> cartModel.putItemIntoCart(item));
        assertEquals("カートには10商品を超えて入れることはできません", exception.getMessage());

    }

    /**
     * 正常系
     * カートの商品を増減させる
     */
    @Test
    void modifyItem_AddExpectedAmount_Success(){
        // arrange
        var cartModel = new CartModel(null, null, null);
        List<CartItem> cartItems = new ArrayList<>();
        String cartItemId = UUID.randomUUID().toString();
        String cartItemId2 = UUID.randomUUID().toString();
        CartItem item = new CartItem(cartModel.CartId(), cartItemId,
                "商品A", 5000, 4,  5);
        CartItem item2 = new CartItem(cartModel.CartId(), cartItemId2,
                "商品B", 2000, 5,  5);
        cartItems.add(item);
        cartItems.add(item2);
        cartModel = new CartModel(cartModel.CartId(), cartModel.Buyer(), cartItems);

        // act
        var addItem = new CartItem(cartModel.CartId(), cartItemId,
                "商品A", 5000, 5,  5);
        var subtractItem = new CartItem(cartModel.CartId(), cartItemId2,
                "商品B", 2000, 4,  5);
        cartModel.modifyItemInCart(addItem);
        cartModel.modifyItemInCart(subtractItem);

        // assert
        assertEquals(2, cartModel.CartItems().size());
        var itemInCart = cartModel.CartItems()
                .stream()
                .filter(x -> x.itemId() == cartItemId)
                .findFirst().get();
        assertEquals(5, itemInCart.expectedAmount());

        var itemInCart2 = cartModel.CartItems()
                .stream()
                .filter(x -> x.itemId() == cartItemId2)
                .findFirst().get();
        assertEquals(4, itemInCart2.expectedAmount());
    }

    /**
     * 異常系
     * カート内の商品を変更で購入限度数以上に増加させる
     */
    @Test
    void modifyItem_AddOverExpectedItem_ThrowError(){
        // arrange
        var cartModel = new CartModel(null, null,null);
        List<CartItem> cartItems = new ArrayList<>();
        String cartItemId = UUID.randomUUID().toString();
        CartItem item = new CartItem(cartModel.CartId(), cartItemId,
                "商品A", 5000, 4,  5);
        cartItems.add(item);
        var cartModelTest = new CartModel(cartModel.CartId(), cartModel.Buyer(), cartItems);

        // act
        var addItem = new CartItem(cartModel.CartId(), cartItemId,
                "商品A", 5000, 6,  5);

        // assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> cartModelTest.modifyItemInCart(addItem));
        assertEquals("購入限度数5を超えてカートに入れることはできません", exception.getMessage());

    }

    /**
     * 正常系
     * カートに入っている商品と同じ商品で販売価格が異なるものを入れる
     * 　新しく入れた販売価格で他のものも更新される
     */
    @Test
    void putItem_ChangeExpectedPrice_Success(){
        // arrange
        var cartModel = new CartModel(null, null, null);
        List<CartItem> cartItems = new ArrayList<>();
        String cartItemId = UUID.randomUUID().toString();
        CartItem item = new CartItem(cartModel.CartId(), cartItemId,
                "商品A", 5000, 4,  5);
        cartItems.add(item);
        cartModel = new CartModel(cartModel.CartId(), cartModel.Buyer(), cartItems);

        // act
        var addItem = new CartItem(cartModel.CartId(), cartItemId,
                "商品A", 3000, 1,  5);
        cartModel.putItemIntoCart(addItem);

        // assert
        assertEquals(1, cartModel.CartItems().size());
        var itemInCart = cartModel.CartItems()
                .stream()
                .filter(x -> x.itemId() == cartItemId)
                .findFirst().get();
        assertEquals(5, itemInCart.expectedAmount());
        assertEquals(3000, itemInCart.expectedPrice());
    }
}