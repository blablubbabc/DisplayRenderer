package de.blablubbabc.displayRenderer.utils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SpriteSheetBuilder {

	private BufferedImage spriteSheet;
	private int rows;
	private int columns;
	private int spriteWidth;
	private int spriteHeight;
	private int spriteCount;

	public SpriteSheetBuilder withSheet(BufferedImage image) {
		spriteSheet = image;
		return this;
	}

	public SpriteSheetBuilder withRows(int rows) {
		this.rows = rows;
		return this;
	}

	public SpriteSheetBuilder withColumns(int columns) {
		this.columns = columns;
		return this;
	}

	public SpriteSheetBuilder withSpriteSize(int width, int height) {
		this.spriteWidth = width;
		this.spriteHeight = height;
		return this;
	}

	public SpriteSheetBuilder withSpriteCount(int count) {
		this.spriteCount = count;
		return this;
	}

	protected int getSpriteCount() {
		return spriteCount;
	}

	protected int getColumns() {
		return columns;
	}

	protected int getRows() {
		return rows;
	}

	protected int getSpriteHeight() {
		return spriteHeight;
	}

	protected BufferedImage getSpriteSheet() {
		return spriteSheet;
	}

	protected int getSpriteWidth() {
		return spriteWidth;
	}

	public SpriteSheet build() {
		int count = getSpriteCount();
		int rows = getRows();
		int cols = getColumns();
		if (count == 0) {
			count = rows * cols;
		}

		BufferedImage sheet = this.getSpriteSheet();

		int width = this.getSpriteWidth();
		int height = this.getSpriteHeight();
		if (width == 0) {
			width = sheet.getWidth() / cols;
		}
		if (height == 0) {
			height = sheet.getHeight() / rows;
		}

		int x = 0;
		int y = 0;
		List<BufferedImage> sprites = new ArrayList<>(count);

		for (int index = 0; index < count; index++) {
			sprites.add(sheet.getSubimage(x, y, width, height));
			x += width;
			if (x >= width * cols) {
				x = 0;
				y += height;
			}
		}

		return new SpriteSheet(sprites);
	}
}
