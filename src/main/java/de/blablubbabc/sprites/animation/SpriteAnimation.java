package de.blablubbabc.sprites.animation;

import de.blablubbabc.sprites.sprites.Sprite;
import de.blablubbabc.sprites.utils.java.MathUtils;
import de.blablubbabc.sprites.utils.java.Validate;

public class SpriteAnimation extends Animation {

	private final SpriteAnimationClip animationClip;

	public SpriteAnimation(AnimationSystem animationSystem, SpriteAnimationClip animationClip) {
		super(
				animationSystem,
				Validate.notNull(animationClip, "animationClip").getName(),
				animationClip.getLengthSeconds()
		);
		this.animationClip = animationClip;
	}

	/**
	 * @return the sprite for the current animation progress
	 */
	public Sprite getSprite() {
		var sprites = animationClip.getSprites();
		var spriteIndex = MathUtils.lerpIndex(sprites.size(), this.getProgress());
		var sprite = sprites.get(spriteIndex);
		return sprite;
	}
}
