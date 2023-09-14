package dev.mkukwan.cart.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mkukwan.cart.infrastructure.entity.CatalogueItemEntity;
import dev.mkukwan.cart.infrastructure.jpa.CatalogueItemJpaRepository;
import dev.mkukwan.cart.presentation.viewmodel.CatalogueViewModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class CatalogueControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(CatalogueControllerTest.class);

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

    @Test
    void getAllTest() throws Exception {
        // arrange
        ObjectMapper mapper = new ObjectMapper();

        // act
        var result = mockMvc.perform(get("/api/v1/catalogue/all/1"))
                .andExpect(status().isOk())
                .andReturn();

        logger.info(result.getResponse().getContentAsString());
        var catalogueList = Arrays.asList(mapper.readValue(result.getResponse().getContentAsString(), CatalogueViewModel[].class));

        // assertion (CommandLineRunnerでDomoデータ作成すると10となりますので注意
        assertEquals(10, catalogueList.size());

    }


}

/**
 * MockHttpServletRequest:
 *       HTTP Method = GET
 *       Request URI = /api/v1/catalogue/all/1
 *        Parameters = {}
 *           Headers = []
 *              Body = null
 *     Session Attrs = {}
 *
 * Handler:
 *              Type = com.mkuwan.cartsample.presentation.api.CatalogueController
 *            Method = com.mkuwan.cartsample.presentation.api.CatalogueController#getAll(int)
 *
 * Async:
 *     Async started = false
 *      Async result = null
 *
 * Resolved Exception:
 *              Type = null
 *
 * ModelAndView:
 *         View name = null
 *              View = null
 *             Model = null
 *
 * FlashMap:
 *        Attributes = null
 *
 * MockHttpServletResponse:
 *            Status = 200
 *     Error message = null
 *           Headers = [Content-Type:"application/json"]
 *      Content type = application/json
 *              Body = [{"catalogItemId":"item-id-1","catalogItemName":"商品1","salesPrice":100,"stockAmount":1,"purchaseLimit":5},{"catalogItemId":"item-id-10","catalogItemName":"商品10","salesPrice":1000,"stockAmount":10,"purchaseLimit":5},{"catalogItemId":"item-id-100","catalogItemName":"商品100","salesPrice":10000,"stockAmount":100,"purchaseLimit":5},{"catalogItemId":"item-id-1000","catalogItemName":"商品1000","salesPrice":100000,"stockAmount":1000,"purchaseLimit":5},{"catalogItemId":"item-id-101","catalogItemName":"商品101","salesPrice":10100,"stockAmount":101,"purchaseLimit":5},{"catalogItemId":"item-id-102","catalogItemName":"商品102","salesPrice":10200,"stockAmount":102,"purchaseLimit":5},{"catalogItemId":"item-id-103","catalogItemName":"商品103","salesPrice":10300,"stockAmount":103,"purchaseLimit":5},{"catalogItemId":"item-id-104","catalogItemName":"商品104","salesPrice":10400,"stockAmount":104,"purchaseLimit":5},{"catalogItemId":"item-id-105","catalogItemName":"商品105","salesPrice":10500,"stockAmount":105,"purchaseLimit":5},{"catalogItemId":"item-id-106","catalogItemName":"商品106","salesPrice":10600,"stockAmount":106,"purchaseLimit":5}]
 *     Forwarded URL = null
 *    Redirected URL = null
 *           Cookies = []
 */