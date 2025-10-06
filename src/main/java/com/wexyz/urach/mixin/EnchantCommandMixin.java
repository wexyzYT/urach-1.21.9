package com.wexyz.urach.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.wexyz.urach.enchantment.UrachEnchantmentFilters;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.EnchantCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(EnchantCommand.class)
public abstract class EnchantCommandMixin {
    @Shadow private static SimpleCommandExceptionType FAILED_EXCEPTION;

    @Inject(method = "execute", at = @At("HEAD"), cancellable = true)
    private static void urach$restrictEnchantCommand(ServerCommandSource source, Collection<? extends Entity> targets, RegistryEntry<Enchantment> enchantment, int level, CallbackInfoReturnable<Integer> cir) throws CommandSyntaxException {
        if (!UrachEnchantmentFilters.isAllowed(enchantment)) {
            throw FAILED_EXCEPTION.create();
        }
    }
}
