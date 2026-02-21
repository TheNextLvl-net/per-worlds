package net.thenextlvl.perworlds.data;

import com.google.common.base.Preconditions;
import io.papermc.paper.math.Position;
import org.bukkit.WorldBorder;
import org.jspecify.annotations.NullMarked;

@NullMarked
record WorldBorderDataImpl(
        double centerX,
        double centerZ,
        double size,
        double damageAmount,
        double damageBuffer,
        long duration,
        int warningDistance,
        int warningTime
) implements WorldBorderData {
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

    WorldBorderDataImpl() {
        this(0d, 0d, MAX_SIZE, 0.2d, 5.0d, 0, 5, 15);
    }

    WorldBorderDataImpl(final WorldBorder border) {
        this(
                border.getCenter().getX(),
                border.getCenter().getZ(),
                border.getSize(),
                border.getDamageAmount(),
                border.getDamageBuffer(),
                0,
                border.getWarningDistance(),
                border.getWarningTime()
        );
    }

    @Override
    public WorldBorderData centerX(final double x) throws IllegalArgumentException {
        return center(x, centerZ);
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
        Preconditions.checkArgument(Math.abs(x) <= MAX_CENTER_COORDINATE, "x coordinate cannot be outside +- %s but got %s", MAX_CENTER_COORDINATE, x);
        Preconditions.checkArgument(Math.abs(z) <= MAX_CENTER_COORDINATE, "z coordinate cannot be outside +- %s but got %s", MAX_CENTER_COORDINATE, z);
        return new WorldBorderDataImpl(x, z, size, damageAmount, damageBuffer, duration, warningDistance, warningTime);
    }

    @Override
    public WorldBorderData size(final double size) throws IllegalArgumentException {
        Preconditions.checkArgument(size >= MIN_SIZE && size <= MAX_SIZE, "size must be between %s and %s but got %s", MIN_SIZE, MAX_SIZE, size);
        return new WorldBorderDataImpl(centerX, centerZ, size, damageAmount, damageBuffer, duration, warningDistance, warningTime);
    }

    @Override
    public WorldBorderData duration(long duration) throws IllegalArgumentException {
        Preconditions.checkArgument(duration >= 0, "time cannot be lower than 0 but got %s", duration);
        return new WorldBorderDataImpl(centerX, centerZ, size, damageAmount, damageBuffer, duration, warningDistance, warningTime);
    }

    @Override
    public WorldBorderData damageAmount(final double damage) {
        return new WorldBorderDataImpl(centerX, centerZ, size, damage, damageBuffer, duration, warningDistance, warningTime);
    }

    @Override
    public WorldBorderData damageBuffer(final double blocks) {
        return new WorldBorderDataImpl(centerX, centerZ, size, damageAmount, blocks, duration, warningDistance, warningTime);
    }

    @Override
    public WorldBorderData warningDistance(final int blocks) {
        return new WorldBorderDataImpl(centerX, centerZ, size, damageAmount, damageBuffer, duration, blocks, warningTime);
    }

    @Override
    public WorldBorderData warningTime(int seconds) {
        return new WorldBorderDataImpl(centerX, centerZ, size, damageAmount, damageBuffer, duration, warningDistance, seconds);
    }
}
