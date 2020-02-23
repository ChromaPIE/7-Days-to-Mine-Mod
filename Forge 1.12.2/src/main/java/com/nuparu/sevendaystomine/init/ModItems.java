package com.nuparu.sevendaystomine.init;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.item.EnumLength;
import com.nuparu.sevendaystomine.item.EnumMaterial;
import com.nuparu.sevendaystomine.item.ItemAK47;
import com.nuparu.sevendaystomine.item.ItemAdvancedBandage;
import com.nuparu.sevendaystomine.item.ItemAlcoholDrink;
import com.nuparu.sevendaystomine.item.ItemAnalogCamera;
import com.nuparu.sevendaystomine.item.ItemAntibiotics;
import com.nuparu.sevendaystomine.item.ItemArmorBase;
import com.nuparu.sevendaystomine.item.ItemAuger;
import com.nuparu.sevendaystomine.item.ItemBackpack;
import com.nuparu.sevendaystomine.item.ItemBandage;
import com.nuparu.sevendaystomine.item.ItemBloodBag;
import com.nuparu.sevendaystomine.item.ItemBloodDrawKit;
import com.nuparu.sevendaystomine.item.ItemCannedFood;
import com.nuparu.sevendaystomine.item.ItemClothingChest;
import com.nuparu.sevendaystomine.item.ItemClothingLegs;
import com.nuparu.sevendaystomine.item.ItemClub;
import com.nuparu.sevendaystomine.item.ItemCoffeeBeans;
import com.nuparu.sevendaystomine.item.ItemCoffeeDrink;
import com.nuparu.sevendaystomine.item.ItemDoorBase;
import com.nuparu.sevendaystomine.item.ItemDrink;
import com.nuparu.sevendaystomine.item.ItemEmptyCan;
import com.nuparu.sevendaystomine.item.ItemEmptyJar;
import com.nuparu.sevendaystomine.item.ItemFirstAidKit;
import com.nuparu.sevendaystomine.item.ItemHuntingRifle;
import com.nuparu.sevendaystomine.item.ItemInstallDisc;
import com.nuparu.sevendaystomine.item.ItemMP3;
import com.nuparu.sevendaystomine.item.ItemMold;
import com.nuparu.sevendaystomine.item.ItemMurkyWater;
import com.nuparu.sevendaystomine.item.ItemPhoto;
import com.nuparu.sevendaystomine.item.ItemPistol;
import com.nuparu.sevendaystomine.item.ItemQualityAxe;
import com.nuparu.sevendaystomine.item.ItemQualityScrapable;
import com.nuparu.sevendaystomine.item.ItemQualitySword;
import com.nuparu.sevendaystomine.item.ItemRealityWand;
import com.nuparu.sevendaystomine.item.ItemRefrigerator;
import com.nuparu.sevendaystomine.item.ItemScrap;
import com.nuparu.sevendaystomine.item.ItemScrapable;
import com.nuparu.sevendaystomine.item.ItemScrewdriver;
import com.nuparu.sevendaystomine.item.ItemSleepingBag;
import com.nuparu.sevendaystomine.item.ItemStoneAxe;
import com.nuparu.sevendaystomine.item.ItemStoneShovel;
import com.nuparu.sevendaystomine.item.ItemStreetSign;
import com.nuparu.sevendaystomine.item.ItemTea;
import com.nuparu.sevendaystomine.item.ItemUpgrader;
import com.nuparu.sevendaystomine.item.ItemWire;
import com.nuparu.sevendaystomine.tileentity.TileEntityComputer.EnumSystem;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraftforge.common.util.EnumHelper;

public class ModItems {
	public static final Item REALITY_WAND = new ItemRealityWand().setRegistryName("RealityWand")
			.setUnlocalizedName("RealityWand");
	
	public static final Item IRON_SCRAP = new ItemScrap(EnumMaterial.IRON).setRegistryName("ScrapIron")
			.setUnlocalizedName("ScrapIron");
	public static final Item BRASS_SCRAP = new ItemScrap(EnumMaterial.BRASS).setRegistryName("ScrapBrass")
			.setUnlocalizedName("ScrapBrass");
	public static final Item LEAD_SCRAP = new ItemScrap(EnumMaterial.LEAD).setRegistryName("ScrapLead")
			.setUnlocalizedName("ScrapLead");
	public static final Item EMPTY_CAN = new ItemEmptyCan().setRegistryName("EmptyCan").setUnlocalizedName("EmptyCan");
	public static final Item PLANK_WOOD = new ItemScrap(EnumMaterial.WOOD).setRegistryName("WoodPlank")
			.setUnlocalizedName("WoodPlank");
	public static final Item SMALL_STONE = new ItemScrap(EnumMaterial.STONE).setRegistryName("SmallStone")
			.setUnlocalizedName("SmallStone");
	public static final Item PLANT_FIBER = new ItemScrap(EnumMaterial.PLANT_FIBER).setRegistryName("PlantFiber")
			.setUnlocalizedName("PlantFiber");
	public static final Item EMPTY_JAR = new ItemEmptyJar().setRegistryName("EmptyJar").setUnlocalizedName("EmptyJar");
	public static final Item GLASS_SCRAP = new ItemScrap(EnumMaterial.GLASS).setRegistryName("ScrapGlass")
			.setUnlocalizedName("ScrapGlass");
	public static final Item COFFEE_BEANS = new ItemCoffeeBeans().setRegistryName("CoffeeBeans")
			.setUnlocalizedName("CoffeeBeans");
	public static final Item CLOTH = new ItemScrap(EnumMaterial.CLOTH).setRegistryName("Cloth")
			.setUnlocalizedName("Cloth");

	public static final Item STONE_AXE = new ItemStoneAxe().setRegistryName("StoneAxe").setUnlocalizedName("StoneAxe");
	public static final Item STONE_SHOVEL = new ItemStoneShovel(SevenDaysToMine.STONE_TOOLS, -1f, 8f)
			.setRegistryName("StoneShovel").setUnlocalizedName("StoneShovel");
	public static final Item BONE_SHIV = new ItemQualitySword(SevenDaysToMine.BONE_TOOLS).setRegistryName("BoneShiv")
			.setUnlocalizedName("BoneShiv");
	public static final Item CRUDE_CLUB = new ItemClub(SevenDaysToMine.CRUDE_TOOLS, -3.14d).setRegistryName("CrudeClub")
			.setUnlocalizedName("CrudeClub");
	public static final Item WOODEN_CLUB = new ItemClub(SevenDaysToMine.WOODEN_TOOLS, -3.14d)
			.setRegistryName("WoodenClub").setUnlocalizedName("WoodenClub");
	public static final Item IRON_REINFORCED_CLUB = new ItemClub(SevenDaysToMine.WOODEN_REINFORCED_TOOLS, -3.2d)
			.setRegistryName("IronReinforcedClub").setUnlocalizedName("IronReinforcedClub");
	public static final Item BARBED_CLUB = new ItemClub(SevenDaysToMine.BARBED_TOOLS, -3.18d)
			.setRegistryName("BarbedClub").setUnlocalizedName("BarbedClub");
	public static final Item SPIKED_CLUB = new ItemClub(SevenDaysToMine.SPIKED_TOOLS, -3.18d)
			.setRegistryName("SpikedClub").setUnlocalizedName("SpikedClub");
	public static final Item CLAWHAMMER = new ItemUpgrader(SevenDaysToMine.IRON_TOOLS).setEffectiveness(0.5f).setLength(EnumLength.SHORT)
			.setRegistryName("Clawhammer").setUnlocalizedName("Clawhammer");
	public static final Item WRENCH = new ItemUpgrader(SevenDaysToMine.IRON_TOOLS).setEffectiveness(0.33334f)
			.setRegistryName("Wrench").setUnlocalizedName("Wrench");
	public static final Item KITCHEN_KNIFE = new ItemQualitySword(SevenDaysToMine.IRON_TOOLS,EnumLength.SHORT)
			.setRegistryName("KitchenKnife").setUnlocalizedName("KitchenKnife");
	public static final Item ARMY_KNIFE = new ItemQualitySword(SevenDaysToMine.ARMY_TOOLS,EnumLength.SHORT).setRegistryName("ArmyKnife")
			.setUnlocalizedName("ArmyKnife");
	public static final Item MACHETE = new ItemQualitySword(SevenDaysToMine.MACHETE).setRegistryName("Machete")
			.setUnlocalizedName("Machete");
	public static final Item SCREWDRIVER = new ItemScrewdriver().setRegistryName("screwdriver").setUnlocalizedName("screwdriver").setCreativeTab(CreativeTabs.TOOLS).setMaxStackSize(1);

	public static final Item PISTOL = new ItemPistol().setRegistryName("Pistol").setUnlocalizedName("Pistol");
	public static final Item AK47 = new ItemAK47().setRegistryName("AK47").setUnlocalizedName("AK47");
	public static final Item HUNTING_RIFLE = new ItemHuntingRifle().setRegistryName("hunting_rifle").setUnlocalizedName("hunting_rifle");

	public static final Item COOKING_GRILL = new ItemScrapable(EnumMaterial.IRON).setRegistryName("CookingGrill")
			.setUnlocalizedName("CookingGrill").setMaxStackSize(1);

	public static final Item BOTTLED_MURKY_WATER = new ItemMurkyWater(0, 250, 0).setRegistryName("BottledMurkyWater")
			.setUnlocalizedName("BottledMurkyWater").setContainerItem(EMPTY_JAR);
	public static final Item BOTTLED_WATER = new ItemDrink(0, 250, 50).setRegistryName("BottledWater")
			.setUnlocalizedName("BottledWater").setContainerItem(EMPTY_JAR);
	public static final Item BOTTLED_BEER = new ItemAlcoholDrink(0, 250, 50).setRegistryName("BottledBeer")
			.setUnlocalizedName("BottledBeer").setContainerItem(EMPTY_JAR);
	public static final Item GOLDENROD_TEA = new ItemTea(0, 300, 150).setRegistryName("GoldenrodTea")
			.setUnlocalizedName("GoldenrodTea").setContainerItem(EMPTY_JAR);
	public static final Item BOTTLED_COFFEE = new ItemCoffeeDrink(0, 300, 150).setRegistryName("BottledCoffee")
			.setUnlocalizedName("BottledCoffee").setContainerItem(EMPTY_JAR);
	public static final Item JAR_OF_HONEY = new ItemDrink(5, 0, 300, 150).setRegistryName("JarOfHoney")
			.setUnlocalizedName("JarOfHoney").setContainerItem(EMPTY_JAR);
	public static final Item CANNED_MURKY_WATER = new ItemMurkyWater(0, 250, 0).setRegistryName("CannedMurkyWater")
			.setUnlocalizedName("CannedMurkyWater").setContainerItem(EMPTY_CAN);
	public static final Item CANNED_WATER = new ItemDrink(0, 250, 50).setRegistryName("CannedWater")
			.setUnlocalizedName("CannedWater").setContainerItem(EMPTY_CAN);

	public static final Item CANNED_CATFOOD = new ItemCannedFood(3, 0.3f, true).setRegistryName("CannedCatFood")
			.setUnlocalizedName("CannedCatFood");
	public static final Item CANNED_DOGFOOD = new ItemCannedFood(3, 0.3f, true).setRegistryName("CannedDogFood")
			.setUnlocalizedName("CannedDogFood");
	public static final Item CANNED_HAM = new ItemCannedFood(6, 0.9f, true).setRegistryName("CannedHam")
			.setUnlocalizedName("CannedHam");
	public static final Item CANNED_CHICKEN = new ItemCannedFood(5, 0.7f, true).setRegistryName("CannedChicken")
			.setUnlocalizedName("CannedChicken");
	public static final Item CANNED_CHILI = new ItemCannedFood(4, 0.45f, false).setRegistryName("CannedChili")
			.setUnlocalizedName("CannedChili");
	public static final Item CANNED_MISO = new ItemCannedFood(4, 0.45f, false).setRegistryName("CannedMiso")
			.setUnlocalizedName("CannedMiso");
	public static final Item CANNED_PASTA = new ItemCannedFood(5, 0.5f, false).setRegistryName("CannedPasta")
			.setUnlocalizedName("CannedPasta");
	public static final Item CANNED_PEARS = new ItemCannedFood(4, 0.35f, false).setRegistryName("CannedPears")
			.setUnlocalizedName("CannedPears");
	public static final Item CANNED_PEAS = new ItemCannedFood(4, 0.35f, false).setRegistryName("CannedPeas")
			.setUnlocalizedName("CannedPeas");
	public static final Item CANNED_SALMON = new ItemCannedFood(5, 0.4f, false).setRegistryName("CannedSalmon")
			.setUnlocalizedName("CannedSalmon");
	public static final Item CANNED_SOUP = new ItemCannedFood(5, 0.5f, false).setRegistryName("CannedSoup")
			.setUnlocalizedName("CannedSoup");
	public static final Item CANNED_STOCK = new ItemCannedFood(4, 0.45f, false).setRegistryName("CannedStock")
			.setUnlocalizedName("CannedStock");
	public static final Item CANNED_TUNA = new ItemCannedFood(5, 0.4f, false).setRegistryName("CannedTuna")
			.setUnlocalizedName("CannedTuna");
	public static final Item CANNED_BEEF = new ItemCannedFood(7, 1f, true).setRegistryName("CannedBeef")
			.setUnlocalizedName("CannedBeef");
	public static final Item CANNED_LAMB = new ItemCannedFood(7, 1f, true).setRegistryName("CannedLamb")
			.setUnlocalizedName("CannedLamb");

	public static final Item CORN = new ItemFood(1, 0.3f, false).setRegistryName("Corn").setUnlocalizedName("Corn");
	public static final Item BLUEBERRY = new ItemFood(1, 0.3f, false).setRegistryName("Blueberry")
			.setUnlocalizedName("Blueberry");
	public static final Item BANEBERRY = new ItemFood(1, 0.3f, false).setRegistryName("Baneberry")
			.setUnlocalizedName("Baneberry");

	public static final Item BANDAGE = new ItemBandage().setRegistryName("Bandage").setUnlocalizedName("Bandage");
	public static final Item BANDAGE_ADVANCED = new ItemAdvancedBandage().setRegistryName("AdvancedBandage")
			.setUnlocalizedName("AdvancedBandage");
	public static final Item FIRST_AID_KIT = new ItemFirstAidKit().setRegistryName("FirstAidKit")
			.setUnlocalizedName("FirstAidKit");
	public static final Item BLOOD_BAG = new ItemBloodBag().setRegistryName("BloodBag").setUnlocalizedName("BloodBag");
	public static final Item BLOOD_DRAW_KIT = new ItemBloodDrawKit().setRegistryName("BloodDrawKit")
			.setUnlocalizedName("BloodDrawKit");
	public static final Item ANTIBIOTICS = new ItemAntibiotics().setRegistryName("Antibiotics")
			.setUnlocalizedName("Antibiotics");

	public static final Item INGOT_IRON = new ItemScrapable(EnumMaterial.IRON, 6).setRegistryName("IronIngot")
			.setUnlocalizedName("IronIngot");
	public static final Item INGOT_LEAD = new ItemScrapable(EnumMaterial.LEAD, 6).setRegistryName("LeadIngot")
			.setUnlocalizedName("LeadIngot");
	public static final Item INGOT_BRASS = new ItemScrapable(EnumMaterial.BRASS, 6).setRegistryName("BrassIngot")
			.setUnlocalizedName("BrassIngot");
	public static final Item INGOT_STEEL = new ItemScrapable(EnumMaterial.STEEL, 6).setRegistryName("SteelIngot")
			.setUnlocalizedName("SteelIngot");
	public static final Item INGOT_COPPER = new ItemScrapable(EnumMaterial.COPPER, 6).setRegistryName("CopperIngot")
			.setUnlocalizedName("CopperIngot");
	public static final Item INGOT_TIN = new ItemScrapable(EnumMaterial.TIN, 6).setRegistryName("TinIngot")
			.setUnlocalizedName("TinIngot");
	public static final Item INGOT_ZINC = new ItemScrapable(EnumMaterial.ZINC, 6).setRegistryName("ZincIngot")
			.setUnlocalizedName("ZincIngot");
	public static final Item INGOT_BRONZE = new ItemScrapable(EnumMaterial.BRONZE, 6).setRegistryName("BronzeIngot")
			.setUnlocalizedName("BronzeIngot");
	public static final Item INGOT_GOLD = new ItemScrapable(EnumMaterial.GOLD, 6).setRegistryName("GoldIngot")
			.setUnlocalizedName("GoldIngot");
	public static final Item CENT = new ItemScrapable(EnumMaterial.IRON, 6).setRegistryName("cent")
			.setUnlocalizedName("cent");
	public static final Item BACKPACK = new ItemBackpack().setRegistryName("backpack")
			.setUnlocalizedName("backpack");


	public static final Item MOLD_INGOT = new ItemMold().setRegistryName("IngotMold").setUnlocalizedName("IngotMold");

	public static final Item CONCRETE_MIX = new Item().setRegistryName("ConcreteMix").setUnlocalizedName("ConcreteMix")
			.setCreativeTab(SevenDaysToMine.TAB_MATERIALS);
	public static final Item CEMENT = new ItemScrap(EnumMaterial.CONCRETE).setRegistryName("Cement")
			.setUnlocalizedName("Cement");

	public static final Item NINE_MM_BULLET = new Item().setRegistryName("NineMMBullet")
			.setUnlocalizedName("NineMMBullet").setCreativeTab(CreativeTabs.COMBAT);
	public static final Item SEVEN_MM_BULLET = new Item().setRegistryName("SevenMMBullet")
			.setUnlocalizedName("SevenMMBullet").setCreativeTab(CreativeTabs.COMBAT);

	public static final Item FRIDGE = new ItemRefrigerator().setRegistryName("FridgeItem").setUnlocalizedName("Fridge")
			.setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	public static final Item SLEEPING_BAG = new ItemSleepingBag().setRegistryName("SleepingBagItem")
			.setUnlocalizedName("SleepingBagItem").setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	public static final Item WOODEN_DOOR_ITEM = new ItemDoorBase() {
		public Block getDoor() {
			return ModBlocks.WOODEN_DOOR;
		}
	}.setRegistryName("WoodenDoorItem").setUnlocalizedName("WoodenDoorItem")
			.setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	public static final Item WOODEN_DOOR_REINFORCED_ITEM = new ItemDoorBase() {
		public Block getDoor() {
			return ModBlocks.WOODEN_DOOR_REINFORCED;
		}
	}.setRegistryName("WoodenDoorReinforcedItem").setUnlocalizedName("WoodenDoorReinforcedItem")
			.setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	
	public static final Item LOCKED_DOOR_ITEM = new ItemDoorBase() {
		public Block getDoor() {
			return ModBlocks.LOCKED_DOOR;
		}
	}.setRegistryName("locked_door_item").setUnlocalizedName("locked_door_item")
			.setCreativeTab(SevenDaysToMine.TAB_BUILDING);

	public static final Item AUGER = new ItemAuger(20, 20, EnumHelper.addToolMaterial("AUGER", 5, 600, 26F, 80, 0))
			.setRegistryName("Auger").setUnlocalizedName("Auger");
	
	public static final Item MP3_PLAYER = new ItemMP3().setRegistryName("MP3Player").setUnlocalizedName("MP3Player").setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
	
	public static final Item RAM = new Item().setRegistryName("ram").setUnlocalizedName("ram").setMaxStackSize(16).setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
	public static final Item MOTHERBOARD = new Item().setRegistryName("motherboard").setUnlocalizedName("motherboard").setMaxStackSize(1).setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
	public static final Item CPU = new Item().setRegistryName("cpu").setUnlocalizedName("cpu").setMaxStackSize(16).setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
	public static final Item GPU = new Item().setRegistryName("gpu").setUnlocalizedName("gpu").setMaxStackSize(16).setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
	public static final Item HDD = new Item().setRegistryName("hdd").setUnlocalizedName("hdd").setMaxStackSize(16).setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
	public static final Item POWER_SUPPLY = new Item().setRegistryName("power_supply").setUnlocalizedName("power_supply").setMaxStackSize(16).setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
	
	public static final Item DISC = new Item().setRegistryName("disc").setUnlocalizedName("disc").setMaxStackSize(16).setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
	public static final Item LINUX_DISC = new ItemInstallDisc(EnumSystem.LINUX).setRegistryName("linux_disc").setUnlocalizedName("linux_disc").setMaxStackSize(1);
	public static final Item MAC_DISC = new ItemInstallDisc(EnumSystem.MAC).setRegistryName("mac_disc").setUnlocalizedName("mac_disc").setMaxStackSize(1);
	public static final Item WIN98_DISC = new ItemInstallDisc(EnumSystem.WIN98).setRegistryName("win98_disc").setUnlocalizedName("win98_disc").setMaxStackSize(1);
	public static final Item WINXP_DISC = new ItemInstallDisc(EnumSystem.WINXP).setRegistryName("winxp_disc").setUnlocalizedName("winxp_disc").setMaxStackSize(1);
	public static final Item WIN7_DISC = new ItemInstallDisc(EnumSystem.WIN7).setRegistryName("win7_disc").setUnlocalizedName("win7_disc").setMaxStackSize(1);
	public static final Item WIN8_DISC = new ItemInstallDisc(EnumSystem.WIN8).setRegistryName("win8_disc").setUnlocalizedName("win8_disc").setMaxStackSize(1);
	public static final Item WIN10_DISC = new ItemInstallDisc(EnumSystem.WIN10).setRegistryName("win10_disc").setUnlocalizedName("win10_disc").setMaxStackSize(1);

	public static final Item WIRE = new ItemWire().setRegistryName("wire").setUnlocalizedName("wire");
	public static final Item STREET_SIGN = new ItemStreetSign().setRegistryName("street_sign").setUnlocalizedName("street_sign");
	public static final Item PHOTO = new ItemPhoto().setRegistryName("photo").setUnlocalizedName("photo");
	public static final Item ANALOG_CAMERA = new ItemAnalogCamera().setRegistryName("analog_camera").setUnlocalizedName("analog_camera");
	
	public static final Item SHORTS = new ItemClothingLegs(true,false,"shorts").setRegistryName("shorts").setUnlocalizedName("shorts");
	public static final Item SKIRT = new ItemClothingLegs(true,false,"skirt").setRegistryName("skirt").setUnlocalizedName("skirt");
	public static final Item JEANS = new ItemClothingLegs(true,true,"jeans").setRegistryName("jeans").setUnlocalizedName("jeans");
	public static final Item SHORTS_LONG = new ItemClothingLegs(true,false,"shorts_longer").setRegistryName("shorts_long").setUnlocalizedName("shorts_long");
	public static final Item JACKET = new ItemClothingChest(true,false,"jacket").setRegistryName("jacket").setUnlocalizedName("jacket");
	public static final Item JUMPER = new ItemClothingChest(true,false,"jumper").setRegistryName("jumper").setUnlocalizedName("jumper");
	public static final Item SHIRT = new ItemClothingChest(true,false,"shirt").setRegistryName("shirt").setUnlocalizedName("shirt");
	public static final Item SHORT_SLEEVED_SHIRT = new ItemClothingChest(true,false,"short_sleeved_shirt").setRegistryName("short_sleeved_shirt").setUnlocalizedName("short_sleeved_shirt");
	public static final Item T_SHIRT_0 = new ItemClothingChest(true,false,"t_shirt_0").setRegistryName("t_shirt_0").setUnlocalizedName("t_shirt_0");
	public static final Item T_SHIRT_1 = new ItemClothingChest(true,false,"t_shirt_1").setRegistryName("t_shirt_1").setUnlocalizedName("t_shirt_1");
	public static final Item COAT = new ItemClothingChest(true,false,"coat").setRegistryName("coat").setUnlocalizedName("coat");
	
	public static final Item CAR_BATTERY = new ItemQualityScrapable(EnumMaterial.LEAD,9).setRegistryName("car_battery").setUnlocalizedName("car_battery").setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY).setMaxStackSize(1);
	public static final Item SMALL_ENGINE = new ItemQualityScrapable(EnumMaterial.IRON,5).setRegistryName("small_engine").setUnlocalizedName("small_engine").setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY).setMaxStackSize(1);
    public static final Item MINIBIKE_SEAT = new ItemQualityScrapable(EnumMaterial.LEATHER,5).setRegistryName("minibike_seat").setUnlocalizedName("minibike_seat").setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY).setMaxStackSize(1);
	public static final Item MINIBIKE_HANDLES = new ItemQualityScrapable(EnumMaterial.IRON,4).setRegistryName("minibike_handles").setUnlocalizedName("minibike_handles").setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY).setMaxStackSize(1);
	public static final Item MINIBIKE_CHASSIS = new ItemQualityScrapable(EnumMaterial.IRON,4).setRegistryName("minibike_chassis").setUnlocalizedName("minibike_chassis").setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY).setMaxStackSize(1);
	
	public static final Item FIBER_CHESTPLATE = new ItemArmorBase(SevenDaysToMine.FIBER, 0, EntityEquipmentSlot.CHEST, "fiber_chestplate");
	public static final Item FIBER_LEGGINGS = new ItemArmorBase(SevenDaysToMine.FIBER, 1, EntityEquipmentSlot.LEGS, "fiber_leggings");
	public static final Item FIBER_BOOTS = new ItemArmorBase(SevenDaysToMine.FIBER, 0, EntityEquipmentSlot.FEET, "fiber_boots");
	public static final Item FIBER_HAT = new ItemArmorBase(SevenDaysToMine.FIBER, 0, EntityEquipmentSlot.HEAD, "fiber_hat");
	
	public static final Item STEEL_CHESTPLATE = new ItemArmorBase(SevenDaysToMine.STEEL_ARMOR, 0, EntityEquipmentSlot.CHEST, "steel_chestplate");
	public static final Item STEEL_LEGGINGS = new ItemArmorBase(SevenDaysToMine.STEEL_ARMOR, 1, EntityEquipmentSlot.LEGS, "steel_leggings");
	public static final Item STEEL_BOOTS = new ItemArmorBase(SevenDaysToMine.STEEL_ARMOR, 0, EntityEquipmentSlot.FEET, "steel_boots");
	public static final Item STEEL_HELMET = new ItemArmorBase(SevenDaysToMine.STEEL_ARMOR, 0, EntityEquipmentSlot.HEAD, "steel_helmet");

	public static final Item IRON_AXE = new ItemQualityAxe(SevenDaysToMine.IRON_TOOLS,-2.8).setRegistryName("iron_axe").setUnlocalizedName("iron_axe");
}