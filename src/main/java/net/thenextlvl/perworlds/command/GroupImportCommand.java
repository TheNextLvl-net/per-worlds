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
import net.thenextlvl.perworlds.importer.Importer;

final class GroupImportCommand extends SimpleCommand {
    private GroupImportCommand(PerWorldsPlugin plugin) {
        super(plugin, "import", "perworlds.command.group.import");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PerWorldsPlugin plugin) {
        var command = new GroupImportCommand(plugin);
        return command.create().then(Commands.argument("provider", StringArgumentType.string())
                .suggests(new DataImportSuggestionProvider<>(plugin))
                .executes(command));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var provider = context.getArgument("provider", String.class);
        var importer = plugin.importers().stream()
                .filter(i -> i.getName().equals(provider))
                .filter(Importer::isAvailable)
                .findAny()
                .orElse(null);
        var success = importer != null && importer.load(context.getSource().getSender());
        var message = success ? "group.data.import.success" : "group.data.import.failed";
        plugin.bundle().sendMessage(context.getSource().getSender(), message,
                Placeholder.parsed("provider", provider));
        return success ? 1 : 0;
    }
}
