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
