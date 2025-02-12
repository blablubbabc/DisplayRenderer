package de.blablubbabc.sprites;

import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import de.blablubbabc.sprites.animation.AnimationSystem;
import de.blablubbabc.sprites.animation.SpriteAnimation;
import de.blablubbabc.sprites.animation.SpriteAnimationClip;
import de.blablubbabc.sprites.controls.EventListener;
import de.blablubbabc.sprites.controls.PlayerInputHandler;
import de.blablubbabc.sprites.entity.SpriteEntity;
import de.blablubbabc.sprites.sprites.Sprite;
import de.blablubbabc.sprites.sprites.SpriteSheetBuilder;
import de.blablubbabc.sprites.utils.bukkit.Log;
import de.blablubbabc.sprites.utils.java.Validate;

public class SpritesPlugin extends JavaPlugin {

	private static SpritesPlugin instance;

	public static boolean isPluginEnabled() {
		return (instance != null);
	}

	public static SpritesPlugin getInstance() {
		return Validate.State.notNull(instance, "Plugin is not enabled!");
	}

	private final PlayerInputHandler playerInputHandler = new PlayerInputHandler(this);
	private final AnimationSystem animationSystem = new AnimationSystem(this);

	private SpriteAnimationClip animationClip;

	@Override
	public void onLoad() {
		Log.setLogger(this.getLogger());
		instance = this;
	}

	@Override
	public void onEnable() {
		playerInputHandler.onEnable();
		animationSystem.startUpdates();

		Bukkit.getPluginManager().registerEvents(new EventListener(this), this);

		try {
			var sheet = ImageIO.read(this.getResource("explosion-1-d_64.png"));
			var spriteSheet = new SpriteSheetBuilder()
					.withSheet(sheet)
					.withColumns(12)
					.withRows(1)
					.withSpriteCount(12)
					.build();
			this.getLogger().info("SpriteSheet loaded. size=" + spriteSheet.getSize()
					+ ", width=" + spriteSheet.getWidth()
					+ ", height=" + spriteSheet.getWidth());

			var sprites = new ArrayList<Sprite>();
			for (int i = 0; i < spriteSheet.getSize(); i++) {
				char character = (char) ('a' + i);
				sprites.add(new Sprite(character, "sprites:explosion-1-d_128"));
			}
			this.animationClip = new SpriteAnimationClip("explosion-1-d_128", sprites, 1.0D);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// TODO

		// Load spritesheets
		// Generate resource pack
		// Write resource pack if not existing or outdated.
	}

	@Override
	public void onDisable() {
		playerInputHandler.onDisable();
		animationSystem.stopUpdates();
		animationSystem.stopAllAnimations();
		instance = null;
	}

	public void playEffect(Location spawnLocation) {
		var entity = new SpriteEntity();
		entity.setLocation(spawnLocation);

		var animation = new SpriteAnimation(animationSystem, animationClip);
		animation.setLooping(false);

		entity.getAnimator().setAnimation(animation);
	}
}
