package net.thenextlvl.perworlds.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.perworlds.GroupSettings;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import net.thenextlvl.perworlds.command.brigadier.BrigadierCommand;
import org.jspecify.annotations.NullMarked;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static net.thenextlvl.perworlds.command.GroupCommand.groupArgument;

@NullMarked
final class GroupOptionCommand extends BrigadierCommand {
    private GroupOptionCommand(PerWorldsPlugin plugin) {
        super(plugin, "option", "perworlds.command.group.option");
    }

    public static ArgumentBuilder<CommandSourceStack, ?> create(PerWorldsPlugin plugin) {
        var command = new GroupOptionCommand(plugin);
        return command.create()
                .then(command.option("absorption", GroupSettings::absorption, GroupSettings::absorption))
                .then(command.option("advancementMessages", GroupSettings::advancementMessages, GroupSettings::advancementMessages))
                .then(command.option("advancements", GroupSettings::advancements, GroupSettings::advancements))
                .then(command.option("arrowsInBody", GroupSettings::arrowsInBody, GroupSettings::arrowsInBody))
                .then(command.option("attributes", GroupSettings::attributes, GroupSettings::attributes))
                .then(command.option("beeStingersInBody", GroupSettings::beeStingersInBody, GroupSettings::beeStingersInBody))
                .then(command.option("chat", GroupSettings::chat, GroupSettings::chat))
                .then(command.option("deathMessages", GroupSettings::deathMessages, GroupSettings::deathMessages))
                .then(command.option("difficulty", GroupSettings::difficulty, GroupSettings::difficulty))
                .then(command.option("enabled", GroupSettings::enabled, GroupSettings::enabled))
                .then(command.option("endCredits", GroupSettings::endCredits, GroupSettings::endCredits))
                .then(command.option("enderChest", GroupSettings::enderChest, GroupSettings::enderChest))
                .then(command.option("exhaustion", GroupSettings::exhaustion, GroupSettings::exhaustion))
                .then(command.option("experience", GroupSettings::experience, GroupSettings::experience))
                .then(command.option("fallDistance", GroupSettings::fallDistance, GroupSettings::fallDistance))
                .then(command.option("fireTicks", GroupSettings::fireTicks, GroupSettings::fireTicks))
                .then(command.option("flySpeed", GroupSettings::flySpeed, GroupSettings::flySpeed))
                .then(command.option("flyState", GroupSettings::flyState, GroupSettings::flyState))
                .then(command.option("foodLevel", GroupSettings::foodLevel, GroupSettings::foodLevel))
                .then(command.option("freezeTicks", GroupSettings::freezeTicks, GroupSettings::freezeTicks))
                .then(command.option("gameMode", GroupSettings::gameMode, GroupSettings::gameMode))
                .then(command.option("gameRules", GroupSettings::gameRules, GroupSettings::gameRules))
                .then(command.option("gliding", GroupSettings::gliding, GroupSettings::gliding))
                .then(command.option("health", GroupSettings::health, GroupSettings::health))
                .then(command.option("hotbarSlot", GroupSettings::hotbarSlot, GroupSettings::hotbarSlot))
                .then(command.option("inventory", GroupSettings::inventory, GroupSettings::inventory))
                .then(command.option("invulnerable", GroupSettings::invulnerable, GroupSettings::invulnerable))
                .then(command.option("joinMessages", GroupSettings::joinMessages, GroupSettings::joinMessages))
                .then(command.option("lastDeathLocation", GroupSettings::lastDeathLocation, GroupSettings::lastDeathLocation))
                .then(command.option("lastLocation", GroupSettings::lastLocation, GroupSettings::lastLocation))
                .then(command.option("lockFreezeTicks", GroupSettings::lockFreezeTicks, GroupSettings::lockFreezeTicks))
                .then(command.option("portalCooldown", GroupSettings::portalCooldown, GroupSettings::portalCooldown))
                .then(command.option("potionEffects", GroupSettings::potionEffects, GroupSettings::potionEffects))
                .then(command.option("quitMessages", GroupSettings::quitMessages, GroupSettings::quitMessages))
                .then(command.option("recipes", GroupSettings::recipes, GroupSettings::recipes))
                .then(command.option("remainingAir", GroupSettings::remainingAir, GroupSettings::remainingAir))
                .then(command.option("respawnLocation", GroupSettings::respawnLocation, GroupSettings::respawnLocation))
                .then(command.option("saturation", GroupSettings::saturation, GroupSettings::saturation))
                .then(command.option("score", GroupSettings::score, GroupSettings::score))
                .then(command.option("statistics", GroupSettings::statistics, GroupSettings::statistics))
                .then(command.option("tabList", GroupSettings::tabList, GroupSettings::tabList))
                .then(command.option("time", GroupSettings::time, GroupSettings::time))
                .then(command.option("velocity", GroupSettings::velocity, GroupSettings::velocity))
                .then(command.option("visualFire", GroupSettings::visualFire, GroupSettings::visualFire))
                .then(command.option("walkSpeed", GroupSettings::walkSpeed, GroupSettings::walkSpeed))
                .then(command.option("wardenSpawnTracker", GroupSettings::wardenSpawnTracker, GroupSettings::wardenSpawnTracker))
                .then(command.option("weather", GroupSettings::weather, GroupSettings::weather))
                .then(command.option("worldBorder", GroupSettings::worldBorder, GroupSettings::worldBorder));
    }

    private ArgumentBuilder<CommandSourceStack, ?> option(String name, Function<GroupSettings, Boolean> getter, BiConsumer<GroupSettings, Boolean> setter) {
        return Commands.literal(name).then(groupArgument(plugin, true)
                .then(Commands.argument("value", BoolArgumentType.bool())
                        .executes(context -> set(context, name, getter, setter)))
                .executes(context -> query(context, name, getter)));
    }

    private int set(CommandContext<CommandSourceStack> context, String option, Function<GroupSettings, Boolean> getter,
                    BiConsumer<GroupSettings, Boolean> setter) {
        var value = context.getArgument("value", boolean.class);
        var group = context.getArgument("group", WorldGroup.class);
        var success = !getter.apply(group.getSettings()).equals(value);
        if (success) setter.accept(group.getSettings(), value);
        var message = success ? "group.option.set" : "nothing.changed";
        plugin.bundle().sendMessage(context.getSource().getSender(), message,
                Formatter.booleanChoice("value", value),
                Placeholder.unparsed("group", group.getName()),
                Placeholder.unparsed("option", option));
        return Command.SINGLE_SUCCESS;
    }

    private int query(CommandContext<CommandSourceStack> context, String option, Function<GroupSettings, Boolean> getter) {
        var group = context.getArgument("group", WorldGroup.class);
        var value = getter.apply(group.getSettings());
        plugin.bundle().sendMessage(context.getSource().getSender(), "group.option",
                Formatter.booleanChoice("value", value),
                Placeholder.unparsed("group", group.getName()),
                Placeholder.unparsed("option", option));
        return Command.SINGLE_SUCCESS;
    }
}
