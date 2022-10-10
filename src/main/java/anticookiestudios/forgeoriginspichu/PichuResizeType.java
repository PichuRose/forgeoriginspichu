//GiantLuigi4
package anticookiestudios.forgeoriginspichu;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.threetag.threecore.capability.ISizeChanging;
import net.threetag.threecore.entity.attributes.TCAttributes;
import net.threetag.threecore.sizechanging.DefaultSizeChangeType;
import net.threetag.threecore.sizechanging.SizeChangeType;

public class PichuResizeType extends DefaultSizeChangeType {
    public static final DeferredRegister<SizeChangeType> pichuSizeChangeTypes = DeferredRegister.create(SizeChangeType.class, "forgeoriginspichu");
    public static final RegistryObject<SizeChangeType> PICHU_CHANGE_TYPE = pichuSizeChangeTypes.register("pichu_size_change", PichuResizeType::new);

    @Override
    public int getSizeChangingTime(Entity entity, ISizeChanging data, float estimatedSize) {
        System.out.println(data.getScale());
        System.out.println(estimatedSize);
        return (int) Math.abs((data.getScale() - estimatedSize) * 10);
    }

    @Override
    public void onSizeChanged(Entity entity, ISizeChanging data, float size) {
        super.onSizeChanged(entity, data, size);
        if (entity instanceof LivingEntity) {
            float scl = 1f / (1 - (1f / size));
            if (scl < 0) {
                scl = (0 - scl) - 8;
                scl = ((scl / 8f) / 13f);
            }
            if (size >= 1f / 4) scl = 0;
            this.setAttribute((LivingEntity) entity, Attributes.MOVEMENT_SPEED, scl, AttributeModifier.Operation.ADDITION, SizeChangeType.ATTRIBUTE_UUID);
            this.setAttribute((LivingEntity) entity, TCAttributes.JUMP_HEIGHT.get(), -1, AttributeModifier.Operation.ADDITION, SizeChangeType.ATTRIBUTE_UUID);
        }
    }
}