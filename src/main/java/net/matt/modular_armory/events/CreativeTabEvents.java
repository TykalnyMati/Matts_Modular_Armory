package net.matt.modular_armory.events;

import net.matt.modular_armory.MattsModularArmory;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@EventBusSubscriber(modid = MattsModularArmory.MOD_ID)
public class CreativeTabEvents {
    @SubscribeEvent
    public static void onBuildCreativeModeTabContentsEvent(BuildCreativeModeTabContentsEvent event) {
        event.getParameters().holders().lookup(Registries.ENCHANTMENT)
            .flatMap(registry -> registry.get(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "vampire"))))
            .ifPresent(holder -> {
                ItemStack vampireBook = new ItemStack(Items.ENCHANTED_BOOK);

                ItemEnchantments.Mutable mutableEnchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
                mutableEnchantments.set(holder, 1);

                vampireBook.set(DataComponents.STORED_ENCHANTMENTS, mutableEnchantments.toImmutable());

                event.remove(vampireBook, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        });
    }
}
