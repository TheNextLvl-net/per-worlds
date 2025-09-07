package net.thenextlvl.perworlds.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public final class GroupArgument implements CustomArgumentType.Converted<WorldGroup, String> {
    private final PerWorldsPlugin plugin;
    private final boolean listAll;

    public GroupArgument(PerWorldsPlugin plugin, boolean listAll) {
        this.plugin = plugin;
        this.listAll = listAll;
    }

    @Override
    public WorldGroup convert(String nativeType) {
        return plugin.groupProvider().getGroup(nativeType)
                .orElseThrow(() -> new RuntimeException("Group not found"));
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        var groups = listAll ? plugin.groupProvider().getAllGroups() : plugin.groupProvider().getGroups();
        groups.stream().map(WorldGroup::getName)
                .filter(name -> name.contains(builder.getRemaining()))
                .map(StringArgumentType::escapeIfRequired)
                .forEach(builder::suggest);
        return builder.buildFuture();
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }
}
