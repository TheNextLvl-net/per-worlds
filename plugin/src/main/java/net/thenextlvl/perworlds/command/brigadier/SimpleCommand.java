package net.thenextlvl.perworlds.command.brigadier;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class SimpleCommand extends BrigadierCommand implements Command<CommandSourceStack> {
    protected SimpleCommand(final PerWorldsPlugin plugin, final String name, @Nullable final String permission) {
        super(plugin, name, permission);
    }
}
