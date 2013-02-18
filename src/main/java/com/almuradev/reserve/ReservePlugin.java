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

import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;

import com.almuradev.reserve.config.ReserveConfiguration;
import com.almuradev.reserve.econ.Bank;
import com.almuradev.reserve.econ.VaultUtil;
import com.almuradev.reserve.gui.popup.BankPopup;
import com.almuradev.reserve.gui.popup.ReservePopup;
import com.almuradev.reserve.npc.CitizensUtil;
import com.almuradev.reserve.storage.Reserve;
import com.almuradev.reserve.storage.Storage;
import com.almuradev.reserve.task.InterestTask;

import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class ReservePlugin extends JavaPlugin implements Listener {
	public static final Locale CURRENCY_LOCALE = new Locale("en", "US");
	public static final NumberFormat NUMBER_FORMAT = NumberFormat.getCurrencyInstance(CURRENCY_LOCALE);
	public static final String INPUT_REGEX = "([a-zA-Z-\\s]+)";
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
		scheduler = getServer().getScheduler();
		if (config.shouldInterest()) {
			scheduler.scheduleSyncRepeatingTask(this, new InterestTask(this, reserve), 0, config.getInterestInterval());
		}
		scheduler.scheduleSyncRepeatingTask(this, reserve, 0, config.getSaveInterval());
		//Register listeners
		getServer().getPluginManager().registerEvents(this, this);
		final Plugin plugin = getServer().getPluginManager().getPlugin("Citizens");
		if (plugin != null) {
			new CitizensUtil().onEnable();
		}
	}

	public static Reserve getReserve() {
		return reserve;
	}

	public static ReserveConfiguration getConfiguration() {
		return config;
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (!config.shouldTaxDeath() || !event.getEntity().hasPermission("reserve.tax.death")) {
			return;
		}
		final Player died = event.getEntity();
		final double deathTax = config.getDeathTax();
		final double carrying = VaultUtil.getBalance(died.getName());
		final double taxed = carrying - (carrying * deathTax);
		VaultUtil.add(died.getName(), -taxed);
		died.sendMessage("You lost: " + ChatColor.RED + NUMBER_FORMAT.format(taxed) + "!");
		if (died.hasPermission("reserve.tax.death.broadcast")) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.getName().equalsIgnoreCase(event.getEntity().getName())) {
					continue;
				}
				player.sendMessage(ChatColor.AQUA + died.getDisplayName() + " died and lost: " + ChatColor.GOLD + NUMBER_FORMAT.format(taxed) + "!");
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.isCancelled() || !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		final Block interacted = event.getClickedBlock();
		final SpoutPlayer sPlayer = (SpoutPlayer) event.getPlayer();
		
		if (interacted.getState() == null || !(interacted.getState() instanceof Sign)) {
			return;
		}
		final Sign sign = (Sign) interacted.getState();
		//Not a reserve sign
		if (!ChatColor.stripColor(sign.getLine(0).trim().toLowerCase()).contains("[reserve]")) {
			return;
		}
		event.setCancelled(true); //Don't want to place the block
		final String bankName = ChatColor.stripColor(sign.getLine(1).trim().toLowerCase());
		final Bank bank = reserve.get(bankName, sPlayer.getWorld().getName());
		if (bank == null) {
			return;
		}
		new BankPopup(this, sPlayer, bank);
	}

	//TESTING
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Cannot execute Reserve commands from the console!");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("reserve")) {
			final SpoutPlayer player = SpoutManager.getPlayer((Player) sender);
			if (!player.isSpoutCraftEnabled()) {
				player.sendMessage("This command opens the Reserve popup, only available for Spoutcraft clients!");
				return true;
			}
			player.getMainScreen().attachPopupScreen(new ReservePopup(this, player));
			return true;
		}
		return false;
	}
}
