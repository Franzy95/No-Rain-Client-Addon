package com.franzy.norainclient.mixin;

import net.minecraft.src.GuiInventory;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.Gui;
import net.minecraft.src.Minecraft;
import net.minecraft.src.World;
import net.minecraft.src.TextureManager;
import net.minecraft.src.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(GuiInventory.class)
public abstract class GuiInventoryMixin {

    @Inject(method = "drawGuiContainerBackgroundLayer", at = @At("TAIL"))
    private void drawWeatherIcon(float partialTicks, int mouseX, int mouseY, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.theWorld == null) return;

        System.out.println("Drawing weather icon");

        World world = mc.theWorld;
        ResourceLocation texture;

        if (world.isThundering()) {
            texture = new ResourceLocation("norainclient", "textures/gui/weather_thunder.png");
        } else if (world.isRaining()) {
            texture = new ResourceLocation("norainclient", "textures/gui/weather_rain.png");
        } else {
            texture = new ResourceLocation("norainclient", "textures/gui/weather_sun.png");
        }

        TextureManager manager = mc.renderEngine;
        manager.bindTexture(texture);

        GuiContainer container = (GuiContainer)(Object)this;

        int guiLeft = getInt(container, "field_74198_m");
        int guiTop = getInt(container, "field_74197_n");
        int xSize = getInt(container, "field_74194_b");

        int iconSize = 16;
        int x = guiLeft + xSize - iconSize - 4;
        int y = guiTop + 4;

        ((Gui)(Object)this).drawTexturedModalRect(x, y, 0, 0, iconSize, iconSize);
    }

    private int getInt(Object obj, String name) {
        try {
            Field f = obj.getClass().getSuperclass().getDeclaredField(name);
            f.setAccessible(true);
            return f.getInt(obj);
        } catch (Exception e) {
            return 0;
        }
    }
}








