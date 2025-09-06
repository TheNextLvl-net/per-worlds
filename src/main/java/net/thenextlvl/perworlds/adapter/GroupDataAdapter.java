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
public class GroupDataAdapter implements TagAdapter<GroupData> {
    private final Server server;

    public GroupDataAdapter(Server server) {
        this.server = server;
    }

    @Override
    @SuppressWarnings("unchecked")
    public GroupData deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var root = tag.getAsCompound();
        var data = new PaperGroupData();
        root.optional("defaultGameMode").map(tag1 -> context.deserialize(tag1, GameMode.class)).ifPresent(data::setDefaultGameMode);
        root.optional("difficulty").map(tag1 -> context.deserialize(tag1, Difficulty.class)).ifPresent(data::setDifficulty);
        root.optional("spawnLocation").map(tag1 -> context.deserialize(tag1, Location.class)).ifPresent(data::setSpawnLocation);
        root.optional("worldBorder").map(tag1 -> context.deserialize(tag1, WorldBorderData.class)).ifPresent(data::setWorldBorder);
        root.optional("hardcore").map(tag1 -> {
            if (!(tag1 instanceof ByteTag)) return context.deserialize(tag1, TriState.class);
            var value = tag1.getAsBoolean();
            return value == server.isHardcore() ? TriState.NOT_SET : TriState.byBoolean(value);
        }).ifPresent(data::setHardcore);
        root.optional("raining").map(Tag::getAsBoolean).ifPresent(data::setRaining);
        root.optional("thundering").map(Tag::getAsBoolean).ifPresent(data::setThundering);
        root.optional("thunderDuration").map(Tag::getAsInt).ifPresent(data::setThunderDuration);
        root.optional("clearWeatherDuration").map(Tag::getAsInt).ifPresent(data::clearWeatherDuration);
        root.optional("rainDuration").map(Tag::getAsInt).ifPresent(data::setRainDuration);
        root.optional("time").map(Tag::getAsLong).ifPresent(data::setTime);
        root.optional("gameRules").map(Tag::getAsCompound).ifPresent(rules -> rules.entrySet().forEach(entry -> {
            var rule = (GameRule<Object>) GameRule.getByName(entry.getKey());
            if (rule != null) data.setGameRule(rule, context.deserialize(entry.getValue(), rule.getType()));
        }));
        return data;
    }

    @Override
    public Tag serialize(GroupData data, TagSerializationContext context) throws ParserException {
        var tag = CompoundTag.empty();
        var rules = CompoundTag.empty();
        data.forEachGameRule((rule, value) -> rules.add(rule.getName(), context.serialize(value)));
        data.getSpawnLocation().ifPresent(location -> tag.add("spawnLocation", context.serialize(location)));
        data.getDefaultGameMode().ifPresent(mode -> tag.add("defaultGameMode", context.serialize(mode)));
        tag.add("difficulty", context.serialize(data.getDifficulty()));
        tag.add("worldBorder", context.serialize(data.getWorldBorder()));
        tag.add("gameRules", rules);
        tag.add("hardcore", context.serialize(data.getHardcore()));
        tag.add("raining", data.isRaining());
        tag.add("thundering", data.isThundering());
        tag.add("thunderDuration", data.getThunderDuration());
        tag.add("clearWeatherDuration", data.clearWeatherDuration());
        tag.add("rainDuration", data.getRainDuration());
        tag.add("time", data.getTime());
        return tag;
    }
}
