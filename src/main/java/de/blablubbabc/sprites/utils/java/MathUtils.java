package de.blablubbabc.sprites.utils.java;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Math and number helper functions.
 */
public final class MathUtils {

	/**
	 * The default maximum difference between two floating point numbers that are still considered
	 * equal in a fuzzy comparison.
	 */
	public static final double EPSILON = 0.00001D;

	/**
	 * Checks if the given values are roughly equal, using {@link #EPSILON} as the maximum
	 * tolerance.
	 * 
	 * @param a
	 *            the first value
	 * @param b
	 *            the second value
	 * @return <code>true</code> if the values are considered equal
	 * @see #fuzzyEquals(double, double, double)
	 */
	public static boolean fuzzyEquals(double a, double b) {
		return fuzzyEquals(a, b, EPSILON);
	}

	/**
	 * Checks if the given values are roughly equal within the given {@code tolerance}.
	 * <p>
	 * Notes on the comparison of special values:
	 * <ul>
	 * <li>All {@link Double#NaN} values are considered equal.
	 * <li>{@link Double#POSITIVE_INFINITY} and {@link Double#NEGATIVE_INFINITY} are each considered
	 * equal to themselves.
	 * <li>Positive and negative zero are considered equal.
	 * </ul>
	 * 
	 * @param a
	 *            the first value
	 * @param b
	 *            the second value
	 * @param tolerance
	 *            the maximum difference between both values to still be considered equal
	 * @return <code>true</code> if the values are considered equal
	 */
	public static boolean fuzzyEquals(double a, double b, double tolerance) {
		return Double.compare(a, b) == 0 // Accounts for NaN and infinities
				|| Math.abs(a - b) <= tolerance;
	}

	/**
	 * Gets a random integer value between the given minimum value (inclusive) and maximum value
	 * (exclusive).
	 * 
	 * @param min
	 *            the minimum value (inclusive)
	 * @param max
	 *            the maximum value (exclusive)
	 * @return a random value within the specified range
	 * @throws IllegalArgumentException
	 *             if {@code max} is greater than or equal to {@code min}
	 */
	public static int randomIntInRange(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}

	/**
	 * Gets a random float value between the given minimum value (inclusive) and maximum value
	 * (exclusive).
	 * 
	 * @param min
	 *            the minimum value (inclusive)
	 * @param max
	 *            the maximum value (exclusive)
	 * @return a random value within the specified range
	 * @throws IllegalArgumentException
	 *             if {@code max} is greater than or equal to {@code min}
	 */
	public static float randomFloatInRange(float min, float max) {
		Validate.isTrue(min < max, "max must be greater than min");
		float value = min + ThreadLocalRandom.current().nextFloat() * (max - min);
		if (value >= max) {// Correct for rounding
			value = Float.intBitsToFloat(Float.floatToIntBits(max) - 1);
		}
		return value;
	}

	/**
	 * Gets the sum of the given integers, clamped to {@link Integer#MAX_VALUE} and
	 * {@link Integer#MIN_VALUE} if an overflow would occur.
	 * 
	 * @param x
	 *            the first value
	 * @param y
	 *            the second value
	 * @return the result
	 */
	public static int addSaturated(int x, int y) {
		int result = x + y;
		if (((x ^ result) & (y ^ result)) < 0) {
			if (result < 0) {
				// Overflow occurred:
				return Integer.MAX_VALUE;
			} else {
				// Underflow occurred:
				return Integer.MIN_VALUE;
			}
		}
		return result;
	}

	/**
	 * Clamps the value to the specified range, returning the closest bound if the value is outside
	 * the range.
	 * 
	 * @param value
	 *            the value
	 * @param min
	 *            the lower bound (inclusive)
	 * @param max
	 *            the upper bound (inclusive)
	 * @return the value clamped to the specified range
	 */
	public static int clamp(int value, int min, int max) {
		if (value <= min) return min;
		if (value >= max) return max;
		return value;
	}

	/**
	 * Clamps the value to the specified range, returning the closest bound if the value is outside
	 * the range.
	 * 
	 * @param value
	 *            the value
	 * @param min
	 *            the lower bound (inclusive)
	 * @param max
	 *            the upper bound (inclusive)
	 * @return the value clamped to the specified range
	 */
	public static double clamp(double value, double min, double max) {
		if (value <= min) return min;
		if (value >= max) return max;
		return value;
	}

	/**
	 * Calculates the value in the middle of the specified range, rounded to the nearest integer
	 * towards zero.
	 * 
	 * @param start
	 *            the range start value, inclusive
	 * @param end
	 *            the range end value, exclusive
	 * @return the value in the middle of the specified range
	 */
	public static int middle(int start, int end) {
		return start + (end - 1 - start) / 2;
	}

	/**
	 * Calculates the average of the given values.
	 * <p>
	 * The average is calculated by forming the sum of all values and then dividing by the number of
	 * values. If the sum of the given values does not fit into a single <code>long</code>, it can
	 * overflow and produce an incorrect result.
	 * <p>
	 * If no values are given, {@code 0} is returned.
	 * 
	 * @param values
	 *            the values
	 * @return the average
	 */
	public static double average(long[] values) {
		long total = 0L;
		for (long value : values) {
			total += value;
		}
		return ((double) total / values.length);
	}

	/**
	 * Calculates the average of the given values, ignoring values that match the specified one.
	 * <p>
	 * The average is calculated by forming the sum of all values and then dividing by the number of
	 * values. If the sum of the given values does not fit into a single <code>long</code>, it can
	 * overflow and produce an incorrect result.
	 * <p>
	 * If no values are given, {@code 0} is returned.
	 * 
	 * @param values
	 *            the values
	 * @param ignore
	 *            the value to ignore
	 * @return the average
	 */
	public static double average(long[] values, long ignore) {
		long total = 0L;
		int ignored = 0;
		for (long value : values) {
			if (value == ignore) {
				ignored += 1;
				continue;
			}
			total += value;
		}
		int elementCount = values.length - ignored;
		if (elementCount == 0) {
			return 0L;
		} else {
			return ((double) total / elementCount);
		}
	}

	/**
	 * Calculates the maximum of the given values.
	 * <p>
	 * If no values are given, {@code 0} is returned.
	 * 
	 * @param values
	 *            the values
	 * @return the max value
	 */
	public static long max(long[] values) {
		if (values.length == 0) return 0L;
		long max = Long.MIN_VALUE;
		for (long value : values) {
			if (value > max) {
				max = value;
			}
		}
		return max;
	}

	/**
	 * Calculates the maximum of the given values, ignoring values that match the specified one.
	 * <p>
	 * If no values are given, {@code 0} is returned.
	 * 
	 * @param values
	 *            the values
	 * @param ignore
	 *            the value to ignore
	 * @return the max value
	 */
	public static long max(long[] values, long ignore) {
		long max = Long.MIN_VALUE;
		int ignored = 0;
		for (long value : values) {
			if (value == ignore) {
				ignored += 1;
				continue;
			}
			if (value > max) {
				max = value;
			}
		}
		if (values.length - ignored == 0) return 0L;
		return max;
	}

	/**
	 * Brings the given value into the specified range via a modulo (cyclic) operation.
	 * 
	 * @param value
	 *            the value
	 * @param min
	 *            the lower bound (inclusive)
	 * @param max
	 *            the upper bound (inclusive)
	 * @return the value within the specified range
	 */
	public static int rangeModulo(int value, int min, int max) {
		Validate.isTrue(min <= max, "min > max");
		// Note: The value can be outside this range.
		int offset = min;
		int range = max - min + 1;
		int modulo = (value - offset) % range;
		if (modulo < 0) modulo += range;
		return offset + modulo;
	}

	/**
	 * Linearly interpolates between the given start and end values using the given factor.
	 * 
	 * @param start
	 *            the start value, inclusive
	 * @param end
	 *            the end value, inclusive
	 * @param t
	 *            the interpolation factor, clamped to the range of <code>0.0</code> and
	 *            <code>1.0</code>
	 * @return the interpolated value
	 */
	public static double lerp(double start, double end, double t) {
		t = clamp(t, 0.0, 1.0);
		return start + t * (end - start);
	}

	/**
	 * Calculates an index between {@code 0} and {@code end} (exclusive) by linear interpolation.
	 * 
	 * @param end
	 *            the end value, exclusive
	 * @param t
	 *            the interpolation factor, clamped to the range of <code>0.0</code> and
	 *            <code>1.0</code>
	 * @return the interpolated value
	 */
	public static int lerpIndex(int end, double t) {
		int index = (int) lerp(0, end, t);
		return index == end ? index - 1 : index;
	}

	private MathUtils() {
	}
}
