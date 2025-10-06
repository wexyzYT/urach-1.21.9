package com.wexyz.urach.mixin;

import com.wexyz.urach.enchantment.UrachEnchantmentFilters;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Stream;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
    @Inject(method = "getPossibleEntries", at = @At("RETURN"), cancellable = true)
    private static void urach$filterPossibleEntries(int level, ItemStack stack, Stream<RegistryEntry<Enchantment>> possibleEnchantments, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
        List<EnchantmentLevelEntry> entries = cir.getReturnValue();
        if (entries == null || entries.isEmpty()) {
            return;
        }

        List<EnchantmentLevelEntry> filtered = entries.stream()
                .filter(entry -> UrachEnchantmentFilters.isAllowed(entry.enchantment()))
                .toList();

        if (filtered.size() != entries.size()) {
            cir.setReturnValue(filtered);
        }
    }

    @Inject(method = "generateEnchantments", at = @At("RETURN"), cancellable = true)
    private static void urach$filterGeneratedEnchantments(Random random, ItemStack stack, int level, Stream<RegistryEntry<Enchantment>> possibleEnchantments, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
        List<EnchantmentLevelEntry> entries = cir.getReturnValue();
        if (entries == null || entries.isEmpty()) {
            return;
        }

        List<EnchantmentLevelEntry> filtered = entries.stream()
                .filter(entry -> UrachEnchantmentFilters.isAllowed(entry.enchantment()))
                .toList();

        if (filtered.size() != entries.size()) {
            cir.setReturnValue(filtered);
        }
    }
}
