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
package com.almuradev.reserve.gui;

import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.GenericButton;

public class CommandButton extends GenericButton {
	private Object gui;
	private int commandGoal;

	public CommandButton(Object gui, int commandGoal, String text) {
		super(text);
		this.gui = gui;
		this.commandGoal = commandGoal;
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		if (gui instanceof BankMainGUI) {
			((BankMainGUI) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof ReserveMainGUI) {
			((ReserveMainGUI) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof CreateAccountGUI) {
			((CreateAccountGUI) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof AckGUI) {
			((AckGUI) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof DepositGUI) {
			((DepositGUI) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof WithdrawGUI) {
			((WithdrawGUI) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof ReserveConfigGUI) {
			((ReserveConfigGUI) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof BankConfigGUI) {
			((BankConfigGUI) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof OptionsGUI) {
			((OptionsGUI) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof DeleteAccountGUI) {
			((DeleteAccountGUI) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof CreateBankGUI) {
			((CreateBankGUI) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof DeleteBankGUI) {
			((DeleteBankGUI) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof BankStatusGUI) {
			((BankStatusGUI) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof AccountTypesGUI) {
			((AccountTypesGUI) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof RenameBankGUI) {
			((RenameBankGUI) gui).onClickCommand(commandGoal);
		}
	}
}