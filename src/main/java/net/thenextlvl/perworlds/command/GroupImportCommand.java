package net.thenextlvl.perworlds.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.command.brigadier.SimpleCommand;
import net.thenextlvl.perworlds.command.suggestion.DataImportSuggestionProvider;

import java.util.concurrent.atomic.AtomicBoolean;

final class GroupImportCommand extends SimpleCommand {
    private final AtomicBoolean importing = new AtomicBoolean();

    private GroupImportCommand(final PerWorldsPlugin plugin) {
        super(plugin, "import", "perworlds.command.group.import");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(final PerWorldsPlugin plugin) {
        final var command = new GroupImportCommand(plugin);
        return command.create().then(Commands.argument("provider", StringArgumentType.string())
                .suggests(new DataImportSuggestionProvider<>(plugin))
                .executes(command));
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final var sender = context.getSource().getSender();
        if (!importing.compareAndSet(false, true)) {
            plugin.bundle().sendMessage(sender, "group.data.import.active");
            return 0;
        }
        final var provider = context.getArgument("provider", String.class);
        final var optional = plugin.importers().stream()
                .filter(i -> i.getName().equalsIgnoreCase(provider))
                .findAny();
        optional.ifPresentOrElse(importer -> {
            importer.load(sender).handle((success, error) -> {
                return error == null && success;
            }).thenAccept(success -> {
                final var message = success ? "group.data.import.success" : "group.data.import.failed";
                plugin.bundle().sendMessage(sender, message, Placeholder.parsed("provider", importer.getName()));
                importing.set(false);
            });
        }, () -> {
            plugin.bundle().sendMessage(sender, "group.data.import.failed", Placeholder.parsed("provider", provider));
            importing.set(false);
        });
        return SINGLE_SUCCESS;
    }
}
