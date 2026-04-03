package com.example.adaprivelearningnavigator.service;

public interface ExportService {
    String exportPlanAsJson(Long userId, Long planId);

    String exportPlanAsMarkdown(Long userId, Long planId);

    byte[] exportPlanAsPdf(Long userId, Long planId);
}
