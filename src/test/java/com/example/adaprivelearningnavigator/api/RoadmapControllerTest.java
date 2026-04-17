package com.example.adaprivelearningnavigator.api;

import com.example.adaprivelearningnavigator.security.UserPrincipal;
import com.example.adaprivelearningnavigator.service.RoadmapService;
import com.example.adaprivelearningnavigator.service.dto.common.PageResponse;
import com.example.adaprivelearningnavigator.service.dto.roadmap.RoadmapDetailResponse;
import com.example.adaprivelearningnavigator.service.dto.roadmap.RoadmapSummaryResponse;
import com.example.adaprivelearningnavigator.service.dto.roadmap.RoadmapTopicDetailResponse;
import com.example.adaprivelearningnavigator.service.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Clock;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RoadmapControllerTest {

    @Mock
    private RoadmapService roadmapService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();

        mockMvc = MockMvcBuilders.standaloneSetup(new RoadmapController(roadmapService))
                .setControllerAdvice(new GlobalExceptionHandler(Clock.systemUTC()))
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void shouldReturnUnauthorizedWhenAuthenticationMissing() throws Exception {
        mockMvc.perform(get("/api/roadmaps"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldReturnRoadmapCatalog() throws Exception {
        when(roadmapService.getRoadmaps(0, 12))
                .thenReturn(PageResponse.<RoadmapSummaryResponse>builder()
                        .items(List.of(RoadmapSummaryResponse.builder()
                                .id(9201L)
                                .code("java-backend")
                                .name("Java Backend Developer")
                                .topicCount(8)
                                .requiredTopicCount(6)
                                .build()))
                        .total(1)
                        .page(0)
                        .size(12)
                        .build());

        mockMvc.perform(get("/api/roadmaps")
                        .principal(authentication()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].code").value("java-backend"))
                .andExpect(jsonPath("$.items[0].topicCount").value(8));
    }

    @Test
    void shouldReturnRoadmapDetail() throws Exception {
        when(roadmapService.getRoadmap(9201L))
                .thenReturn(RoadmapDetailResponse.builder()
                        .id(9201L)
                        .code("java-backend")
                        .name("Java Backend Developer")
                        .topics(List.of())
                        .build());

        mockMvc.perform(get("/api/roadmaps/9201")
                        .principal(authentication()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("java-backend"));
    }

    @Test
    void shouldReturnRoadmapTopicDetail() throws Exception {
        when(roadmapService.getRoadmapTopic(9201L, 9107L))
                .thenReturn(RoadmapTopicDetailResponse.builder()
                        .roleId(9201L)
                        .roleCode("java-backend")
                        .topicId(9107L)
                        .topicCode("SPRING_BOOT")
                        .topicTitle("Spring Boot")
                        .resources(List.of())
                        .build());

        mockMvc.perform(get("/api/roadmaps/9201/topics/9107")
                        .principal(authentication()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.topicCode").value("SPRING_BOOT"));
    }

    private UsernamePasswordAuthenticationToken authentication() {
        UserPrincipal principal = new UserPrincipal(42L, "test@example.com");
        return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    }
}
