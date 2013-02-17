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
import java.util.Random;

import com.almuradev.reserve.ReservePlugin;

import org.bukkit.configuration.file.FileConfiguration;

public class ReserveConfiguration {
	private final ReservePlugin plugin;
	private FileConfiguration config;
	private final static Random RANDOM = new Random();

	public ReserveConfiguration(ReservePlugin plugin) {
		this.plugin = plugin;
	}

	public void onEnable() {
		//Read in default config.yml
		if (!new File(plugin.getDataFolder(), "config.yml").exists()) {
			plugin.saveDefaultConfig();
		}
		config = plugin.getConfig();
	}

	public boolean shouldInterest() {
		return config.getBoolean("modifier.interest", true);
	}

	public boolean shouldTaxDeath() {
		return config.getBoolean("modifier.tax-death", true);
	}

	public long getInterestInterval() {
		return config.getLong("interval.interest", 1728000);
	}

	public long getSaveInterval() {
		return config.getLong("interval.save", 2400);
	}

	public double getDeathTax() {
		final String raw = config.getString("tax.death-range", "25-75");
		final String[] parsed = raw.split("-");
		double lower, upper;
		lower = upper = 0;
		//Parse lower and upper
		try {
			lower = Double.parseDouble(parsed[0]);
		} catch (Exception e) {
			lower = 0;
		}
		if (parsed.length == 2) {
			try {
				upper = Double.parseDouble(parsed[1]);
			} catch (Exception e) {
				upper = 0;
			}
		}
		//Pick random from range
		return lower + (upper - lower) * RANDOM.nextDouble();
	}
}
