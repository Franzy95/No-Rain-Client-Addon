package com.franzy.norainclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;

@Mixin(targets = "net.minecraft.src.GuiContainer")
public abstract class GuiInventoryMixin {
    @Unique
    private static final Object INDICATOR = createResourceLocation("norainclient", "textures/gui/indicator.png");

    private static Object createResourceLocation(String domain, String path) {
        try {
            Class<?> clazz = Class.forName("net.minecraft.src.ResourceLocation");
            return clazz.getConstructor(String.class, String.class).newInstance(domain, path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Inject(method = "drawGuiContainerBackgroundLayer(FII)V", at = @At("HEAD"))
    private void drawWeatherIcon(float partialTicks, int mouseX, int mouseY, CallbackInfo ci) {
        try {
            // Only draw on GuiInventory, not all GuiContainers
            if (!this.getClass().getName().equals("net.minecraft.src.GuiInventory")) {
                return;
            }

            // Get Minecraft instance
            Class<?> mcClass = Class.forName("net.minecraft.src.Minecraft");
            Method getMinecraft = mcClass.getMethod("getMinecraft");
            Object mc = getMinecraft.invoke(null);
            if (mc == null) return;

            Object theWorld = mcClass.getField("theWorld").get(mc);
            if (theWorld == null) return;

            // Check weather
            Class<?> worldClass = theWorld.getClass();
            boolean isThundering = (Boolean) worldClass.getMethod("isThundering").invoke(theWorld);
            boolean isRaining = (Boolean) worldClass.getMethod("isRaining").invoke(theWorld);

            int textureU = 0;
            if (isThundering) {
                textureU = 32;
            } else if (isRaining) {
                textureU = 16;
            }

            // Get GUI position - access methods on the mixin target (this is the GuiContainer instance)
            Class<?> guiContainerClass = Class.forName("net.minecraft.src.GuiContainer");
            Method getGuiLeft = guiContainerClass.getMethod("getGuiLeft");
            Method getGuiTop = guiContainerClass.getMethod("getGuiTop");
            Method getxSize = guiContainerClass.getMethod("getxSize");

            int guiLeft = (Integer) getGuiLeft.invoke(this);
            int guiTop = (Integer) getGuiTop.invoke(this);
            int xSize = (Integer) getxSize.invoke(this);

            int iconSize = 16;
            int x = guiLeft + xSize - iconSize - 4;
            int y = guiTop + 4;

            // Bind texture and draw
            Object renderEngine = mcClass.getField("renderEngine").get(mc);
            Class<?> textureManagerClass = renderEngine.getClass();
            Method bindTexture = textureManagerClass.getMethod("bindTexture", Class.forName("net.minecraft.src.ResourceLocation"));
            bindTexture.invoke(renderEngine, INDICATOR);

            Method drawTexturedModalRect = guiContainerClass.getMethod("drawTexturedModalRect", int.class, int.class, int.class, int.class, int.class, int.class);
            drawTexturedModalRect.invoke(this, x, y, textureU, 0, iconSize, iconSize);
        } catch (Exception e) {
            // Silently fail if reflection fails
        }
    }
}








