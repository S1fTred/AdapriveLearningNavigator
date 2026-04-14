package com.example.adaprivelearningnavigator.web;

import org.junit.jupiter.api.Test;

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
        assertEquals("forward:/progress.html", controller.progress());
    }

    @Test
    void shouldMapAuthRoutes() {
        assertEquals("forward:/login.html", controller.login());
        assertEquals("forward:/register.html", controller.register());
    }
}
