package de.blablubbabc.sprites.debug;

import java.util.ArrayList;
import java.util.List;

/**
 * Access to the debugging state.
 */
public final class Debug {

	private static boolean debugging = false;
	private static final List<String> debugOptions = new ArrayList<>();

	public static boolean isDebugging() {
		return isDebugging(null);
	}

	public static boolean isDebugging(String option) {
		return debugging && (option == null || debugOptions.contains(option));
	}

	private Debug() {
	}
}
