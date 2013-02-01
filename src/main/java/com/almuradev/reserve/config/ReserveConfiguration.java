/*
 * This file is part of Reserve.
 *
 * © 2013 AlmuraDev <http://www.almuradev.com/>
 * Reserve is licensed under the GNU General Public License.
 *
 * Reserve is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Reserve is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License. If not,
 * see <http://www.gnu.org/licenses/> for the GNU General Public License.
 */
package com.almuradev.reserve.config;

import java.io.File;

import com.almuradev.reserve.ReservePlugin;
import com.almuradev.reserve.storage.StorageType;

import org.yaml.snakeyaml.error.YAMLException;

import org.bukkit.configuration.file.FileConfiguration;

public class ReserveConfiguration {
	private final ReservePlugin plugin;
	private FileConfiguration config;

	public ReserveConfiguration(ReservePlugin plugin) {
		this.plugin = plugin;
	}

	public void onLoad() {
		//Read in default config.yml
		if (!new File(plugin.getDataFolder(), "config.yml").exists()) {
			plugin.saveDefaultConfig();
		}
		config = plugin.getConfig();
	}

	public StorageType getSqlType() {
		final String mode = config.getString("sql.mode").toLowerCase();
		switch (mode) {
			case "sql":
				return StorageType.MYSQL;
			case "sqlite":
				return StorageType.SQLITE;
			case "h2":
				return StorageType.H2;
			default:
				throw new YAMLException("Specified mode for SQL configuration: " + mode + " is invalid.");
		}
	}

	public final String getDatabaseName() {
		return config.getString("sql.name", "minecraft");
	}

	public final String getUsername() {
		return config.getString("sql.username", "minecraft");
	}

	public final String getPassword() {
		return config.getString("sql.password", "minecraft");
	}

	public final String getHost() {
		return config.getString("sql.host", "localhost");
	}

	public final int getPort() {
		return config.getInt("sql.port", 25564);
	}
}
