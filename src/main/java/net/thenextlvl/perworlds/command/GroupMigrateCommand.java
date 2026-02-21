package net.thenextlvl.perworlds.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import net.thenextlvl.perworlds.command.brigadier.SimpleCommand;
import org.jspecify.annotations.NullMarked;

import static net.thenextlvl.perworlds.command.WorldCommand.groupArgument;

@NullMarked
final class GroupMigrateCommand extends SimpleCommand {
    private GroupMigrateCommand(final PerWorldsPlugin plugin) {
        super(plugin, "migrate", "perworlds.command.group.migrate");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(final PerWorldsPlugin plugin) {
        final var command = new GroupMigrateCommand(plugin);
        return command.create()
                .then(groupArgument(plugin, true).executes(command))
                .executes(command);
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final var group = tryGetArgument(context, "group", WorldGroup.class).orElse(null);
        if (group == null) {
            final var migrate = plugin.config().migrateToGroup;
            final var message = migrate != null ? "group.migrate" : "group.migrate.none";
            plugin.bundle().sendMessage(context.getSource().getSender(), message,
                    Placeholder.parsed("group", String.valueOf(migrate)));
            return SINGLE_SUCCESS;
        }
        final var success = plugin.config().migrateToGroup(plugin, group);
        final var message = success ? "group.migrate" : "nothing.changed";
        plugin.bundle().sendMessage(context.getSource().getSender(), message,
                Placeholder.parsed("group", group.getName()));
        return success ? SINGLE_SUCCESS : 0;
    }
}
