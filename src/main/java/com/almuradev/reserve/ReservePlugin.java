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

import com.almuradev.reserve.config.ReserveConfiguration;
import com.almuradev.reserve.econ.VaultUtil;
import com.almuradev.reserve.gui.popup.BankPopup;
import com.almuradev.reserve.gui.popup.ReservePopup;
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
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class ReservePlugin extends JavaPlugin implements Listener {
	public static final Locale CURRENCY_LOCALE = new Locale("en", "US");
	public static final NumberFormat NUMBER_FORMAT = NumberFormat.getCurrencyInstance(CURRENCY_LOCALE);
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
	}

	public static Reserve getReserve() {
		return reserve;
	}

	public static ReserveConfiguration getConfiguration() {
		return config;
	}

	public static String getPrefix() {
		return "[Reserve] ";
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
		died.sendMessage(getPrefix() + "You lost: " + NUMBER_FORMAT.format(taxed) + "!");
		if (died.hasPermission("reserve.tax.death.broadcast")) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.getName().equalsIgnoreCase(event.getEntity().getName())) {
					continue;
				}
				player.sendMessage(getPrefix() + died.getDisplayName() + " died and lost: " + NUMBER_FORMAT.format(taxed) + "!");
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
		event.setCancelled(true); //Don't want to place the block
		final Sign sign = (Sign) interacted.getState();
		//Not a reserve sign
		if (!ChatColor.stripColor(sign.getLine(0).trim().toLowerCase()).contains("[reserve]")) {
			return;
		}
		final String bankName = ChatColor.stripColor(sign.getLine(1).trim().toLowerCase());
		new BankPopup(this, sPlayer, ReservePlugin.getReserve().get(bankName.trim(), sPlayer.getWorld().getName()));
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
			if (!player.hasPermission("reserve.admin")) {
				player.sendMessage("You do not have permission to open the Reserve popup!");
				return true;
			}
			player.getMainScreen().attachPopupScreen(new ReservePopup(this, player));
			return true;
		}
		return false;
	}
}
