package de.nurteam.economy.utils;

import de.nurteam.economy.Economy;

public class EconomyCalculator {

	Economy economy;

	public EconomyCalculator(Economy economy) {
		this.economy = economy;
	}

	public String toMoney(long euros, long cents) {
		long different = cents + (euros * 100);

		long euroz = 0;
		long centz = 0;

		while (different > 0) {
			different -= 1;
			centz++;
		}
		while (centz >= 100) {
			centz -= 100;
			euroz++;
		}

		String centzz = String.valueOf(centz);

		if (centzz.length() < 2) {
			centzz = "" + (String.valueOf(0) + centz);
		}

		return euroz + "," + centzz + " Euro";
	}
}
