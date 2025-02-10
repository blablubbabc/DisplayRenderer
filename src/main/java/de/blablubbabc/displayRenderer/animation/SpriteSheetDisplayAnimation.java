package de.blablubbabc.displayRenderer.animation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.TextDisplay;
import org.joml.Matrix4f;

import de.blablubbabc.displayRenderer.DisplayRendererPlugin;
import de.blablubbabc.displayRenderer.utils.ARGBColorCache;
import de.blablubbabc.displayRenderer.utils.SpriteSheet;

public class SpriteSheetDisplayAnimation implements Animation {

	// Display height:
	private static final float HEIGHT = 2.0F;

	// Additional downscaling:
	private static final int PIXEL_PER_ENTITY = 2;

	private final DisplayRendererPlugin plugin;
	private final SpriteSheet spriteSheet;
	private final double totalLengthSeconds;

	private final Location spawnLocation;
	private final List<TextDisplay> entities = new ArrayList<>();

	private long startTime = 0;
	private double progress = 0;

	public SpriteSheetDisplayAnimation(
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
			var imageWidth = spriteSheet.getWidth();
			var imageHeight = spriteSheet.getHeight();

			var width = HEIGHT * imageWidth / imageHeight;
			var heightPerStep = (HEIGHT / imageHeight) * PIXEL_PER_ENTITY;
			var widthPerStep = (width / imageWidth) * PIXEL_PER_ENTITY;

			var textBackgroundTransform = new Matrix4f()
					.translate(0.4f, 0f, 0f)
					.scale(8.0f, 4.0f, 1f);

			var world = spawnLocation.getWorld();
			for (int x = 0; x < spriteSheet.getWidth() / PIXEL_PER_ENTITY; x++) {
				for (int y = 0; y < spriteSheet.getHeight() / PIXEL_PER_ENTITY; y++) {
					var transform = new Matrix4f()
							.translate(x * widthPerStep - width / 2F, y * heightPerStep, 0f)
							.scale(widthPerStep, heightPerStep, 1f)
							.mul(textBackgroundTransform);
					var entity = world.spawn(spawnLocation, TextDisplay.class, e -> {
						this.prepareEntity(e, transform);
					});
					entities.add(entity);
				}
			}
		} catch (Exception e) {
			// Something went wrong. Stop this renderer:
			plugin.getLogger().log(Level.SEVERE, "Spawning text display entities failed!", e);
			progress = 1;
			this.despawn();
		}
	}

	private void prepareEntity(TextDisplay entity, Matrix4f transform) {
		entity.setPersistent(false);
		entity.setBillboard(Billboard.VERTICAL);
		entity.setText(" ");
		entity.setTransformationMatrix(transform);
		entity.setBackgroundColor(ARGBColorCache.SHARED.getOrAdd(0));
		entity.setBrightness(new Brightness(15, 15));
	}

	private void despawn() {
		plugin.debugLog("Despawning effect");
		entities.forEach(x -> x.remove());
	}

	private void render() {
		assert entities.size() > 0;

		var image = spriteSheet.getSprite(progress);

		for (int x = 0; x < spriteSheet.getWidth() / PIXEL_PER_ENTITY; x++) {
			for (int y = 0; y < spriteSheet.getHeight() / PIXEL_PER_ENTITY; y++) {
				var entityIndex = x * spriteSheet.getHeight() / PIXEL_PER_ENTITY + y;
				assert entityIndex >= 0 && entityIndex < entities.size();
				var entity = entities.get(entityIndex);
				assert entity != null;

				// BufferedImage has its origin in the top left corner:
				int argb = image.getRGB(x * PIXEL_PER_ENTITY, spriteSheet.getHeight() - (y * PIXEL_PER_ENTITY) - 1);
				var color = ARGBColorCache.SHARED.getOrAdd(argb);

				entity.setBackgroundColor(color);
			}
		}
	}
}
