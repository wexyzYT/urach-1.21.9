package com.wexyz.urach.mixin;

import com.wexyz.urach.enchantment.UrachEnchantmentFilters;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;

@Mixin(ItemGroups.class)
public abstract class ItemGroupsMixin {
    @Inject(method = "method_59969", at = @At("HEAD"), cancellable = true)
    private static void urach$filterAllLevelBooks(RegistryEntry.Reference enchantmentEntry, CallbackInfoReturnable<Stream> cir) {
        if (!UrachEnchantmentFilters.isAllowed(enchantmentEntry)) {
            cir.setReturnValue(Stream.empty());
        }
    }

    @Inject(method = "method_59972", at = @At("HEAD"), cancellable = true)
    private static void urach$filterMaxLevelBooks(RegistryEntry.Reference enchantmentEntry, CallbackInfoReturnable<ItemStack> cir) {
        if (!UrachEnchantmentFilters.isAllowed(enchantmentEntry)) {
            cir.setReturnValue(ItemStack.EMPTY);
        }
    }
}
