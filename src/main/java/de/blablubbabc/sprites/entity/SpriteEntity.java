package de.blablubbabc.sprites.entity;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import de.blablubbabc.sprites.sprites.SpriteRenderer;

public class SpriteEntity {

	private String worldName = "";
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;

	private final SpriteRenderer spriteRenderer = new SpriteRenderer();
	private final Animator animator = new Animator(this);

	public SpriteEntity() {
	}

	public SpriteRenderer getRenderer() {
		return spriteRenderer;
	}

	public Animator getAnimator() {
		return animator;
	}

	/**
	 * @return the worldName
	 */
	public String getWorldName() {
		return worldName;
	}

	/**
	 * @return the world, or <code>null</code> if not found
	 */
	public World getWorld() {
		return worldName.isEmpty() ? null : Bukkit.getWorld(worldName);
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return the z
	 */
	public double getZ() {
		return z;
	}

	/**
	 * @return the yaw
	 */
	public float getYaw() {
		return yaw;
	}

	/**
	 * @return the pitch
	 */
	public float getPitch() {
		return pitch;
	}

	/**
	 * @return the location, or <code>null</code> if the world is not found
	 */
	public Location getLocation() {
		var world = this.getWorld();
		if (world == null) return null;

		return new Location(world, x, y, z, yaw, pitch);
	}

	public void setLocation(String worldName, double x, double y, double z) {
		this.setLocation(worldName, x, y, z, yaw, pitch);
	}

	public void setLocation(String worldName, double x, double y, double z, float yaw, float pitch) {
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;

		this.onLocationChanged();
	}

	public void setLocation(Location location) {
		this.setLocation(
				location.getWorld().getName(),
				location.getX(),
				location.getY(),
				location.getZ(),
				location.getYaw(),
				location.getPitch()
		);
	}

	private void onLocationChanged() {
		var location = this.getLocation();
		if (location == null) {
			spriteRenderer.despawn();
		} else {
			spriteRenderer.setLocation(location);
		}
	}
}
