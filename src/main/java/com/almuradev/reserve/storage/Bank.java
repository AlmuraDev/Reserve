package com.almuradev.reserve.storage;

import java.util.UUID;

import org.bukkit.World;

public class Bank {
	private final UUID holder;
	private final World world;
	private double balance;

	public Bank(World world, UUID holder) {
		this.world = world;
		this.holder = holder;
	}

	public void add(double amount) {
		balance = balance + amount;
	}

	public World getWorld() {
		return world;
	}

	public UUID getHolder() {
		return holder;
	}
}
