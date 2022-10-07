package anticookiestudios.forgeoriginspichu;

import io.github.apace100.origins.command.OriginArgument;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.origin.OriginLayers;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.PowerTypes;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.threetag.threecore.sizechanging.DefaultSizeChangeType;
import net.threetag.threecore.sizechanging.SizeChangeType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleModifier;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("forgeoriginspichu")
public class Forgeoriginspichu {

    //GiantLuigi4
    public static final AtomicReference<ScaleModifier> PichuScaleModifier = new AtomicReference<>();
    public static final AtomicReference<ScaleType> PichuScaleType = new AtomicReference<>();


    // Directly reference a log4j logger.
    static final Logger LOGGER = LogManager.getLogger();

    public Forgeoriginspichu() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        //ItemRegistry.ITEMS.register(bus);
        if (ModList.get().isLoaded("threecore")) {
            PichuResizeType.pichuSizeChangeTypes.register(bus);
        }
        //MinecraftForge.EVENT_BUS.addListener(this::onLivingHurtEvent);
        //MinecraftForge.EVENT_BUS.addListener(this::onLivingEquipmentChangeEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerTickEvent);

        //GiantLuigi4
        if (ModList.get().isLoaded("pehkui")) {
            ScaleModifier modifier = new ScaleModifier() {
                @Override
                public float modifyScale(ScaleData scaleData, float modifiedScale, float delta) {
                    //return PichuScaleType.get().getScaleData(scaleData.getEntity()).getScale(delta) * modifiedScale;
                    return PichuScaleType.get().getScaleData(scaleData.getEntity()).getScale(delta) * modifiedScale;
                }
            };

            ScaleRegistries.SCALE_MODIFIERS.put(new ResourceLocation("forgeoriginspichu:pichu_resize"), modifier);
            PichuScaleModifier.set(modifier);
            ScaleType type = ScaleType.Builder.create()
                    .affectsDimensions()
                    .addDependentModifier(PichuScaleModifier.get())
                    .build();
            ScaleRegistries.SCALE_TYPES.put(new ResourceLocation("pichu_resize"), type);
            //ScaleType.defaultBaseValueModifiers.add(modifier);
            ScaleType.Builder builder = new ScaleType.Builder.create();
            PehkuiSupport.PichuScaleType.set(type);
            PehkuiSupport.PichuScaleType.
        }


    }

    public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
        if(event.player.getEntityWorld().getWorldInfo().getGameTime()%10!=0){
            return;
        }


        PlayerEntity player = event.player;
        String[] origins = getOrigin(player);
        if(origins == null)
            ResizingUtils.resize(event.player, 1);

        float scale = ResizingUtils.getSize(event.player);
        float fairyScale = 0.0625F;
        float buildlingScale = 4F;
        float macroScale = 16F;
        float nanoScale = 0.00390625F;
        //float[] chuHeights = {.01F, .0175F, .03125F, .0475F, .0625F, .15F, .25F, .5F, .7F, .85F, 1F, 1.125F, 1.25F, 1.5F, 2F};


        float playerHp = player.getHealth();
        float playerHpPercentage = playerHp/20;

        float newScale = 1;

        boolean isFairy = false;
        boolean isBuildling = false;
        boolean isMacro = false;
        boolean isNano = false;
        boolean isChu = false;


        for(String o:origins) {
            if(o == null)
                continue;
            isFairy = o.contains("origin.pichuorigins.fairy.name");
            isBuildling = o.contains("origin.pichuorigins.buildling.name");
            isMacro = o.contains("origin.pichuorigins.macro.name");
            isNano = o.contains("origin.pichuorigins.nano.name");
            isChu = o.contains("origin.pichuorigins.chu.name");
        }
        if(isChu) {

            if(event.player.isInWater()){
                player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 20, 2, false, false));
            }

            if (playerHp >= 0) {
                float chuScale = 1;

                chuScale *= Math.pow(playerHpPercentage*2, 1.23);

                chuScale = (float)Math.max(chuScale, 0.00001);
                chuScale = (float)Math.min(chuScale, 2.65);
                newScale *= chuScale;
            }
        }
        if (isFairy)
            newScale *= fairyScale;
        if(isBuildling)
            newScale *= buildlingScale;
        if(isMacro)
            newScale *= macroScale;
        if(isNano)
            newScale *= nanoScale;

        if(newScale != scale)
            ResizingUtils.resize(event.player, newScale);


    }

    /*public boolean isOrigin(PlayerEntity player, String name){
        HashMap<OriginLayer, Origin> originHashMap = Origin.get(player);
        Collection<Origin> origins = originHashMap.values();
        Object[] originArray = origins.toArray();
        for(Object o:originArray)
            if(((Origin)o).getName().toString().toLowerCase().contains(name.toLowerCase()))
                return true;
        return false;
    }*/

    public String[] getOrigin(PlayerEntity player){
        HashMap<OriginLayer, Origin> originHashMap = Origin.get(player);
        Collection<Origin> origins = originHashMap.values();
        Object[] originArray = origins.toArray();
        String[] originsStrings = new String[originArray.length];
        for(int i = 0; i < originArray.length;i++) {
            originsStrings[i] = ((Origin) originArray[i]).getName().toString();
        }
        return originsStrings;
    }

    private void setup(final FMLCommonSetupEvent event) {
        if (ModList.get().isLoaded("pehkui")) PehkuiSupport.setup();
    }
}

