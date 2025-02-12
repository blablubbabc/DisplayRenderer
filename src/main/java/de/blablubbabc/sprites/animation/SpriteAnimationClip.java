package de.blablubbabc.sprites.animation;

import java.util.Collections;
import java.util.List;

import de.blablubbabc.sprites.sprites.Sprite;
import de.blablubbabc.sprites.utils.java.Validate;

/**
 * A sequence of sprites that make up a sprite animation.
 */
public class SpriteAnimationClip {

	private final String name;
	private final List<Sprite> sprites;
	private final double lengthSeconds;

	public SpriteAnimationClip(String name, List<Sprite> sprites, double lenghtSeconds) {
		Validate.notEmpty(name, "name");
		Validate.noNullElements(sprites, "sprites");
		Validate.isTrue(lenghtSeconds >= 0, "lengthSeconds cannot be negative");

		this.name = name;
		this.sprites = Collections.unmodifiableList(sprites);
		this.lengthSeconds = lenghtSeconds;
	}

	public String getName() {
		return name;
	}

	public List<Sprite> getSprites() {
		return sprites;
	}

	public double getLengthSeconds() {
		return lengthSeconds;
	}
}
