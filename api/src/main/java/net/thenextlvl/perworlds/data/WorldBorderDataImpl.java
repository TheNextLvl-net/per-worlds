package net.thenextlvl.perworlds.data;

import com.google.common.base.Preconditions;
import io.papermc.paper.math.Position;
import net.kyori.adventure.util.Ticks;
import org.bukkit.WorldBorder;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;

@NullMarked
final class WorldBorderDataImpl implements WorldBorderData {
    private final double centerX;
    private final double centerZ;
    private final double size;
    private final double damageAmount;
    private final double damageBuffer;
    private final Duration transitionDuration;
    private final int warningDistance;
    private final Duration warningTime;

    /**
     * @see net.minecraft.world.level.border.WorldBorder#MAX_SIZE
     */
    @SuppressWarnings("JavadocReference")
    static final double MAX_SIZE = 5.999997E7F;
    static final double MIN_SIZE = 1F;
    /**
     * @see net.minecraft.world.level.border.WorldBorder#MAX_CENTER_COORDINATE
     */
    @SuppressWarnings("JavadocReference")
    static final double MAX_CENTER_COORDINATE = 5.999997E7F;

    public WorldBorderDataImpl() {
        this(0d, 0d, MAX_SIZE, 0.2d, 5.0d, Duration.ZERO, 5, Duration.ofSeconds(15));
    }

    public WorldBorderDataImpl(final WorldBorder border) {
        this(
                border.getCenter().getX(),
                border.getCenter().getZ(),
                border.getSize(),
                border.getDamageAmount(),
                border.getDamageBuffer(),
                Duration.ZERO,
                border.getWarningDistance(),
                Ticks.duration(border.getWarningTimeTicks())
        );
    }

    public WorldBorderDataImpl(
            final double centerX,
            final double centerZ,
            final double size,
            final double damageAmount,
            final double damageBuffer,
            final Duration transitionDuration,
            final int warningDistance,
            final Duration warningTime
    ) throws IllegalArgumentException {
        Preconditions.checkArgument(!transitionDuration.isNegative(), "time cannot be lower than 0ms but got %sms", transitionDuration.toMillis());
        Preconditions.checkArgument(!warningTime.isNegative(), "time cannot be lower than 0ms but got %sms", warningTime.toMillis());
        Preconditions.checkArgument(size >= MIN_SIZE && size <= MAX_SIZE, "size must be between %s and %s but got %s", MIN_SIZE, MAX_SIZE, size);
        Preconditions.checkArgument(Math.abs(centerX) <= MAX_CENTER_COORDINATE, "x coordinate cannot be outside +- %s but got %s", MAX_CENTER_COORDINATE, centerX);
        Preconditions.checkArgument(Math.abs(centerZ) <= MAX_CENTER_COORDINATE, "z coordinate cannot be outside +- %s but got %s", MAX_CENTER_COORDINATE, centerZ);
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.size = size;
        this.damageAmount = damageAmount;
        this.damageBuffer = damageBuffer;
        this.transitionDuration = transitionDuration;
        this.warningDistance = warningDistance;
        this.warningTime = warningTime;
    }

    @Override
    public double centerX() {
        return centerX;
    }

    @Override
    public WorldBorderData centerX(final double x) throws IllegalArgumentException {
        return center(x, centerZ);
    }

    @Override
    public double centerZ() {
        return centerZ;
    }

    @Override
    public WorldBorderData centerZ(final double z) throws IllegalArgumentException {
        return center(centerX, z);
    }

    @Override
    public Position center() {
        return Position.fine(centerX, 0, centerZ);
    }

    @Override
    public WorldBorderData center(final Position position) throws IllegalArgumentException {
        return center(position.x(), position.z());
    }

    @Override
    public WorldBorderData center(final double x, final double z) throws IllegalArgumentException {
        return new WorldBorderDataImpl(x, z, size, damageAmount, damageBuffer, transitionDuration, warningDistance, warningTime);
    }

    @Override
    public double size() {
        return size;
    }

    @Override
    public WorldBorderData size(final double size) throws IllegalArgumentException {
        return new WorldBorderDataImpl(centerX, centerZ, size, damageAmount, damageBuffer, transitionDuration, warningDistance, warningTime);
    }

    @Override
    public Duration getTransitionDuration() {
        return transitionDuration;
    }

    @Override
    public WorldBorderData setTransitionDuration(final Duration duration) throws IllegalArgumentException {
        return new WorldBorderDataImpl(centerX, centerZ, size, damageAmount, damageBuffer, duration, warningDistance, warningTime);
    }

    @Override
    public double damageAmount() {
        return damageAmount;
    }

    @Override
    public WorldBorderData damageAmount(final double damage) {
        return new WorldBorderDataImpl(centerX, centerZ, size, damage, damageBuffer, transitionDuration, warningDistance, warningTime);
    }

    @Override
    public double damageBuffer() {
        return damageBuffer;
    }

    @Override
    public WorldBorderData damageBuffer(final double blocks) {
        return new WorldBorderDataImpl(centerX, centerZ, size, damageAmount, blocks, transitionDuration, warningDistance, warningTime);
    }

    @Override
    public int warningDistance() {
        return warningDistance;
    }

    @Override
    public WorldBorderData warningDistance(final int blocks) {
        return new WorldBorderDataImpl(centerX, centerZ, size, damageAmount, damageBuffer, transitionDuration, blocks, warningTime);
    }

    @Override
    public Duration getWarningTime() {
        return warningTime;
    }

    @Override
    public WorldBorderData setWarningTime(final Duration duration) throws IllegalArgumentException {
        return new WorldBorderDataImpl(centerX, centerZ, size, damageAmount, damageBuffer, transitionDuration, warningDistance, duration);
    }
}
