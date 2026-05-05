package net.thenextlvl.perworlds.command.suggestion;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.kyori.adventure.key.Key;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import org.bukkit.Keyed;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public final class UnassignedWorldsSuggestionProvider<S> implements SuggestionProvider<S> {
    private final PerWorldsPlugin plugin;

    public UnassignedWorldsSuggestionProvider(final PerWorldsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        plugin.getServer().getWorlds().stream()
                .filter(world -> !plugin.groupProvider().hasGroup(world))
                .map(Keyed::key)
                .map(Key::asString)
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
