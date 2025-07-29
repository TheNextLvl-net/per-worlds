package net.thenextlvl.perworlds.data;

import org.bukkit.advancement.Advancement;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.Set;

/**
 * The individual data of an advancement.
 */
@NullMarked
@ApiStatus.NonExtendable
public interface AdvancementData {
    /**
     * Retrieves the associated advancement.
     *
     * @return the {@link Advancement} associated with this data
     */
    Advancement getAdvancement();

    /**
     * Check if all criteria for this advancement have been met.
     *
     * @return {@code true} if this advancement is done, otherwise {@code false}
     */
    boolean isDone();

    /**
     * Mark the specified criteria as awarded at the current time.
     *
     * @param criteria the criteria to mark
     * @return {@code true} if awarded, {@code false} if criteria does not exist or already awarded.
     */
    boolean awardCriteria(String criteria);

    /**
     * Mark the specified criteria as uncompleted.
     *
     * @param criteria the criteria to mark
     * @return {@code true} if removed, {@code false} if criteria does not exist or not awarded
     */
    boolean revokeCriteria(String criteria);

    /**
     * Get the time the specified criteria was awarded.
     *
     * @param criteria the criteria to check
     * @return time awarded or {@code null} if unawarded or criteria does not exist
     */
    @Nullable
    Instant getTimeAwarded(String criteria);

    /**
     * Sets the date the specified criteria was awarded.
     *
     * @param criteria the criteria to set the date for
     * @param instant  the time to associate with the awarded criteria
     * @return {@code true} if the date was successfully set, {@code false} if the criteria does not exist or is already awarded
     */
    boolean setTimeAwarded(String criteria, Instant instant);

    /**
     * Get the criteria which have not been awarded.
     *
     * @return an unmodifiable set of the remaining criteria
     */
    @Unmodifiable
    Set<String> getRemainingCriteria();

    /**
     * Gets the criteria which have been awarded.
     *
     * @return an unmodifiable set of the awarded criteria
     */
    @Unmodifiable
    Set<String> getAwardedCriteria();

    /**
     * Determines whether the advancement data should be serialized.
     *
     * @return {@code true} if the advancement data should be serialized, otherwise {@code false}
     */
    boolean shouldSerialize();
}
