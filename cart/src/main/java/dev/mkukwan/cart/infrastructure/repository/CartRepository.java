package dev.mkukwan.cart.infrastructure.repository;

import dev.mkukwan.cart.domain.model.CartModel;
import dev.mkukwan.cart.domain.model.IAggregate;
import dev.mkukwan.cart.domain.repository.ICartRepository;
import dev.mkukwan.cart.domain.valueobject.Buyer;
import dev.mkukwan.cart.domain.valueobject.CartItem;
import dev.mkukwan.cart.infrastructure.entity.CartEntity;
import dev.mkukwan.cart.infrastructure.entity.CartItemEntity;
import dev.mkukwan.cart.infrastructure.entity.CatalogueItemEntity;
import dev.mkukwan.cart.infrastructure.jpa.CartItemJpaRepository;
import dev.mkukwan.cart.infrastructure.jpa.CartJpaRepository;
import dev.mkukwan.cart.infrastructure.jpa.CatalogueItemJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class CartRepository implements ICartRepository {
    private final CartJpaRepository cartJpaRepository;
    private final CartItemJpaRepository cartItemJpaRepository;
    private final CatalogueItemJpaRepository catalogueItemJpaRepository;

    public CartRepository(CartJpaRepository cartJpaRepository,
                          CartItemJpaRepository cartItemJpaRepository,
                          CatalogueItemJpaRepository catalogueItemJpaRepository) {
        this.cartJpaRepository = cartJpaRepository;
        this.cartItemJpaRepository = cartItemJpaRepository;
        this.catalogueItemJpaRepository = catalogueItemJpaRepository;
    }

    @Override
    public void save(IAggregate cartModel) {
        var saveCartModel = (CartModel)cartModel;

        if(saveCartModel.CartItems() != null && saveCartModel.CartItems().size() > 0){
            saveCartModel.CartItems().forEach(x -> {
                var item = fromItemModel(x);
                cartItemJpaRepository.save(item);
            });
        }

        cartJpaRepository.save(fromModel(saveCartModel));
    }

    @Override
    public void clearCartAndSave(IAggregate cartModel) {
        var saveCartModel = (CartModel)cartModel;
        var cartId = saveCartModel.CartId();

        var items = cartItemJpaRepository.findAllByCartId(cartId);
        if(items.isPresent()){
            items.get().forEach(cartItemJpaRepository::delete);
        }

        save(cartModel);
    }

    @Override
    public CartModel getCartByCartId(String cartId) {
        if(cartId == null)
            return null;
        var cart = cartJpaRepository.findByCartId(cartId);

        if(cart.isPresent())
            return fromEntity(cart.get());
        else
            return null; // new CartModel(null, null, null);
    }

    @Override
    public CartModel getCartByBuyerId(String buyerId) {
        return null;
    }

    @Override
    public CartItem getItemByItemIdFromCatalog(String itemId) {
        var itemEntity = catalogueItemJpaRepository.getByCatalogItemId(itemId);
        return fromCatalogueItemEntityNoCart(itemEntity);
    }

    private CartEntity fromModel(CartModel cartModel){
        List<CartItemEntity> cartItemEntities = new ArrayList<>();
        if(cartModel.CartItems() != null){
            cartModel.CartItems().forEach(x -> {
                cartItemEntities.add(fromItemModel(x));
            });
        }
        var entity = cartJpaRepository.findByCartIdAndBuyerId(cartModel.CartId(), cartModel.Buyer().buyerId());
        if(entity.isPresent()){
            var existEntity = entity.get();
            existEntity.setCartItemEntities(cartItemEntities);
            return existEntity;
        } else {
            return new CartEntity(UUID.randomUUID().toString(),
                    cartModel.CartId(), cartModel.Buyer().buyerId(),
                    cartItemEntities);
        }
    }

    private CartItemEntity fromItemModel(CartItem cartItem){
        var existItem = cartItemJpaRepository.findByCartIdAndItemId(cartItem.cartId(), cartItem.itemId());

        if(existItem.isPresent()){
            var item = existItem.get();
            return new CartItemEntity(item.getId(), item.getCartId(), item.getItemId(),
                    item.getItemName(),
                    cartItem.expectedPrice(),
                    cartItem.expectedAmount(), cartItem.itemLimitedCount());
        } else {
            return new CartItemEntity(UUID.randomUUID().toString(), cartItem.cartId(), cartItem.itemId(),
                    cartItem.itemName(),
                    cartItem.expectedPrice(),
                    cartItem.expectedAmount(), cartItem.itemLimitedCount());
        }
    }

    private CartModel fromEntity(CartEntity cartEntity){
        var cartItems = cartEntity.getCartItemEntities();
        List<CartItem> cartItemList = new ArrayList<>();

        if(cartItems != null){
            cartItems.forEach(x -> {
                cartItemList.add(fromItemEntity(x));
            });
        }

        return new CartModel(cartEntity.getCartId(),
                new Buyer(cartEntity.getBuyerId()),
                cartItemList);
    }

    private CartItem fromItemEntity(CartItemEntity cartItemEntity){
        return new CartItem(cartItemEntity.getCartId(),
                cartItemEntity.getItemId(),
                cartItemEntity.getItemName(),
                cartItemEntity.getItemPrice(),
                cartItemEntity.getItemAmount(),cartItemEntity.getItemLimitedCount());
    }

    private CartItem fromCatalogueItemEntityNoCart(CatalogueItemEntity catalogueItem){
        return new CartItem(null,
                catalogueItem.getCatalogItemId(),
                catalogueItem.getCatalogItemName(),
                catalogueItem.getSalesPrice(),
                catalogueItem.getStockAmount(),
                catalogueItem.getPurchaseLimit());
    }

}