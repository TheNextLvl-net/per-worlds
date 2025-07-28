package net.thenextlvl.perworlds.generator;

import net.thenextlvl.perworlds.generator.adapter.GroupSettingsAdapterGenerator;

import java.io.IOException;
import java.nio.file.Paths;

public final class SourceGenerator {
    public static void main(String[] args) throws IOException {
        var output = Paths.get(args[0]);
        new GroupSettingsAdapterGenerator().writeToFile(output);
    }
}
