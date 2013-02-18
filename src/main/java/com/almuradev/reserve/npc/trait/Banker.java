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
package com.almuradev.reserve.npc.trait;

import com.almuradev.reserve.ReservePlugin;
import com.almuradev.reserve.econ.Bank;
import com.almuradev.reserve.gui.popup.BankPopup;

import net.citizensnpcs.api.ai.speech.SpeechContext;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.util.DataKey;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Banker extends Trait implements Listener {
	@Persist(value = "reserve.bank", required = true) String bankName;
	private final ReservePlugin plugin;

	public Banker() {
		super("Banker");
		this.plugin = (ReservePlugin) Bukkit.getServer().getPluginManager().getPlugin("Reserve");
	}

	@Override
	public void onAttach() {
		bankName = "";
	}

	@Override
	public void load(DataKey key) {
		bankName = key.getString("reserve.bank", "");
	}

	@Override
	public void save(DataKey key) {
		key.setString("reserve.bank", bankName);
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	@EventHandler
	public void onClicked(NPCRightClickEvent event) {
		if (event.getNPC() != getNPC()) {
			return;
		}
		final SpoutPlayer player = SpoutManager.getPlayer(event.getClicker());
		if (player.isSpoutCraftEnabled()) {
			if (bankName == null || bankName.isEmpty()) {

			} else {
				final Bank bank = plugin.getReserve().get(bankName, player.getWorld().getName());
				if (bank == null) {
					getNPC().getDefaultSpeechController().speak(new SpeechContext("My bank was closed. Sorry!", player));
					return;
				}
				player.getMainScreen().attachPopupScreen(new BankPopup(plugin, player, bank));
			}
		}
	}
}
