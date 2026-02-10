package com.cg.controller.admin;

import com.cg.dto.MenuItemDto;
import com.cg.exception.GlobalExceptionHandler;
import com.cg.iservice.IMenuItemService;
import com.cg.iservice.IRestaurantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminMenuItemController.class)
@Import(GlobalExceptionHandler.class)
class AdminMenuItemControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IMenuItemService menuItemService;

    @MockBean
    private IRestaurantService restaurantService;

    /* 1) CREATE */
    @Test
    void save_create_shouldRedirect() throws Exception {
        mvc.perform(post("/admin/menu-items/save")
                        .param("restaurantId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/menu-items"));

        verify(menuItemService).add(any(MenuItemDto.class));
    }

    /* 2) UPDATE */
    @Test
    void save_update_shouldRedirect() throws Exception {
        mvc.perform(post("/admin/menu-items/save")
                        .param("itemId", "5")
                        .param("restaurantId", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/menu-items"));

        verify(menuItemService).update(any(MenuItemDto.class));
    }

    /* 3) DELETE -> FK ERROR */
    @Test
    void delete_fkError_shouldRedirectWithError() throws Exception {
        doThrow(new DataIntegrityViolationException("x"))
                .when(menuItemService).delete(10L);

        mvc.perform(get("/admin/menu-items/delete/10")
                        .header("Referer", "/admin/menu-items"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/menu-items"))
                .andExpect(flash().attributeExists("error"));
    }

    /* 4) EDIT -> ILLEGAL STATE */
    @Test
    void edit_illegalState_shouldRedirectWithError() throws Exception {
        when(menuItemService.getById(7L))
                .thenThrow(new IllegalStateException("not allowed"));

        mvc.perform(get("/admin/menu-items/edit/7")
                        .header("Referer", "/admin/menu-items"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/menu-items"))
                .andExpect(flash().attributeExists("error"));
    }
}