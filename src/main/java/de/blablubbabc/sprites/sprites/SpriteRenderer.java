package de.blablubbabc.sprites.sprites;

import java.util.Objects;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.checkerframework.checker.nullness.qual.Nullable;

import de.blablubbabc.sprites.nms.NMSHandler;
import de.blablubbabc.sprites.utils.bukkit.ColorUtils;
import de.blablubbabc.sprites.utils.bukkit.DisplayUtils;
import de.blablubbabc.sprites.utils.bukkit.Log;
import de.blablubbabc.sprites.utils.bukkit.TransformationUtils;
import de.blablubbabc.sprites.utils.java.Validate;

import net.md_5.bungee.api.ChatColor;

/**
 * Renders a sprite using a {@link TextDisplay}.
 */
public class SpriteRenderer {

	private @Nullable Sprite sprite = null;
	private Billboard billboard = Billboard.VERTICAL;
	private @Nullable Brightness brightness = DisplayUtils.FULL_BRIGHTNESS;
	private @Nullable ChatColor color = null;
	private @Nullable Color backgroundColor = ColorUtils.EMPTY;
	private Transformation transformation = TransformationUtils.IDENTITY;

	private @Nullable TextDisplay entity = null;

	public SpriteRenderer() {
	}

	// CONFIGURATION

	public void setSprite(Sprite sprite) {
		if (Objects.equals(this.sprite, sprite)) return;

		this.sprite = sprite;
		this.updateEntity();
	}

	public void setColor(@Nullable ChatColor color) {
		if (Objects.equals(this.color, color)) return;

		this.color = color;
		this.updateEntity();
	}

	public void setBackgroundColor(@Nullable Color backgroundColor) {
		if (Objects.equals(this.backgroundColor, backgroundColor)) return;

		this.backgroundColor = backgroundColor;
		this.updateEntity();
	}

	public void setBillboard(Billboard billboard) {
		if (Objects.equals(this.billboard, billboard)) return;

		this.billboard = billboard;
		this.updateEntity();
	}

	public void setBrightness(@Nullable Brightness brightness) {
		if (Objects.equals(this.brightness, brightness)) return;

		this.brightness = brightness;
		this.updateEntity();
	}

	public void setTransformation(Transformation transformation) {
		if (Objects.equals(this.transformation, transformation)) return;

		this.transformation = transformation;
		this.updateEntity();
	}

	// SPAWNING

	public boolean isSpawned() {
		return entity != null;
	}

	public void spawn(Location location) {
		Validate.notNull(location, "location");
		if (this.isSpawned()) return;

		Log.debug("Spawning SpriteRenderer");
		entity = location.getWorld().spawn(location, TextDisplay.class, this::prepareEntity);
		this.updateEntity();
	}

	private void prepareEntity(TextDisplay entity) {
		entity.setPersistent(false);
		entity.setTeleportDuration(0);
		entity.setViewRange(200);

		// Initially invisible:
		entity.setText("");
		entity.setBackgroundColor(ColorUtils.EMPTY);
	}

	public void despawn() {
		if (!this.isSpawned()) return;

		Log.debug("Despawning SpriteRenderer");
		entity.remove();
		entity = null;
	}

	private void updateEntity() {
		if (!this.isSpawned()) return;

		entity.setBillboard(billboard);
		entity.setBrightness(brightness);
		entity.setBackgroundColor(backgroundColor);
		entity.setTransformation(transformation);

		if (sprite == null) {
			entity.setText(null);
		} else {
			NMSHandler.INSTANCE.setText(entity, sprite.getText(), color, sprite.getFont());
		}
	}

	public void setLocation(Location location) {
		Validate.notNull(location, "location");
		if (!this.isSpawned()) return;

		entity.teleport(location);
	}
}
