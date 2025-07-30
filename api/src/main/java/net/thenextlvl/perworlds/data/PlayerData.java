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

    @Nullable
    ItemStack[] enderChest();

    @Nullable
    ItemStack[] inventory();

    @Unmodifiable
    List<PotionEffect> potionEffects();

    @Nullable
    GameMode gameMode();

    @Nullable
    GameMode previousGameMode();

    @Nullable
    Location lastDeathLocation();

    @Nullable
    Location lastLocation();

    @Nullable
    Location respawnLocation();

    /**
     * @since 0.2.6
     */
    @Nullable
    Key lastAdvancementTab();

    @Contract(mutates = "this")
    PlayerData absorption(double absorption);

    @Contract(mutates = "this")
    PlayerData advancements(Collection<AdvancementData> advancements);

    @Contract(mutates = "this")
    PlayerData arrowsInBody(int arrowsInBody);

    @Contract(mutates = "this")
    PlayerData attributes(Collection<AttributeData> attributes);

    @Contract(mutates = "this")
    PlayerData beeStingersInBody(int beeStingers);

    @Contract(mutates = "this")
    PlayerData discoveredRecipes(Collection<NamespacedKey> recipes);

    @Contract(mutates = "this")
    PlayerData enderChest(@Nullable ItemStack[] contents);

    @Contract(mutates = "this")
    PlayerData exhaustion(float exhaustion);

    @Contract(mutates = "this")
    PlayerData experience(float experience);

    @Contract(mutates = "this")
    PlayerData fallDistance(float fallDistance);

    @Contract(mutates = "this")
    PlayerData fireTicks(int fireTicks);

    @Contract(mutates = "this")
    PlayerData flySpeed(@Range(from = -1, to = 1) float speed);

    @Contract(mutates = "this")
    PlayerData flying(TriState flying);

    @Contract(mutates = "this")
    PlayerData foodLevel(int foodLevel);

    @Contract(mutates = "this")
    PlayerData freezeTicks(int freezeTicks);

    @Contract(mutates = "this")
    PlayerData gameMode(@Nullable GameMode gameMode);

    @Contract(mutates = "this")
    PlayerData gliding(boolean gliding);

    @Contract(mutates = "this")
    PlayerData health(double health);

    @Contract(mutates = "this")
    PlayerData heldItemSlot(int heldItemSlot);

    @Contract(mutates = "this")
    PlayerData inventory(@Nullable ItemStack[] contents);

    @Contract(mutates = "this")
    PlayerData invulnerable(boolean invulnerable);

    @Contract(mutates = "this")
    PlayerData lastDeathLocation(@Nullable Location location);

    @Contract(mutates = "this")
    PlayerData lastLocation(@Nullable Location location);

    /**
     * @since 0.2.6
     */
    @Contract(mutates = "this")
    PlayerData lastAdvancementTab(@Nullable Key key);

    @Contract(mutates = "this")
    PlayerData level(int level);

    @Contract(mutates = "this")
    PlayerData lockFreezeTicks(boolean lockFreezeTicks);

    @Contract(mutates = "this")
    PlayerData mayFly(TriState mayFly);

    @Contract(mutates = "this")
    PlayerData portalCooldown(int cooldown);

    @Contract(mutates = "this")
    PlayerData potionEffects(Collection<PotionEffect> effects);

    @Contract(mutates = "this")
    PlayerData previousGameMode(@Nullable GameMode gameMode);

    @Contract(mutates = "this")
    PlayerData remainingAir(int remainingAir);

    @Contract(mutates = "this")
    PlayerData respawnLocation(@Nullable Location location);

    @Contract(mutates = "this")
    PlayerData saturation(float saturation);

    @Contract(mutates = "this")
    PlayerData score(int score);

    @Contract(mutates = "this")
    PlayerData seenCredits(boolean seenCredits);

    @Contract(mutates = "this")
    PlayerData stats(Stats stats);

    @Contract(mutates = "this")
    PlayerData velocity(Vector velocity);

    /**
     * @since 0.2.0
     */
    @Contract(mutates = "this")
    PlayerData visualFire(TriState visualFire);

    @Contract(mutates = "this")
    PlayerData walkSpeed(@Range(from = -1, to = 1) float speed);

    @Contract(mutates = "this")
    PlayerData wardenSpawnTracker(WardenSpawnTracker tracker);

    @Unmodifiable
    Set<AdvancementData> advancements();

    @Unmodifiable
    Set<AttributeData> attributes();

    @Unmodifiable
    Set<NamespacedKey> discoveredRecipes();

    Stats stats();

    TriState flying();

    TriState mayFly();

    /**
     * @since 0.2.0
     */
    TriState visualFire();

    Vector velocity();

    WardenSpawnTracker wardenSpawnTracker();

    boolean gliding();

    boolean invulnerable();

    boolean lockFreezeTicks();

    boolean seenCredits();

    double absorption();

    double health();

    float exhaustion();

    float experience();

    float fallDistance();

    float flySpeed();

    float saturation();

    float walkSpeed();

    int arrowsInBody();

    int beeStingersInBody();

    int fireTicks();

    int foodLevel();

    int freezeTicks();

    int heldItemSlot();

    int level();

    int portalCooldown();

    int remainingAir();

    int score();

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
