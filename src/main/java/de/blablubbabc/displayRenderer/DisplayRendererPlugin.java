package de.blablubbabc.displayRenderer;

import java.io.IOException;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import de.blablubbabc.displayRenderer.animation.AnimationSystem;
import de.blablubbabc.displayRenderer.animation.SpriteSheetDisplayAnimation;
import de.blablubbabc.displayRenderer.animation.SpriteSheetDisplayFontAnimation;
import de.blablubbabc.displayRenderer.utils.SpriteSheet;
import de.blablubbabc.displayRenderer.utils.SpriteSheetBuilder;

public class DisplayRendererPlugin extends JavaPlugin {

	public static final boolean DEBUG = false;

	private final AnimationSystem animationSystem = new AnimationSystem(this);

	private SpriteSheet spriteSheet;

	@Override
	public void onEnable() {
		animationSystem.start();

		Bukkit.getPluginManager().registerEvents(new EventListener(this), this);

		try {
			var sheet = ImageIO.read(this.getResource("explosion-1-d_64.png"));
			spriteSheet = new SpriteSheetBuilder()
					.withSheet(sheet)
					.withColumns(12)
					.withRows(1)
					.withSpriteCount(12)
					.build();
			this.getLogger().info("SpriteSheet loaded. size=" + spriteSheet.getSize()
					+ ", width=" + spriteSheet.getWidth()
					+ ", height=" + spriteSheet.getWidth());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		animationSystem.stop();
		animationSystem.clear();
	}

	public void debugLog(String message) {
		if (DEBUG) {
			this.getLogger().info(message);
		}
	}

	public void playEffect(Location spawnLocation) {
		var renderer = new SpriteSheetDisplayFontAnimation(this, spriteSheet, 12, spawnLocation);
		animationSystem.add(renderer);
	}
	
	public void playEffectOld(Location spawnLocation) {
		var renderer = new SpriteSheetDisplayAnimation(this, spriteSheet, 8, spawnLocation);
		animationSystem.add(renderer);
	}
}
