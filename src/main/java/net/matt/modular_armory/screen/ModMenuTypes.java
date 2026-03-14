package net.matt.modular_armory.screen;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.screen.custom.AugmentTableMenu;
import net.matt.modular_armory.screen.custom.ExposerMenu;
import net.matt.modular_armory.screen.custom.ModularAssemblerMenu;
import net.matt.modular_armory.screen.custom.RepairTableMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, MattsModularArmory.MOD_ID);


    public static final RegistryObject<MenuType<AugmentTableMenu>> AUGMENT_TABLE_MENU = MENUS.register("augument_table_menu", () -> IForgeMenuType.create(AugmentTableMenu::new));
    public static final RegistryObject<MenuType<ExposerMenu>> EXPOSER_MENU = MENUS.register("exposer_menu", () -> IForgeMenuType.create(ExposerMenu::new));
    public static final RegistryObject<MenuType<RepairTableMenu>> REPAIR_TABLE_MENU = MENUS.register("repair_table_menu", () -> IForgeMenuType.create(RepairTableMenu::new));
    public static final RegistryObject<MenuType<ModularAssemblerMenu>> MODULAR_ASSEMBLER_MENU = MENUS.register("modular_assembler_menu", () -> IForgeMenuType.create(ModularAssemblerMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
