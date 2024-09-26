package com.tacz.guns.mixin.common;

import com.tacz.guns.item.AmmoItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class TweakerooItemMixin {

    @SuppressWarnings("MixinAnnotationTarget, UnresolvedMixinReference")
    @Inject(method = "getMaxStackSize(Lnet/minecraft/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    private void getMaxStackSize(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (((Item) (Object) this) instanceof AmmoItem item) {
            cir.setReturnValue(item.getMaxCount(stack));
        }
    }
}
