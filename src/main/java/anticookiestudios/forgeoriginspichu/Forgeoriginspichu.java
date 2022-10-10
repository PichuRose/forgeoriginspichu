package anticookiestudios.forgeoriginspichu;

import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("forgeoriginspichu")
public class Forgeoriginspichu {

    //GiantLuigi4
    //tf did luigi do - gamma
//    public static final AtomicReference<ScaleModifier> PichuScaleModifier = new AtomicReference<>();
//    public static final AtomicReference<ScaleType> PichuScaleType = new AtomicReference<>();
    private static final UUID PICHU_HEALTH_UUID = new UUID(new Random(8675309).nextLong(), new Random(141234).nextLong());


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


    }

    public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
        if(event.player.getEntityWorld().getWorldInfo().getGameTime()%3!=0){
            return;
        }


        PlayerEntity player = event.player;
        String[] origins = getOrigin(player);
        if(origins == null) {
            ResizingUtils.resize(event.player, 1);
            scalePlayerHP(player, 0);
            return;
        }

        float scale = ResizingUtils.getSize(event.player);
//        float fairyScale = 0.0625F;
        float buildlingScale = 4F;
        float macroScale = 16F;
        float nanoScale = 0.00390625F;
        //float[] chuHeights = {.01F, .0175F, .03125F, .0475F, .0625F, .15F, .25F, .5F, .7F, .85F, 1F, 1.125F, 1.25F, 1.5F, 2F};


        float playerHp = player.getHealth();
        float playerHpPercentage = playerHp/20;

        float hpPercentage = 0;




        //for(ItemStack i:inventory){
            //if(i.getDisplayName())
        //}

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
                player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 10, 2, false, false));
                player.addPotionEffect(new EffectInstance(Effects.SATURATION, 10, 2, false, false));
                player.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 10, 2, false, false));
                player.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, 10, 2, false, false));
            }
            else if(event.player.isInWaterRainOrBubbleColumn()){
                player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 10, 2, false, false));
                player.addPotionEffect(new EffectInstance(Effects.SATURATION, 10, 2, false, false));
            }

            if (playerHp >= 0) {
                float chuScale = 1;

                chuScale *= Math.pow(playerHpPercentage*2, 1.23);

                chuScale = (float)Math.max(chuScale, 0.00001);
                chuScale = (float)Math.min(chuScale, 2.65);
                newScale *= chuScale;
            }
        }
        if (isFairy){

            scalePlayerHP(player, 2);
            int flowerNum = 0;

            List<ItemStack> inventory = player.inventory.mainInventory;

            for(ItemStack i:inventory){
                if(TagUtils.isBlockOfTag(i.getItem(),"minecraft:flowers")){
                    flowerNum+=i.getCount();
                }
            }

            flowerNum = Math.max(flowerNum, 1);
            flowerNum = Math.min(flowerNum, 64);
            float flowerScale = (float)(flowerNum/64.0);
            newScale *= flowerScale;
            float healthScale = (float) Math.pow(newScale,2);

            healthScale = (float)Math.max(healthScale, 0.4);
            healthScale = Math.min(healthScale, 1);
            hpPercentage = -(1-(healthScale));
            //System.out.println(hpReducePercentage);
        }

        if(isBuildling)
            newScale *= buildlingScale;
        if(isMacro)
            newScale *= macroScale;
        if(isNano)
            newScale *= nanoScale;

        if(newScale != scale) {
            ResizingUtils.resize(event.player, newScale);
        }

        //System.out.println(hpUPB);
        scalePlayerHP(player, hpPercentage);


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

    public void scalePlayerHP(PlayerEntity pPlayer, float upb) {
        ModifiableAttributeInstance instance = pPlayer.getAttribute(Attributes.MAX_HEALTH);
        Objects.requireNonNull(instance).removeModifier(PICHU_HEALTH_UUID);
        instance.applyPersistentModifier(
                new AttributeModifier(PICHU_HEALTH_UUID, "pichuorigins:maxhealth", upb, AttributeModifier.Operation.MULTIPLY_TOTAL)
        );
        //isReachSet = true;
    }

}

