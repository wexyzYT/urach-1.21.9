package com.wexyz.urach;

import com.wexyz.urach.datagen.*;
import com.wexyz.urach.enchantment.ModEnchantments;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class UrachDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModBlockTagProvider::new);
        pack.addProvider(ModItemTagProvider::new);
        pack.addProvider(ModRegistryDataGenerator::new);
        pack.addProvider(ModModelProvider::new);
        pack.addProvider(
                (net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack.Factory<DataProvider>)
                        ((FabricDataOutput out) -> new ItemBindingProvider(out))
        );

    }

    @Override
    public void buildRegistry (RegistryBuilder registryBuilder){
        registryBuilder.addRegistry(RegistryKeys.ENCHANTMENT, ModEnchantments::bootstrap);
    }
}
