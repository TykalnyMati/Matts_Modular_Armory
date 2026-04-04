package net.matt.modular_armory.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.block.ModBlocks;
import net.matt.modular_armory.recipe.ModularAssemblerRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModularAssemblerRecipeCategory implements IRecipeCategory<ModularAssemblerRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "modular_assembler");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "textures/gui/modular_assembler/modular_assembler_jei_gui.png");

    public static final RecipeType<ModularAssemblerRecipe> MODULAR_ASSEMBLER_RECIPE_RECIPE_TYPE = new RecipeType<>(UID, ModularAssemblerRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public ModularAssemblerRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 49, 25, 80, 34);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.MODULAR_ASSEMBLER.get()));
    }

    @Override
    public RecipeType<ModularAssemblerRecipe> getRecipeType() {
        return MODULAR_ASSEMBLER_RECIPE_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.mattsmodulararmory.modular_assembler");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public @Nullable IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, ModularAssemblerRecipe modularAssemblerRecipe, IFocusGroup iFocusGroup) {
        int amount = modularAssemblerRecipe.amount();

        List<ItemStack> inputsWithAmount = java.util.Arrays.stream(modularAssemblerRecipe.inputItem().getItems())
                .map(stack -> {
                    ItemStack copy = stack.copy();
                    copy.setCount(amount);
                    return copy;
                })
                .toList();

        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 5, 9).addIngredients(VanillaTypes.ITEM_STACK, inputsWithAmount);

        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 55, 9).addItemStack(modularAssemblerRecipe.getResultItem(null));
    }
}
