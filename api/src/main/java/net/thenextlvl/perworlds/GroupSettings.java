package net.thenextlvl.perworlds;

/**
 * Represents the configurable settings of a group.
 * <p>
 * The settings of a group can be retrieved using {@link WorldGroup#getSettings}.
 * <p>
 * When interacting with a group, ensure to verify whether it is {@link #enabled()}
 * before performing any operations.
 */
public interface GroupSettings {
    /**
     * Determines whether the associated group is enabled.
     *
     * @return {@code true} if the group is enabled, otherwise {@code false}
     */
    boolean enabled();

    void enabled(boolean enabled);


    // whether advancement messages should only be sent to players in the current group context
    boolean advancementMessages();

    void advancementMessages(boolean enabled);

    // whether death messages should only be sent to players in the current group context
    boolean deathMessages();

    void deathMessages(boolean enabled);

    // whether join messages should only be sent to players in the current group context
    boolean joinMessages();

    void joinMessages(boolean enabled);

    // whether quit messages should only be sent to players in the current group context
    boolean quitMessages();

    void quitMessages(boolean enabled);


    // whether chat messages should only be sent to players in the current group context
    boolean chat();

    void chat(boolean enabled);

    // whether the difficulty should be synced across all worlds in the current group context
    boolean difficulty();

    void difficulty(boolean enabled);

    // whether the game rules should be synced across all worlds in the current group context
    boolean gameRules();

    void gameRules(boolean enabled);

    // whether the tablist should be synced across all worlds in the current group context 
    // (only players in the same group will be shown)
    boolean tabList();

    void tabList(boolean enabled);

    // whether the time should be synced across all worlds in the current group context
    boolean time();

    void time(boolean enabled);

    // whether the weather should be synced across all worlds in the current group context
    boolean weather();

    void weather(boolean enabled);

    // whether the world border should be synced across all worlds in the current group context
    boolean worldBorder();

    void worldBorder(boolean enabled);


    // whether Player#getAbsorption should be saved for the current group context 
    boolean absorption();

    void absorption(boolean enabled);

    // whether player advancements should be saved for the current group context
    boolean advancements();

    void advancements(boolean enabled);

    // whether the number of arrows stuck in a player's body should be saved for the current group context
    boolean arrowsInBody();

    void arrowsInBody(boolean enabled);

    // whether player attributes should be saved for the current group context
    boolean attributes();

    void attributes(boolean enabled);

    // whether the number of bee stingers in a player's body should be saved for the current group context
    boolean beeStingersInBody();

    void beeStingersInBody(boolean enabled);

    // whether Player#hasSeenWinScreen should be saved for the current group context 
    boolean endCredits();

    void endCredits(boolean enabled);

    // whether Player#getEnderChest should be saved for the current group context 
    boolean enderChest();

    void enderChest(boolean enabled);

    // whether Player#getExhaustion should be saved for the current group context 
    boolean exhaustion();

    void exhaustion(boolean enabled);

    // whether Player#getExperience should be saved for the current group context 
    boolean experience();

    void experience(boolean enabled);

    // whether Player#getFallDistance should be saved for the current group context 
    boolean fallDistance();

    void fallDistance(boolean enabled);

    // whether Player#getFireTicks should be saved for the current group context 
    boolean fireTicks();

    void fireTicks(boolean enabled);

    // whether Player#getFlySpeed should be saved for the current group context 
    boolean flySpeed();

    void flySpeed(boolean enabled);

    // whether Player#getFlyState should be saved for the current group context 
    boolean flyState();

    void flyState(boolean enabled);

    // whether Player#getFoodLevel should be saved for the current group context 
    boolean foodLevel();

    void foodLevel(boolean enabled);

    // whether Player#getFreezeTicks should be saved for the current group context
    boolean freezeTicks();

    void freezeTicks(boolean enabled);

    // whether Player#getGameMode should be saved for the current group context 
    boolean gameMode();

    void gameMode(boolean enabled);

    // whether Player#isGliding should be saved for the current group context 
    boolean gliding();

    void gliding(boolean enabled);

    // whether Player#getHealth should be saved for the current group context 
    boolean health();

    void health(boolean enabled);

    // whether the player's selected hotbar slot should be saved for the current group context 
    boolean hotbarSlot();

    void hotbarSlot(boolean enabled);

    // whether Player#getInventory should be saved for the current group context 
    boolean inventory();

    void inventory(boolean enabled);

    // whether Player#isInvulnerable should be saved for the current group context 
    boolean invulnerable();

    void invulnerable(boolean enabled);

    // whether Player#getLastDeathLocation should be saved for the current group context 
    boolean lastDeathLocation();

    void lastDeathLocation(boolean enabled);

    // whether Player#getLocation should be saved for the current group context 
    boolean lastLocation();

    void lastLocation(boolean enabled);

    // whether Player#lockFreezeTicks should be saved for the current group context 
    boolean lockFreezeTicks();

    void lockFreezeTicks(boolean enabled);

    // whether Player#getPortalCooldown should be saved for the current group context 
    boolean portalCooldown();

    void portalCooldown(boolean enabled);

    // whether Player#getActivePotionEffects should be saved for the current group context 
    boolean potionEffects();

    void potionEffects(boolean enabled);

    // whether a player's recipe book should be saved for the current group context
    boolean recipes();

    void recipes(boolean enabled);

    // whether Player#getRemainingAir should be saved for the current group context 
    boolean remainingAir();

    void remainingAir(boolean enabled);

    // whether Player#getRespawnLocation should be saved for the current group context 
    boolean respawnLocation();

    void respawnLocation(boolean enabled);

    // whether Player#getSaturation should be saved for the current group context 
    boolean saturation();

    void saturation(boolean enabled);

    // whether Player#getDeathScreenScore should be saved for the current group context 
    boolean score();

    void score(boolean enabled);

    // whether a player's statistics should be saved for the current group context 
    boolean statistics();

    void statistics(boolean enabled);

    // whether Player#getVelocity should be saved for the current group context 
    boolean velocity();

    void velocity(boolean enabled);

    // whether Player#getVisualFire should be saved for the current group context 
    boolean visualFire();

    void visualFire(boolean enabled);

    // whether Player#getWalkSpeed should be saved for the current group context 
    boolean walkSpeed();

    void walkSpeed(boolean enabled);

    // whether Player#getWardenSpawnTracker should be saved for the current group context 
    boolean wardenSpawnTracker();

    void wardenSpawnTracker(boolean enabled);
}
