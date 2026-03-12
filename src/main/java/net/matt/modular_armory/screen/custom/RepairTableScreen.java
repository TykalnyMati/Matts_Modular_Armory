package net.matt.modular_armory.screen.custom;

import net.matt.modular_armory.MattsModularArmory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class RepairTableScreen extends ItemCombinerScreen<RepairTableMenu> {
    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "textures/gui/repair_table/repair_table_gui.png");

    public RepairTableScreen(RepairTableMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, GUI_TEXTURE);

        this.titleLabelX = 44;
        this.titleLabelY = 15;
    }

    @Override
    protected void renderErrorIcon(GuiGraphics pGuiGraphics, int pX, int pY) {

    }
}
