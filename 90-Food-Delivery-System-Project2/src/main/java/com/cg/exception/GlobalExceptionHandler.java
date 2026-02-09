package com.cg.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Helper to determine where to send the user back to.
     * It uses the 'Referer' header to go back to the previous list (Menu or Restaurant).
     */
    private String getRedirectUrl(HttpServletRequest request, String fallback) {
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            // Redirect back to the exact page the user was on
            return "redirect:" + referer;
        }
        return "redirect:" + fallback;
    }

    // ------- Data integrity (FK constraint violations on delete) -------
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleFKViolation(DataIntegrityViolationException ex,
                                    HttpServletRequest request,
                                    RedirectAttributes ra) {

        String path = request.getRequestURI();
        String msg = "Cannot delete this delivery agent because there are deliveries or assignments linked.";
        String fallback = "/admin/restaurants";

        if (path != null && path.contains("/admin/menu-items")) {
            msg = "Cannot delete this item because it’s part of one or more orders.";
            fallback = "/admin/menu-items";
        } else if (path != null && path.contains("/admin/restaurants")) {
            msg = "Cannot delete this restaurant: some of its menu items are used in existing orders.";
            fallback = "/admin/restaurants";
        }

        ra.addFlashAttribute("error", msg);
        return getRedirectUrl(request, fallback);
    }

    // ------- Business rule violations thrown from services -------
    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalState(IllegalStateException ex, HttpServletRequest request, RedirectAttributes ra) {
        ra.addFlashAttribute("error", ex.getMessage());
        return getRedirectUrl(request, "/admin/restaurants");
    }

    // ------- Safety net: any unexpected exception -------
    @ExceptionHandler(Exception.class)
    public String handleAny(Exception ex, HttpServletRequest request, RedirectAttributes ra) {
        // Logging the actual error is helpful for debugging
        System.err.println("Unexpected Error: " + ex.getMessage());
        
        ra.addFlashAttribute("error", "We couldn’t complete that action. Please try again.");
        return getRedirectUrl(request, "/admin/restaurants");
    }
}
