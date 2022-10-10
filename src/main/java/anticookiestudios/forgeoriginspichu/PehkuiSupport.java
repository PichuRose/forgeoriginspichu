//giantluigi4
package anticookiestudios.forgeoriginspichu;

import net.minecraft.util.ResourceLocation;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleModifier;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class PehkuiSupport {
    public static final AtomicReference<ScaleModifier> PichuScaleModifier = new AtomicReference<>();
    public static final AtomicReference<ScaleType> PichuScaleType = new AtomicReference<>();

    public static void setup() {
        Forgeoriginspichu.LOGGER.info("Pehkui detected; enabling pichupport");

        ScaleModifier modifier = new ScaleModifier() {
            @Override
            public float modifyScale(ScaleData scaleData, float modifiedScale, float delta) {
                return PichuScaleType.get().getScaleData(scaleData.getEntity()).getScale(delta) * modifiedScale;
            }
        };
        ScaleRegistries.SCALE_MODIFIERS.put(new ResourceLocation("pichu:pichu_resize"), modifier);
        PichuScaleModifier.set(modifier);
        ScaleType pichuType = ScaleType.Builder.create()
                .affectsDimensions()
                .addDependentModifier(PichuScaleModifier.get())
                .build();
        ScaleRegistries.SCALE_TYPES.put(new ResourceLocation("pichu:pichu_resize"), pichuType);
        Optional<ScaleType> baseType = getType("base");
        //pichu suppress warning because I don't want to risk accidental class loading, nor do I want intelliJ constantly warning me about the fact that I do this
        //solution: right click -> functional style expression - gamma
        baseType.ifPresent(scaleType -> scaleType.getDefaultBaseValueModifiers().add(modifier));
        PichuScaleType.set(pichuType);
    }

    // using optional to prevent accidental class loading
    //smart - gama
    public static Optional<ScaleType> getType(String name) {
        return Optional.of(ScaleRegistries.getEntry(ScaleRegistries.SCALE_TYPES, new ResourceLocation("pehkui", name)));
    }
}