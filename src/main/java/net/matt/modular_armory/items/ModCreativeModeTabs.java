package net.matt.modular_armory.items;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MattsModularArmory.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MATTS_MODULAR_ARMORY_TAB = CREATIVE_MODE_TABS.register("matts_modular_armory_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.MODULAR_CLAY.get()))
                    .title(Component.translatable("creativetab.mattsmodulararmory.mattsmodulararmory"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.MODULAR_CLAY.get());
                        output.accept(ModItems.BASIC_MODULAR_PLATE.get());
                        output.accept(ModBlocks.MODULAR_CLAY_BLOCK.get());
                        output.accept(ModItems.MODULAR_CLAY_EXTRACTOR.get());
                        output.accept(ModItems.FLESH_MEAL.get());
                        output.accept(ModBlocks.ADAPTIVE_FLESH_BLOCK.get());
                        output.accept(ModBlocks.AUGMENT_TABLE.get());
                        output.accept(ModBlocks.EXPOSER.get());
                        output.accept(ModItems.EXPOSER_SLOT_PLUG.get());

                        output.accept(ModItems.MODULAR_CLAY_PICKAXE.get());
                        output.accept(ModItems.MODULAR_CLAY_AXE.get());
                        output.accept(ModItems.MODULAR_CLAY_SHOVEL.get());

                        output.accept(ModItems.EFFICIENCY_AUGMENT.get());
                        output.accept(ModItems.UNBREAKING_AUGMENT.get());
                        output.accept(ModItems.SILK_TOUCH_AUGMENT.get());
                        output.accept(ModItems.FORTUNE_AUGMENT.get());
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
