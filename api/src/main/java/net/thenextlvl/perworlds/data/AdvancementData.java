package net.thenextlvl.perworlds.data;

import org.bukkit.advancement.Advancement;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * The individual data of an advancement.
 *
 * @since 0.1.0
 */
@NullMarked
@ApiStatus.NonExtendable
public interface AdvancementData {
    /**
     * Retrieves the associated advancement.
     *
     * @return the {@link Advancement} associated with this data
     */
    @Contract(pure = true)
    Advancement getAdvancement();

    /**
     * Check if all criteria for this advancement have been met.
     *
     * @return {@code true} if this advancement is done, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean isDone();

    /**
     * Checks if there is progress for this advancement.
     *
     * @return {@code true} if there is progress in this advancement, otherwise {@code false}
     * @since 1.0.0
     */
    @Contract(pure = true)
    boolean hasProgress();

    /**
     * Mark the specified criteria as awarded at the current time.
     *
     * @param criteria the criteria to mark
     * @return {@code true} if awarded, {@code false} if criteria does not exist or already awarded.
     */
    @Contract(mutates = "this")
    boolean awardCriteria(String criteria);

    /**
     * Mark the specified criteria as uncompleted.
     *
     * @param criteria the criteria to mark
     * @return {@code true} if removed, {@code false} if criteria does not exist or not awarded
     */
    @Contract(mutates = "this")
    boolean revokeCriteria(String criteria);

    /**
     * Get the time the specified criteria was awarded.
     *
     * @param criteria the criteria to check
     * @return time awarded or {@code null} if unawarded or criteria does not exist
     * @since 0.2.6
     */
    @Nullable
    @Contract(pure = true)
    Instant getTimeAwarded(String criteria);

    /**
     * Sets the date the specified criteria was awarded.
     *
     * @param criteria the criteria to set the date for
     * @param instant  the time to associate with the awarded criteria
     * @return {@code true} if the date was successfully set, {@code false} if the criteria does not exist or is already awarded
     * @since 0.2.6
     */
    @Contract(mutates = "this")
    boolean setTimeAwarded(String criteria, Instant instant);

    /**
     * Get the criteria which have not been awarded.
     *
     * @return an unmodifiable set of the remaining criteria
     */
    @Unmodifiable
    @Contract(pure = true)
    Set<String> getRemainingCriteria();

    /**
     * Gets the criteria which have been awarded.
     *
     * @return an unmodifiable set of the awarded criteria
     */
    @Unmodifiable
    @Contract(pure = true)
    Set<String> getAwardedCriteria();

    /**
     * Performs an operation for each awarded criterion along with its associated time of award.
     *
     * @param action a {@link BiConsumer} that accepts the criterion's name and the time it was awarded
     */
    void forEachAwardedCriteria(BiConsumer<String, Instant> action);

    /**
     * Creates an {@link AdvancementData} instance based on the provided {@link Advancement}.
     *
     * @param advancement the advancement to associate with the data
     * @return a new instance of {@code AdvancementData} using the specified advancement
     * @since 1.0.0
     */
    @Contract(value = "_ -> new", pure = true)
    static AdvancementData of(final Advancement advancement) {
        return new AdvancementDataImpl(advancement, Map.of(), advancement.getCriteria());
    }

    /**
     * Creates an {@link AdvancementData} instance with the specified advancement,
     * awarded criteria, and remaining criteria.
     *
     * @param advancement       the advancement associated with the data
     * @param awardedCriteria   a map of criteria to the time they were awarded
     * @param remainingCriteria a collection of criteria that are yet to be completed
     * @return an instance of {@code AdvancementData} representing the specified parameters
     * @since 1.0.0
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    static AdvancementData of(final Advancement advancement, final Map<String, Instant> awardedCriteria, final Collection<String> remainingCriteria) {
        return new AdvancementDataImpl(advancement, awardedCriteria, remainingCriteria);
    }
}
