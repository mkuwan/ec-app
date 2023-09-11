package dev.mkukwan.cart.presentation.api;

import dev.mkukwan.cart.presentation.request.PutCartItemRequest;
import dev.mkukwan.cart.presentation.viewmodel.CartItemViewModel;
import dev.mkukwan.cart.presentation.viewmodel.CartViewModel;
import dev.mkukwan.cart.usecase.service.CartUseCaseService;
import dev.mkukwan.cart.usecase.service.ICartUseCaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/cart")
public class CartController {
    private final ICartUseCaseService cartUseCaseService;

    public CartController(CartUseCaseService cartUseCaseService) {
        this.cartUseCaseService = cartUseCaseService;
    }

    @GetMapping
    public ResponseEntity<String> getSample(){
        return ResponseEntity.ok()
                .body("サンプルですよ");
    }

    @PostMapping(path = "/test")
    public ResponseEntity<String> test(@RequestBody CartItemViewModel item){
        if(item.getItemPrice() > 2000)
            return ResponseEntity.badRequest()
                    .body("高すぎる(T.T)");

        return ResponseEntity.ok()
                .body(item.getItemName());
    }

    @PostMapping(path = "/put")
    public ResponseEntity<CartViewModel> putItem(@RequestBody PutCartItemRequest request){
        try {
            var result = CartViewModel.fromDto(cartUseCaseService
                    .putItemIntoCart(request.getCartId(), request.toDto()));

            return ResponseEntity.ok()
                    .body(result);

        } catch (Exception ex){
            CartViewModel response = new CartViewModel(request.getCartId(), request.getBuyerId(),
                    null, ex.getMessage());
            return ResponseEntity.badRequest()
                    .body(response);
        }
    }
}
