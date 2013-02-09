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
package com.almuradev.reserve.task;

import java.util.List;
import java.util.Map;

import com.almuradev.reserve.ReservePlugin;
import com.almuradev.reserve.econ.Bank;
import com.almuradev.reserve.storage.Reserve;
import com.almuradev.reserve.storage.Storage;

public class SaveTask implements Runnable {
	private final ReservePlugin plugin;
	private final Reserve reserve;
	private final Storage storage;

	public SaveTask(ReservePlugin plugin, Reserve reserve, Storage storage) {
		this.plugin = plugin;
		this.reserve = reserve;
		this.storage = storage;
	}

	@Override
	public void run() {
		final Map<String, List<Bank>> BANKS = reserve.retrieveBanks();
		for (String worldEntry : BANKS.keySet()) {
			for (Bank bankEntry : BANKS.get(worldEntry)) {
				if (!bankEntry.isDirty()) {
					continue;
				}
				//Save to flat file.
				bankEntry.setDirty(false);
			}
		}
	}
}