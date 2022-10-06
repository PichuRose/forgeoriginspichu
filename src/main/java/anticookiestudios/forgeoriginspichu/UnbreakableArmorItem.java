package anticookiestudios.forgeoriginspichu;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;

public class UnbreakableArmorItem extends ArmorItem {
    public UnbreakableArmorItem(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builderIn) {
        super(materialIn, slot, builderIn);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }
}
