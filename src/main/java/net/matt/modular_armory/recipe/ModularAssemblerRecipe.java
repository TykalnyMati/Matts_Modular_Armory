package net.matt.modular_armory.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record ModularAssemblerRecipe(Ingredient inputItem, Integer amount, ItemStack output) implements Recipe<ModularAssemblerRecipeInput> {
    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(inputItem);
        return list;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean matches(ModularAssemblerRecipeInput pInput, Level pLevel) {
        if(pLevel.isClientSide()) {
            return false;
        }

        return inputItem.test(pInput.getItem(0));
    }

    @Override
    public ItemStack assemble(ModularAssemblerRecipeInput modularAssemblerRecipeInput, HolderLookup.Provider provider) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.MODULAR_ASSEMBLER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.MODULAR_ASSEMBLER_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<ModularAssemblerRecipe> {
        public static final MapCodec<ModularAssemblerRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(ModularAssemblerRecipe::inputItem),
                Codec.INT.fieldOf("amount").forGetter(ModularAssemblerRecipe::amount),
                ItemStack.CODEC.fieldOf("result").forGetter(ModularAssemblerRecipe::output)
        ).apply(inst, ModularAssemblerRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, ModularAssemblerRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, ModularAssemblerRecipe::inputItem,
                ByteBufCodecs.INT, ModularAssemblerRecipe::amount,
                ItemStack.STREAM_CODEC, ModularAssemblerRecipe::output,
                ModularAssemblerRecipe::new
        );

        @Override
        public MapCodec<ModularAssemblerRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ModularAssemblerRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
