package de.blablubbabc.sprites.entity;

import org.checkerframework.checker.nullness.qual.Nullable;

import de.blablubbabc.sprites.animation.Animation;
import de.blablubbabc.sprites.animation.AnimationListener;
import de.blablubbabc.sprites.animation.SpriteAnimation;
import de.blablubbabc.sprites.utils.java.Validate;

/**
 * Coordinates of the active animation of a {@link SpriteEntity}.
 */
public class Animator {

	private final SpriteEntity spriteEntity;
	private final AnimationListener animationListener = new AnimationListener() {
		@Override
		public void onUpdate() {
			if (animation instanceof SpriteAnimation spriteAnimation) {
				spriteEntity.getRenderer().setSprite(spriteAnimation.getSprite());
			}
		}

		@Override
		public void onStop() {
			setAnimation(null);
		}
	};

	private @Nullable Animation animation = null;

	public Animator(SpriteEntity spriteEntity) {
		Validate.notNull(spriteEntity, "spriteEntity");
		this.spriteEntity = spriteEntity;
	}

	public @Nullable Animation getAnimation() {
		return animation;
	}

	public void setAnimation(@Nullable Animation animation) {
		if (animation == this.animation) return;

		if (this.animation != null) {
			this.animation.stop();
			this.animation.removeListener(animationListener);
		}

		this.animation = animation;

		if (animation != null) {
			animation.addListener(animationListener);
			animation.reset();
			animation.start();

			var location = spriteEntity.getLocation();
			if (location != null) {
				spriteEntity.getRenderer().spawn(location);
			}
		} else {
			spriteEntity.getRenderer().despawn();
		}
	}
}
