package de.blablubbabc.displayRenderer.animation;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class AnimationSystem {

	private final Plugin plugin;
	private final List<Animation> animations = new ArrayList<>();

	private BukkitTask task;

	public AnimationSystem(Plugin plugin) {
		this.plugin = plugin;
	}

	public void start() {
		if (task != null) return;

		task = Bukkit.getScheduler().runTaskTimer(plugin, new AnimationSystemTask(), 1L, 1L);
	}

	public void stop() {
		if (task != null) {
			task.cancel();
			task = null;
		}
	}

	public void clear() {
		animations.clear();
	}

	public void add(Animation animation) {
		animations.add(animation);
	}

	private class AnimationSystemTask implements Runnable {
		@Override
		public void run() {
			update();
		}
	}

	private void update() {
		animations.removeIf(x -> x.removeIfOver());
		animations.forEach(x -> x.update());
		// Note: Remove animations that are now over not here, but at the start of the next update
		// in order to still show their final frame.
	}
}
