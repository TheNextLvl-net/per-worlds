package net.thenextlvl.perworlds.importer.myworlds;

import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import net.thenextlvl.perworlds.data.PlayerData;
import net.thenextlvl.perworlds.importer.Importer;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@NullMarked
public class MWImporter extends Importer {
    public MWImporter(final PerWorldsPlugin plugin) {
        super(plugin, "My_Worlds");
    }

    @Override
    public Map<String, Set<String>> readGroups() throws IOException {
        return Map.of(); // todo
    }

    @Override
    public Map<UUID, String> readPlayers() throws IOException {
        return Map.of(); // todo
    }

    @Override
    public void readPlayer(final UUID uuid, final String name, final WorldGroup group, final PlayerData data) throws IOException {
        // todo
    }
}
