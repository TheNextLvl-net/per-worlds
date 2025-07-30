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

    PlayerData absorption(double absorption);

    PlayerData advancements(Collection<AdvancementData> advancements);

    PlayerData arrowsInBody(int arrowsInBody);

    PlayerData attributes(Collection<AttributeData> attributes);

    PlayerData beeStingersInBody(int beeStingers);

    PlayerData discoveredRecipes(Collection<NamespacedKey> recipes);

    PlayerData enderChest(@Nullable ItemStack[] contents);

    PlayerData exhaustion(float exhaustion);

    PlayerData experience(float experience);

    PlayerData fallDistance(float fallDistance);

    PlayerData fireTicks(int fireTicks);

    PlayerData flySpeed(@Range(from = -1, to = 1) float speed);

    PlayerData flying(TriState flying);

    PlayerData foodLevel(int foodLevel);

    PlayerData freezeTicks(int freezeTicks);

    PlayerData gameMode(@Nullable GameMode gameMode);

    PlayerData gliding(boolean gliding);

    PlayerData health(double health);

    PlayerData heldItemSlot(int heldItemSlot);

    PlayerData inventory(@Nullable ItemStack[] contents);

    PlayerData invulnerable(boolean invulnerable);

    PlayerData lastDeathLocation(@Nullable Location location);

    PlayerData lastLocation(@Nullable Location location);

    /**
     * @since 0.2.6
     */
    PlayerData lastAdvancementTab(@Nullable Key key);

    PlayerData level(int level);

    PlayerData lockFreezeTicks(boolean lockFreezeTicks);

    PlayerData mayFly(TriState mayFly);

    PlayerData portalCooldown(int cooldown);

    PlayerData potionEffects(Collection<PotionEffect> effects);

    PlayerData previousGameMode(@Nullable GameMode gameMode);

    PlayerData remainingAir(int remainingAir);

    PlayerData respawnLocation(@Nullable Location location);

    PlayerData saturation(float saturation);

    PlayerData score(int score);

    PlayerData seenCredits(boolean seenCredits);

    PlayerData stats(Stats stats);

    PlayerData velocity(Vector velocity);

    /**
     * @since 0.2.0
     */
    PlayerData visualFire(TriState visualFire);

    PlayerData walkSpeed(@Range(from = -1, to = 1) float speed);

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
    CompletableFuture<Boolean> load(Player player, boolean position);
}
