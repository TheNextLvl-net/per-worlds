package net.thenextlvl.perworlds;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * Represents the configurable settings of a group.
 * <p>
 * The settings of a group can be retrieved using {@link WorldGroup#getSettings}.
 * <p>
 * When interacting with a group, ensure to verify whether it is {@link #enabled()}
 * before performing any operations.
 *
 * @since 0.1.0
 */
@ApiStatus.NonExtendable
public interface GroupSettings {
    /**
     * Determines whether the associated group is enabled.
     *
     * @return {@code true} if the group is enabled, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean enabled();

    /**
     * Configures whether the associated group is enabled or disabled.
     *
     * @param enabled {@code true} to enable the group, or {@code false} to disable it
     */
    @Contract(mutates = "this")
    void enabled(boolean enabled);

    ///
    /// Chat-related settings
    ///

    /**
     * Determines whether advancement messages should only be sent to players within the current group context.
     *
     * @return {@code true} if advancement messages are restricted to the current group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean advancementMessages();

    @Contract(mutates = "this")
    void advancementMessages(boolean enabled);

    /**
     * Determines whether death messages should only be sent to players within the current group context.
     *
     * @return {@code true} if death messages are restricted to the current group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean deathMessages();

    @Contract(mutates = "this")
    void deathMessages(boolean enabled);

    /**
     * Determines whether join messages should only be sent to players within the current group context.
     *
     * @return {@code true} if join messages are restricted to the current group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean joinMessages();

    @Contract(mutates = "this")
    void joinMessages(boolean enabled);

    /**
     * Determines whether quit messages should only be sent to players within the current group context.
     *
     * @return {@code true} if quit messages are restricted to the current group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean quitMessages();

    @Contract(mutates = "this")
    void quitMessages(boolean enabled);

    ///
    /// Server-related settings
    ///

    /**
     * Determines whether chat messages should only be sent to players within the current group context.
     *
     * @return {@code true} if chat messages are restricted to the current group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean chat();

    @Contract(mutates = "this")
    void chat(boolean enabled);

    /**
     * Determines whether the difficulty should be synced across all worlds in the current group context.
     *
     * @return {@code true} if the difficulty is synchronized across all worlds in the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean difficulty();

    @Contract(mutates = "this")
    void difficulty(boolean enabled);

    /**
     * Determines whether the game-rules should be synced across all worlds in the current group context.
     *
     * @return {@code true} if the game-rules are synchronized across all worlds in the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean gameRules();

    @Contract(mutates = "this")
    void gameRules(boolean enabled);

    /**
     * Determines whether the tablist should be synced across all worlds in the current group context.
     *
     * @return {@code true} if the tablist is synchronized across all worlds in the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean tabList();

    @Contract(mutates = "this")
    void tabList(boolean enabled);

    /**
     * Determines whether the time should be synced across all worlds in the current group context.
     *
     * @return {@code true} if the time is synchronized across all worlds in the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean time();

    @Contract(mutates = "this")
    void time(boolean enabled);

    /**
     * Determines whether the weather should be synced across all worlds in the current group context.
     *
     * @return {@code true} if the weather is synchronized across all worlds in the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean weather();

    @Contract(mutates = "this")
    void weather(boolean enabled);

    /**
     * Determines whether the world-border should be synced across all worlds in the current group context.
     *
     * @return {@code true} if the world-border is synchronized across all worlds in the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean worldBorder();

    @Contract(mutates = "this")
    void worldBorder(boolean enabled);

    ///
    /// Player-related settings
    ///

    /**
     * Determines whether players' absorption amount should be restored for the current group context.
     * <p>
     * Will restore the default absorption amount if disabled
     *
     * @return {@code true} if the player's absorption data should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean absorption();

    @Contract(mutates = "this")
    void absorption(boolean enabled);

    /**
     * Determines whether players' advancements should be restored for the current group context.
     * <p>
     * Will restore no advancements if disabled
     *
     * @return {@code true} if the player's advancements should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean advancements();

    @Contract(mutates = "this")
    void advancements(boolean enabled);

    /**
     * Determines whether players' arrows stuck in their body should be restored for the current group context.
     * <p>
     * Will restore no arrows stuck in the body if disabled
     *
     * @return {@code true} if the player's arrows stuck in body should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean arrowsInBody();

    @Contract(mutates = "this")
    void arrowsInBody(boolean enabled);

    /**
     * Determines whether players' attributes should be restored for the current group context.
     * <p>
     * Will restore no attributes if disabled
     *
     * @return {@code true} if the player's attributes should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean attributes();

    @Contract(mutates = "this")
    void attributes(boolean enabled);

    /**
     * Determines whether players' bee stingers stuck in their body should be restored for the current group context.
     * <p>
     * Will restore no bee stingers stuck in the body if disabled
     *
     * @return {@code true} if the player's bee stingers stuck in body should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean beeStingersInBody();

    @Contract(mutates = "this")
    void beeStingersInBody(boolean enabled);

    /**
     * Determines whether players' state having seen the win screen should be restored for the current group context.
     * <p>
     * Will restore as unseen if disabled
     *
     * @return {@code true} if the player's state having seen the win screen should be restored for the group context,
     * otherwise {@code false}
     */
    @Contract(pure = true)
    boolean endCredits();

    @Contract(mutates = "this")
    void endCredits(boolean enabled);

    /**
     * Determines whether players' enderchest should be restored for the current group context.
     * <p>
     * Will restore an empty enderchest if disabled
     *
     * @return {@code true} if the player's enderchest should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean enderChest();

    @Contract(mutates = "this")
    void enderChest(boolean enabled);

    /**
     * Determines whether players' exhaustion should be restored for the current group context.
     * <p>
     * Will restore no exhaustion if disabled
     *
     * @return {@code true} if the player's exhaustion should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean exhaustion();

    @Contract(mutates = "this")
    void exhaustion(boolean enabled);

    /**
     * Determines whether players' experience should be restored for the current group context.
     * <p>
     * Will restore no experience if disabled
     *
     * @return {@code true} if the player's experience should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean experience();

    @Contract(mutates = "this")
    void experience(boolean enabled);

    /**
     * Determines whether players' fall distance should be restored for the current group context.
     * <p>
     * Will restore no fall distance if disabled
     *
     * @return {@code true} if the player's fall distance should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean fallDistance();

    @Contract(mutates = "this")
    void fallDistance(boolean enabled);

    /**
     * Determines whether players' fire ticks should be restored for the current group context.
     * <p>
     * Will restore no fire ticks if disabled
     *
     * @return {@code true} if the player's fire ticks should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean fireTicks();

    @Contract(mutates = "this")
    void fireTicks(boolean enabled);

    /**
     * Determines whether players' fly speed should be restored for the current group context.
     * <p>
     * Will restore the default fly speed if disabled
     *
     * @return {@code true} if the player's fly speed should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean flySpeed();

    @Contract(mutates = "this")
    void flySpeed(boolean enabled);

    /**
     * Determines whether players' fly state should be restored for the current group context.
     * <p>
     * Will restore the default fly state if disabled
     *
     * @return {@code true} if the player's fly state should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean flyState();

    @Contract(mutates = "this")
    void flyState(boolean enabled);

    /**
     * Determines whether players' food level should be restored for the current group context.
     * <p>
     * Will restore the default food level if disabled
     *
     * @return {@code true} if the player's food level should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean foodLevel();

    @Contract(mutates = "this")
    void foodLevel(boolean enabled);

    /**
     * Determines whether players' freeze ticks should be restored for the current group context.
     * <p>
     * Will restore no freeze ticks if disabled
     *
     * @return {@code true} if the player's freeze ticks should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean freezeTicks();

    @Contract(mutates = "this")
    void freezeTicks(boolean enabled);

    /**
     * Determines whether players' gamemode should be restored for the current group context.
     * <p>
     * Will restore the default gamemode if disabled
     *
     * @return {@code true} if the player's gamemode should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean gameMode();

    @Contract(mutates = "this")
    void gameMode(boolean enabled);

    /**
     * Determines whether players' gliding state should be restored for the current group context.
     * <p>
     * Will restore no gliding state if disabled
     *
     * @return {@code true} if the player's gliding state should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean gliding();

    @Contract(mutates = "this")
    void gliding(boolean enabled);

    /**
     * Determines whether players' health should be restored for the current group context.
     * <p>
     * Will restore the default health if disabled
     *
     * @return {@code true} if the player's health should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean health();

    @Contract(mutates = "this")
    void health(boolean enabled);

    /**
     * Determines whether players' selected hotbar slot should be restored for the current group context.
     * <p>
     * Will restore the default selected hotbar slot if disabled
     *
     * @return {@code true} if the player's selected hotbar slot should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean hotbarSlot();

    @Contract(mutates = "this")
    void hotbarSlot(boolean enabled);

    /**
     * Determines whether players' inventory should be restored for the current group context.
     * <p>
     * Will restore an empty inventory if disabled
     *
     * @return {@code true} if the player's inventory should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean inventory();

    @Contract(mutates = "this")
    void inventory(boolean enabled);

    /**
     * Determines whether players' invulnerability should be restored for the current group context.
     * <p>
     * Will restore no invulnerability if disabled
     *
     * @return {@code true} if the player's invulnerability should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean invulnerable();

    @Contract(mutates = "this")
    void invulnerable(boolean enabled);

    /**
     * Determines whether players' last death location should be restored for the current group context.
     * <p>
     * Will restore no last death location if disabled
     *
     * @return {@code true} if the player's last death location should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean lastDeathLocation();

    @Contract(mutates = "this")
    void lastDeathLocation(boolean enabled);

    /**
     * Determines whether players' last location should be restored for the current group context.
     * <p>
     * Will restore the default spawn location if disabled
     *
     * @return {@code true} if the player's last location should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean lastLocation();

    @Contract(mutates = "this")
    void lastLocation(boolean enabled);

    /**
     * Determines whether players' freeze-ticks-lock should be restored for the current group context.
     * <p>
     * Will restore no freeze-ticks-lock if disabled
     *
     * @return {@code true} if the player's freeze-ticks-lock should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean lockFreezeTicks();

    @Contract(mutates = "this")
    void lockFreezeTicks(boolean enabled);

    /**
     * Determines whether players' portal cooldown should be restored for the current group context.
     * <p>
     * Will restore the default portal cooldown if disabled
     *
     * @return {@code true} if the player's portal cooldown should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean portalCooldown();

    @Contract(mutates = "this")
    void portalCooldown(boolean enabled);

    /**
     * Determines whether players' potion effects should be restored for the current group context.
     * <p>
     * Will restore no potion effects if disabled
     *
     * @return {@code true} if the player's potion effects should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean potionEffects();

    @Contract(mutates = "this")
    void potionEffects(boolean enabled);

    /**
     * Determines whether players' discovered recipes should be restored for the current group context.
     * <p>
     * Will restore no recipes if disabled
     *
     * @return {@code true} if the player's discovered recipes should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean recipes();

    @Contract(mutates = "this")
    void recipes(boolean enabled);

    /**
     * Determines whether players' remaining air should be restored for the current group context.
     * <p>
     * Will restore the default remaining air if disabled
     *
     * @return {@code true} if the player's remaining air should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean remainingAir();

    @Contract(mutates = "this")
    void remainingAir(boolean enabled);

    /**
     * Determines whether players' respawn location should be restored for the current group context.
     * <p>
     * Will restore no respawn location if disabled
     *
     * @return {@code true} if the player's respawn location should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean respawnLocation();

    @Contract(mutates = "this")
    void respawnLocation(boolean enabled);

    /**
     * Determines whether players' saturation should be restored for the current group context.
     * <p>
     * Will restore the default saturation if disabled
     *
     * @return {@code true} if the player's saturation should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean saturation();

    @Contract(mutates = "this")
    void saturation(boolean enabled);

    /**
     * Determines whether players' death screen score should be restored for the current group context.
     * <p>
     * Will restore no death screen score if disabled
     *
     * @return {@code true} if the player's death screen score should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean score();

    @Contract(mutates = "this")
    void score(boolean enabled);

    /**
     * Determines whether players' statistics should be restored for the current group context.
     * <p>
     * Will restore no statistics if disabled
     *
     * @return {@code true} if the player's statistics should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean statistics();

    @Contract(mutates = "this")
    void statistics(boolean enabled);

    /**
     * Determines whether players' velocity should be restored for the current group context.
     * <p>
     * Will restore no velocity if disabled
     *
     * @return {@code true} if the player's velocity should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean velocity();

    @Contract(mutates = "this")
    void velocity(boolean enabled);

    /**
     * Determines whether players' visual fire state should be restored for the current group context.
     * <p>
     * Will restore the default visual fire state if disabled
     *
     * @return {@code true} if the player's visual fire state should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean visualFire();

    @Contract(mutates = "this")
    void visualFire(boolean enabled);

    /**
     * Determines whether players' walk speed should be restored for the current group context.
     * <p>
     * Will restore the default walk speed if disabled
     *
     * @return {@code true} if the player's walk speed should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean walkSpeed();

    @Contract(mutates = "this")
    void walkSpeed(boolean enabled);

    /**
     * Determines whether players' warden spawn tracker should be restored for the current group context.
     * <p>
     * Will restore the default warden spawn tracker if disabled
     *
     * @return {@code true} if the player's warden spawn tracker should be restored for the group context, otherwise {@code false}
     */
    @Contract(pure = true)
    boolean wardenSpawnTracker();

    @Contract(mutates = "this")
    void wardenSpawnTracker(boolean enabled);
}
