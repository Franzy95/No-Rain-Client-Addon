package com.franzy.norainclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.src.EntityRenderer")
public class EntityRendererMixin {

    @Inject(method = "method_1341()V", at = @At("HEAD"), cancellable = true, remap = false)
    private void disableRainParticles(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "method_1334(F)V", at = @At("HEAD"), cancellable = true, remap = false)
    private void disableRenderRainSnow(float partialTicks, CallbackInfo ci) {
        ci.cancel();
    }
}



