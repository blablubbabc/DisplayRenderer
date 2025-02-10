package de.blablubbabc.displayRenderer.animation;

import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.TextDisplay;

import de.blablubbabc.displayRenderer.DisplayRendererPlugin;
import de.blablubbabc.displayRenderer.nms.NMSHandler;
import de.blablubbabc.displayRenderer.utils.ARGBColorCache;
import de.blablubbabc.displayRenderer.utils.SpriteSheet;

public class SpriteSheetDisplayFontAnimation implements Animation {

	private static final char[] ALPHABET = new char[128];
	static {
		char start = 'a';
		for (int i = 0; i < 128; i++) {
			ALPHABET[i] = (char) (start + i);
		}
	}

	private final DisplayRendererPlugin plugin;
	private final SpriteSheet spriteSheet;
	private final double totalLengthSeconds;

	private final Location spawnLocation;

	private TextDisplay entity;
	private long startTime = 0;
	private double progress = 0;

	public SpriteSheetDisplayFontAnimation(
			DisplayRendererPlugin plugin,
			SpriteSheet spriteSheet,
			double fps,
			Location spawnLocation
	) {
		this.plugin = plugin;
		this.spriteSheet = spriteSheet;
		this.totalLengthSeconds = spriteSheet.getSize() / fps;
		this.spawnLocation = spawnLocation.clone();
	}

	@Override
	public void update() {
		if (this.isOver()) {
			this.despawn();
			return;
		}

		var now = System.nanoTime();

		if (startTime == 0) {
			startTime = now;
			this.spawn();
			if (this.isOver()) {
				return;
			}
		}

		var effectTimeSeconds = (now - startTime) / 1_000_000_000.0D;

		progress = effectTimeSeconds / totalLengthSeconds;

		plugin.debugLog("Progress: " + progress);
		this.render();
	}

	@Override
	public boolean isOver() {
		return startTime != 0 && progress >= 1;
	}

	@Override
	public boolean removeIfOver() {
		if (this.isOver()) {
			this.despawn();
			return true;
		}

		return false;
	}

	private void spawn() {
		plugin.debugLog("Spawning effect");
		try {
			var world = spawnLocation.getWorld();
			this.entity = world.spawn(spawnLocation, TextDisplay.class, e -> {
				this.prepareEntity(e);
			});
		} catch (Exception e) {
			// Something went wrong. Stop this renderer:
			plugin.getLogger().log(Level.SEVERE, "Spawning text display entities failed!", e);
			progress = 1;
			this.despawn();
		}
	}

	private void prepareEntity(TextDisplay entity) {
		entity.setPersistent(false);
		entity.setBillboard(Billboard.VERTICAL);
		entity.setText(String.valueOf(ALPHABET[0]));
		// entity.setTransformationMatrix(transform);
		entity.setBackgroundColor(ARGBColorCache.SHARED.getOrAdd(0));
		entity.setBrightness(new Brightness(15, 15));
	}

	private void despawn() {
		if (entity == null) return;
		plugin.debugLog("Despawning effect");
		entity.remove();
		entity = null;
	}

	private void render() {
		assert entity != null;

		var spriteIndex = spriteSheet.toSpriteIndex(progress);
		NMSHandler.INSTANCE.setText(entity, String.valueOf(ALPHABET[spriteIndex]), "display_renderer:explosion-1-d_128");
	}
}
