package com.a6raywa1cher.db_rgr.config;

import lombok.SneakyThrows;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;

public class ConfigLoader {
	private final String fileName;
	private Config config;

	public ConfigLoader(String fileName) {
		this.fileName = fileName;
	}

	@SneakyThrows
	public Config getConfig() {
		if (config == null) {
			Constructor constructor = new Constructor(Config.class);
			Yaml yaml = new Yaml(constructor);

			Path path = Path.of(System.getProperty("user.dir"), fileName);
			File file = path.toFile();

			if (file.exists()) {
				config = yaml.load(new FileInputStream(file));
			} else {
				config = yaml.load(getClass().getClassLoader().getResourceAsStream(fileName));
			}
		}
		return config;
	}
}
