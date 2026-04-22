package com.example.adaprivelearningnavigator.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.server.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class BrowserLaunchOnStartup implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger log = LoggerFactory.getLogger(BrowserLaunchOnStartup.class);

    private final boolean autoOpenEnabled;
    private final String urlPath;
    private final AtomicBoolean attempted = new AtomicBoolean(false);

    public BrowserLaunchOnStartup(@Value("${app.browser.auto-open:true}") boolean autoOpenEnabled,
                                  @Value("${app.browser.url-path:/}") String urlPath) {
        this.autoOpenEnabled = autoOpenEnabled;
        this.urlPath = normalizePath(urlPath);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        openFromContext(event.getApplicationContext());
    }

    @EventListener
    public void onWebServerInitialized(WebServerInitializedEvent event) {
        openFromPort(event.getWebServer().getPort());
    }

    public void openFromContext(ApplicationContext applicationContext) {
        if (!autoOpenEnabled) {
            log.info("Автооткрытие браузера отключено в конфигурации.");
            return;
        }

        Integer port = extractPort(applicationContext);
        if (port == null) {
            log.info("Автооткрытие браузера пропущено: web server context недоступен.");
            return;
        }

        openFromPort(port);
    }

    private void openFromPort(int port) {
        if (!attempted.compareAndSet(false, true)) {
            return;
        }

        try {
            URI uri = new URI("http://localhost:" + port + urlPath);
            log.info("Пробуем открыть браузер на адресе: {}", uri);

            if (tryDesktopBrowse(uri) || trySystemOpen(uri.toString())) {
                log.info("Стартовая страница открыта в браузере: {}", uri);
                return;
            }

            log.warn("Не удалось автоматически открыть браузер. Откройте вручную: {}", uri);
        } catch (Exception ex) {
            log.warn("Не удалось автоматически открыть браузер", ex);
        }
    }

    private boolean tryDesktopBrowse(URI uri) {
        if (GraphicsEnvironment.isHeadless() || !Desktop.isDesktopSupported()) {
            log.info("Desktop API недоступен. Используется системный fallback.");
            return false;
        }

        try {
            Desktop desktop = Desktop.getDesktop();
            if (!desktop.isSupported(Desktop.Action.BROWSE)) {
                log.info("Desktop browse action недоступен. Используется системный fallback.");
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
        List<List<String>> commandVariants = buildCommands(os, url);
        if (commandVariants.isEmpty()) {
            return false;
        }

        for (List<String> command : commandVariants) {
            try {
                new ProcessBuilder(command).start();
                return true;
            } catch (IOException ex) {
                log.debug("Команда открытия браузера не сработала: {}", command, ex);
            }
        }

        return false;
    }

    private List<List<String>> buildCommands(String os, String url) {
        if (os.contains("win")) {
            return List.of(
                    List.of("cmd", "/c", "start", "", url),
                    List.of("powershell", "-NoProfile", "-Command", "Start-Process", url),
                    List.of("explorer.exe", url)
            );
        }
        if (os.contains("mac")) {
            return List.of(List.of("open", url));
        }
        if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            return List.of(List.of("xdg-open", url));
        }
        return List.of();
    }

    private Integer extractPort(ApplicationContext applicationContext) {
        try {
            Method getWebServer = applicationContext.getClass().getMethod("getWebServer");
            Object webServer = getWebServer.invoke(applicationContext);
            if (webServer == null) {
                return null;
            }

            Method getPort = webServer.getClass().getMethod("getPort");
            Object port = getPort.invoke(webServer);
            return port instanceof Integer ? (Integer) port : null;
        } catch (Exception ex) {
            log.debug("Не удалось определить порт web server для автооткрытия браузера.", ex);
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
