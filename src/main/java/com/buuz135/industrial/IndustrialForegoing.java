package com.buuz135.industrial;

import com.buuz135.industrial.proxy.BlockRegistry;
import com.buuz135.industrial.proxy.CommonProxy;
import com.buuz135.industrial.proxy.FluidsRegistry;
import com.buuz135.industrial.utils.IFFakePlayer;
import com.buuz135.industrial.utils.Reference;
import com.buuz135.industrial.utils.StrawUtils;
import com.buuz135.industrial.utils.drinkhandlers.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.ndrei.teslacorelib.config.TeslaCoreLibConfig;
import net.ndrei.teslacorelib.items.gears.CoreGearType;

import java.util.Arrays;
import java.util.HashMap;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_ID, version = Reference.VERSION, dependencies = "required-after:teslacorelib", guiFactory = Reference.GUI_FACTORY)
public class IndustrialForegoing {

    public static CreativeTabs creativeTab = new CreativeTabs(Reference.MOD_ID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(BlockRegistry.blackHoleUnitBlock);
        }
    };
    @SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_COMMON)
    private static CommonProxy proxy;
    private static HashMap<Integer, IFFakePlayer> worldFakePlayer = new HashMap<>();

    static {
        if (!FluidRegistry.isUniversalBucketEnabled()) FluidRegistry.enableUniversalBucket();
    }

    public static IFFakePlayer getFakePlayer(World world) {
        if (worldFakePlayer.containsKey(world.provider.getDimension()))
            return worldFakePlayer.get(world.provider.getDimension());
        if (world instanceof WorldServer) {
            IFFakePlayer fakePlayer = new IFFakePlayer((WorldServer) world);
            worldFakePlayer.put(world.provider.getDimension(), fakePlayer);
            return fakePlayer;
        }
        return null;
    }

    public static void registerDrinkHandlers() {
        StrawUtils.register(FluidRegistry.WATER, new DrinkHandlerWater());
        StrawUtils.register(FluidRegistry.LAVA, new DrinkHandlerLava());
        StrawUtils.register(FluidsRegistry.BIOFUEL, new DrinkHandlerBiofuel());
        StrawUtils.register(FluidsRegistry.SLUDGE, new DrinkHandlerSludge());
        StrawUtils.register(FluidsRegistry.SEWAGE, new DrinkHandlerSewage());
        StrawUtils.register(FluidsRegistry.MILK, new DrinkHandlerMilk());
        StrawUtils.register(FluidsRegistry.ESSENCE, new DrinkHandlerEssence());
        StrawUtils.register(FluidsRegistry.MEAT, new DrinkHandlerMeat());
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        registerDrinkHandlers();
        proxy.postInit();
    }

    @Mod.EventHandler
    public void construction(FMLConstructionEvent event) {
        Arrays.asList(TeslaCoreLibConfig.REGISTER_GEARS,
                "${TeslaCoreLibConfig.REGISTER_GEAR_TYPES}#${CoreGearType.WOOD.material}",
                "${TeslaCoreLibConfig.REGISTER_GEAR_TYPES}#${CoreGearType.STONE.material}",
                TeslaCoreLibConfig.REGISTER_GEAR_TYPES + "#" + CoreGearType.IRON.getMaterial(),
                TeslaCoreLibConfig.REGISTER_GEAR_TYPES + "#" + CoreGearType.GOLD.getMaterial(),
                TeslaCoreLibConfig.REGISTER_GEAR_TYPES + "#" + CoreGearType.DIAMOND.getMaterial(),
                TeslaCoreLibConfig.REGISTER_MACHINE_CASE,
                TeslaCoreLibConfig.REGISTER_ADDONS,
                TeslaCoreLibConfig.REGISTER_SPEED_ADDONS,
                TeslaCoreLibConfig.REGISTER_ENERGY_ADDONS).forEach(s -> TeslaCoreLibConfig.INSTANCE.setDefaultFlag(s, true));
    }
}
