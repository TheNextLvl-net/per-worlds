package net.thenextlvl.perworlds.data;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.util.TriState;
import net.thenextlvl.perworlds.GroupSettings;
import net.thenextlvl.perworlds.WorldGroup;
import net.thenextlvl.perworlds.statistics.Stats;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @since 0.1.0
 */
@NullMarked
@ApiStatus.NonExtendable
public interface PlayerData {
    /**
     * @since 0.2.4
     */
    WorldGroup group();

    /**
     * Retrieves the uuid of the player to whom this data belongs.
     *
     * @return the uuid of the owning player
     * @since 1.0.0
     */
    UUID uuid();

    @Nullable
    @Contract(pure = true)
    ItemStack[] enderChest();

    @Contract(mutates = "this")
    PlayerData enderChest(@Nullable ItemStack[] contents);

    @Nullable
    @Contract(pure = true)
    ItemStack[] inventory();

    @Contract(mutates = "this")
    PlayerData inventory(@Nullable ItemStack[] contents);

    @Unmodifiable
    @Contract(pure = true)
    List<PotionEffect> potionEffects();

    @Contract(mutates = "this")
    PlayerData potionEffects(Collection<PotionEffect> effects);

    @Nullable
    @Contract(pure = true)
    GameMode gameMode();

    @Contract(mutates = "this")
    PlayerData gameMode(@Nullable GameMode gameMode);

    @Nullable
    @Contract(pure = true)
    GameMode previousGameMode();

    @Contract(mutates = "this")
    PlayerData previousGameMode(@Nullable GameMode gameMode);

    @Nullable
    @Contract(pure = true)
    Location lastDeathLocation();

    @Contract(mutates = "this")
    PlayerData lastDeathLocation(@Nullable Location location);

    @Nullable
    @Contract(pure = true)
    Location lastLocation();

    @Contract(mutates = "this")
    PlayerData lastLocation(@Nullable Location location);

    @Nullable
    @Contract(pure = true)
    Location respawnLocation();

    @Contract(mutates = "this")
    PlayerData respawnLocation(@Nullable Location location);

    /**
     * @since 0.2.6
     */
    @Nullable
    @Contract(pure = true)
    Key lastAdvancementTab();

    /**
     * @since 0.2.6
     */
    @Contract(mutates = "this")
    PlayerData lastAdvancementTab(@Nullable Key key);

    @Contract(pure = true)
    double absorption();

    @Contract(mutates = "this")
    PlayerData absorption(double absorption);

    @Unmodifiable
    @Contract(pure = true)
    Set<AdvancementData> advancements();

    @Contract(mutates = "this")
    PlayerData advancements(Collection<AdvancementData> advancements);

    @Contract(pure = true)
    int arrowsInBody();

    @Contract(mutates = "this")
    PlayerData arrowsInBody(int arrowsInBody);

    @Unmodifiable
    @Contract(pure = true)
    Set<AttributeData> attributes();

    @Contract(mutates = "this")
    PlayerData attributes(Collection<AttributeData> attributes);

    @Contract(pure = true)
    int beeStingersInBody();

    @Contract(mutates = "this")
    PlayerData beeStingersInBody(int beeStingers);

    @Unmodifiable
    @Contract(pure = true)
    Set<NamespacedKey> discoveredRecipes();

    @Contract(mutates = "this")
    PlayerData discoveredRecipes(Collection<NamespacedKey> recipes);

    @Contract(pure = true)
    float exhaustion();

    @Contract(mutates = "this")
    PlayerData exhaustion(float exhaustion);

    @Contract(pure = true)
    float experience();

    @Contract(mutates = "this")
    PlayerData experience(float experience);

    @Contract(pure = true)
    float fallDistance();

    @Contract(mutates = "this")
    PlayerData fallDistance(float fallDistance);

    @Contract(pure = true)
    int fireTicks();

    @Contract(mutates = "this")
    PlayerData fireTicks(int fireTicks);

    @Contract(pure = true)
    @Range(from = -1, to = 1)
    float flySpeed();

    @Contract(mutates = "this")
    PlayerData flySpeed(@Range(from = -1, to = 1) float speed);

    @Contract(pure = true)
    TriState flying();

    @Contract(mutates = "this")
    PlayerData flying(TriState flying);

    @Contract(pure = true)
    int foodLevel();

    @Contract(mutates = "this")
    PlayerData foodLevel(int foodLevel);

    @Contract(pure = true)
    int freezeTicks();

    @Contract(mutates = "this")
    PlayerData freezeTicks(int freezeTicks);

    @Contract(pure = true)
    boolean gliding();

    @Contract(mutates = "this")
    PlayerData gliding(boolean gliding);

    @Contract(pure = true)
    double health();

    @Contract(mutates = "this")
    PlayerData health(double health);

    @Contract(pure = true)
    int heldItemSlot();

    @Contract(mutates = "this")
    PlayerData heldItemSlot(int heldItemSlot);

    @Contract(pure = true)
    boolean invulnerable();

    @Contract(mutates = "this")
    PlayerData invulnerable(boolean invulnerable);

    @Contract(pure = true)
    int level();

    @Contract(mutates = "this")
    PlayerData level(int level);

    @Contract(pure = true)
    boolean lockFreezeTicks();

    @Contract(mutates = "this")
    PlayerData lockFreezeTicks(boolean lockFreezeTicks);

    @Contract(pure = true)
    TriState mayFly();

    @Contract(mutates = "this")
    PlayerData mayFly(TriState mayFly);

    @Contract(pure = true)
    int portalCooldown();

    @Contract(mutates = "this")
    PlayerData portalCooldown(int cooldown);

    @Contract(pure = true)
    int remainingAir();

    @Contract(mutates = "this")
    PlayerData remainingAir(int remainingAir);

    @Contract(pure = true)
    float saturation();

    @Contract(mutates = "this")
    PlayerData saturation(float saturation);

    @Contract(pure = true)
    int score();

    @Contract(mutates = "this")
    PlayerData score(int score);

    @Contract(pure = true)
    boolean seenCredits();

    @Contract(mutates = "this")
    PlayerData seenCredits(boolean seenCredits);

    @ApiStatus.Experimental
    Stats stats();

    @ApiStatus.Experimental
    @Contract(mutates = "this")
    PlayerData stats(Stats stats);

    @Contract(value = " -> new", pure = true)
    Vector velocity();

    @Contract(mutates = "this")
    PlayerData velocity(Vector velocity);

    /**
     * @since 0.2.0
     */
    @Contract(pure = true)
    TriState visualFire();

    /**
     * @since 0.2.0
     */
    @Contract(mutates = "this")
    PlayerData visualFire(TriState visualFire);

    @Contract(pure = true)
    float walkSpeed();

    @Contract(mutates = "this")
    PlayerData walkSpeed(@Range(from = -1, to = 1) float speed);

    @Contract(pure = true)
    WardenSpawnTracker wardenSpawnTracker();

    @Contract(mutates = "this")
    PlayerData wardenSpawnTracker(WardenSpawnTracker tracker);

    /**
     * Loads the player's data based on the specified group and optionally modifies the player's
     * position and related attributes according to the group's configuration.
     * <p>
     * The result will always complete with {@code false} if the group is not {@link GroupSettings#enabled() enabled}
     *
     * @param player   the player whose data is being loaded
     * @param position defines whether position-related settings should be applied
     * @since 0.2.4
     */
    @Contract(mutates = "param1")
    CompletableFuture<Boolean> load(Player player, boolean position);
}
