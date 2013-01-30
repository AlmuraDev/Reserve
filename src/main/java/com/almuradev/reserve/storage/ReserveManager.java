package com.almuradev.reserve.storage;

import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class ReserveManager {
	private static final ArrayList<Bank> BANKS = new ArrayList<>();

	public Bank put(World world, Player player, double amount) {
		if (world == null || player == null) {
			throw new NullPointerException("Specified world or player is null!");
		}
		Bank bank = null;
		for (Bank temp : BANKS) {
			if (temp.getWorld().equals(world) && temp.getHolder().equals(player.getUniqueId())) {
				bank = temp;
				break;
			}
		}
		if (bank == null) {
			bank = new Bank(world, player.getUniqueId());
			BANKS.add(bank);
		}
		bank.add(amount);
		return bank;
	}

	public Bank get(World world, Player player) {
		if (world == null || player == null) {
			throw new NullPointerException("Specified world or player is null!");
		}
		for (Bank bank : BANKS) {
			if (bank.getWorld().equals(world) && bank.getHolder().equals(player.getUniqueId())) {
				return bank;
			}
		}
		return null;
	}

	public Bank remove(World world, Player player) {
		final Bank bank = get(world, player);
		BANKS.remove(bank);
		return bank;
	}
}
