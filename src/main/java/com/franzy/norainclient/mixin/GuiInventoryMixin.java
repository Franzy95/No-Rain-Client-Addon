package com.franzy.norainclient.mixin;

import net.minecraft.src.*;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(GuiInventory.class)
public abstract class GuiInventoryMixin extends InventoryEffectRenderer {
    @Unique
    private static final ResourceLocation INDICATOR = new ResourceLocation("norainclient", "textures/gui/indicator.png");

    public GuiInventoryMixin(Container par1Container) {
        super(par1Container);
    }

    @Inject(method = "drawGuiContainerBackgroundLayer", at = @At("TAIL"))
    private void drawWeatherIcon(float partialTicks, int mouseX, int mouseY, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.theWorld == null) return;

        World world = mc.theWorld;
        int textureU = 0;

        if (world.isThundering()) {
            textureU = 32;
        } else if (world.isRaining()) {
            textureU = 16;
        }

        int guiLeft = this.getGuiLeft();
        int guiTop = this.getGuiTop();
        int xSize = this.getxSize();

        int iconSize = 16;
        int x = guiLeft + xSize - iconSize - 4;
        int y = guiTop + 4;

        TextureManager manager = mc.renderEngine;
        manager.bindTexture(INDICATOR);
        this.drawTexturedModalRect(x, y, textureU, 0, iconSize, iconSize);
    }
}








