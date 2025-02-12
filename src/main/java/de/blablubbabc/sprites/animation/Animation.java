package de.blablubbabc.sprites.animation;

import java.util.ArrayList;
import java.util.List;

import de.blablubbabc.sprites.utils.bukkit.Log;
import de.blablubbabc.sprites.utils.java.MathUtils;
import de.blablubbabc.sprites.utils.java.Validate;

/**
 * Animation run state.
 */
public class Animation {

	private final AnimationSystem animationSystem;

	private final String name;
	private final double lengthSeconds;

	private final List<AnimationListener> listeners = new ArrayList<>(1);
	private List<? extends AnimationListener> listenersSnapshot = null;

	private double speed = 1.0D;
	private boolean looping = true;

	private boolean running = false;
	private long lastUpdateTime = 0;

	// The animation time within the current iteration:
	private double animationTimeSeconds = 0.0D;
	// Progress of the current iteration, between 0 and 1 (exclusive):
	private double progress = 0.0D;
	// Iteration counter since last reset.
	// Long: Unlikely to ever overrun.
	private long iteration = 1;

	public Animation(
			AnimationSystem animationSystem,
			String name,
			double lengthSeconds
	) {
		Validate.notNull(animationSystem, "animationSystem");
		Validate.notEmpty(name, "name");
		Validate.isTrue(lengthSeconds >= 0, "lengthSeconds must be positive");

		this.animationSystem = animationSystem;
		this.name = name;
		this.lengthSeconds = lengthSeconds;
	}

	// Configuration

	public final String getName() {
		return name;
	}

	/**
	 * The animation speed multiplier.
	 * <p>
	 * Can also be negative to play the animation in reverse. However, the iteration counter is only
	 * incremented, never decremented.
	 * 
	 * @return the animation speed multiplier
	 */
	public final double getSpeed() {
		return speed;
	}

	public final void setSpeed(double speed) {
		this.speed = speed;
	}

	/**
	 * @return <code>true</code> if the animation is looping, <code>false</code> if the animation is
	 *         automatically stopped at the end of the first iteration
	 */
	public final boolean isLooping() {
		return looping;
	}

	public final void setLooping(boolean looping) {
		this.looping = looping;
	}

	// Listeners

	public void addListener(AnimationListener listener) {
		Validate.notNull(listener, "listener");
		listeners.add(listener);
		listenersSnapshot = null;
	}

	public void removeListener(AnimationListener listener) {
		if (listeners.remove(listener)) {
			listenersSnapshot = null;
		}
	}

	private List<? extends AnimationListener> getListenersSnapshot() {
		if (listenersSnapshot == null) {
			listenersSnapshot = new ArrayList<>(listeners);
		}
		return listenersSnapshot;
	}

	// Run state

	public final boolean isRunning() {
		return running;
	}

	public final void start() {
		if (this.isRunning()) return;

		lastUpdateTime = System.nanoTime();
		running = true;
		// Not resetting progress and iteration here: Animations can be paused and continued.

		animationSystem.add(this);

		this.onStart();

		// Immediately invoke the start and update events for the current frame:
		this.callEvents();
	}

	public final void stop() {
		if (!this.isRunning()) return;

		running = false;
		animationSystem.remove(this);

		this.onStop();
	}

	public final double getProgress() {
		return progress;
	}

	// Does not affect the iteration counter. Does not
	public final void setProgress(double progress) {
		var clampedProgress = MathUtils.clamp(progress, 0.0D, 1.0D);
		if (clampedProgress == 1.0D) {
			clampedProgress = 0.0D;
		}

		if (this.progress == clampedProgress) return;

		this.progress = clampedProgress;

		this.callEvents();
	}

	public final long getIteration() {
		return iteration;
	}

	public final void reset() {
		if (this.progress == 0.0D && this.iteration == 1) return;

		progress = 0.0D;
		iteration = 1;

		this.callEvents();
	}

	public final boolean isOver() {
		return !this.isLooping() && iteration > 1;
	}

	public final void update() {
		if (!this.isRunning()) return;
		if (this.isOver()) {
			this.stop();
			return;
		}

		var now = System.nanoTime();

		var deltaSeconds = (now - lastUpdateTime) / 1_000_000_000.0D;
		this.animationTimeSeconds += deltaSeconds * speed;
		this.lastUpdateTime = now;

		var endReached = false;
		var newProgress = animationTimeSeconds / lengthSeconds;
		if (newProgress < 0.0D) {
			newProgress *= -1;
		}
		if (newProgress >= 1.0D) {
			endReached = true;

			var iterationIncrements = (long) newProgress;
			newProgress -= iterationIncrements;
			this.iteration += iterationIncrements;
			this.animationTimeSeconds -= (int) animationTimeSeconds;
		}

		this.progress = newProgress;
		Log.debug(() -> "Animation " + name + ": " + "Progress=" + progress);

		this.onUpdate();

		if (endReached) {
			// Note: Even if the iteration counter was incremented by more than one, we trigger the
			// onIterationEnd event at most once.
			this.onIterationEnd();

			if (this.isLooping()) {
				this.onIterationStart();
			} else {
				this.stop();
			}
		}
	}

	private void callEvents() {
		if (!this.isRunning()) return;

		if (progress == 0.0D) {
			this.onIterationStart();
		}
		this.onUpdate();
	}

	/**
	 * Called when the animation is started.
	 */
	protected void onStart() {
		this.getListenersSnapshot().forEach(AnimationListener::onStart);
	}

	/**
	 * Called when the animation is stopped.
	 */
	protected void onStop() {
		this.getListenersSnapshot().forEach(AnimationListener::onStop);
	}

	/**
	 * Called at the beginning of each animation iteration, i.e. when the animation is started and
	 * whenever the iteration has ended and the animation is {@link #isLooping() looping}.
	 */
	protected void onIterationStart() {
		this.getListenersSnapshot().forEach(AnimationListener::onIterationStart);
	}

	/**
	 * Called at the end of each animation iteration.
	 * <p>
	 * If the iteration counter is incremented by more than {@code 1} during a single update, this
	 * callback is still only invoked once.
	 */
	protected void onIterationEnd() {
		this.getListenersSnapshot().forEach(AnimationListener::onIterationEnd);
	}

	/**
	 * Called whenever the progress has changed while the animation is running, and when the
	 * animation is started.
	 * <p>
	 * This can for example be used to render the current animation state.
	 */
	protected void onUpdate() {
		this.getListenersSnapshot().forEach(AnimationListener::onUpdate);
	}
}
