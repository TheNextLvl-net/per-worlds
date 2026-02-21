package net.thenextlvl.perworlds.group;

import net.thenextlvl.perworlds.GroupSettings;

public final class PaperGroupSettings implements GroupSettings {
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
    public void enabled(final boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean advancementMessages() {
        return advancementMessages;
    }

    @Override
    public void advancementMessages(final boolean enabled) {
        this.advancementMessages = enabled;
    }

    @Override
    public boolean deathMessages() {
        return deathMessages;
    }

    @Override
    public void deathMessages(final boolean enabled) {
        this.deathMessages = enabled;
    }

    @Override
    public boolean joinMessages() {
        return joinMessages;
    }

    @Override
    public void joinMessages(final boolean enabled) {
        this.joinMessages = enabled;
    }

    @Override
    public boolean quitMessages() {
        return quitMessages;
    }

    @Override
    public void quitMessages(final boolean enabled) {
        this.quitMessages = enabled;
    }

    @Override
    public boolean chat() {
        return chat;
    }

    @Override
    public void chat(final boolean enabled) {
        this.chat = enabled;
    }

    @Override
    public boolean difficulty() {
        return difficulty;
    }

    @Override
    public void difficulty(final boolean enabled) {
        this.difficulty = enabled;
    }

    @Override
    public boolean gameRules() {
        return gameRules;
    }

    @Override
    public void gameRules(final boolean enabled) {
        this.gameRules = enabled;
    }

    @Override
    public boolean tabList() {
        return tabList;
    }

    @Override
    public void tabList(final boolean enabled) {
        this.tabList = enabled;
    }

    @Override
    public boolean time() {
        return time;
    }

    @Override
    public void time(final boolean enabled) {
        this.time = enabled;
    }

    @Override
    public boolean weather() {
        return weather;
    }

    @Override
    public void weather(final boolean enabled) {
        this.weather = enabled;
    }

    @Override
    public boolean worldBorder() {
        return worldBorder;
    }

    @Override
    public void worldBorder(final boolean enabled) {
        this.worldBorder = enabled;
    }

    @Override
    public boolean absorption() {
        return absorption;
    }

    @Override
    public void absorption(final boolean enabled) {
        this.absorption = enabled;
    }

    @Override
    public boolean advancements() {
        return advancements;
    }

    @Override
    public void advancements(final boolean enabled) {
        this.advancements = enabled;
    }

    @Override
    public boolean arrowsInBody() {
        return arrowsInBody;
    }

    @Override
    public void arrowsInBody(final boolean enabled) {
        this.arrowsInBody = enabled;
    }

    @Override
    public boolean attributes() {
        return attributes;
    }

    @Override
    public void attributes(final boolean enabled) {
        this.attributes = enabled;
    }

    @Override
    public boolean beeStingersInBody() {
        return beeStingersInBody;
    }

    @Override
    public void beeStingersInBody(final boolean enabled) {
        this.beeStingersInBody = enabled;
    }

    @Override
    public boolean endCredits() {
        return endCredits;
    }

    @Override
    public void endCredits(final boolean enabled) {
        this.endCredits = enabled;
    }

    @Override
    public boolean enderChest() {
        return enderChest;
    }

    @Override
    public void enderChest(final boolean enabled) {
        this.enderChest = enabled;
    }

    @Override
    public boolean exhaustion() {
        return exhaustion;
    }

    @Override
    public void exhaustion(final boolean enabled) {
        this.exhaustion = enabled;
    }

    @Override
    public boolean experience() {
        return experience;
    }

    @Override
    public void experience(final boolean enabled) {
        this.experience = enabled;
    }

    @Override
    public boolean fallDistance() {
        return fallDistance;
    }

    @Override
    public void fallDistance(final boolean enabled) {
        this.fallDistance = enabled;
    }

    @Override
    public boolean fireTicks() {
        return fireTicks;
    }

    @Override
    public void fireTicks(final boolean enabled) {
        this.fireTicks = enabled;
    }

    @Override
    public boolean flySpeed() {
        return flySpeed;
    }

    @Override
    public void flySpeed(final boolean enabled) {
        this.flySpeed = enabled;
    }

    @Override
    public boolean flyState() {
        return flyState;
    }

    @Override
    public void flyState(final boolean enabled) {
        this.flyState = enabled;
    }

    @Override
    public boolean foodLevel() {
        return foodLevel;
    }

    @Override
    public void foodLevel(final boolean enabled) {
        this.foodLevel = enabled;
    }

    @Override
    public boolean freezeTicks() {
        return freezeTicks;
    }

    @Override
    public void freezeTicks(final boolean enabled) {
        this.freezeTicks = enabled;
    }

    @Override
    public boolean gameMode() {
        return gameMode;
    }

    @Override
    public void gameMode(final boolean enabled) {
        this.gameMode = enabled;
    }

    @Override
    public boolean gliding() {
        return gliding;
    }

    @Override
    public void gliding(final boolean enabled) {
        this.gliding = enabled;
    }

    @Override
    public boolean health() {
        return health;
    }

    @Override
    public void health(final boolean enabled) {
        this.health = enabled;
    }

    @Override
    public boolean hotbarSlot() {
        return hotbarSlot;
    }

    @Override
    public void hotbarSlot(final boolean enabled) {
        this.hotbarSlot = enabled;
    }

    @Override
    public boolean inventory() {
        return inventory;
    }

    @Override
    public void inventory(final boolean enabled) {
        this.inventory = enabled;
    }

    @Override
    public boolean invulnerable() {
        return invulnerable;
    }

    @Override
    public void invulnerable(final boolean enabled) {
        this.invulnerable = enabled;
    }

    @Override
    public boolean lastDeathLocation() {
        return lastDeathLocation;
    }

    @Override
    public void lastDeathLocation(final boolean enabled) {
        this.lastDeathLocation = enabled;
    }

    @Override
    public boolean lastLocation() {
        return lastLocation;
    }

    @Override
    public void lastLocation(final boolean enabled) {
        this.lastLocation = enabled;
    }

    @Override
    public boolean lockFreezeTicks() {
        return lockFreezeTicks;
    }

    @Override
    public void lockFreezeTicks(final boolean enabled) {
        this.lockFreezeTicks = enabled;
    }

    @Override
    public boolean portalCooldown() {
        return portalCooldown;
    }

    @Override
    public void portalCooldown(final boolean enabled) {
        this.portalCooldown = enabled;
    }

    @Override
    public boolean potionEffects() {
        return potionEffects;
    }

    @Override
    public void potionEffects(final boolean enabled) {
        this.potionEffects = enabled;
    }

    @Override
    public boolean recipes() {
        return recipes;
    }

    @Override
    public void recipes(final boolean enabled) {
        this.recipes = enabled;
    }

    @Override
    public boolean remainingAir() {
        return remainingAir;
    }

    @Override
    public void remainingAir(final boolean enabled) {
        this.remainingAir = enabled;
    }

    @Override
    public boolean respawnLocation() {
        return respawnLocation;
    }

    @Override
    public void respawnLocation(final boolean enabled) {
        this.respawnLocation = enabled;
    }

    @Override
    public boolean saturation() {
        return saturation;
    }

    @Override
    public void saturation(final boolean enabled) {
        this.saturation = enabled;
    }

    @Override
    public boolean score() {
        return score;
    }

    @Override
    public void score(final boolean enabled) {
        this.score = enabled;
    }

    @Override
    public boolean statistics() {
        return statistics;
    }

    @Override
    public void statistics(final boolean enabled) {
        this.statistics = enabled;
    }

    @Override
    public boolean velocity() {
        return velocity;
    }

    @Override
    public void velocity(final boolean enabled) {
        this.velocity = enabled;
    }

    @Override
    public boolean visualFire() {
        return visualFire;
    }

    @Override
    public void visualFire(final boolean enabled) {
        this.visualFire = enabled;
    }

    @Override
    public boolean walkSpeed() {
        return walkSpeed;
    }

    @Override
    public void walkSpeed(final boolean enabled) {
        this.walkSpeed = enabled;
    }

    @Override
    public boolean wardenSpawnTracker() {
        return wardenSpawnTracker;
    }

    @Override
    public void wardenSpawnTracker(final boolean enabled) {
        this.wardenSpawnTracker = enabled;
    }

    @Override
    public GroupSettings copyFrom(final GroupSettings other) {
        this.absorption = other.absorption();
        this.advancementMessages = other.advancementMessages();
        this.advancements = other.advancements();
        this.arrowsInBody = other.arrowsInBody();
        this.attributes = other.attributes();
        this.beeStingersInBody = other.beeStingersInBody();
        this.chat = other.chat();
        this.deathMessages = other.deathMessages();
        this.difficulty = other.difficulty();
        this.enabled = other.enabled();
        this.endCredits = other.endCredits();
        this.enderChest = other.enderChest();
        this.exhaustion = other.exhaustion();
        this.experience = other.experience();
        this.fallDistance = other.fallDistance();
        this.fireTicks = other.fireTicks();
        this.flySpeed = other.flySpeed();
        this.flyState = other.flyState();
        this.foodLevel = other.foodLevel();
        this.freezeTicks = other.freezeTicks();
        this.gameMode = other.gameMode();
        this.gameRules = other.gameRules();
        this.gliding = other.gliding();
        this.health = other.health();
        this.hotbarSlot = other.hotbarSlot();
        this.inventory = other.inventory();
        this.invulnerable = other.invulnerable();
        this.joinMessages = other.joinMessages();
        this.lastDeathLocation = other.lastDeathLocation();
        this.lastLocation = other.lastLocation();
        this.lockFreezeTicks = other.lockFreezeTicks();
        this.portalCooldown = other.portalCooldown();
        this.potionEffects = other.potionEffects();
        this.quitMessages = other.quitMessages();
        this.recipes = other.recipes();
        this.remainingAir = other.remainingAir();
        this.respawnLocation = other.respawnLocation();
        this.saturation = other.saturation();
        this.score = other.score();
        this.statistics = other.statistics();
        this.tabList = other.tabList();
        this.time = other.time();
        this.velocity = other.velocity();
        this.visualFire = other.visualFire();
        this.walkSpeed = other.walkSpeed();
        this.wardenSpawnTracker = other.wardenSpawnTracker();
        this.weather = other.weather();
        this.worldBorder = other.worldBorder();
        return this;
    }
}
