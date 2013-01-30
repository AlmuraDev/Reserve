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

import com.almuradev.reserve.ReservePlugin;
import com.almuradev.reserve.storage.Bank;
import com.almuradev.reserve.storage.Reserve;

public class TaxTask implements Runnable {
	private final ReservePlugin plugin;
	private final Reserve reserve;

	public TaxTask(ReservePlugin plugin, Reserve reserve) {
		this.plugin = plugin;
		this.reserve = reserve;
	}

	@Override
	public void run() {
		final List<Bank> BANKS = reserve.retrieveAccounts();
		//Tax the suckers here!
	}
}