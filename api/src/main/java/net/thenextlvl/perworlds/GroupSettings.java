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
    // whether Player#getAbsorption should be saved for the current group context 
    boolean absorption();

    // whether advancement messages should only be sent to players in the current group context
    boolean advancementMessages();

    // whether player advancements should be saved for the current group context
    boolean advancements();

    // whether the number of arrows stuck in a player's body should be saved for the current group context
    boolean arrowsInBody();

    // whether player attributes should be saved for the current group context
    boolean attributes();

    // whether the number of bee stingers in a player's body should be saved for the current group context
    boolean beeStingersInBody();

    // whether chat messages should only be sent to players in the current group context
    boolean chat();

    // whether death messages should only be sent to players in the current group context
    boolean deathMessages();

    // whether the difficulty should be synced across all worlds in the current group context
    boolean difficulty();

    /**
     * Determines whether the associated group is enabled.
     *
     * @return {@code true} if the group is enabled, otherwise {@code false}
     */
    boolean enabled();

    // whether Player#hasSeenWinScreen should be saved for the current group context 
    boolean endCredits();

    // whether Player#getEnderChest should be saved for the current group context 
    boolean enderChest();

    // whether Player#getExhaustion should be saved for the current group context 
    boolean exhaustion();

    // whether Player#getExperience should be saved for the current group context 
    boolean experience();

    // whether Player#getFallDistance should be saved for the current group context 
    boolean fallDistance();

    // whether Player#getFireTicks should be saved for the current group context 
    boolean fireTicks();

    // whether Player#getFlySpeed should be saved for the current group context 
    boolean flySpeed();

    // whether Player#getFlyState should be saved for the current group context 
    boolean flyState();

    // whether Player#getFoodLevel should be saved for the current group context 
    boolean foodLevel();

    // whether Player#getFreezeTicks should be saved for the current group context 
    boolean freezeTicks();

    // whether Player#getGameMode should be saved for the current group context 
    boolean gameMode();

    // whether the game rules should be synced across all worlds in the current group context
    boolean gameRules();

    // whether Player#isGliding should be saved for the current group context 
    boolean gliding();

    // whether Player#getHealth should be saved for the current group context 
    boolean health();

    // whether the player's selected hotbar slot should be saved for the current group context 
    boolean hotbarSlot();

    // whether Player#getInventory should be saved for the current group context 
    boolean inventory();

    // whether Player#isInvulnerable should be saved for the current group context 
    boolean invulnerable();

    // whether join messages should only be sent to players in the current group context
    boolean joinMessages();

    // whether Player#getLastDeathLocation should be saved for the current group context 
    boolean lastDeathLocation();

    // whether Player#getLocation should be saved for the current group context 
    boolean lastLocation();

    // whether Player#lockFreezeTicks should be saved for the current group context 
    boolean lockFreezeTicks();

    // whether Player#getPortalCooldown should be saved for the current group context 
    boolean portalCooldown();

    // whether Player#getActivePotionEffects should be saved for the current group context 
    boolean potionEffects();

    // whether quit messages should only be sent to players in the current group context
    boolean quitMessages();

    // whether a player's recipe book should be saved for the current group context
    boolean recipes();

    // whether Player#getRemainingAir should be saved for the current group context 
    boolean remainingAir();

    // whether Player#getRespawnLocation should be saved for the current group context 
    boolean respawnLocation();

    // whether Player#getSaturation should be saved for the current group context 
    boolean saturation();

    // whether Player#getDeathScreenScore should be saved for the current group context 
    boolean score();

    // whether a player's statistics should be saved for the current group context 
    boolean statistics();

    // whether the tablist should be synced across all worlds in the current group context 
    // (only players in the same group will be shown)
    boolean tabList();
    
    // whether the time should be synced across all worlds in the current group context
    boolean time();

    // whether Player#getVelocity should be saved for the current group context 
    boolean velocity();

    // whether Player#getVisualFire should be saved for the current group context 
    boolean visualFire();

    // whether Player#getWalkSpeed should be saved for the current group context 
    boolean walkSpeed();

    // whether Player#getWardenSpawnTracker should be saved for the current group context 
    boolean wardenSpawnTracker();

    // whether the weather should be synced across all worlds in the current group context
    boolean weather();

    // whether the world border should be synced across all worlds in the current group context
    boolean worldBorder();

    void absorption(boolean enabled);

    void advancementMessages(boolean enabled);

    void advancements(boolean enabled);

    void arrowsInBody(boolean enabled);

    void attributes(boolean enabled);

    void beeStingersInBody(boolean enabled);

    void chat(boolean enabled);

    void deathMessages(boolean enabled);

    void difficulty(boolean enabled);

    void enabled(boolean enabled);

    void endCredits(boolean enabled);

    void enderChest(boolean enabled);

    void exhaustion(boolean enabled);

    void experience(boolean enabled);

    void fallDistance(boolean enabled);

    void fireTicks(boolean enabled);

    void flySpeed(boolean enabled);

    void flyState(boolean enabled);

    void foodLevel(boolean enabled);

    void freezeTicks(boolean enabled);

    void gameMode(boolean enabled);

    void gameRules(boolean enabled);

    void gliding(boolean enabled);

    void health(boolean enabled);

    void hotbarSlot(boolean enabled);

    void inventory(boolean enabled);

    void invulnerable(boolean enabled);

    void joinMessages(boolean enabled);

    void lastDeathLocation(boolean enabled);

    void lastLocation(boolean enabled);

    void lockFreezeTicks(boolean enabled);

    void portalCooldown(boolean enabled);

    void potionEffects(boolean enabled);

    void quitMessages(boolean enabled);

    void recipes(boolean enabled);

    void remainingAir(boolean enabled);

    void respawnLocation(boolean enabled);

    void saturation(boolean enabled);

    void score(boolean enabled);

    void statistics(boolean enabled);

    void tabList(boolean enabled);

    void time(boolean enabled);

    void velocity(boolean enabled);

    void visualFire(boolean enabled);

    void walkSpeed(boolean enabled);

    void wardenSpawnTracker(boolean enabled);

    void weather(boolean enabled);

    void worldBorder(boolean enabled);
}
