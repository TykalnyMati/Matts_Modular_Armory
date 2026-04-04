package net.matt.modular_armory.screen.custom;

import net.matt.modular_armory.MattsModularArmory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;

public class ModularAssemblerScreen extends AbstractContainerScreen<ModularAssemblerMenu> {
    private static final ResourceLocation SCROLLER_SPRITE = ResourceLocation.withDefaultNamespace("container/stonecutter/scroller");
    private static final ResourceLocation SCROLLER_DISABLED_SPRITE = ResourceLocation.withDefaultNamespace("container/stonecutter/scroller_disabled");
    private static final ResourceLocation RECIPE_SELECTED_SPRITE = ResourceLocation.withDefaultNamespace("container/stonecutter/recipe_selected");
    private static final ResourceLocation RECIPE_HIGHLIGHTED_SPRITE = ResourceLocation.withDefaultNamespace("container/stonecutter/recipe_highlighted");
    private static final ResourceLocation RECIPE_SPRITE = ResourceLocation.withDefaultNamespace("container/stonecutter/recipe");
    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(MattsModularArmory.MOD_ID, "textures/gui/modular_assembler/modular_assembler_gui.png");

    private float scrollOffs;
    private boolean scrolling;
    private int startIndex;
    private boolean displayRecipes;

    public ModularAssemblerScreen(ModularAssemblerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        pMenu.registerUpdateListener(this::containerChanged);
        this.titleLabelY--;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = this.leftPos;
        int y = this.topPos;

        // Draw Background
        pGuiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        // Draw Scrollbar
        int scrollBarX = x + 119;
        int scrollBarY = y + 14;
        int k = (int)(41.0F * this.scrollOffs);
        ResourceLocation resourcelocation = this.isScrollBarActive() ? SCROLLER_SPRITE : SCROLLER_DISABLED_SPRITE;
        pGuiGraphics.blitSprite(resourcelocation, scrollBarX, scrollBarY + k, 12, 15);

        // Draw Recipe Buttons
        int recipeStartX = x + 52;
        int recipeStartY = y + 14;
        int l = this.startIndex + 12;
        this.renderButtons(pGuiGraphics, pMouseX, pMouseY, recipeStartX, recipeStartY, l);
        this.renderRecipes(pGuiGraphics, recipeStartX, recipeStartY, l);
    }

    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderTooltip(pGuiGraphics, pX, pY);
        if (this.displayRecipes) {
            int i = this.leftPos + 52;
            int j = this.topPos + 14;
            int k = this.startIndex + 12;
            List<ItemStack> list = this.menu.getAvailableTools();

            for (int l = this.startIndex; l < k && l < this.menu.getNumRecipes(); l++) {
                int i1 = l - this.startIndex;
                int j1 = i + i1 % 4 * 16;
                int k1 = j + i1 / 4 * 18 + 2;
                if (pX >= j1 && pX < j1 + 16 && pY >= k1 && pY < k1 + 18) {
                    ItemStack stack = list.get(l);
                    List<Component> tooltipLines = new ArrayList<>(stack.getTooltipLines(
                            Item.TooltipContext.of(this.minecraft.level),
                            this.minecraft.player,
                            this.minecraft.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL
                    ));

                    int cost = this.menu.getCostAtIndex(l);
                    int currentPlates = this.menu.inputSlot.getItem().getCount();
                    ChatFormatting color = (currentPlates >= cost) ? ChatFormatting.GOLD : ChatFormatting.RED;

                    Component costComponent = Component.translatable("container.mattsmodulararmory.modular_assembler_cost_tooltip")
                            .append(Component.literal(" " + cost))
                            .withStyle(color);

                    if (!tooltipLines.isEmpty()) {
                        tooltipLines.add(1, costComponent);
                    } else {
                        tooltipLines.add(costComponent);
                    }

                    pGuiGraphics.renderComponentTooltip(this.font, tooltipLines, pX, pY);
                }
            }
        }
    }

    private void renderButtons(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, int pX, int pY, int pLastIndex) {
        for (int i = this.startIndex; i < pLastIndex && i < this.menu.getNumRecipes(); ++i) {
            int j = i - this.startIndex;
            int btnX = pX + j % 4 * 16;
            int row = j / 4;
            int btnY = pY + row * 18 + 2;

            ResourceLocation resourcelocation;
            if (i == this.menu.getSelectedRecipeIndex()) {
                resourcelocation = RECIPE_SELECTED_SPRITE;
            } else if (pMouseX >= btnX && pMouseY >= btnY && pMouseX < btnX + 16 && pMouseY < btnY + 18) {
                resourcelocation = RECIPE_HIGHLIGHTED_SPRITE;
            } else {
                resourcelocation = RECIPE_SPRITE;
            }

            pGuiGraphics.blitSprite(resourcelocation, btnX, btnY - 1, 16, 18);
        }
    }

    private void renderRecipes(GuiGraphics pGuiGraphics, int pX, int pY, int pLastIndex) {
        List<ItemStack> list = this.menu.getAvailableTools();
        float scale = 0.9F;
        for (int i = this.startIndex; i < pLastIndex && i < list.size(); ++i) {
            int j = i - this.startIndex;
            int iconX = pX + j % 4 * 16;
            int row = j / 4;
            int iconY = pY + row * 18 + 1;

            pGuiGraphics.pose().pushPose();

            pGuiGraphics.pose().translate(iconX, iconY, 0);

            pGuiGraphics.pose().translate(8.0f, 8.0f, 0);
            pGuiGraphics.pose().scale(scale, scale, 1.0f);
            pGuiGraphics.pose().translate(-8.0f, -8.0f, 0);

            pGuiGraphics.renderItem(list.get(i), 0, 0);

            pGuiGraphics.pose().popPose();
        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        this.scrolling = false;
        if (this.displayRecipes) {
            int i = this.leftPos + 52;
            int j = this.topPos + 14;
            int k = this.startIndex + 12;

            for(int l = this.startIndex; l < k; ++l) {
                int m = l - this.startIndex;
                double d0 = pMouseX - (double)(i + m % 4 * 16);
                double d1 = pMouseY - (double)(j + m / 4 * 18);
                if (d0 >= 0.0D && d1 >= 0.0D && d0 < 16.0D && d1 < 18.0D && this.menu.clickMenuButton(this.minecraft.player, l)) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                    this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, l);
                    return true;
                }
            }

            i = this.leftPos + 119;
            j = this.topPos + 14;
            if (pMouseX >= (double)i && pMouseX < (double)(i + 12) && pMouseY >= (double)j && pMouseY < (double)(j + 54)) {
                this.scrolling = true;
            }
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (this.scrolling && this.isScrollBarActive()) {
            int i = this.topPos + 14;
            int j = i + 54;
            this.scrollOffs = ((float)pMouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            this.startIndex = (int)((double)(this.scrollOffs * (float)this.getOffscreenRows()) + 0.5D) * 4;
            return true;
        } else {
            return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        }
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pScrollX, double pScrollY) {
        if (this.isScrollBarActive()) {
            int i = this.getOffscreenRows();
            float f = (float)pScrollY / (float)i;
            this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
            this.startIndex = (int)((double)(this.scrollOffs * (float)i) + 0.5D) * 4;
        }
        return true;
    }

    private boolean isScrollBarActive() {
        return this.displayRecipes && this.menu.getNumRecipes() > 12;
    }

    protected int getOffscreenRows() {
        return (this.menu.getNumRecipes() + 4 - 1) / 4 - 3;
    }

    private void containerChanged() {
        this.displayRecipes = this.menu.hasInputItem();
        if (!this.displayRecipes) {
            this.scrollOffs = 0.0F;
            this.startIndex = 0;
        }
    }
}
