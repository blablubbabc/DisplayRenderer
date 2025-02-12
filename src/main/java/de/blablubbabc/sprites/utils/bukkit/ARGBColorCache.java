package de.blablubbabc.sprites.utils.bukkit;

import org.bukkit.Color;

import de.blablubbabc.sprites.utils.java.LRUCache;

public class ARGBColorCache extends LRUCache<Integer, Color> {

	private static final long serialVersionUID = -5522180022665988388L;

	// Enough space for a 512 x 512 image with each pixel having a distinct color. Roughly 15MB.
	public static final ARGBColorCache SHARED = new ARGBColorCache(512 * 512);

	private ARGBColorCache(int maxSize) {
		super(maxSize);
	}

	public Color getOrAdd(int argb) {
		return this.computeIfAbsent(argb, Color::fromARGB);
	}
}
