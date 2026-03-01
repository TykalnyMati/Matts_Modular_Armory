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
import net.matt.modular_armory.recipe.ExposerRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ExposerRecipeCategory implements IRecipeCategory<ExposerRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "exposer");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "textures/gui/exposer/exposer_gui.png");

    public static final RecipeType<ExposerRecipe> EXPOSER_RECIPE_RECIPE_TYPE = new RecipeType<>(UID, ExposerRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public ExposerRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 10, 10, 150, 60);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.EXPOSER.get()));
    }

    @Override
    public RecipeType<ExposerRecipe> getRecipeType() {
        return EXPOSER_RECIPE_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.mattsmodulararmory.exposer");
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
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, ExposerRecipe exposerRecipe, IFocusGroup iFocusGroup) {
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 44, 24).addIngredients(exposerRecipe.getIngredients().get(0));

        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 94, 24).addItemStack(exposerRecipe.getResultItem(null));
    }
}
