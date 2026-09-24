package nico.test_mod.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @WrapMethod(method = "getTooltip")
    public List<Text> getTooltip(@Nullable PlayerEntity player, TooltipContext context, Operation<List<Text>> original) {
        if (!Screen.hasShiftDown()) return original.call(player, context);
        List<Text> list = original.call(player, context);

        ItemStack stack = ((ItemStack) (Object) this);
        var iterator = stack.getItem().getRegistryEntry().streamTags().iterator();
        while (iterator.hasNext()) {
            var tag = iterator.next();
            Text text = Text.literal(tag.id().toString() + (iterator.hasNext() ? ", " : "")).setStyle(Style.EMPTY.withColor(Colors.GRAY));
            list.add(text);
        }

        return list;
    }
}
