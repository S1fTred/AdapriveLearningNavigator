package com.example.adaprivelearningnavigator.service.support;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KnowledgeBaseLocalizationUtilTest {

    @Test
    void shouldLocalizeRoleNamesForRoadmaps() {
        assertThat(KnowledgeBaseLocalizationUtil.localizeRoleName("android", "Android Developer"))
                .isEqualTo("Android-разработчик");
        assertThat(KnowledgeBaseLocalizationUtil.localizeRoleName("python", "Python Developer"))
                .isEqualTo("Python-разработчик");
        assertThat(KnowledgeBaseLocalizationUtil.localizeRoleName("game-developer", "Game Developer"))
                .isEqualTo("Разработчик игр");
    }

    @Test
    void shouldLocalizeCommonTopicTitles() {
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_ANDROID_LANG", "Pick a Language"))
                .isEqualTo("Выбор языка");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_ANDROID_OOP", "Basics of OOP"))
                .isEqualTo("Основы ООП");
        assertThat(KnowledgeBaseLocalizationUtil.localizeTopicTitle("RM_ANDROID_VERSION", "Version Control"))
                .isEqualTo("Контроль версий");
    }

    @Test
    void shouldLocalizeFormulaDescriptions() {
        String localized = KnowledgeBaseLocalizationUtil.localizeDescription(
                "Тема roadmap по направлению «Android Developer»: Pick a Language. К теме прикреплены материалы для самостоятельного изучения.",
                "Android Developer",
                "Android-разработчик",
                "Pick a Language",
                "Выбор языка"
        );

        assertThat(localized).contains("Android-разработчик");
        assertThat(localized).contains("Выбор языка");
        assertThat(localized).doesNotContain("Android Developer");
        assertThat(localized).doesNotContain("Pick a Language");
        assertThat(localized).doesNotContain("roadmap.sh");
    }
}
