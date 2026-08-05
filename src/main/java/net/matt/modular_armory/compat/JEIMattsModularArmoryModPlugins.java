package net.matt.modular_armory.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.block.ModBlocks;
import net.matt.modular_armory.items.tools.helpers.ModularItemInterface;
import net.matt.modular_armory.network.SetGhostSlotPayload;
import net.matt.modular_armory.recipe.ExposerRecipe;
import net.matt.modular_armory.recipe.ModRecipes;
import net.matt.modular_armory.recipe.ModularAssemblerRecipe;
import net.matt.modular_armory.screen.custom.ExposerScreen;
import net.matt.modular_armory.screen.custom.FilterScreen;
import net.matt.modular_armory.screen.custom.helpers.GhostSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JEIMattsModularArmoryModPlugins implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ExposerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ModularAssemblerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (Minecraft.getInstance().getConnection() == null) {
            return; // Happens on title screen during JEI bootstrap
        }

        RecipeManager recipeManager = Minecraft.getInstance()
                .getConnection()
                .getRecipeManager();

        List<ExposerRecipe> exposerRecipes = recipeManager
                .getAllRecipesFor(ModRecipes.EXPOSER_TYPE.get())
                .stream()
                .map(RecipeHolder::value)
                .toList();

        List<ModularAssemblerRecipe> modularAssemblerRecipes = recipeManager
                .getAllRecipesFor(ModRecipes.MODULAR_ASSEMBLER_TYPE.get())
                .stream()
                .map(RecipeHolder::value)
                .toList();

        registration.addRecipes(ExposerRecipeCategory.EXPOSER_RECIPE_RECIPE_TYPE, exposerRecipes);
        registration.addRecipes(ModularAssemblerRecipeCategory.MODULAR_ASSEMBLER_RECIPE_RECIPE_TYPE, modularAssemblerRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ExposerScreen.class, 70, 30, 25, 20, ExposerRecipeCategory.EXPOSER_RECIPE_RECIPE_TYPE);

        // --- JEI Ghost Ingredient Handler for FilterScreen ---
        registration.addGhostIngredientHandler(FilterScreen.class, new IGhostIngredientHandler<FilterScreen>() {
            @Override
            public <I> List<Target<I>> getTargetsTyped(FilterScreen gui, ITypedIngredient<I> ingredient, boolean doExecute) {
                List<Target<I>> targets = new ArrayList<>();

                // Extract ItemStack from JEI Typed Ingredient
                ItemStack draggedStack = ingredient.getItemStack()
                        .orElseGet(() -> ingredient.getIngredient(VanillaTypes.ITEM_STACK).orElse(ItemStack.EMPTY));

                if (!draggedStack.isEmpty()) {
                    for (Slot slot : gui.getMenu().slots) {
                        if (slot instanceof GhostSlot ghostSlot) {
                            int slotX = gui.getGuiLeft() + slot.x;
                            int slotY = gui.getGuiTop() + slot.y;
                            Rect2i area = new Rect2i(slotX, slotY, 16, 16);

                            targets.add(new Target<I>() {
                                @Override
                                public Rect2i getArea() {
                                    return area;
                                }

                                @Override
                                public void accept(I ingredient) {
                                    ghostSlot.handleGhostClick(draggedStack);
                                    PacketDistributor.sendToServer(new SetGhostSlotPayload(slot.index, draggedStack));
                                }
                            });
                        }
                    }
                }
                return targets;
            }

            @Override
            public void onComplete() {
            }
        });
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.EXPOSER.get().asItem()), ExposerRecipeCategory.EXPOSER_RECIPE_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.MODULAR_ASSEMBLER.get().asItem()), ModularAssemblerRecipeCategory.MODULAR_ASSEMBLER_RECIPE_RECIPE_TYPE);
    }

    // Hide grindstone and anvil recipes for modular items
    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        IRecipeManager recipeManager = jeiRuntime.getRecipeManager();

        List<mezz.jei.api.recipe.vanilla.IJeiGrindstoneRecipe> grindstoneToHide = recipeManager.createRecipeLookup(RecipeTypes.GRINDSTONE)
                .get()
                .filter(recipe -> {
                    for (ItemStack slotIngredients : recipe.getTopInputs()) {
                        if (slotIngredients.getItem() instanceof ModularItemInterface) {
                                return true;
                        }
                    }
                    return false;
                })
                .toList();

        if (!grindstoneToHide.isEmpty()) {
            recipeManager.hideRecipes(RecipeTypes.GRINDSTONE, grindstoneToHide);
        }
    }
}
