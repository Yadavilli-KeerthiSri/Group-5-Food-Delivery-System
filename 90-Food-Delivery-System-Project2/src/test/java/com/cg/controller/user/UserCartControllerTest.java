package com.cg.controller.user;

import com.cg.model.CartItem;
import com.cg.service.MenuItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Very simple tests for UserCartController.
 * - Only basic redirects/views and minimal verifies.
 * - Uses session attribute "cart" as a simple HashMap.
 */
@WebMvcTest(UserCartController.class)
class UserCartControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MenuItemService menuItemService;

    /* 1) ADD -> redirects to Referer and calls service */
    @Test
    void add_shouldRedirectBackAndCallService() throws Exception {
        Map<Long, CartItem> cart = new HashMap<>();

        mvc.perform(get("/user/cart/add/11")
                        .header("Referer", "/user/restaurants/3")
                        .sessionAttr("cart", cart))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/user/restaurants/3"));

        verify(menuItemService).getById(11L);
    }

    /* 2) INCREASE -> redirects to /user/cart/view */
    @Test
    void increase_shouldRedirectToView() throws Exception {
        Map<Long, CartItem> cart = new HashMap<>();
        CartItem item = mock(CartItem.class);
        when(item.getQuantity()).thenReturn(1); // minimal stub
        cart.put(5L, item);

        mvc.perform(get("/user/cart/increase/5")
                        .sessionAttr("cart", cart))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/user/cart/view"));

        // optional: verify a quantity update was attempted
        verify(item, atLeastOnce()).setQuantity(anyInt());
    }

    /* 3) DECREASE when qty == 1 -> redirects (and would remove) */
    @Test
    void decrease_qtyOne_shouldRedirectToView() throws Exception {
        Map<Long, CartItem> cart = new HashMap<>();
        CartItem item = mock(CartItem.class);
        when(item.getQuantity()).thenReturn(1);
        cart.put(7L, item);

        mvc.perform(get("/user/cart/decrease/7")
                        .sessionAttr("cart", cart))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/user/cart/view"));
    }

    /* 4) VIEW (empty cart) -> returns view with default previousPage and total */
    @Test
    void view_emptyCart_shouldReturnView() throws Exception {
        Map<Long, CartItem> emptyCart = new HashMap<>();

        mvc.perform(get("/user/cart/view")
                        .sessionAttr("cart", emptyCart))
           .andExpect(status().isOk())
           .andExpect(view().name("user/cart"))
           .andExpect(model().attributeExists("cartItems"))
           .andExpect(model().attributeExists("total"))
           .andExpect(model().attributeExists("previousPage"));
    }
}
