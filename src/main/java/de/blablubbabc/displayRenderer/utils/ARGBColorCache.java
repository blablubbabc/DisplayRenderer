package de.blablubbabc.displayRenderer.utils;

import org.bukkit.Color;

public class ARGBColorCache extends LRUCache<Integer, Color> {

	private static final long serialVersionUID = -5522180022665988388L;

	// 1024 * 1024: Enough space for a 1024 x 1024 image with each pixel having a distinct color.
	// Roughly 60MB at most:
	public static final ARGBColorCache SHARED = new ARGBColorCache(1024 * 1024);

	private ARGBColorCache(int maxSize) {
		super(maxSize);
	}

	public Color getOrAdd(int argb) {
		return this.computeIfAbsent(argb, Color::fromARGB);
	}
}
