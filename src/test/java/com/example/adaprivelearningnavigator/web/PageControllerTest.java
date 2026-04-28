package com.example.adaprivelearningnavigator.web;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PageControllerTest {

    private final PageController controller = new PageController();

    @Test
    void shouldMapLandingRoute() {
        assertEquals("forward:/index.html", controller.landing());
    }

    @Test
    void shouldMapPrivateRoutes() {
        assertEquals("forward:/dashboard.html", controller.dashboard());
        assertEquals("forward:/roadmap.html", controller.roadmap());
        assertEquals("forward:/plan.html", controller.plan());
        assertEquals("forward:/profile.html", controller.profile());
        assertEquals("redirect:/plan", controller.progress(new MockHttpServletRequest()));
    }

    @Test
    void shouldRedirectProgressRouteWithQueryParams() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setQueryString("planId=42");

        assertEquals("redirect:/plan?planId=42", controller.progress(request));
    }

    @Test
    void shouldMapAuthRoutes() {
        assertEquals("forward:/login.html", controller.login());
        assertEquals("forward:/register.html", controller.register());
    }
}
