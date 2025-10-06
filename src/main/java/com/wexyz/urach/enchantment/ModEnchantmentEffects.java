package com.wexyz.urach.enchantment;

import com.mojang.serialization.MapCodec;
import com.wexyz.urach.Urach;
import com.wexyz.urach.enchantment.custom.ThermoabsorptionEnchantmentEffect;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEnchantmentEffects {
    public static final MapCodec<? extends EnchantmentEntityEffect> THERMOABSORPTION = registerEntityEffect("thermoabsorption", ThermoabsorptionEnchantmentEffect.CODEC);



    private static MapCodec<? extends EnchantmentEntityEffect> registerEntityEffect(String name, MapCodec<? extends EnchantmentEntityEffect> codec) {
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.of(Urach.MOD_ID, name), codec);
    };



    public static void registerEnchantmentEffects() {
        Urach.LOGGER.info("enkants");
    }
}
