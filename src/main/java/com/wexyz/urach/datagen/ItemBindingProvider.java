package com.wexyz.urach.datagen;

import com.google.gson.*;
import com.wexyz.urach.Urach;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ItemBindingProvider implements DataProvider {
    public enum DuplicatePolicy {
        SKIP_IF_IDENTICAL,   // skip if same content; error if different
        OVERWRITE,           // always overwrite
        ERROR_ON_DIFFERENT   // error if different content (even if on disk)
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final FabricDataOutput output;
    private final DuplicatePolicy policy;

    // Track duplicates within the *same run*
    private final Map<Path, String> plannedWrites = new HashMap<>();

    public ItemBindingProvider(FabricDataOutput output) {
        this(output, DuplicatePolicy.SKIP_IF_IDENTICAL); // default policy
    }

    public ItemBindingProvider(FabricDataOutput output, DuplicatePolicy policy) {
        this.output = output;
        this.policy = policy;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (Identifier id : Registries.ITEM.getIds()) {
            if (id.getNamespace().equals(Urach.MOD_ID)) {
                futures.add(saveBinding(writer, id));
            }
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    private CompletableFuture<?> saveBinding(DataWriter writer, Identifier itemId) {
        // Build JSON
        JsonObject root = new JsonObject();
        JsonObject model = new JsonObject();
        model.addProperty("type", "minecraft:model");
        model.addProperty("model", itemId.getNamespace() + ":item/" + itemId.getPath());
        root.add("model", model);

        String jsonText = GSON.toJson(root);

        // Resolve path: assets/<modid>/items/<name>.json
        Path path = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "items")
                .resolveJson(itemId);

        // 1) Handle duplicates within the same run
        String previousPlanned = plannedWrites.putIfAbsent(path, jsonText);
        if (previousPlanned != null) {
            // Another write already scheduled this run
            if (previousPlanned.equals(jsonText)) {
                // Identical → no-op
                return CompletableFuture.completedFuture(null);
            } else {
                switch (policy) {
                    case OVERWRITE -> {
                        // Update planned content, then write
                        plannedWrites.put(path, jsonText);
                    }
                    case SKIP_IF_IDENTICAL, ERROR_ON_DIFFERENT -> {
                        return CompletableFuture.failedFuture(new IllegalStateException(
                                "Duplicate generation with different content in same run: " + path));
                    }
                }
            }
        }

        // 2) Handle duplicates against what’s already on disk (from previous runs)
        if (Files.exists(path)) {
            try {
                String existing = Files.readString(path, StandardCharsets.UTF_8);
                boolean identical = normalize(existing).equals(normalize(jsonText));
                switch (policy) {
                    case SKIP_IF_IDENTICAL -> {
                        if (identical) {
                            // No need to write
                            return CompletableFuture.completedFuture(null);
                        } else {
                            return CompletableFuture.failedFuture(new IllegalStateException(
                                    "Existing file differs (policy SKIP_IF_IDENTICAL). Path: " + path));
                        }
                    }
                    case ERROR_ON_DIFFERENT -> {
                        if (!identical) {
                            return CompletableFuture.failedFuture(new IllegalStateException(
                                    "Existing file differs (policy ERROR_ON_DIFFERENT). Path: " + path));
                        } else {
                            return CompletableFuture.completedFuture(null);
                        }
                    }
                    case OVERWRITE -> {
                        // fall through to write
                    }
                }
            } catch (IOException e) {
                // If we can't read, honor policy: OVERWRITE will proceed; others will error
                if (policy != DuplicatePolicy.OVERWRITE) {
                    return CompletableFuture.failedFuture(new IllegalStateException(
                            "Failed to read existing file for duplicate check: " + path, e));
                }
            }
        }

        // Write (returns a future)
        return DataProvider.writeToPath(writer, JsonParser.parseString(jsonText), path);
    }

    // Normalize JSON to avoid false diffs from whitespace/formatting
    private static String normalize(String text) {
        try {
            JsonElement el = JsonParser.parseString(text);
            return GSON.toJson(el);
        } catch (JsonSyntaxException e) {
            // If not valid JSON, just return as-is so difference is detected
            return text;
        }
    }

    @Override
    public String getName() {
        return "Urach Item→Model Bindings (assets/<modid>/items) with duplicate handling";
    }
}
