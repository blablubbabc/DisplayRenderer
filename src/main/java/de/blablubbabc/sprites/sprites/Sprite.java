package de.blablubbabc.sprites.sprites;

import de.blablubbabc.sprites.utils.java.Validate;

/**
 * A sprite represented as a glyph of a font.
 */
public class Sprite {

	private final char character;
	private final String text; // Cache String containing the character
	private final String font;

	public Sprite(char character, String font) {
		Validate.notEmpty(font);
		this.character = character;
		this.text = String.valueOf(character);
		this.font = font;
	}

	public char getCharacter() {
		return character;
	}

	public String getText() {
		return text;
	}

	public String getFont() {
		return font;
	}
}
