package com.example.adaprivelearningnavigator.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.Locale;

@Component
public class BrowserLaunchOnStartup {

    private static final Logger log = LoggerFactory.getLogger(BrowserLaunchOnStartup.class);

    private final boolean autoOpenEnabled;
    private final String urlPath;

    public BrowserLaunchOnStartup(@Value("${app.browser.auto-open:true}") boolean autoOpenEnabled,
                                  @Value("${app.browser.url-path:/}") String urlPath) {
        this.autoOpenEnabled = autoOpenEnabled;
        this.urlPath = normalizePath(urlPath);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void openBrowser(ApplicationReadyEvent event) {
        if (!autoOpenEnabled) {
            return;
        }

        try {
            Integer port = extractPort(event);
            if (port == null) {
                log.info("Автооткрытие браузера пропущено: web server context недоступен.");
                return;
            }

            URI uri = new URI("http://localhost:" + port + urlPath);
            if (tryDesktopBrowse(uri) || trySystemOpen(uri.toString())) {
                log.info("Открыта стартовая страница в браузере: {}", uri);
                return;
            }

            log.warn("Не удалось автоматически открыть браузер. Откройте вручную: {}", uri);
        } catch (Exception ex) {
            log.warn("Не удалось автоматически открыть браузер", ex);
        }
    }

    private boolean tryDesktopBrowse(URI uri) {
        if (GraphicsEnvironment.isHeadless() || !Desktop.isDesktopSupported()) {
            log.info("Desktop API недоступен. Используется fallback через системный запуск URL.");
            return false;
        }

        try {
            Desktop desktop = Desktop.getDesktop();
            if (!desktop.isSupported(Desktop.Action.BROWSE)) {
                log.info("Desktop browse action недоступен. Используется fallback через системный запуск URL.");
                return false;
            }

            desktop.browse(uri);
            return true;
        } catch (Exception ex) {
            log.debug("Desktop browse не сработал, пробуем системный fallback.", ex);
            return false;
        }
    }

    private boolean trySystemOpen(String url) {
        String os = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        List<String> command = buildCommand(os, url);
        if (command.isEmpty()) {
            return false;
        }

        try {
            new ProcessBuilder(command).start();
            return true;
        } catch (IOException ex) {
            log.debug("Системный запуск URL не сработал.", ex);
            return false;
        }
    }

    private List<String> buildCommand(String os, String url) {
        if (os.contains("win")) {
            return List.of("rundll32", "url.dll,FileProtocolHandler", url);
        }
        if (os.contains("mac")) {
            return List.of("open", url);
        }
        if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            return List.of("xdg-open", url);
        }
        return List.of();
    }

    private Integer extractPort(ApplicationReadyEvent event) {
        try {
            Object applicationContext = event.getApplicationContext();
            Method getWebServer = applicationContext.getClass().getMethod("getWebServer");
            Object webServer = getWebServer.invoke(applicationContext);
            if (webServer == null) {
                return null;
            }

            Method getPort = webServer.getClass().getMethod("getPort");
            Object port = getPort.invoke(webServer);
            return port instanceof Integer ? (Integer) port : null;
        } catch (Exception ex) {
            return null;
        }
    }

    private String normalizePath(String rawPath) {
        if (rawPath == null || rawPath.isBlank()) {
            return "/";
        }
        return rawPath.startsWith("/") ? rawPath : "/" + rawPath;
    }
}
