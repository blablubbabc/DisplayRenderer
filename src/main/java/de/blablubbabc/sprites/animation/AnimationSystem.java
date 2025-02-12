package de.blablubbabc.sprites.animation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.nullness.qual.Nullable;

import de.blablubbabc.sprites.utils.java.Validate;

/**
 * Updates animations.
 */
public class AnimationSystem {

	private final Plugin plugin;
	private final List<Animation> animations = new ArrayList<>();
	private @Nullable List<? extends Animation> animationsSnapshot = null;

	private BukkitTask updateTask;

	public AnimationSystem(Plugin plugin) {
		Validate.notNull(plugin, "plugin");
		this.plugin = plugin;
	}

	/**
	 * Gets an unmodifiable snapshot of the currently active animations that can be iterated without
	 * the risk of encountering a {@link ConcurrentModificationException} if the active animations
	 * are modified during the iteration. Any such modifications are not reflected by the returned
	 * snapshot.
	 * <p>
	 * As long as the animations are not modified, this may reuse a previously created snapshot.
	 * 
	 * @return an unmodifiable snapshot of the active animations, not <code>null</code>
	 */
	private List<? extends Animation> getAnimationsSnapshot() {
		if (animationsSnapshot == null) {
			animationsSnapshot = Collections.unmodifiableList(new ArrayList<>(animations));
		}
		assert animationsSnapshot != null;
		return animationsSnapshot;
	}

	public void startUpdates() {
		if (updateTask != null) return;

		updateTask = Bukkit.getScheduler().runTaskTimer(plugin, new AnimationsUpdateTask(), 1L, 1L);
	}

	public void stopUpdates() {
		if (updateTask != null) {
			updateTask.cancel();
			updateTask = null;
		}
	}

	public void stopAllAnimations() {
		this.getAnimationsSnapshot().forEach(Animation::stop);
		animations.clear(); // Just in case
		animationsSnapshot = null;
	}

	// Invoked when an animation is started.
	void add(Animation animation) {
		Validate.notNull(animation, "animation");
		Validate.isTrue(!animations.contains(animation), "animation already added");

		animations.add(animation);
		animationsSnapshot = null;
	}

	// Invoked when an animation is stopped.
	void remove(Animation animation) {
		Validate.notNull(animation, "animation");

		animations.remove(animation);
		animationsSnapshot = null;
	}

	private class AnimationsUpdateTask implements Runnable {
		@Override
		public void run() {
			update();
		}
	}

	private void update() {
		// Iterate a snapshot: Animations may stop themselves during the update, e.g. if they reach
		// their end and are non-looping.
		this.getAnimationsSnapshot().forEach(Animation::update);
	}
}
