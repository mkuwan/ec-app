package dev.mkukwan.cart.presentation.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mkukwan.cart.infrastructure.entity.CatalogueItemEntity;
import dev.mkukwan.cart.infrastructure.jpa.CatalogueItemJpaRepository;
import dev.mkukwan.cart.presentation.request.PutCartItemRequest;
import dev.mkukwan.cart.presentation.viewmodel.CartViewModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(CartControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CatalogueItemJpaRepository catalogueItemJpaRepository;

    @BeforeEach
    void setUp(){
        for (int i = 0; i < 15; i++) {
            CatalogueItemEntity catalogueItem = new CatalogueItemEntity("item-id-" + (i + 1),
                    "商品" + (i + 1), 100 * (i + 1), i + 1, 5);
            catalogueItemJpaRepository.save(catalogueItem);
        }
    }

    @AfterEach
    void tearDown(){
        for (int i = 0; i < 15; i++){
            catalogueItemJpaRepository.deleteById("item-id-" + (i + 1));
        }
    }

    /**
     * 正常系
     * カートを作成して商品を入れる
     * @throws Exception
     */
    @Test
    void putItemIntoCart_Success() throws Exception {
        // arrange
        ObjectMapper mapper = new ObjectMapper();

        var itemEntity = catalogueItemJpaRepository.findById("item-id-1").get();
        PutCartItemRequest request = new PutCartItemRequest();
        request.setCartId(null);
        request.setBuyerId(null);
        request.setItemId(itemEntity.getCatalogItemId());
        request.setItemName(itemEntity.getCatalogItemName());
        request.setItemPrice(itemEntity.getSalesPrice());
        request.setItemAmount(2);
        request.setItemLimitedCount(itemEntity.getPurchaseLimit());
        var jsonRequest = mapper.writeValueAsString(request);

        // act
        var result = mockMvc.perform(post("/api/v1/cart/put")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        // assertion
        System.out.println("Json response is " + result.getResponse().getContentAsString());
        var response = mapper.readValue(result.getResponse().getContentAsString(), CartViewModel.class);
        assertEquals(1, response.getCartItemViewList().size());
    }

    /**
     * 異常系
     * カートに購入限度数以上の商品を入れる
     */
    @Test
    void putItemIntoCartOverLimitedAmount_Error() throws Exception {
        // arrange
        ObjectMapper mapper = new ObjectMapper();

        var itemEntity = catalogueItemJpaRepository.findById("item-id-1").get();
        PutCartItemRequest request = new PutCartItemRequest();
        request.setCartId(null);
        request.setBuyerId(null);
        request.setItemId(itemEntity.getCatalogItemId());
        request.setItemName(itemEntity.getCatalogItemName());
        request.setItemPrice(itemEntity.getSalesPrice());
        request.setItemAmount(6);
        request.setItemLimitedCount(itemEntity.getPurchaseLimit());
        var jsonRequest = mapper.writeValueAsString(request);

        // act
//        var header = new HttpHeaders().add("Content-Type", "text/html; charset=utf-8");
        var result = mockMvc.perform(post("/api/v1/cart/put")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

        // assertion
        // StandardCharsets.UTF_8 を指定しないとISO_8859_1になって文字化けします
        System.out.println("文字化け: Json response is " + result.getResponse().getContentAsString());
        var response = mapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), CartViewModel.class);
        assertEquals("購入限度数5を超えてカートに入れることはできません", response.getMessage());

//        assertEquals("è³¼å\u0085¥é\u0099\u0090åº¦æ\u0095°5ã\u0082\u0092è¶\u0085ã\u0081\u0088ã\u0081¦ã\u0082«ã\u0083¼ã\u0083\u0088ã\u0081«å\u0085¥ã\u0082\u008Cã\u0082\u008Bã\u0081\u0093ã\u0081¨ã\u0081¯ã\u0081§ã\u0081\u008Dã\u0081¾ã\u0081\u009Bã\u0082\u0093", response.getMessage());
    }


}