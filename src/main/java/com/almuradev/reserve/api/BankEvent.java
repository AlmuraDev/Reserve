package com.almuradev.reserve.api;

import com.almuradev.reserve.econ.Bank;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BankEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final Bank bank;
	private boolean cancel = false;

	public BankEvent(Bank bank) {
		this.bank = bank;
	}

	public final Bank getBank() {
		return bank;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
