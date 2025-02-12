package de.blablubbabc.sprites.animation;

public interface AnimationListener {

	/**
	 * Called when the animation is started.
	 */
	public default void onStart() {
	}

	/**
	 * Called when the animation is stopped.
	 */
	public default void onStop() {
	}

	/**
	 * Called at the beginning of each animation iteration, i.e. when the animation is started and
	 * whenever the iteration has ended and the animation is {@link Animation#isLooping() looping}.
	 */
	public default void onIterationStart() {
	}

	/**
	 * Called at the end of each animation iteration.
	 * <p>
	 * If the iteration counter is incremented by more than {@code 1} during a single update, this
	 * callback is still only invoked once.
	 */
	public default void onIterationEnd() {
	}

	/**
	 * Called whenever the progress has changed while the animation is running, and when the
	 * animation is started.
	 * <p>
	 * This can for example be used to render the current animation state.
	 */
	public default void onUpdate() {
	}
}
