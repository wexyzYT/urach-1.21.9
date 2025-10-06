package com.wexyz.urach.enchantment;

import com.wexyz.urach.Urach;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

public final class UrachEnchantmentFilters {
    private UrachEnchantmentFilters() {
    }

    public static boolean isAllowed(RegistryEntry<Enchantment> enchantmentEntry) {
        return enchantmentEntry.getKey()
                .map(RegistryKey::getValue)
                .map(identifier -> Urach.MOD_ID.equals(identifier.getNamespace()))
                .orElse(false);
    }
}
