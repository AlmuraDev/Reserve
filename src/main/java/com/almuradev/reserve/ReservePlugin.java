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
package com.almuradev.reserve;

import com.almuradev.reserve.config.ReserveConfiguration;
import com.almuradev.reserve.gui.ReserveMainGUI;
import com.almuradev.reserve.storage.Reserve;
import com.almuradev.reserve.storage.SaveTask;
import com.almuradev.reserve.storage.Storage;
import com.almuradev.reserve.task.InterestTask;

import org.getspout.spoutapi.player.SpoutPlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class ReservePlugin extends JavaPlugin {
	private static Reserve reserve;
	private static Storage storage;
	private static ReserveConfiguration config;
	private BukkitScheduler scheduler;

	@Override
	public void onDisable() {
		scheduler.cancelTasks(this);
		reserve.onDisable();
	}

	@Override
	public void onEnable() {
		//Load configuration
		config = new ReserveConfiguration(this);
		config.onEnable();
		//Load storage
		storage = new Storage(this);
		storage.onEnable();
		//Load reserve
		reserve = new Reserve(storage);
		reserve.onEnable();
		//Schedule tasks
		scheduler = Bukkit.getServer().getScheduler();
		if (config.shouldInterest()) {
			scheduler.scheduleSyncRepeatingTask(this, new InterestTask(this, reserve), 0, config.getInterestInterval());
		}
		scheduler.scheduleSyncRepeatingTask(this, new SaveTask(this, reserve, storage), 0, config.getSaveInterval());
	}

	public static Reserve getReserve() {
		return reserve;
	}

	public static Storage getStorage() {
		return storage;
	}

	public static ReserveConfiguration getConfiguration() {
		return config;
	}

	//TESTING
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}

		if (cmd.getName().equalsIgnoreCase("reserve")) {
			if (player == null) {
				sender.sendMessage("Reserve cannot be opened from the server console.");
			} else {
				((SpoutPlayer) sender).getMainScreen().attachPopupScreen(new ReserveMainGUI(this, (SpoutPlayer) sender));
			}
			return true;
		}
		return false;
	}
}
