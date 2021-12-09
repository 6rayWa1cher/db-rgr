package com.a6raywa1cher.db_rgr.lib;

import lombok.SneakyThrows;

import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;

public final class ResourcesUtils {
    @SneakyThrows
    public static Path getPathOfResource(String resource) {
        URL url = Thread.currentThread().getContextClassLoader().getResource("config.yml");
        Objects.requireNonNull(url);
        return Path.of(url.toURI());
    }
}
