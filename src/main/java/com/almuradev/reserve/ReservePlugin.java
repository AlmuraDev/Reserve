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

import com.almuradev.reserve.npc.ReserveNPCTrait;
import com.almuradev.reserve.storage.Reserve;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;

import org.bukkit.plugin.java.JavaPlugin;

public class ReservePlugin extends JavaPlugin {
	private static final Reserve reserve;

	static {
		reserve = new Reserve();
	}

	@Override
	public void onEnable() {
		//Register the 'Banker' Trait.
		//To set the NPC to that trait, select it and do /trait banker
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(ReserveNPCTrait.class));
	}

	public static Reserve getReserve() {
		return reserve;
	}
}
