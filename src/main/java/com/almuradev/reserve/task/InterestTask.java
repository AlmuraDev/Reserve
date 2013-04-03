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
import com.almuradev.reserve.econ.Account;
import com.almuradev.reserve.econ.Bank;
import com.almuradev.reserve.storage.Reserve;

public class InterestTask implements Runnable {
	private final ReservePlugin plugin;
	private final Reserve reserve;

	public InterestTask(ReservePlugin plugin, Reserve reserve) {
		this.plugin = plugin;
		this.reserve = reserve;
	}

	@Override
	public void run() {
		final Map<String, List<Bank>> BANKS = reserve.retrieveBanks();
		for (String world : BANKS.keySet()) {
			for (Bank bank : BANKS.get(world)) {
				final Account interestPayable = bank.getInterestPayable();
				for (Account account : bank.retrieveAccounts()) {
					if (!account.getType().receivesInterest()) {
						continue;
					}
					//I = P r t
					//Rate is per account basis, time is based on 6 times daily (once every 4 hours).
					//TODO Make this configurable for Reserve 1.0
					double interest = account.getBalance() * account.getType().getInterestRate() * (1 / 2190);
					//Have the bank's balance payout interest if no account is specified to do interest payouts.
					if (interestPayable == null) {
						if (bank.getBalance() <= 0) {
							continue;
						}
						if (bank.getBalance() - interest <= 0) {
							interest = bank.getBalance();
						}
						account.add(interest);
						bank.setBalance(bank.getBalance() - interest);
					} else {
						if (interestPayable.getBalance() <= 0) {
							continue;
						}
						if (interestPayable.getBalance() - interest <= 0) {
							interest = account.getBalance();
						}
						account.add(interest);
						interestPayable.add(-interest);
					}
				}
			}
		}
	}
}
