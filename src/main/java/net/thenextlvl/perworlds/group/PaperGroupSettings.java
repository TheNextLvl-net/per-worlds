package net.thenextlvl.perworlds.group;

import net.thenextlvl.perworlds.GroupSettings;

public class PaperGroupSettings implements GroupSettings {
    private boolean absorption = true;
    private boolean advancementMessages = true;
    private boolean advancements = true;
    private boolean arrowsInBody = true;
    private boolean attributes = true;
    private boolean beeStingersInBody = true;
    private boolean chat = false;
    private boolean deathMessages = true;
    private boolean difficulty = true;
    private boolean enabled = true;
    private boolean endCredits = true;
    private boolean enderChest = true;
    private boolean exhaustion = true;
    private boolean experience = true;
    private boolean fallDistance = true;
    private boolean fireTicks = true;
    private boolean flySpeed = true;
    private boolean flyState = true;
    private boolean foodLevel = true;
    private boolean freezeTicks = true;
    private boolean gameMode = true;
    private boolean gameRules = true;
    private boolean gliding = true;
    private boolean health = true;
    private boolean hotbarSlot = true;
    private boolean inventory = true;
    private boolean invulnerable = true;
    private boolean joinMessages = false;
    private boolean lastDeathLocation = true;
    private boolean lastLocation = true;
    private boolean lockFreezeTicks = false;
    private boolean portalCooldown = true;
    private boolean potionEffects = true;
    private boolean quitMessages = false;
    private boolean recipes = true;
    private boolean remainingAir = true;
    private boolean respawnLocation = true;
    private boolean saturation = true;
    private boolean score = true;
    private boolean statistics = true;
    private boolean tabList = true;
    private boolean time = true;
    private boolean velocity = true;
    private boolean visualFire = false;
    private boolean walkSpeed = true;
    private boolean wardenSpawnTracker = true;
    private boolean weather = true;
    private boolean worldBorder = true;

    @Override
    public boolean enabled() {
        return enabled;
    }

    @Override
    public void enabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean advancementMessages() {
        return advancementMessages;
    }

    @Override
    public void advancementMessages(boolean enabled) {
        this.advancementMessages = enabled;
    }

    @Override
    public boolean deathMessages() {
        return deathMessages;
    }

    @Override
    public void deathMessages(boolean enabled) {
        this.deathMessages = enabled;
    }

    @Override
    public boolean joinMessages() {
        return joinMessages;
    }

    @Override
    public void joinMessages(boolean enabled) {
        this.joinMessages = enabled;
    }

    @Override
    public boolean quitMessages() {
        return quitMessages;
    }

    @Override
    public void quitMessages(boolean enabled) {
        this.quitMessages = enabled;
    }

    @Override
    public boolean chat() {
        return chat;
    }

    @Override
    public void chat(boolean enabled) {
        this.chat = enabled;
    }

    @Override
    public boolean difficulty() {
        return difficulty;
    }

    @Override
    public void difficulty(boolean enabled) {
        this.difficulty = enabled;
    }

    @Override
    public boolean gameRules() {
        return gameRules;
    }

    @Override
    public void gameRules(boolean enabled) {
        this.gameRules = enabled;
    }

    @Override
    public boolean tabList() {
        return tabList;
    }

    @Override
    public void tabList(boolean enabled) {
        this.tabList = enabled;
    }

    @Override
    public boolean time() {
        return time;
    }

    @Override
    public void time(boolean enabled) {
        this.time = enabled;
    }

    @Override
    public boolean weather() {
        return weather;
    }

    @Override
    public void weather(boolean enabled) {
        this.weather = enabled;
    }

    @Override
    public boolean worldBorder() {
        return worldBorder;
    }

    @Override
    public void worldBorder(boolean enabled) {
        this.worldBorder = enabled;
    }

    @Override
    public boolean absorption() {
        return absorption;
    }

    @Override
    public void absorption(boolean enabled) {
        this.absorption = enabled;
    }

    @Override
    public boolean advancements() {
        return advancements;
    }

    @Override
    public void advancements(boolean enabled) {
        this.advancements = enabled;
    }

    @Override
    public boolean arrowsInBody() {
        return arrowsInBody;
    }

    @Override
    public void arrowsInBody(boolean enabled) {
        this.arrowsInBody = enabled;
    }

    @Override
    public boolean attributes() {
        return attributes;
    }

    @Override
    public void attributes(boolean enabled) {
        this.attributes = enabled;
    }

    @Override
    public boolean beeStingersInBody() {
        return beeStingersInBody;
    }

    @Override
    public void beeStingersInBody(boolean enabled) {
        this.beeStingersInBody = enabled;
    }

    @Override
    public boolean endCredits() {
        return endCredits;
    }

    @Override
    public void endCredits(boolean enabled) {
        this.endCredits = enabled;
    }

    @Override
    public boolean enderChest() {
        return enderChest;
    }

    @Override
    public void enderChest(boolean enabled) {
        this.enderChest = enabled;
    }

    @Override
    public boolean exhaustion() {
        return exhaustion;
    }

    @Override
    public void exhaustion(boolean enabled) {
        this.exhaustion = enabled;
    }

    @Override
    public boolean experience() {
        return experience;
    }

    @Override
    public void experience(boolean enabled) {
        this.experience = enabled;
    }

    @Override
    public boolean fallDistance() {
        return fallDistance;
    }

    @Override
    public void fallDistance(boolean enabled) {
        this.fallDistance = enabled;
    }

    @Override
    public boolean fireTicks() {
        return fireTicks;
    }

    @Override
    public void fireTicks(boolean enabled) {
        this.fireTicks = enabled;
    }

    @Override
    public boolean flySpeed() {
        return flySpeed;
    }

    @Override
    public void flySpeed(boolean enabled) {
        this.flySpeed = enabled;
    }

    @Override
    public boolean flyState() {
        return flyState;
    }

    @Override
    public void flyState(boolean enabled) {
        this.flyState = enabled;
    }

    @Override
    public boolean foodLevel() {
        return foodLevel;
    }

    @Override
    public void foodLevel(boolean enabled) {
        this.foodLevel = enabled;
    }

    @Override
    public boolean freezeTicks() {
        return freezeTicks;
    }

    @Override
    public void freezeTicks(boolean enabled) {
        this.freezeTicks = enabled;
    }

    @Override
    public boolean gameMode() {
        return gameMode;
    }

    @Override
    public void gameMode(boolean enabled) {
        this.gameMode = enabled;
    }

    @Override
    public boolean gliding() {
        return gliding;
    }

    @Override
    public void gliding(boolean enabled) {
        this.gliding = enabled;
    }

    @Override
    public boolean health() {
        return health;
    }

    @Override
    public void health(boolean enabled) {
        this.health = enabled;
    }

    @Override
    public boolean hotbarSlot() {
        return hotbarSlot;
    }

    @Override
    public void hotbarSlot(boolean enabled) {
        this.hotbarSlot = enabled;
    }

    @Override
    public boolean inventory() {
        return inventory;
    }

    @Override
    public void inventory(boolean enabled) {
        this.inventory = enabled;
    }

    @Override
    public boolean invulnerable() {
        return invulnerable;
    }

    @Override
    public void invulnerable(boolean enabled) {
        this.invulnerable = enabled;
    }

    @Override
    public boolean lastDeathLocation() {
        return lastDeathLocation;
    }

    @Override
    public void lastDeathLocation(boolean enabled) {
        this.lastDeathLocation = enabled;
    }

    @Override
    public boolean lastLocation() {
        return lastLocation;
    }

    @Override
    public void lastLocation(boolean enabled) {
        this.lastLocation = enabled;
    }

    @Override
    public boolean lockFreezeTicks() {
        return lockFreezeTicks;
    }

    @Override
    public void lockFreezeTicks(boolean enabled) {
        this.lockFreezeTicks = enabled;
    }

    @Override
    public boolean portalCooldown() {
        return portalCooldown;
    }

    @Override
    public void portalCooldown(boolean enabled) {
        this.portalCooldown = enabled;
    }

    @Override
    public boolean potionEffects() {
        return potionEffects;
    }

    @Override
    public void potionEffects(boolean enabled) {
        this.potionEffects = enabled;
    }

    @Override
    public boolean recipes() {
        return recipes;
    }

    @Override
    public void recipes(boolean enabled) {
        this.recipes = enabled;
    }

    @Override
    public boolean remainingAir() {
        return remainingAir;
    }

    @Override
    public void remainingAir(boolean enabled) {
        this.remainingAir = enabled;
    }

    @Override
    public boolean respawnLocation() {
        return respawnLocation;
    }

    @Override
    public void respawnLocation(boolean enabled) {
        this.respawnLocation = enabled;
    }

    @Override
    public boolean saturation() {
        return saturation;
    }

    @Override
    public void saturation(boolean enabled) {
        this.saturation = enabled;
    }

    @Override
    public boolean score() {
        return score;
    }

    @Override
    public void score(boolean enabled) {
        this.score = enabled;
    }

    @Override
    public boolean statistics() {
        return statistics;
    }

    @Override
    public void statistics(boolean enabled) {
        this.statistics = enabled;
    }

    @Override
    public boolean velocity() {
        return velocity;
    }

    @Override
    public void velocity(boolean enabled) {
        this.velocity = enabled;
    }

    @Override
    public boolean visualFire() {
        return visualFire;
    }

    @Override
    public void visualFire(boolean enabled) {
        this.visualFire = enabled;
    }

    @Override
    public boolean walkSpeed() {
        return walkSpeed;
    }

    @Override
    public void walkSpeed(boolean enabled) {
        this.walkSpeed = enabled;
    }

    @Override
    public boolean wardenSpawnTracker() {
        return wardenSpawnTracker;
    }

    @Override
    public void wardenSpawnTracker(boolean enabled) {
        this.wardenSpawnTracker = enabled;
    }
}
