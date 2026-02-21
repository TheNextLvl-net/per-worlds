package net.thenextlvl.perworlds.adapter;

import net.kyori.adventure.util.TriState;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.ByteTag;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.perworlds.GroupData;
import net.thenextlvl.perworlds.data.WorldBorderData;
import net.thenextlvl.perworlds.group.PaperGroupData;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Server;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class GroupDataAdapter implements TagAdapter<GroupData> {
    private final Server server;

    public GroupDataAdapter(final Server server) {
        this.server = server;
    }

    @Override
    @SuppressWarnings("unchecked")
    public GroupData deserialize(final Tag tag, final TagDeserializationContext context) throws ParserException {
        final var root = tag.getAsCompound();
        final var data = new PaperGroupData();
        root.optional("defaultGameMode").map(tag1 -> context.deserialize(tag1, GameMode.class)).ifPresent(data::setDefaultGameMode);
        root.optional("difficulty").map(tag1 -> context.deserialize(tag1, Difficulty.class)).ifPresent(data::setDifficulty);
        root.optional("spawnLocation").map(tag1 -> context.deserialize(tag1, Location.class)).ifPresent(data::setSpawnLocation);
        root.optional("worldBorder").map(tag1 -> context.deserialize(tag1, WorldBorderData.class)).ifPresent(data::setWorldBorder);
        root.optional("hardcore").map(tag1 -> {
            if (!(tag1 instanceof ByteTag)) return context.deserialize(tag1, TriState.class);
            final var value = tag1.getAsBoolean();
            return value == server.isHardcore() ? TriState.NOT_SET : TriState.byBoolean(value);
        }).ifPresent(data::setHardcore);
        root.optional("raining").map(Tag::getAsBoolean).ifPresent(data::setRaining);
        root.optional("thundering").map(Tag::getAsBoolean).ifPresent(data::setThundering);
        root.optional("thunderDuration").map(Tag::getAsInt).ifPresent(data::setThunderDuration);
        root.optional("clearWeatherDuration").map(Tag::getAsInt).ifPresent(data::clearWeatherDuration);
        root.optional("rainDuration").map(Tag::getAsInt).ifPresent(data::setRainDuration);
        root.optional("time").map(Tag::getAsLong).ifPresent(data::setTime);
        root.optional("gameRules").map(Tag::getAsCompound).ifPresent(rules -> rules.entrySet().forEach(entry -> {
            final var rule = (GameRule<Object>) GameRule.getByName(entry.getKey());
            if (rule != null) data.setGameRule(rule, context.deserialize(entry.getValue(), rule.getType()));
        }));
        return data;
    }

    @Override
    public Tag serialize(final GroupData data, final TagSerializationContext context) throws ParserException {
        final var tag = CompoundTag.builder();
        final var rules = CompoundTag.builder();
        data.forEachGameRule((rule, value) -> rules.put(rule.getName(), context.serialize(value)));
        data.getSpawnLocation().ifPresent(location -> tag.put("spawnLocation", context.serialize(location)));
        data.getDefaultGameMode().ifPresent(mode -> tag.put("defaultGameMode", context.serialize(mode)));
        tag.put("difficulty", context.serialize(data.getDifficulty()));
        tag.put("worldBorder", context.serialize(data.getWorldBorder()));
        tag.put("gameRules", rules.build());
        tag.put("hardcore", context.serialize(data.getHardcore()));
        tag.put("raining", data.isRaining());
        tag.put("thundering", data.isThundering());
        tag.put("thunderDuration", data.getThunderDuration());
        tag.put("clearWeatherDuration", data.clearWeatherDuration());
        tag.put("rainDuration", data.getRainDuration());
        tag.put("time", data.getTime());
        return tag.build();
    }
}
