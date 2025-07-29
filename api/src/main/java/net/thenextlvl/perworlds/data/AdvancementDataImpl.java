package net.thenextlvl.perworlds.data;

import org.bukkit.advancement.Advancement;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@NullMarked
class AdvancementDataImpl implements AdvancementData {
    private final Advancement advancement;
    private final Set<String> remainingCriteria = new HashSet<>();
    private final Map<String, Instant> awardedCriteria = new HashMap<>();
    
    AdvancementDataImpl(Advancement advancement, Map<String, Instant> awardedCriteria, Collection<String> remainingCriteria) {
        this.advancement = advancement;
        this.awardedCriteria.putAll(awardedCriteria);
        this.remainingCriteria.addAll(remainingCriteria);
    }
    
    @Override
    public Advancement getAdvancement() {
        return advancement;
    }

    @Override
    public boolean isDone() {
        return remainingCriteria.isEmpty();
    }

    @Override
    public boolean hasProgress() {
        return !awardedCriteria.isEmpty();
    }

    @Override
    public boolean awardCriteria(String criteria) {
        return remainingCriteria.remove(criteria) && awardedCriteria.putIfAbsent(criteria, Instant.now()) == null;
    }

    @Override
    public boolean revokeCriteria(String criteria) {
        return remainingCriteria.add(criteria) && awardedCriteria.remove(criteria) != null;
    }

    @Override
    public @Nullable Instant getTimeAwarded(String criteria) {
        return awardedCriteria.get(criteria);
    }

    @Override
    public boolean setTimeAwarded(String criteria, Instant instant) {
        return awardedCriteria.containsKey(criteria) && awardedCriteria.put(criteria, instant) != null;
    }

    @Override
    public @Unmodifiable Set<String> getRemainingCriteria() {
        return Set.copyOf(remainingCriteria);
    }

    @Override
    public @Unmodifiable Set<String> getAwardedCriteria() {
        return Set.copyOf(awardedCriteria.keySet());
    }

    @Override
    public @Unmodifiable Map<String, Instant> awardedCriteria() {
        return Map.copyOf(awardedCriteria);
    }
}
