package net.matt.modular_armory.recipe;

import net.matt.modular_armory.MattsModularArmory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, MattsModularArmory.MOD_ID);

    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, MattsModularArmory.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ExposerRecipe>> EXPOSER_SERIALIZER = SERIALIZERS.register("exposer", ExposerRecipe.Serializer::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<ExposerRecipe>> EXPOSER_TYPE = TYPES.register("exposer", () -> new RecipeType<ExposerRecipe>() {
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
