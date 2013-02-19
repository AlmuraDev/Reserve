package com.almuradev.reserve.api;

import com.almuradev.reserve.econ.Bank;

/**
 * Event fired for when a Bank  is created.
 */
public class CreateBankEvent extends BankEvent {
	public CreateBankEvent(Bank bank) {
		super(bank);
	}
}
