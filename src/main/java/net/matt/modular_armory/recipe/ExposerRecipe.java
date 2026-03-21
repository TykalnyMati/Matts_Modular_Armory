package net.matt.modular_armory.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public record ExposerRecipe(Ingredient inputItem, List<BlockState> surroundings, ItemStack output, Integer time) implements Recipe<ExposerRecipeInput> {
    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(inputItem);
        return list;
    }

    public int getTime() {
        return time;
    }

    public NonNullList<ItemStack> getSurroundings() {
        NonNullList<ItemStack> list = NonNullList.create();
        for (BlockState adjacent : surroundings) {
            if (adjacent.getBlock() == Blocks.WATER) {
                ItemStack waterBucket = new ItemStack(Items.WATER_BUCKET);

                waterBucket.set(DataComponents.LORE, new ItemLore(List.of(
                        Component.translatable("container.mattsmodulararmory.exposer.liquid").withStyle(ChatFormatting.BLUE)
                )));

                list.add(waterBucket);
            } else if (adjacent.getBlock() == Blocks.LAVA) {
                ItemStack lavaBucket = new ItemStack(Items.LAVA_BUCKET);

                lavaBucket.set(DataComponents.LORE, new ItemLore(List.of(
                        Component.translatable("container.mattsmodulararmory.exposer.liquid").withStyle(ChatFormatting.BLUE)
                )));

                list.add(lavaBucket);
            } else if (adjacent.getBlock() == Blocks.WATER_CAULDRON) {
                ItemStack cauldron = new ItemStack(Items.CAULDRON);

                cauldron.set(DataComponents.LORE, new ItemLore(List.of(
                        Component.translatable("container.mattsmodulararmory.exposer.water_cauldron").withStyle(ChatFormatting.BLUE)
                )));

                list.add(cauldron);
            } else {
                list.add(new ItemStack(adjacent.getBlock()));
            }
        }
        return list;
    }

    @Override
    public boolean matches(ExposerRecipeInput pInput, Level pLevel) {
        if(pLevel.isClientSide()) {
            return false;
        }

        return inputItem.test(pInput.getItem(0));
    }

    @Override
    public ItemStack assemble(ExposerRecipeInput pInput, HolderLookup.Provider pRegistries) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.EXPOSER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.EXPOSER_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<ExposerRecipe> {
        public static final MapCodec<ExposerRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(ExposerRecipe::inputItem),
            BlockState.CODEC.listOf().fieldOf("surroundings").flatXmap(
                        surroundings -> {
                            if (surroundings.isEmpty()) {
                                return DataResult.error(() -> "surroundings list cannot be empty");
                            }
                            return DataResult.success(List.copyOf(surroundings));
                        },
                        DataResult::success
                        ).forGetter(ExposerRecipe::surroundings),
            ItemStack.CODEC.fieldOf("result").forGetter(ExposerRecipe::output),
            Codec.INT.fieldOf("time").forGetter(ExposerRecipe::time)
        ).apply(inst, ExposerRecipe::new));

        private static final StreamCodec<ByteBuf, BlockState> BLOCKSTATE_STREAM_CODEC =
                StreamCodec.of(
                        (buf, state) -> buf.writeInt(net.minecraft.world.level.block.Block.getId(state)),
                        buf -> net.minecraft.world.level.block.Block.stateById(buf.readInt())
                );

        private static final StreamCodec<ByteBuf, List<BlockState>> BLOCKSTATE_LIST_STREAM_CODEC =
                StreamCodec.of(
                        (buf, list) -> {
                            buf.writeInt(list.size());
                            for (BlockState state : list) {
                                BLOCKSTATE_STREAM_CODEC.encode(buf, state);
                            }
                        },
                        buf -> {
                            int size = buf.readInt();
                            List<BlockState> list = new java.util.ArrayList<>(size);

                            for (int i = 0; i < size; i++) {
                                list.add(BLOCKSTATE_STREAM_CODEC.decode(buf));
                            }

                            return List.copyOf(list); // immutable like vanilla
                        }
                );

        public static final StreamCodec<RegistryFriendlyByteBuf, ExposerRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, ExposerRecipe::inputItem,
                BLOCKSTATE_LIST_STREAM_CODEC.mapStream(buf -> buf), ExposerRecipe::surroundings,
                ItemStack.STREAM_CODEC, ExposerRecipe::output,
                ByteBufCodecs.INT, ExposerRecipe::time,
                ExposerRecipe::new
        );

        @Override
        public MapCodec<ExposerRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ExposerRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
