package com.wexyz.urach;

import com.wexyz.urach.datagen.ModBlockTagProvider;
import com.wexyz.urach.datagen.ModItemTagProvider;
import com.wexyz.urach.datagen.ModRegistryDataGenerator;
import com.wexyz.urach.enchantment.ModEnchantments;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class UrachDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModBlockTagProvider::new);
        pack.addProvider(ModItemTagProvider::new);
        pack.addProvider(ModItemTagProvider::new);
        pack.addProvider(ModRegistryDataGenerator::new);
	}

    @Override
    public void buildRegistry (RegistryBuilder registryBuilder){
        registryBuilder.addRegistry(RegistryKeys.ENCHANTMENT, ModEnchantments::bootstrap);
    }
}
