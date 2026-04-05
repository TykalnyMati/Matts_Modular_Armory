package net.matt.modular_armory.datagen;

import net.matt.modular_armory.MattsModularArmory;
import net.matt.modular_armory.items.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.LinkedHashMap;

public class ModItemModelProvider extends ItemModelProvider {
    private static LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();
    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
    }

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MattsModularArmory.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.MODULAR_CLAY.get());
        basicItem(ModItems.BASIC_MODULAR_PLATE.get());
        basicItem(ModItems.MODULAR_CLAY_EXTRACTOR.get());
        basicItem(ModItems.ADVANCED_MODULAR_PLATE.get());
        basicItem(ModItems.FLESH_MEAL.get());
        basicItem(ModItems.ADAPTIVE_FLESH.get());
        basicItem(ModItems.ADAPTIVE_FLESH_PLATE.get());
        basicItem(ModItems.BIORITE_PLATE.get());
        basicItem(ModItems.EXPOSER_SLOT_PLUG.get());

        // Smithing Template Upgrades
        basicItem(ModItems.ADVANCED_MODULAR_CLAY_UPGRADE_SMITHING_TEMPLATE.get());
        basicItem(ModItems.ADAPTIVE_FLESH_UPGRADE_SMITHING_TEMPLATE.get());
        basicItem(ModItems.BIORITE_UPGRADE_SMITHING_TEMPLATE.get());

        // Tier 0 modular items
        handHeldItem(ModItems.MODULAR_CLAY_PICKAXE);
        handHeldItem(ModItems.MODULAR_CLAY_AXE);
        handHeldItem(ModItems.MODULAR_CLAY_SHOVEL);
        handHeldItem(ModItems.MODULAR_CLAY_HOE);
        handHeldItem(ModItems.MODULAR_CLAY_SWORD);
        trimmedArmorItem(ModItems.MODULAR_CLAY_HELMET);
        trimmedArmorItem(ModItems.MODULAR_CLAY_CHESTPLATE);
        trimmedArmorItem(ModItems.MODULAR_CLAY_LEGGINGS);
        trimmedArmorItem(ModItems.MODULAR_CLAY_BOOTS);

        // Tier 1 modular items
        handHeldItem(ModItems.ADVANCED_MODULAR_CLAY_PICKAXE);
        handHeldItem(ModItems.ADVANCED_MODULAR_CLAY_AXE);
        handHeldItem(ModItems.ADVANCED_MODULAR_CLAY_SHOVEL);
        handHeldItem(ModItems.ADVANCED_MODULAR_CLAY_HOE);
        handHeldItem(ModItems.ADVANCED_MODULAR_CLAY_SWORD);
        trimmedArmorItem(ModItems.ADVANCED_MODULAR_CLAY_HELMET);
        trimmedArmorItem(ModItems.ADVANCED_MODULAR_CLAY_CHESTPLATE);
        trimmedArmorItem(ModItems.ADVANCED_MODULAR_CLAY_LEGGINGS);
        trimmedArmorItem(ModItems.ADVANCED_MODULAR_CLAY_BOOTS);

        // Tier 2 modular items
        handHeldItem(ModItems.ADAPTIVE_FLESH_PICKAXE);
        handHeldItem(ModItems.ADAPTIVE_FLESH_AXE);
        handHeldItem(ModItems.ADAPTIVE_FLESH_SHOVEL);
        handHeldItem(ModItems.ADAPTIVE_FLESH_HOE);
        handHeldItem(ModItems.ADAPTIVE_FLESH_SWORD);

        // Tier 3 modular items
        handHeldItem(ModItems.BIORITE_PICKAXE);
        handHeldItem(ModItems.BIORITE_AXE);
        handHeldItem(ModItems.BIORITE_SHOVEL);
        handHeldItem(ModItems.BIORITE_HOE);
        handHeldItem(ModItems.BIORITE_SWORD);

        // Augments
        basicItem(ModItems.UNBREAKING_AUGMENT.get());
        basicItem(ModItems.MENDING_AUGMENT.get());

        basicItem(ModItems.EFFICIENCY_AUGMENT.get());
        basicItem(ModItems.SILK_TOUCH_AUGMENT.get());
        basicItem(ModItems.FORTUNE_AUGMENT.get());

        basicItem(ModItems.SHARPNESS_AUGMENT.get());
        basicItem(ModItems.SMITE_AUGMENT.get());
        basicItem(ModItems.BANE_OF_ARTHROPODS_AUGMENT.get());
        basicItem(ModItems.IMPALING_AUGMENT.get());
        basicItem(ModItems.LOOTING_AUGMENT.get());
        basicItem(ModItems.KNOCKBACK_AUGMENT.get());
        basicItem(ModItems.FIRE_ASPECT_AUGMENT.get());

        basicItem(ModItems.PROTECTION_AUGMENT.get());

        // Custom Augments
        basicItem(ModItems.AUTO_SMELT_AUGMENT.get());
        basicItem(ModItems.DIAMOND_SHARPENED_AUGMENT.get());
        basicItem(ModItems.MODULAR_CLAY_EXTRACTOR_AUGMENT.get());
        basicItem(ModItems.SCAFFOLDING_AUGMENT.get());
        basicItem(ModItems.ILLUMINATOR_AUGMENT.get());
        basicItem(ModItems.MAGNET_AUGMENT.get());
    }

    private ItemModelBuilder handHeldItem(DeferredHolder<Item, ? extends Item> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/handheld")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "item/" + item.getId().getPath()));
    }

    // Shoutout to El_Redstoniano for making this
    private void trimmedArmorItem(DeferredHolder<Item, Item> itemDeferredItem) {
        final String MOD_ID = MattsModularArmory.MOD_ID; // Change this to your mod id

        if(itemDeferredItem.get() instanceof ArmorItem armorItem) {
            trimMaterials.forEach((trimMaterial, value) -> {
                float trimValue = value;

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                String armorItemPath = armorItem.toString();
                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation armorItemResLoc = ResourceLocation.parse(armorItemPath);
                ResourceLocation trimResLoc = ResourceLocation.parse(trimPath); // minecraft namespace
                ResourceLocation trimNameResLoc = ResourceLocation.parse(currentTrimName);

                // This is used for making the ExistingFileHelper acknowledge that this texture exist, so this will
                // avoid an IllegalArgumentException
                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                // Trimmed armorItem files
                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc.getNamespace() + ":item/" + armorItemResLoc.getPath())
                        .texture("layer1", trimResLoc);

                // Non-trimmed armorItem file (normal variant)
                this.withExistingParent(itemDeferredItem.getId().getPath(),
                                mcLoc("item/generated"))
                        .override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResLoc.getNamespace()  + ":item/" + trimNameResLoc.getPath()))
                        .predicate(mcLoc("trim_type"), trimValue).end()
                        .texture("layer0",
                                ResourceLocation.fromNamespaceAndPath(MOD_ID,
                                        "item/" + itemDeferredItem.getId().getPath()));
            });
        }
    }
}
