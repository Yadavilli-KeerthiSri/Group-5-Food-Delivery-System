package com.cg.controller.admin;

import com.cg.dto.RestaurantDto;
import com.cg.exception.GlobalExceptionHandler;
import com.cg.iservice.IRestaurantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminRestaurantController.class)
@Import(GlobalExceptionHandler.class)
class AdminRestaurantControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IRestaurantService restaurantService;

    /* 1) CREATE */
    @Test
    void save_create_shouldRedirect() throws Exception {
        mvc.perform(post("/admin/restaurants/save"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/restaurants"));

        verify(restaurantService).add(any(RestaurantDto.class));
    }

    /* 2) UPDATE */
    @Test
    void save_update_shouldRedirect() throws Exception {
        mvc.perform(post("/admin/restaurants/save")
                        .param("restaurantId", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/restaurants"));

        verify(restaurantService).update(any(RestaurantDto.class));
    }

    /* 3) DELETE -> FK ERROR */
    @Test
    void delete_fkError_shouldRedirectWithError() throws Exception {
        doThrow(new DataIntegrityViolationException("x"))
                .when(restaurantService).delete(5L);

        mvc.perform(get("/admin/restaurants/delete/5")
                        .header("Referer", "/admin/restaurants"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/restaurants"))
                .andExpect(flash().attributeExists("error"));
    }

    /* 4) EDIT -> ILLEGAL STATE */
    @Test
    void edit_illegalState_shouldRedirectWithError() throws Exception {
        when(restaurantService.getById(15L))
                .thenThrow(new IllegalStateException("not allowed"));

        mvc.perform(get("/admin/restaurants/edit/15")
                        .header("Referer", "/admin/restaurants"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/restaurants"))
                .andExpect(flash().attributeExists("error"));
    }
}
