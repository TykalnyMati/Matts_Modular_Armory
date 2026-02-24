package net.matt.modular_armory.recipe;

import net.matt.modular_armory.MattsModularArmory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MattsModularArmory.MOD_ID);

    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, MattsModularArmory.MOD_ID);

    public static final RegistryObject<RecipeSerializer<ExposerRecipe>> EXPOSER_SERIALIZER = SERIALIZERS.register("exposer", ExposerRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<ExposerRecipe>> EXPOSER_TYPE = TYPES.register("exposer", () -> new RecipeType<ExposerRecipe>() {
        @Override
        public String toString() {
            return "exposer";
        }
    });

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}
