package net.thenextlvl.perworlds.model.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.jspecify.annotations.NullMarked;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

@NullMarked
public final class GsonFile<R> {
    private static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private final Path file;
    private final R defaultRoot;

    private R root;
    private boolean loaded;

    public GsonFile(Path file, R root) {
        this.defaultRoot = root;
        this.file = file;
        this.root = root;
    }

    public R getRoot() {
        if (loaded) return root;
        loaded = true;
        return root = load();
    }

    public GsonFile<R> saveIfAbsent(FileAttribute<?>... attributes) {
        return Files.isRegularFile(file) ? this : save(attributes);
    }

    private R load() {
        if (!Files.isRegularFile(file)) return getRoot();
        try (var reader = new JsonReader(new InputStreamReader(
                Files.newInputStream(file, READ),
                StandardCharsets.UTF_8
        ))) {
            R root = GSON.fromJson(reader, this.root.getClass());
            return root != null ? root : defaultRoot;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GsonFile<R> save(FileAttribute<?>... attributes) {
        try {
            var root = getRoot();
            Files.createDirectories(file.getParent(), attributes);
            try (var writer = new BufferedWriter(new OutputStreamWriter(
                    Files.newOutputStream(file, WRITE, CREATE, TRUNCATE_EXISTING),
                    StandardCharsets.UTF_8
            ))) {
                GSON.toJson(root, root.getClass(), writer);
                return this;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
