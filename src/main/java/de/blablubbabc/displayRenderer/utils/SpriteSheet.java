package de.blablubbabc.displayRenderer.utils;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;

public class SpriteSheet {

	private final List<BufferedImage> sprites;
	private final List<BufferedImage> spritesView;
	private final int width;
	private final int height;

	public SpriteSheet(List<BufferedImage> sprites) {
		if (sprites == null || sprites.size() == 0) {
			throw new IllegalArgumentException("sprites is null or empty!");
		}

		this.sprites = sprites;
		this.spritesView = Collections.unmodifiableList(sprites);
		this.width = sprites.get(0).getWidth();
		this.height = sprites.get(0).getHeight();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getSize() {
		return sprites.size();
	}

	public List<BufferedImage> getSprites() {
		return spritesView;
	}

	public int toSpriteIndex(double progress) {
		if (progress < 0) progress = 0;
		else if (progress > 1) progress = 1;

		int spriteIndex = (int) (sprites.size() * progress);
		if (spriteIndex == sprites.size()) spriteIndex -= 1;

		return spriteIndex;
	}
	
	public BufferedImage getSprite(double progress) {
		return sprites.get(this.toSpriteIndex(progress));
	}
}
