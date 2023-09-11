package dev.mkukwan.cart.usecase.service;

import dev.mkukwan.cart.domain.model.CartModel;
import dev.mkukwan.cart.domain.repository.ICartRepository;
import dev.mkukwan.cart.domain.valueobject.Buyer;
import dev.mkukwan.cart.domain.valueobject.CartItem;
import dev.mkukwan.cart.usecase.dto.CartDto;
import dev.mkukwan.cart.usecase.dto.CartItemDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CartUseCaseService implements ICartUseCaseService {
    private final ICartRepository cartRepository;

    public CartUseCaseService(ICartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public CartDto getCartByCartId(String cartId) {
        var cart = cartRepository.getCartByCartId(cartId);
        return CartDto.fromCartModel(cart);
    }

    @Override
    public CartDto putItemIntoCart(String cartId, CartItemDto cartItemDto) {
        // カートのデータ取得
        var cart = cartRepository.getCartByCartId(cartId);

        // カートがない場合は新規作成する、購入者IDも新規で割り振る
        if(cart == null)
            cart = new CartModel(null, new Buyer(null), null);

        // 追加する商品をドメインモデルに変換
        CartItem item = new CartItem(cart.CartId(), cartItemDto.getItemId(), cartItemDto.getItemName(),
                cartItemDto.getItemPrice(), cartItemDto.getItemAmount(),
                cartItemDto.getItemLimitedCount());

        // カートに商品追加する このようにドメインモデルのメソッドを使用すること
        // cart.CartItems().add(item); という書き方はダメ ドメインルールが効かなくなってしまう
        cart.putItemIntoCart(item);

        // カートを保存する
        cartRepository.save(cart);

        // カートを返す
        return CartDto.fromCartModel(cart);
    }

    @Override
    public CartDto modifyCartItem(String cartId, CartItemDto cartItemDto) {
        // カートのデータ取得
        var cart = cartRepository.getCartByCartId(cartId);

        // カートがないというのはあり得ないのでErrorとする
        if(cart == null)
            throw new IllegalArgumentException("カートが作成されていません");


        // 商品の最新情報を取得します
        // ここは本当はCartItemで受け取るのはあまりよくない
        // カタログモデルとして受け取る方がいいかも
        var latestItemInfoByCartItemModel = cartRepository.getItemByItemIdFromCatalog(cartItemDto.getItemId());
        var latestItemDto = CartItemDto.fromItemModel(latestItemInfoByCartItemModel);

        // 変更する商品をドメインモデルに変換
        // 商品名、商品価格、商品限度数を更新しています
        CartItem item = new CartItem(cartId, cartItemDto.getItemId(), latestItemDto.getItemName(),
                latestItemDto.getItemPrice(), cartItemDto.getItemAmount(),
                latestItemDto.getItemLimitedCount());
        // カートの商品を更新する
        cart.modifyItemInCart(item);

        // カートを保存する
        cartRepository.save(cart);

        // カートを返す
        return CartDto.fromCartModel(cart);
    }
}
