package com.cg.controller.user;

import com.cg.dto.MenuItemDto;
import com.cg.dto.RestaurantDto;
import com.cg.iservice.IMenuItemService;
import com.cg.iservice.IRestaurantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Very simple tests for UserRestaurantController:
 * - Only checks view names, model attributes, redirects not used here.
 * - Minimal verify() calls.
 */
@WebMvcTest(UserRestaurantController.class)
class UserRestaurantControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IRestaurantService restaurantService;

    @MockBean
    private IMenuItemService menuItemService;

    /* 1) LIST (default filter=all) -> returns 'user/restaurants' with 'restaurants' */
    @Test
    void list_default_shouldReturnRestaurantsView() throws Exception {
        when(restaurantService.getAll()).thenReturn(Collections.emptyList());

        mvc.perform(get("/user/restaurants"))
           .andExpect(status().isOk())
           .andExpect(view().name("user/restaurants"))
           .andExpect(model().attributeExists("restaurants"));

        verify(restaurantService).getAll();
    }

    /* 2) LIST (filter=top) -> returns 'user/restaurants' using findTopRated() */
    @Test
    void list_top_shouldReturnRestaurantsView() throws Exception {
        when(restaurantService.findTopRated()).thenReturn(Collections.emptyList());

        mvc.perform(get("/user/restaurants").param("filter", "top"))
           .andExpect(status().isOk())
           .andExpect(view().name("user/restaurants"))
           .andExpect(model().attributeExists("restaurants"));

        verify(restaurantService).findTopRated();
    }

    /* 3) VIEW MENU -> returns 'user/restaurant-menu' with restaurant + menuItems */
    @Test
    void viewMenu_shouldReturnRestaurantMenuView() throws Exception {
        when(restaurantService.getById(7L)).thenReturn(new RestaurantDto());
        when(menuItemService.getByRestaurant(7L)).thenReturn(Collections.emptyList());

        mvc.perform(get("/user/restaurants/7"))
           .andExpect(status().isOk())
           .andExpect(view().name("user/restaurant-menu"))
           .andExpect(model().attributeExists("restaurant"))
           .andExpect(model().attributeExists("menuItems"));

        verify(restaurantService).getById(7L);
        verify(menuItemService).getByRestaurant(7L);
    }

    /* 4) DASHBOARD -> returns 'user/user-dashboard' with 'restaurants' */
    @Test
    void dashboard_shouldReturnUserDashboard() throws Exception {
        when(restaurantService.findTopForDashboard()).thenReturn(Collections.emptyList());

        mvc.perform(get("/user/restaurants/dashboard"))
           .andExpect(status().isOk())
           .andExpect(view().name("user/user-dashboard"))
           .andExpect(model().attributeExists("restaurants"));

        verify(restaurantService).findTopForDashboard();
    }
}
