package de.blablubbabc.sprites.utils.bukkit;

import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class TransformationUtils {

	public static final Transformation IDENTITY = new Transformation(
			new Vector3f(),
			new Quaternionf(),
			new Vector3f(1.0F, 1.0F, 1.0F),
			new Quaternionf()
	);

	private TransformationUtils() {
	}
}
