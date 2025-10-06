package com.wexyz.urach.mixin;

import com.wexyz.urach.enchantment.UrachEnchantmentFilters;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Mixin(RegistryEntryReferenceArgumentType.class)
public abstract class RegistryEntryReferenceArgumentTypeMixin {
    @Shadow private RegistryKey<?> registryRef;
    @Shadow private RegistryWrapper registryWrapper;

    @SuppressWarnings("unchecked")
    @Inject(method = "listSuggestions", at = @At("HEAD"), cancellable = true)
    private void urach$filterEnchantmentSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder, CallbackInfoReturnable<CompletableFuture<Suggestions>> cir) {
        if (!RegistryKeys.ENCHANTMENT.equals(this.registryRef)) {
            return;
        }

        Stream<RegistryEntry<Enchantment>> entries = (Stream<RegistryEntry<Enchantment>>) (Object) this.registryWrapper.streamEntries();
        CompletableFuture<Suggestions> suggestions = CommandSource.suggestIdentifiers(
                entries
                        .filter(UrachEnchantmentFilters::isAllowed)
                        .map(entry -> entry.getKey().orElseThrow().getValue()),
                builder
        );

        cir.setReturnValue(suggestions);
    }
}
