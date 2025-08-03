package net.thenextlvl.perworlds.command.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import core.paper.command.WrappedArgumentType;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class GroupArgument extends WrappedArgumentType<String, WorldGroup> {
    public GroupArgument(PerWorldsPlugin plugin, boolean listAll) {
        super(StringArgumentType.string(), (reader, type) -> plugin.groupProvider().getGroup(type)
                .orElseThrow(() -> new RuntimeException("Group not found")), (context, builder) -> {
            var groups = listAll ? plugin.groupProvider().getAllGroups() : plugin.groupProvider().getGroups();
            groups.stream().map(WorldGroup::getName)
                    .filter(name -> name.contains(builder.getRemaining()))
                    .map(StringArgumentType::escapeIfRequired)
                    .forEach(builder::suggest);
            return builder.buildFuture();
        });
    }
}
