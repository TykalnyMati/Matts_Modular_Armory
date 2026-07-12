package net.matt.modular_armory.screen;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.screen.custom.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, MattsModularArmory.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<AugmentTableMenu>> AUGMENT_TABLE_MENU = MENUS.register("augument_table_menu", () -> IMenuTypeExtension.create(AugmentTableMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<ExposerMenu>> EXPOSER_MENU = MENUS.register("exposer_menu", () -> IMenuTypeExtension.create(ExposerMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<RepairTableMenu>> REPAIR_TABLE_MENU = MENUS.register("repair_table_menu", () -> IMenuTypeExtension.create(RepairTableMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<ModularAssemblerMenu>> MODULAR_ASSEMBLER_MENU = MENUS.register("modular_assembler_menu", () -> IMenuTypeExtension.create(ModularAssemblerMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<ModularMinerMenu>> MODULAR_MINER_MENU = MENUS.register("modular_miner_menu", () -> IMenuTypeExtension.create(ModularMinerMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
