package com.a6raywa1cher.db_rgr.config;

import lombok.SneakyThrows;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.nio.file.Path;

public class ConfigLoader {
	private final Path configPath;
	private Config config;

	public ConfigLoader(Path configPath) {
		this.configPath = configPath;
	}

	@SneakyThrows
	public Config getConfig() {
		if (config == null) {
			Constructor constructor = new Constructor(Config.class);
			Yaml yaml = new Yaml(constructor);
			config = yaml.load(new FileInputStream(configPath.toFile()));
		}
		return config;
	}
}
