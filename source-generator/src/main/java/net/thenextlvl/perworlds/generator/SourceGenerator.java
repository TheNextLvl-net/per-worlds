package net.thenextlvl.perworlds.generator;

import net.thenextlvl.perworlds.generator.adapter.GroupSettingsAdapterGenerator;

import java.io.IOException;
import java.nio.file.Paths;

public final class SourceGenerator {
    public static void main(final String[] args) throws IOException {
        final var output = Paths.get(args[0]);
        new GroupSettingsAdapterGenerator().writeToFile(output);
    }
}
