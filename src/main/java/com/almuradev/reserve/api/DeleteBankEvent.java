package com.almuradev.reserve.api;

import com.almuradev.reserve.econ.Bank;

public class DeleteBankEvent extends BankEvent {
	public DeleteBankEvent(Bank bank) {
		super(bank);
	}
}
