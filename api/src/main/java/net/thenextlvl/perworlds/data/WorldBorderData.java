package net.thenextlvl.perworlds.data;

import io.papermc.paper.math.Position;
import org.bukkit.WorldBorder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * This interface represents a world border.
 *
 * @since 0.1.0
 */
@NullMarked
@ApiStatus.NonExtendable
public interface WorldBorderData {
    /**
     * A default instance of {@link WorldBorderData} that serves as the standard,
     * preset configuration for a world border.
     *
     * @since 1.0.0
     */
    WorldBorderData DEFAULT = new WorldBorderDataImpl();

    /**
     * Retrieves the center X-coordinate of the world's border.
     *
     * @return the X-coordinate of the center
     * @see WorldBorder#getCenter()
     */
    @Contract(pure = true)
    double centerX();

    /**
     * Sets the new border center x-coordinate.
     *
     * @param x The new X-coordinate of the border center.
     * @return the current WorldBorderData instance for chaining
     * @throws IllegalArgumentException if the absolute value of {@code x}
     *                                  is higher than {@link #getMaxCenterCoordinate()}
     * @since 0.2.2
     */
    @Contract(value = "_ -> new", pure = true)
    WorldBorderData centerX(double x) throws IllegalArgumentException;

    /**
     * Retrieves the center Z-coordinate of the world's border.
     *
     * @return the Z-coordinate of the center
     * @see WorldBorder#getCenter()
     */
    @Contract(pure = true)
    double centerZ();

    /**
     * Sets the new border center z-coordinate.
     *
     * @param z The new Z-coordinate of the border center.
     * @return the current WorldBorderData instance for chaining
     * @throws IllegalArgumentException if the absolute value of {@code z}
     *                                  is higher than {@link #getMaxCenterCoordinate()}
     * @since 0.2.2
     */
    @Contract(value = "_ -> new", pure = true)
    WorldBorderData centerZ(double z) throws IllegalArgumentException;

    /**
     * Retrieves the current center position of the world's border.
     *
     * @return the center of the border as a {@link Position}
     */
    @Contract(pure = true)
    Position center();

    /**
     * Sets the new border center.
     *
     * @param position The new position of the border center.
     * @return the current WorldBorderData instance for chaining
     * @throws IllegalArgumentException if the absolute value of {@link Position#x()} or {@link Position#z()}
     *                                  is higher than {@link #getMaxCenterCoordinate()}
     * @since 0.2.2
     */
    @Contract(value = "_ -> new", pure = true)
    WorldBorderData center(Position position) throws IllegalArgumentException;

    /**
     * Sets the new border center.
     *
     * @param x The new X-coordinate of the border center.
     * @param z The new Z-coordinate of the border center.
     * @return the current WorldBorderData instance for chaining
     * @throws IllegalArgumentException if the absolute value of {@code x} or {@code z}
     *                                  is higher than {@link #getMaxCenterCoordinate()}
     * @since 0.2.2
     */
    @Contract(value = "_, _ -> new", pure = true)
    WorldBorderData center(double x, double z) throws IllegalArgumentException;

    /**
     * Gets the current side length of the border.
     *
     * @return The current side length of the border.
     * @see WorldBorder#getSize()
     */
    @Contract(pure = true)
    double size();

    /**
     * Sets the border to a square region with the specified side length in blocks.
     *
     * @param size The new size of the border.
     * @return the current WorldBorderData instance for chaining
     * @throws IllegalArgumentException if {@code size} is less than {@link #getMinSize()} or greater than {@link #getMaxSize()}
     * @see WorldBorder#setSize(double)
     * @since 0.2.2
     */
    @Contract(value = "_ -> new", pure = true)
    WorldBorderData size(double size) throws IllegalArgumentException;

    /**
     * Retrieves the current transition duration of the world's border.
     *
     * @return the duration in milliseconds
     * @see WorldBorder#setSize(double, TimeUnit, long)
     * @deprecated use {@link #getTransitionDuration()}
     */
    @Contract(pure = true)
    @Deprecated(forRemoval = true, since = "1.3.0")
    default long duration() {
        return getTransitionDuration().toMillis();
    }

    /**
     * Retrieves the current transition duration of the world's border.
     *
     * @return the duration
     * @see WorldBorder#changeSize(double, long)
     * @since 1.3.0
     */
    @Contract(pure = true)
    Duration getTransitionDuration();

    /**
     * Sets the transition duration in milliseconds.
     *
     * @param duration the duration to be set, in milliseconds
     * @return the current WorldBorderData instance for chaining
     * @throws IllegalArgumentException if the duration is less than 0
     * @see WorldBorder#setSize(double, TimeUnit, long)
     * @since 0.2.2
     * @deprecated use {@link #setTransitionDuration(Duration)}
     */
    @Contract(value = "_ -> new", pure = true)
    @Deprecated(forRemoval = true, since = "1.3.0")
    default WorldBorderData duration(final long duration) throws IllegalArgumentException {
        return setTransitionDuration(Duration.ofMillis(duration));
    }

    /**
     * Sets the transition duration.
     *
     * @param duration the duration to be set
     * @return the current WorldBorderData instance for chaining
     * @throws IllegalArgumentException if the duration is negative
     * @see WorldBorder#changeSize(double, long)
     * @since 1.3.0
     */
    @Contract(value = "_ -> new", pure = true)
    WorldBorderData setTransitionDuration(Duration duration) throws IllegalArgumentException;

    /**
     * Gets the current border damage amount.
     *
     * @return The current border damage amount.
     * @see WorldBorder#getDamageAmount()
     */
    @Contract(pure = true)
    double damageAmount();

    /**
     * Sets the amount of damage a player takes when outside the border plus the border buffer.
     *
     * @param damage The amount of damage.
     * @return the current WorldBorderData instance for chaining
     * @see WorldBorder#setDamageAmount(double)
     * @since 0.2.2
     */
    @Contract(value = "_ -> new", pure = true)
    WorldBorderData damageAmount(double damage);

    /**
     * Gets the current border damage buffer.
     *
     * @return The current border damage buffer.
     * @see WorldBorder#getDamageBuffer()
     * @since 0.2.2
     */
    @Contract(pure = true)
    double damageBuffer();

    /**
     * Sets the number of blocks a player may safely be outside the border before taking damage.
     *
     * @param blocks The number of blocks.
     * @return the current WorldBorderData instance for chaining
     * @see WorldBorder#setDamageBuffer(double)
     */
    @Contract(value = "_ -> new", pure = true)
    WorldBorderData damageBuffer(double blocks);

    /**
     * Gets the current border warning distance.
     *
     * @return The current border warning distance.
     * @see WorldBorder#getWarningDistance()
     */
    @Contract(pure = true)
    int warningDistance();

    /**
     * Sets the warning distance that causes the screen to be tinted red when the player is within the specified number of blocks from the border.
     *
     * @param blocks The distance in blocks.
     * @return the current WorldBorderData instance for chaining
     * @see WorldBorder#setWarningDistance(int)
     * @since 0.2.2
     */
    @Contract(value = "_ -> new", pure = true)
    WorldBorderData warningDistance(int blocks);

    /**
     * Gets the current border warning time in seconds.
     *
     * @return The current border warning time in seconds.
     * @see WorldBorder#getWarningTimeTicks()
     */
    @Contract(pure = true)
    @Deprecated(forRemoval = true, since = "1.3.0")
    default int warningTime() {
        return (int) getWarningTime().toSeconds();
    }

    /**
     * Gets the current border warning time.
     *
     * @return The current border warning time.
     * @see WorldBorder#getWarningTimeTicks()
     */
    @Contract(pure = true)
    Duration getWarningTime();

    /**
     * Sets the warning time that causes the screen to be tinted red when a contracting border will reach the player within the specified time.
     *
     * @param seconds The amount of time in seconds.
     * @return the current WorldBorderData instance for chaining
     * @throws IllegalArgumentException if the seconds are negative
     * @see WorldBorder#setWarningTime(int)
     * @since 0.2.2
     */
    @Contract(value = "_ -> new", pure = true)
    default WorldBorderData warningTime(final int seconds) throws IllegalArgumentException {
        return setWarningTime(Duration.ofSeconds(seconds));
    }

    /**
     * Sets the warning time that causes the screen to be tinted red when a contracting border will reach the player within the specified time.
     *
     * @param duration The amount of time.
     * @return the current WorldBorderData instance for chaining
     * @throws IllegalArgumentException if the duration is negative
     * @see WorldBorder#setWarningTimeTicks(int)
     * @since 1.3.0
     */
    @Contract(value = "_ -> new", pure = true)
    WorldBorderData setWarningTime(Duration duration) throws IllegalArgumentException;

    /**
     * Retrieves the maximum allowed size of the border.
     *
     * @return the maximum size of the border
     * @see WorldBorder#getMaxSize()
     * @since 1.0.0
     */
    @Contract(pure = true)
    static double getMaxSize() {
        return WorldBorderDataImpl.MAX_SIZE;
    }

    /**
     * Retrieves the minimum allowed size of the border.
     *
     * @return the minimum size of the border
     * @since 1.0.0
     */
    @Contract(pure = true)
    static double getMinSize() {
        return WorldBorderDataImpl.MIN_SIZE;
    }

    /**
     * Retrieves the maximum allowed absolute value for the center coordinates of the border.
     *
     * @return the maximum center coordinate value that can be set for the border
     * @see WorldBorder#getMaxCenterCoordinate()
     * @since 1.0.0
     */
    @Contract(pure = true)
    static double getMaxCenterCoordinate() {
        return WorldBorderDataImpl.MAX_CENTER_COORDINATE;
    }

    /**
     * Creates a new instance of {@code WorldBorderData} with the specified parameters.
     *
     * @param centerX         the X-coordinate of the border's center
     * @param centerZ         the Z-coordinate of the border's center
     * @param size            the size of the border
     * @param damageAmount    the amount of damage applied outside the border beyond the buffer
     * @param damageBuffer    the distance from the border within which no damage is applied
     * @param duration        the transition duration of the border in milliseconds
     * @param warningDistance the number of blocks from the border where a warning is displayed
     * @param warningTime     the amount of time in seconds for the warning display
     * @return a new instance of {@code WorldBorderData} with the specified configuration
     * @since 1.0.0
     * @deprecated use {@link #create(double, double, double, double, double, Duration, int, Duration)}
     */
    @Deprecated(forRemoval = true, since = "1.3.0")
    @Contract(value = "_, _, _, _, _, _, _, _ -> new", pure = true)
    static WorldBorderData create(final double centerX, final double centerZ, final double size, final double damageAmount, final double damageBuffer, final long duration, final int warningDistance, final int warningTime) {
        return create(centerX, centerZ, size, damageAmount, damageBuffer, Duration.ofMillis(duration), warningDistance, Duration.ofSeconds(warningTime));
    }

    /**
     * Creates a new instance of {@code WorldBorderData} with the specified parameters.
     *
     * @param centerX            the X-coordinate of the border's center
     * @param centerZ            the Z-coordinate of the border's center
     * @param size               the size of the border
     * @param damageAmount       the amount of damage applied outside the border beyond the buffer
     * @param damageBuffer       the distance from the border within which no damage is applied
     * @param transitionDuration the transition duration of the border
     * @param warningDistance    the number of blocks from the border where a warning is displayed
     * @param warningTime        the amount of time for the warning display
     * @return a new instance of {@code WorldBorderData} with the specified configuration
     * @since 1.3.0
     */
    @Contract(value = "_, _, _, _, _, _, _, _ -> new", pure = true)
    static WorldBorderData create(final double centerX, final double centerZ, final double size, final double damageAmount, final double damageBuffer, final Duration transitionDuration, final int warningDistance, final Duration warningTime) {
        return new WorldBorderDataImpl(centerX, centerZ, size, damageAmount, damageBuffer, transitionDuration, warningDistance, warningTime);
    }

    /**
     * Creates a new {@code WorldBorderData} instance based on the provided {@code WorldBorder}.
     *
     * @param border The {@code WorldBorder} instance to derive the {@code WorldBorderData} from.
     * @return A new {@code WorldBorderData} instance containing the data from the given {@code WorldBorder}.
     * @since 1.0.0
     */
    static WorldBorderData of(final WorldBorder border) {
        return new WorldBorderDataImpl(border);
    }
}
