package flaxbeard.immersivepetroleum.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.api.energy.DieselHandler;
import blusunrize.immersiveengineering.api.multiblocks.MultiblockHandler;
import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler;
import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler.ChemthrowerEffect_Potion;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.CrusherTileEntity;
import blusunrize.immersiveengineering.common.blocks.metal.ExcavatorTileEntity;
import blusunrize.immersiveengineering.common.util.IEPotions;
import flaxbeard.immersivepetroleum.ImmersivePetroleum;
import flaxbeard.immersivepetroleum.api.IPTags;
import flaxbeard.immersivepetroleum.api.crafting.LubricantHandler;
import flaxbeard.immersivepetroleum.api.crafting.LubricatedHandler;
import flaxbeard.immersivepetroleum.api.crafting.LubricatedHandler.LubricantEffect;
import flaxbeard.immersivepetroleum.common.blocks.AsphaltBlock;
import flaxbeard.immersivepetroleum.common.blocks.AutoLubricatorBlock;
import flaxbeard.immersivepetroleum.common.blocks.BlockDummy;
import flaxbeard.immersivepetroleum.common.blocks.DistillationTowerBlock;
import flaxbeard.immersivepetroleum.common.blocks.FlarestackBlock;
import flaxbeard.immersivepetroleum.common.blocks.GasGeneratorBlock;
import flaxbeard.immersivepetroleum.common.blocks.IPBlockBase;
import flaxbeard.immersivepetroleum.common.blocks.PumpjackBlock;
import flaxbeard.immersivepetroleum.common.blocks.tileentities.AutoLubricatorTileEntity;
import flaxbeard.immersivepetroleum.common.blocks.tileentities.DistillationTowerTileEntity;
import flaxbeard.immersivepetroleum.common.blocks.tileentities.FlarestackTileEntity;
import flaxbeard.immersivepetroleum.common.blocks.tileentities.GasGeneratorTileEntity;
import flaxbeard.immersivepetroleum.common.blocks.tileentities.PumpjackTileEntity;
import flaxbeard.immersivepetroleum.common.entity.SpeedboatEntity;
import flaxbeard.immersivepetroleum.common.items.DebugItem;
import flaxbeard.immersivepetroleum.common.items.IPItemBase;
import flaxbeard.immersivepetroleum.common.items.IPUpgradeItem;
import flaxbeard.immersivepetroleum.common.items.OilCanItem;
import flaxbeard.immersivepetroleum.common.items.ProjectorItem;
import flaxbeard.immersivepetroleum.common.items.SpeedboatItem;
import flaxbeard.immersivepetroleum.common.lubehandlers.CrusherLubricationHandler;
import flaxbeard.immersivepetroleum.common.lubehandlers.ExcavatorLubricationHandler;
import flaxbeard.immersivepetroleum.common.lubehandlers.PumpjackLubricationHandler;
import flaxbeard.immersivepetroleum.common.multiblocks.DistillationTowerMultiblock;
import flaxbeard.immersivepetroleum.common.multiblocks.PumpjackMultiblock;
import flaxbeard.immersivepetroleum.common.util.IPEffects;
import flaxbeard.immersivepetroleum.common.util.fluids.IPFluid;
import flaxbeard.immersivepetroleum.common.util.fluids.NapalmFluid;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = ImmersivePetroleum.MODID, bus = Bus.MOD)
public class IPContent{
	public static final Logger log = LogManager.getLogger(ImmersivePetroleum.MODID + "/Content");
	
	public static final List<Block> registeredIPBlocks = new ArrayList<>();
	public static final List<Item> registeredIPItems = new ArrayList<>();
	public static final List<Fluid> registeredIPFluids = new ArrayList<>();
	
	public static class Multiblock{
		public static Block distillationtower;
		public static Block pumpjack;
	}
	
	public static class Fluids{
		public static IPFluid crudeOil;
		public static IPFluid diesel;
		public static IPFluid lubricant;
		public static IPFluid gasoline;
		public static IPFluid napalm;
	}
	
	public static class Blocks{
		public static IPBlockBase asphalt;
		
		public static IPBlockBase gas_generator;
		public static IPBlockBase auto_lubricator;
		public static IPBlockBase flarestack;
		
		public static BlockDummy dummyOilOre;
		public static BlockDummy dummyPipe;
		public static BlockDummy dummyConveyor;
	}
	
	public static class Items{
		public static IPItemBase bitumen;
		public static IPItemBase projector;
		public static IPItemBase speedboat;
		public static IPItemBase oil_can;
	}
	
	public static class BoatUpgrades{
		public static IPUpgradeItem reinforced_hull;
		public static IPUpgradeItem ice_breaker;
		public static IPUpgradeItem tank;
		public static IPUpgradeItem rudders;
		public static IPUpgradeItem paddles;
	}
	
	public static DebugItem debugItem;
	
	/** block/item/fluid population */
	public static void populate(){
		IPContent.debugItem = new DebugItem();
		
		Fluids.crudeOil = new IPFluid("oil", 1000, 2250);
		Fluids.diesel = new IPFluid("diesel", 789, 1750);
		Fluids.lubricant = new IPFluid("lubricant", 925, 1000);
		Fluids.gasoline = new IPFluid("gasoline", 789, 1200);
		Fluids.napalm = new NapalmFluid();
		
		Blocks.dummyOilOre = new BlockDummy("dummy_oil_ore");
		Blocks.dummyPipe = new BlockDummy("dummy_pipe");
		Blocks.dummyConveyor = new BlockDummy("dummy_conveyor");
		
		Multiblock.distillationtower = new DistillationTowerBlock();
		Multiblock.pumpjack = new PumpjackBlock();
		
		Blocks.asphalt = new AsphaltBlock();
		Blocks.gas_generator = new GasGeneratorBlock();
		
		Blocks.auto_lubricator = new AutoLubricatorBlock("auto_lubricator");
		Blocks.flarestack = new FlarestackBlock();
		
		Items.bitumen = new IPItemBase("bitumen");
		Items.oil_can = new OilCanItem("oil_can");
		Items.speedboat = new SpeedboatItem("speedboat");
		
		BoatUpgrades.reinforced_hull = new IPUpgradeItem("reinforced_hull", "BOAT");
		BoatUpgrades.ice_breaker = new IPUpgradeItem("icebreaker", "BOAT");
		BoatUpgrades.tank = new IPUpgradeItem("tank", "BOAT");
		BoatUpgrades.rudders = new IPUpgradeItem("rudders", "BOAT");
		BoatUpgrades.paddles = new IPUpgradeItem("paddles", "BOAT");
		
		Items.projector = new ProjectorItem("projector");
	}
	
	public static void preInit(){
	}
	
	public static void init(){
		//blockFluidCrudeOil.setPotionEffects(new PotionEffect(IEPotions.flammable, 100, 1));
		//blockFluidDiesel.setPotionEffects(new PotionEffect(IEPotions.flammable, 100, 1));
		//blockFluidLubricant.setPotionEffects(new PotionEffect(IEPotions.slippery, 100, 1));
		//blockFluidNapalm.setPotionEffects(new PotionEffect(IEPotions.flammable, 140, 2));
		
		ChemthrowerHandler.registerEffect(IPTags.Fluids.lubricant, new LubricantEffect());
		ChemthrowerHandler.registerEffect(IPTags.Fluids.lubricant, new ChemthrowerEffect_Potion(null, 0, IEPotions.slippery, 60, 1));
		ChemthrowerHandler.registerEffect(IETags.fluidPlantoil, new LubricantEffect());
		ChemthrowerHandler.registerEffect(IPTags.Fluids.gasoline, new ChemthrowerEffect_Potion(null, 0, IEPotions.flammable, 60, 1));
		ChemthrowerHandler.registerFlammable(IPTags.Fluids.gasoline);
		ChemthrowerHandler.registerEffect(IPTags.Fluids.napalm, new ChemthrowerEffect_Potion(null, 0, IEPotions.flammable, 60, 2));
		ChemthrowerHandler.registerFlammable(IPTags.Fluids.napalm);
		
		MultiblockHandler.registerMultiblock(DistillationTowerMultiblock.INSTANCE);
		MultiblockHandler.registerMultiblock(PumpjackMultiblock.INSTANCE);
		
		IPConfig.Utils.addFuel(IPConfig.GENERATION.fuels.get());
		IPConfig.Utils.addBoatFuel(IPConfig.MISCELLANEOUS.boat_fuels.get());
		
		DieselHandler.registerFuel(IPTags.Fluids.diesel, 150);
		
		LubricantHandler.registerLubricant(Fluids.lubricant, 3);
		LubricantHandler.registerLubricant(IEContent.fluidPlantoil, 12);
		
		LubricatedHandler.registerLubricatedTile(PumpjackTileEntity.class, PumpjackLubricationHandler::new);
		LubricatedHandler.registerLubricatedTile(ExcavatorTileEntity.class, ExcavatorLubricationHandler::new);
		LubricatedHandler.registerLubricatedTile(CrusherTileEntity.class, CrusherLubricationHandler::new);
	}
	
	@SubscribeEvent
	public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event){
		registerTile(event, DistillationTowerTileEntity.class, Multiblock.distillationtower);
		registerTile(event, PumpjackTileEntity.class, Multiblock.pumpjack);
		registerTile(event, AutoLubricatorTileEntity.class, Blocks.auto_lubricator);
		registerTile(event, FlarestackTileEntity.class, Blocks.flarestack);
		
		registerTile(event, GasGeneratorTileEntity.class, Blocks.gas_generator);
	}
	
	/**
	 * @param event
	 * @param tile the TileEntity class to register.
	 * 
	 *        <pre>
	 * Requires <code>public static TileEntityType TYPE;</code> field in the class.
	 *        </pre>
	 * 
	 * @param valid
	 */
	public static <T extends TileEntity> void registerTile(RegistryEvent.Register<TileEntityType<?>> event, Class<T> tile, Block... valid){
		String s = tile.getSimpleName();
		s = s.substring(0, s.indexOf("TileEntity")).toLowerCase(Locale.ENGLISH);
		
		TileEntityType<T> type = createType(tile, valid);
		type.setRegistryName(ImmersivePetroleum.MODID, s);
		event.getRegistry().register(type);
		
		try{
			tile.getField("TYPE").set(null, type);
		}catch(NoSuchFieldException | IllegalAccessException e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		log.debug("Registered TileEntity: {} as {}", tile, type.getRegistryName());
	}
	
	private static <T extends TileEntity> TileEntityType<T> createType(Class<T> typeClass, Block... valid){
		Set<Block> validSet = new HashSet<>(Arrays.asList(valid));
		TileEntityType<T> type = new TileEntityType<>(() -> {
			try{
				return typeClass.newInstance();
			}catch(InstantiationException | IllegalAccessException e){
				e.printStackTrace();
			}
			return null;
		}, validSet, null);
		
		return type;
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event){
		for(Block block:registeredIPBlocks){
			try{
				event.getRegistry().register(block);
			}catch(Throwable e){
				log.error("Failed to register a block. ({})", block);
				throw e;
			}
		}
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event){
		for(Item item:registeredIPItems){
			try{
				event.getRegistry().register(item);
			}catch(Throwable e){
				log.error("Failed to register an item. ({}, {})", item, item.getRegistryName());
				throw e;
			}
		}
	}
	
	@SubscribeEvent
	public static void registerFluids(RegistryEvent.Register<Fluid> event){
		for(Fluid fluid:registeredIPFluids){
			try{
				event.getRegistry().register(fluid);
			}catch(Throwable e){
				log.error("Failed to register a fluid. ({}, {})", fluid, fluid.getRegistryName());
				throw e;
			}
		}
	}
	
	@SubscribeEvent
	public static void registerEntityTypes(RegistryEvent.Register<EntityType<?>> event){
		try{
			event.getRegistry().register(SpeedboatEntity.TYPE);
		}catch(Throwable e){
			log.error("Failed to register Speedboat Entity. {}", e.getMessage());
			throw e;
		}
	}
	
	@SubscribeEvent
	public static void registerEffects(RegistryEvent.Register<Effect> event){
		IPEffects.init();
	}
}
