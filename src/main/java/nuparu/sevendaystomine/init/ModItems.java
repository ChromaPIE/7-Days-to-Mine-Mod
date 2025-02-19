package nuparu.sevendaystomine.init;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemFood;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.item.EnumLength;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.item.ItemAK47;
import nuparu.sevendaystomine.item.ItemAdvancedBandage;
import nuparu.sevendaystomine.item.ItemAlcoholDrink;
import nuparu.sevendaystomine.item.ItemAnalogCamera;
import nuparu.sevendaystomine.item.ItemAntibiotics;
import nuparu.sevendaystomine.item.ItemArmorBase;
import nuparu.sevendaystomine.item.ItemAuger;
import nuparu.sevendaystomine.item.ItemBackpack;
import nuparu.sevendaystomine.item.ItemBandage;
import nuparu.sevendaystomine.item.ItemBaneberry;
import nuparu.sevendaystomine.item.ItemBattery;
import nuparu.sevendaystomine.item.ItemBeret;
import nuparu.sevendaystomine.item.ItemBloodBag;
import nuparu.sevendaystomine.item.ItemBloodDrawKit;
import nuparu.sevendaystomine.item.ItemBlueberry;
import nuparu.sevendaystomine.item.ItemBlueprint;
import nuparu.sevendaystomine.item.ItemBullet;
import nuparu.sevendaystomine.item.ItemCannedFood;
import nuparu.sevendaystomine.item.ItemCannedSoup;
import nuparu.sevendaystomine.item.ItemCarChassis;
import nuparu.sevendaystomine.item.ItemChainsaw;
import nuparu.sevendaystomine.item.ItemChlorineGrenade;
import nuparu.sevendaystomine.item.ItemChristmasHat;
import nuparu.sevendaystomine.item.ItemCircuit;
import nuparu.sevendaystomine.item.ItemClothingChest;
import nuparu.sevendaystomine.item.ItemClothingLegs;
import nuparu.sevendaystomine.item.ItemClub;
import nuparu.sevendaystomine.item.ItemCoffeeBerry;
import nuparu.sevendaystomine.item.ItemCoffeeDrink;
import nuparu.sevendaystomine.item.ItemConcreteMix;
import nuparu.sevendaystomine.item.ItemCorn;
import nuparu.sevendaystomine.item.ItemDoorBase;
import nuparu.sevendaystomine.item.ItemDrink;
import nuparu.sevendaystomine.item.ItemEmptyCan;
import nuparu.sevendaystomine.item.ItemEmptyJar;
import nuparu.sevendaystomine.item.ItemFirstAidKit;
import nuparu.sevendaystomine.item.ItemFlamethrower;
import nuparu.sevendaystomine.item.ItemFlare;
import nuparu.sevendaystomine.item.ItemFoodBitable;
import nuparu.sevendaystomine.item.ItemFragmentationGrenade;
import nuparu.sevendaystomine.item.ItemGuide;
import nuparu.sevendaystomine.item.ItemGunPart;
import nuparu.sevendaystomine.item.ItemHoney;
import nuparu.sevendaystomine.item.ItemHuntingRifle;
import nuparu.sevendaystomine.item.ItemInstallDisc;
import nuparu.sevendaystomine.item.ItemLeatherArmor;
import nuparu.sevendaystomine.item.ItemLinkTool;
import nuparu.sevendaystomine.item.ItemM4;
import nuparu.sevendaystomine.item.ItemMP3;
import nuparu.sevendaystomine.item.ItemMP5;
import nuparu.sevendaystomine.item.ItemMagnum;
import nuparu.sevendaystomine.item.ItemMinibikeChassis;
import nuparu.sevendaystomine.item.ItemMold;
import nuparu.sevendaystomine.item.ItemMolotov;
import nuparu.sevendaystomine.item.ItemMurkyWater;
import nuparu.sevendaystomine.item.ItemNightVisionDevice;
import nuparu.sevendaystomine.item.ItemPhoto;
import nuparu.sevendaystomine.item.ItemPistol;
import nuparu.sevendaystomine.item.ItemQuality;
import nuparu.sevendaystomine.item.ItemQualityAxe;
import nuparu.sevendaystomine.item.ItemQualityBow;
import nuparu.sevendaystomine.item.ItemQualityHoe;
import nuparu.sevendaystomine.item.ItemQualityPickaxe;
import nuparu.sevendaystomine.item.ItemQualityScrapable;
import nuparu.sevendaystomine.item.ItemQualitySpade;
import nuparu.sevendaystomine.item.ItemQualitySword;
import nuparu.sevendaystomine.item.ItemRPG;
import nuparu.sevendaystomine.item.ItemRealityWand;
import nuparu.sevendaystomine.item.ItemRecipeBook;
import nuparu.sevendaystomine.item.ItemRefrigerator;
import nuparu.sevendaystomine.item.ItemRiotShield;
import nuparu.sevendaystomine.item.ItemScrap;
import nuparu.sevendaystomine.item.ItemScrapable;
import nuparu.sevendaystomine.item.ItemScrewdriver;
import nuparu.sevendaystomine.item.ItemShotgun;
import nuparu.sevendaystomine.item.ItemShotgunShort;
import nuparu.sevendaystomine.item.ItemSleepingBag;
import nuparu.sevendaystomine.item.ItemSniperRifle;
import nuparu.sevendaystomine.item.ItemStethoscope;
import nuparu.sevendaystomine.item.ItemStoneAxe;
import nuparu.sevendaystomine.item.ItemStoneShovel;
import nuparu.sevendaystomine.item.ItemStreetSign;
import nuparu.sevendaystomine.item.ItemTea;
import nuparu.sevendaystomine.item.ItemUpgrader;
import nuparu.sevendaystomine.item.ItemVoltmeter;
import nuparu.sevendaystomine.item.ItemWire;
import nuparu.sevendaystomine.item.ItemWrench;
import nuparu.sevendaystomine.tileentity.TileEntityComputer.EnumSystem;

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
	public static final Item COFFEE_BERRY = new ItemCoffeeBerry().setRegistryName("coffee_berry")
			.setUnlocalizedName("coffee_berry");
	public static final Item COFFEE_BEANS = new Item().setCreativeTab(SevenDaysToMine.TAB_MATERIALS).setRegistryName("coffee_beans")
			.setUnlocalizedName("coffee_beans");
	public static final Item CLOTH = new ItemScrap(EnumMaterial.CLOTH).setRegistryName("Cloth")
			.setUnlocalizedName("Cloth");

	public static final Item STONE_AXE = new ItemStoneAxe(SevenDaysToMine.STONE_TOOLS, -2).setRegistryName("StoneAxe")
			.setUnlocalizedName("StoneAxe");
	public static final Item STONE_SHOVEL = new ItemStoneShovel(SevenDaysToMine.STONE_TOOLS, -1f, 8f)
			.setRegistryName("StoneShovel").setUnlocalizedName("StoneShovel");
	public static final Item BONE_SHIV = new ItemQualitySword(SevenDaysToMine.BONE_TOOLS,EnumLength.SHORT).setRegistryName("BoneShiv")
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
	public static final Item CLAWHAMMER = new ItemUpgrader(SevenDaysToMine.IRON_TOOLS).setEffectiveness(0.5f)
			.setLength(EnumLength.SHORT).setRegistryName("Clawhammer").setUnlocalizedName("Clawhammer");
	public static final Item WRENCH = new ItemWrench().setEffectiveness(0.33334f)
			.setLength(EnumLength.SHORT).setRegistryName("Wrench").setUnlocalizedName("Wrench");
	public static final Item KITCHEN_KNIFE = new ItemQualitySword(SevenDaysToMine.IRON_TOOLS, EnumLength.SHORT)
			.setRegistryName("KitchenKnife").setUnlocalizedName("KitchenKnife");
	public static final Item ARMY_KNIFE = new ItemQualitySword(SevenDaysToMine.ARMY_TOOLS, EnumLength.SHORT)
			.setRegistryName("ArmyKnife").setUnlocalizedName("ArmyKnife");
	public static final Item MACHETE = new ItemQualitySword(SevenDaysToMine.MACHETE).setRegistryName("Machete")
			.setUnlocalizedName("Machete");
	public static final Item SCREWDRIVER = new ItemScrewdriver().setRegistryName("screwdriver")
			.setUnlocalizedName("screwdriver").setCreativeTab(CreativeTabs.TOOLS).setMaxStackSize(1);

	public static final Item PISTOL = new ItemPistol().setRegistryName("Pistol").setUnlocalizedName("Pistol");
	public static final Item AK47 = new ItemAK47().setRegistryName("AK47").setUnlocalizedName("AK47");
	public static final Item HUNTING_RIFLE = new ItemHuntingRifle().setRegistryName("hunting_rifle")
			.setUnlocalizedName("hunting_rifle");
	public static final Item MAGNUM = new ItemMagnum().setRegistryName("magnum").setUnlocalizedName("magnum");
	public static final Item MAGNUM_SCOPED = new ItemMagnum().setFOVFactor(2.5f).setScoped(true)
			.setRegistryName("magnum_scoped").setUnlocalizedName("magnum_scoped");
	public static final Item FLAMETHROWER = new ItemFlamethrower().setRegistryName("flamethrower")
			.setUnlocalizedName("flamethrower");
	public static final Item MP5 = new ItemMP5().setRegistryName("mp5").setUnlocalizedName("mp5");
	public static final Item RPG = new ItemRPG().setRegistryName("rpg").setUnlocalizedName("rpg");
	public static final Item M4 = new ItemM4().setRegistryName("m4").setUnlocalizedName("m4");

	public static final Item COOKING_GRILL = new ItemScrapable(EnumMaterial.IRON).setRegistryName("CookingGrill")
			.setUnlocalizedName("CookingGrill").setMaxStackSize(1);

	public static final Item BOTTLED_MURKY_WATER = new ItemMurkyWater(0, 250, 20).setRegistryName("BottledMurkyWater")
			.setUnlocalizedName("BottledMurkyWater").setContainerItem(EMPTY_JAR);
	public static final Item BOTTLED_WATER = new ItemDrink(0, 250, 50).setRegistryName("BottledWater")
			.setUnlocalizedName("BottledWater").setContainerItem(EMPTY_JAR);
	public static final Item BOTTLED_BEER = new ItemAlcoholDrink(0, 250, 50).setRegistryName("BottledBeer")
			.setUnlocalizedName("BottledBeer").setContainerItem(EMPTY_JAR);
	public static final Item GOLDENROD_TEA = new ItemTea(0, 300, 150).setRegistryName("GoldenrodTea")
			.setUnlocalizedName("GoldenrodTea").setContainerItem(EMPTY_JAR);
	public static final Item BOTTLED_COFFEE = new ItemCoffeeDrink(0, 300, 150,1,600).setRegistryName("BottledCoffee")
			.setUnlocalizedName("BottledCoffee").setContainerItem(EMPTY_JAR);
	public static final Item JAR_OF_HONEY = new ItemHoney().setRegistryName("JarOfHoney")
			.setUnlocalizedName("JarOfHoney").setContainerItem(EMPTY_JAR);
	public static final Item CANNED_MURKY_WATER = new ItemMurkyWater(0, 250, 0).setRegistryName("CannedMurkyWater")
			.setUnlocalizedName("CannedMurkyWater").setContainerItem(EMPTY_CAN);
	public static final Item CANNED_WATER = new ItemDrink(0, 250, 50).setRegistryName("CannedWater")
			.setUnlocalizedName("CannedWater").setContainerItem(EMPTY_CAN);

	public static final Item CANNED_CATFOOD = new ItemCannedFood(3, 1f, true, 2).setRegistryName("CannedCatFood")
			.setUnlocalizedName("CannedCatFood");
	public static final Item CANNED_DOGFOOD = new ItemCannedFood(3, 1f, true, 2).setRegistryName("CannedDogFood")
			.setUnlocalizedName("CannedDogFood");
	public static final Item CANNED_HAM = new ItemCannedFood(6, 2.5f, true, 3).setRegistryName("CannedHam")
			.setUnlocalizedName("CannedHam");
	public static final Item CANNED_CHICKEN = new ItemCannedFood(5, 2f, true, 4).setRegistryName("CannedChicken")
			.setUnlocalizedName("CannedChicken");
	public static final Item CANNED_CHILI = new ItemCannedFood(4, 2f, false, 2).setRegistryName("CannedChili")
			.setUnlocalizedName("CannedChili");
	public static final Item CANNED_MISO = new ItemCannedSoup(4, 1.5f, false, 2, 50,5).setRegistryName("CannedMiso")
			.setUnlocalizedName("CannedMiso");
	public static final Item CANNED_PASTA = new ItemCannedFood(5, 2f, false, 2).setRegistryName("CannedPasta")
			.setUnlocalizedName("CannedPasta");
	public static final Item CANNED_PEARS = new ItemCannedFood(4,1.5f, false, 2).setRegistryName("CannedPears")
			.setUnlocalizedName("CannedPears");
	public static final Item CANNED_PEAS = new ItemCannedFood(4, 1.5f, false, 2).setRegistryName("CannedPeas")
			.setUnlocalizedName("CannedPeas");
	public static final Item CANNED_SALMON = new ItemCannedFood(5, 2f, false, 3).setRegistryName("CannedSalmon")
			.setUnlocalizedName("CannedSalmon");
	public static final Item CANNED_SOUP = new ItemCannedSoup(5, 1.75f, false, 2, 50,5).setRegistryName("CannedSoup")
			.setUnlocalizedName("CannedSoup");
	public static final Item CANNED_STOCK = new ItemCannedSoup(4, 1.6f, false, 3, 50,5).setRegistryName("CannedStock")
			.setUnlocalizedName("CannedStock");
	public static final Item CANNED_TUNA = new ItemCannedFood(5, 2f, false, 3).setRegistryName("CannedTuna")
			.setUnlocalizedName("CannedTuna");
	public static final Item CANNED_BEEF = new ItemCannedFood(7, 2.5f, true, 4).setRegistryName("CannedBeef")
			.setUnlocalizedName("CannedBeef");
	public static final Item CANNED_LAMB = new ItemCannedFood(7, 2.5f, true, 4).setRegistryName("CannedLamb")
			.setUnlocalizedName("CannedLamb");
	
	public static final Item MRE = new ItemFoodBitable(7, 3f, true, 8).setRegistryName("mre")
			.setUnlocalizedName("mre");

	public static final Item CORN = new ItemCorn().setRegistryName("Corn").setUnlocalizedName("Corn");
	public static final Item BLUEBERRY = new ItemBlueberry().setRegistryName("Blueberry")
			.setUnlocalizedName("Blueberry");
	public static final Item BANEBERRY = new ItemBaneberry().setRegistryName("Baneberry")
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

	/*public static final Item INGOT_IRON = new ItemScrapable(EnumMaterial.IRON, 6).setRegistryName("IronIngot")
			.setUnlocalizedName("IronIngot");*/
	public static final Item INGOT_LEAD = new ItemScrapable(EnumMaterial.LEAD, 6).setRegistryName("LeadIngot")
			.setUnlocalizedName("LeadIngot");
	public static final Item INGOT_BRASS = new ItemScrapable(EnumMaterial.BRASS, 6).setRegistryName("BrassIngot")
			.setUnlocalizedName("BrassIngot");
	public static final Item INGOT_STEEL = new ItemScrapable(EnumMaterial.STEEL, 6).setSmallestBit()
			.setRegistryName("SteelIngot").setUnlocalizedName("SteelIngot");
	public static final Item INGOT_COPPER = new ItemScrapable(EnumMaterial.COPPER, 6).setSmallestBit()
			.setRegistryName("CopperIngot").setUnlocalizedName("CopperIngot");
	public static final Item INGOT_TIN = new ItemScrapable(EnumMaterial.TIN, 6).setSmallestBit()
			.setRegistryName("TinIngot").setUnlocalizedName("TinIngot");
	public static final Item INGOT_ZINC = new ItemScrapable(EnumMaterial.ZINC, 6).setSmallestBit()
			.setRegistryName("ZincIngot").setUnlocalizedName("ZincIngot");
	public static final Item INGOT_BRONZE = new ItemScrapable(EnumMaterial.BRONZE, 6).setSmallestBit()
			.setRegistryName("BronzeIngot").setUnlocalizedName("BronzeIngot");
	/*public static final Item INGOT_GOLD = new ItemScrapable(EnumMaterial.GOLD, 6).setRegistryName("GoldIngot")
			.setUnlocalizedName("GoldIngot");*/
	public static final Item CENT = new ItemScrapable(EnumMaterial.BRASS, 6).setRegistryName("cent")
			.setUnlocalizedName("cent");
	public static final Item BACKPACK = new ItemBackpack().setRegistryName("backpack").setUnlocalizedName("backpack");

	public static final Item MOLD_INGOT = new ItemMold().setRegistryName("IngotMold").setUnlocalizedName("IngotMold");
	public static final Item BULLET_TIP_MOLD = new ItemMold().setRegistryName("bullet_tip_mold")
			.setUnlocalizedName("bullet_tip_mold");
	public static final Item BULLET_CASING_MOLD = new ItemMold().setRegistryName("bullet_casing_mold")
			.setUnlocalizedName("bullet_casing_mold");
	public static final Item CEMENT_MOLD = new ItemMold().setRegistryName("cement_mold")
			.setUnlocalizedName("cement_mold");

	public static final Item CONCRETE_MIX = new ItemConcreteMix().setRegistryName("ConcreteMix")
			.setUnlocalizedName("ConcreteMix").setCreativeTab(SevenDaysToMine.TAB_MATERIALS);
	public static final Item CEMENT = new ItemScrap(EnumMaterial.CONCRETE).setRegistryName("Cement")
			.setUnlocalizedName("Cement");

	public static final Item NINE_MM_BULLET = new ItemBullet().setRegistryName("NineMMBullet")
			.setUnlocalizedName("NineMMBullet").setCreativeTab(CreativeTabs.COMBAT);
	public static final Item SEVEN_MM_BULLET = new ItemBullet().setRegistryName("SevenMMBullet")
			.setUnlocalizedName("SevenMMBullet").setCreativeTab(CreativeTabs.COMBAT);
	public static final Item TEN_MM_BULLET = new ItemBullet().setRegistryName("bullet_10mm").setUnlocalizedName("bullet_10mm")
			.setCreativeTab(CreativeTabs.COMBAT);
	public static final Item MAGNUM_BULLET = new ItemBullet().setRegistryName("bullet_magnum")
			.setUnlocalizedName("bullet_magnum").setCreativeTab(CreativeTabs.COMBAT);
	public static final Item SHOTGUN_SHELL = new ItemBullet().setRegistryName("shotgun_shell")
			.setUnlocalizedName("shotgun_shell").setCreativeTab(CreativeTabs.COMBAT);
	public static final Item ROCKET = new ItemBullet().setRegistryName("rocket")
			.setUnlocalizedName("rocket").setCreativeTab(CreativeTabs.COMBAT);
	
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
	
	public static final Item WOODEN_DOOR_IRON_REINFORCED_ITEM = new ItemDoorBase() {
		public Block getDoor() {
			return ModBlocks.WOODEN_DOOR_IRON_REINFORCED;
		}
	}.setRegistryName("wooden_door_iron_reinforced_item").setUnlocalizedName("wooden_door_iron_reinforced_item")
			.setCreativeTab(SevenDaysToMine.TAB_BUILDING);


	public static final Item LOCKED_DOOR_ITEM = new ItemDoorBase() {
		public Block getDoor() {
			return ModBlocks.LOCKED_DOOR;
		}
	}.setRegistryName("locked_door_item").setUnlocalizedName("locked_door_item")
			.setCreativeTab(SevenDaysToMine.TAB_BUILDING);

	public static final Item AUGER = new ItemAuger(20, 20, SevenDaysToMine.AUGER).setRegistryName("auger")
			.setUnlocalizedName("auger");
	public static final Item CHAINSAW = new ItemChainsaw(20, 20, SevenDaysToMine.AUGER).setRegistryName("chainsaw")
			.setUnlocalizedName("chainsaw");

	public static final Item COPPER_PICKAXE = new ItemQualityPickaxe(SevenDaysToMine.COPPER_TOOLS, -2.8)
			.setRegistryName("copper_pickaxe").setUnlocalizedName("copper_pickaxe");
	public static final Item BRONZE_PICKAXE = new ItemQualityPickaxe(SevenDaysToMine.BRONZE_TOOLS, -2.8)
			.setRegistryName("bronze_pickaxe").setUnlocalizedName("bronze_pickaxe");
	public static final Item IRON_PICKAXE = new ItemQualityPickaxe(SevenDaysToMine.IRON_TOOLS, -2.8)
			.setRegistryName("iron_pickaxe").setUnlocalizedName("iron_pickaxe");
	public static final Item STEEL_PICKAXE = new ItemQualityPickaxe(SevenDaysToMine.STEEL_TOOLS, -2.8)
			.setRegistryName("steel_pickaxe").setUnlocalizedName("steel_pickaxe");
	public static final Item SCRAP_PICKAXE = new ItemQualityPickaxe(SevenDaysToMine.SCRAP_TOOLS, -2.8)
			.setRegistryName("scrap_pickaxe").setUnlocalizedName("scrap_pickaxe");

	public static final Item MP3_PLAYER = new ItemMP3().setRegistryName("MP3Player").setUnlocalizedName("MP3Player")
			.setCreativeTab(CreativeTabs.TOOLS);

	public static final Item RAM = new Item().setRegistryName("ram").setUnlocalizedName("ram").setMaxStackSize(16)
			.setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
	public static final Item MOTHERBOARD = new Item().setRegistryName("motherboard").setUnlocalizedName("motherboard")
			.setMaxStackSize(1).setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
	public static final Item CPU = new Item().setRegistryName("cpu").setUnlocalizedName("cpu").setMaxStackSize(16)
			.setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
	public static final Item GPU = new Item().setRegistryName("gpu").setUnlocalizedName("gpu").setMaxStackSize(16)
			.setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
	public static final Item HDD = new Item().setRegistryName("hdd").setUnlocalizedName("hdd").setMaxStackSize(16)
			.setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
	public static final Item POWER_SUPPLY = new Item().setRegistryName("power_supply")
			.setUnlocalizedName("power_supply").setMaxStackSize(16).setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);

	public static final Item DISC = new Item().setRegistryName("disc").setUnlocalizedName("disc").setMaxStackSize(16)
			.setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
	public static final Item LINUX_DISC = new ItemInstallDisc(EnumSystem.LINUX).setRegistryName("linux_disc")
			.setUnlocalizedName("linux_disc").setMaxStackSize(1);
	public static final Item MAC_DISC = new ItemInstallDisc(EnumSystem.MAC).setRegistryName("mac_disc")
			.setUnlocalizedName("mac_disc").setMaxStackSize(1);
	public static final Item WIN98_DISC = new ItemInstallDisc(EnumSystem.WIN98).setRegistryName("win98_disc")
			.setUnlocalizedName("win98_disc").setMaxStackSize(1);
	public static final Item WINXP_DISC = new ItemInstallDisc(EnumSystem.WINXP).setRegistryName("winxp_disc")
			.setUnlocalizedName("winxp_disc").setMaxStackSize(1);
	public static final Item WIN7_DISC = new ItemInstallDisc(EnumSystem.WIN7).setRegistryName("win7_disc")
			.setUnlocalizedName("win7_disc").setMaxStackSize(1);
	public static final Item WIN8_DISC = new ItemInstallDisc(EnumSystem.WIN8).setRegistryName("win8_disc")
			.setUnlocalizedName("win8_disc").setMaxStackSize(1);
	public static final Item WIN10_DISC = new ItemInstallDisc(EnumSystem.WIN10).setRegistryName("win10_disc")
			.setUnlocalizedName("win10_disc").setMaxStackSize(1);

	public static final Item WIRE = new ItemWire().setRegistryName("wire").setUnlocalizedName("wire");
	public static final Item STREET_SIGN = new ItemStreetSign().setRegistryName("street_sign")
			.setUnlocalizedName("street_sign");
	public static final Item PHOTO = new ItemPhoto().setRegistryName("photo").setUnlocalizedName("photo");
	public static final Item ANALOG_CAMERA = new ItemAnalogCamera().setRegistryName("analog_camera")
			.setUnlocalizedName("analog_camera");

	public static final Item SHORTS = new ItemClothingLegs(true, false, "shorts").setRegistryName("shorts")
			.setUnlocalizedName("shorts");
	public static final Item SKIRT = new ItemClothingLegs(true, false, "skirt").setRegistryName("skirt")
			.setUnlocalizedName("skirt");
	public static final Item JEANS = new ItemClothingLegs(true, true, "jeans").setRegistryName("jeans")
			.setUnlocalizedName("jeans");
	public static final Item SHORTS_LONG = new ItemClothingLegs(true, false, "shorts_longer")
			.setRegistryName("shorts_long").setUnlocalizedName("shorts_long");
	public static final Item JACKET = new ItemClothingChest(true, false, "jacket").setRegistryName("jacket")
			.setUnlocalizedName("jacket");
	public static final Item JUMPER = new ItemClothingChest(true, false, "jumper").setRegistryName("jumper")
			.setUnlocalizedName("jumper");
	public static final Item SHIRT = new ItemClothingChest(true, false, "shirt").setRegistryName("shirt")
			.setUnlocalizedName("shirt");
	public static final Item SHORT_SLEEVED_SHIRT = new ItemClothingChest(true, false, "short_sleeved_shirt")
			.setRegistryName("short_sleeved_shirt").setUnlocalizedName("short_sleeved_shirt");
	public static final Item T_SHIRT_0 = new ItemClothingChest(true, false, "t_shirt_0").setRegistryName("t_shirt_0")
			.setUnlocalizedName("t_shirt_0");
	public static final Item T_SHIRT_1 = new ItemClothingChest(true, false, "t_shirt_1").setRegistryName("t_shirt_1")
			.setUnlocalizedName("t_shirt_1");
	public static final Item COAT = new ItemClothingChest(true, false, "coat").setRegistryName("coat")
			.setUnlocalizedName("coat");

	public static final Item CAR_BATTERY = new ItemBattery(EnumMaterial.LEAD, 9).setRegistryName("car_battery")
			.setUnlocalizedName("car_battery").setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY).setMaxStackSize(1);
	public static final Item SMALL_ENGINE = new ItemQualityScrapable(EnumMaterial.IRON, 5)
			.setRegistryName("small_engine").setUnlocalizedName("small_engine")
			.setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY).setMaxStackSize(1);
	public static final Item MINIBIKE_SEAT = new ItemQualityScrapable(EnumMaterial.LEATHER, 5)
			.setRegistryName("minibike_seat").setUnlocalizedName("minibike_seat")
			.setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY).setMaxStackSize(1);
	public static final Item MINIBIKE_HANDLES = new ItemQualityScrapable(EnumMaterial.IRON, 4)
			.setRegistryName("minibike_handles").setUnlocalizedName("minibike_handles")
			.setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY).setMaxStackSize(1);
	public static final Item MINIBIKE_CHASSIS = new ItemMinibikeChassis().setRegistryName("minibike_chassis")
			.setUnlocalizedName("minibike_chassis").setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY).setMaxStackSize(1);
	public static final Item CAR_CHASSIS = new ItemCarChassis().setRegistryName("car_chassis")
			.setUnlocalizedName("car_chassis").setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY).setMaxStackSize(1);

	public static final Item FIBER_CHESTPLATE = new ItemArmorBase(SevenDaysToMine.FIBER, 0, EntityEquipmentSlot.CHEST,
			"fiber_chestplate");
	public static final Item FIBER_LEGGINGS = new ItemArmorBase(SevenDaysToMine.FIBER, 1, EntityEquipmentSlot.LEGS,
			"fiber_leggings");
	public static final Item FIBER_BOOTS = new ItemArmorBase(SevenDaysToMine.FIBER, 0, EntityEquipmentSlot.FEET,
			"fiber_boots");
	public static final Item FIBER_HAT = new ItemArmorBase(SevenDaysToMine.FIBER, 0, EntityEquipmentSlot.HEAD,
			"fiber_hat");

	public static final Item STEEL_CHESTPLATE = new ItemArmorBase(SevenDaysToMine.STEEL_ARMOR, 0,
			EntityEquipmentSlot.CHEST, "steel_chestplate");
	public static final Item STEEL_LEGGINGS = new ItemArmorBase(SevenDaysToMine.STEEL_ARMOR, 1,
			EntityEquipmentSlot.LEGS, "steel_leggings");
	public static final Item STEEL_BOOTS = new ItemArmorBase(SevenDaysToMine.STEEL_ARMOR, 0, EntityEquipmentSlot.FEET,
			"steel_boots");
	public static final Item STEEL_HELMET = new ItemArmorBase(SevenDaysToMine.STEEL_ARMOR, 0, EntityEquipmentSlot.HEAD,
			"steel_helmet");

	public static final Item LEATHER_IRON_CHESTPLATE = new ItemArmorBase(SevenDaysToMine.LEATHER_IRON_ARMOR, 0,
			EntityEquipmentSlot.CHEST, "leather_iron_chestplate");
	public static final Item LEATHER_IRON_LEGGINGS = new ItemArmorBase(SevenDaysToMine.LEATHER_IRON_ARMOR, 1,
			EntityEquipmentSlot.LEGS, "leather_iron_leggings");
	public static final Item LEATHER_IRON_BOOTS = new ItemArmorBase(SevenDaysToMine.LEATHER_IRON_ARMOR, 0,
			EntityEquipmentSlot.FEET, "leather_iron_boots");
	public static final Item LEATHER_IRON_HELMET = new ItemArmorBase(SevenDaysToMine.LEATHER_IRON_ARMOR, 0,
			EntityEquipmentSlot.HEAD, "leather_iron_helmet");
	
	public static final Item IRON_CHESTPLATE = new ItemArmorBase(SevenDaysToMine.IRON_ARMOR ,0,
			EntityEquipmentSlot.CHEST, "iron_chestplate");
	public static final Item IRON_LEGGINGS = new ItemArmorBase(SevenDaysToMine.IRON_ARMOR, 1,
			EntityEquipmentSlot.LEGS, "iron_leggings");
	public static final Item IRON_BOOTS = new ItemArmorBase(SevenDaysToMine.IRON_ARMOR, 0, EntityEquipmentSlot.FEET,
			"iron_boots");
	public static final Item IRON_HELMET = new ItemArmorBase(SevenDaysToMine.IRON_ARMOR, 0, EntityEquipmentSlot.HEAD,
			"iron_helmet");
	
	public static final Item LEATHER_CHESTPLATE = new ItemLeatherArmor(ArmorMaterial.LEATHER ,0,
			EntityEquipmentSlot.CHEST, "leather_chestplate");
	public static final Item LEATHER_LEGGINGS = new ItemLeatherArmor(ArmorMaterial.LEATHER, 1,
			EntityEquipmentSlot.LEGS, "leather_leggings");
	public static final Item LEATHER_BOOTS = new ItemLeatherArmor(ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.FEET,
			"leather_boots");
	public static final Item LEATHER_HELMET = new ItemLeatherArmor(ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.HEAD,
			"leather_helmet");
	
	public static final Item MILITARY_CHESTPLATE = new ItemArmorBase(SevenDaysToMine.MILITARY_ARMOR, 0,
			EntityEquipmentSlot.CHEST, "military_chestplate");
	public static final Item MILITARY_LEGGINGS = new ItemArmorBase(SevenDaysToMine.MILITARY_ARMOR, 1,
			EntityEquipmentSlot.LEGS, "military_leggings");
	public static final Item MILITARY_BOOTS = new ItemArmorBase(SevenDaysToMine.MILITARY_ARMOR, 0, EntityEquipmentSlot.FEET,
			"military_boots");
	public static final Item MILITARY_HELMET = new ItemArmorBase(SevenDaysToMine.MILITARY_ARMOR, 0, EntityEquipmentSlot.HEAD,
			"military_helmet");

	public static final Item COPPER_AXE = new ItemQualityAxe(SevenDaysToMine.COPPER_TOOLS, -2.8)
			.setRegistryName("copper_axe").setUnlocalizedName("copper_axe");
	public static final Item BRONZE_AXE = new ItemQualityAxe(SevenDaysToMine.BRONZE_TOOLS, -2.8)
			.setRegistryName("bronze_axe").setUnlocalizedName("bronze_axe");
	public static final Item IRON_AXE = new ItemQualityAxe(SevenDaysToMine.IRON_TOOLS, -2.8).setRegistryName("iron_axe")
			.setUnlocalizedName("iron_axe");
	public static final Item SCRAP_AXE = new ItemQualityAxe(SevenDaysToMine.SCRAP_TOOLS, -2.8)
			.setRegistryName("scrap_axe").setUnlocalizedName("scrap_axe");
	public static final Item STEEL_AXE = new ItemQualityAxe(SevenDaysToMine.STEEL_TOOLS, -2.8)
			.setRegistryName("steel_axe").setUnlocalizedName("steel_axe");

	public static final Item COPPER_SHOVEL = new ItemQualitySpade(SevenDaysToMine.COPPER_TOOLS)
			.setRegistryName("copper_shovel").setUnlocalizedName("copper_shovel");
	public static final Item BRONZE_SHOVEL = new ItemQualitySpade(SevenDaysToMine.BRONZE_TOOLS)
			.setRegistryName("bronze_shovel").setUnlocalizedName("bronze_shovel");
	public static final Item IRON_SHOVEL = new ItemQualitySpade(SevenDaysToMine.IRON_TOOLS)
			.setRegistryName("iron_shovel").setUnlocalizedName("iron_shovel");
	public static final Item STEEL_SHOVEL = new ItemQualitySpade(SevenDaysToMine.STEEL_TOOLS)
			.setRegistryName("steel_shovel").setUnlocalizedName("steel_shovel");
	public static final Item SCRAP_SHOVEL = new ItemQualitySpade(SevenDaysToMine.SCRAP_TOOLS)
			.setRegistryName("scrap_shovel").setUnlocalizedName("scrap_shovel");

	public static final Item VOLTMETER = new ItemVoltmeter().setRegistryName("voltmeter")
			.setUnlocalizedName("voltmeter");

	public static final Item IRON_PIPE = new ItemScrapable(EnumMaterial.IRON, 1).setRegistryName("iron_pipe")
			.setUnlocalizedName("iron_pipe");

	public static final Item CIRCUIT = new ItemCircuit().setRegistryName("circuit").setUnlocalizedName("circuit");
	public static final Item LINK_TOOL = new ItemLinkTool().setRegistryName("link_tool")
			.setUnlocalizedName("link_tool");

	public static final Item SURVIVAL_GUIDE = new ItemGuide(
			new ResourceLocation(SevenDaysToMine.MODID, "data/books/survival.json")).setRegistryName("survival_guide")
					.setUnlocalizedName("survival_guide");
	public static final Item BOOK_FORGING = new ItemRecipeBook(
			new ResourceLocation(SevenDaysToMine.MODID, "data/books/forging.json"), "forging")
					.setRegistryName("book_forging").setUnlocalizedName("book_forging");
	public static final Item BOOK_AMMO = new ItemRecipeBook(
			new ResourceLocation(SevenDaysToMine.MODID, "data/books/ammo.json"), "ammo").setRegistryName("book_ammo")
					.setUnlocalizedName("book_ammo");
	public static final Item BOOK_COMPUTERS = new ItemRecipeBook(
			new ResourceLocation(SevenDaysToMine.MODID, "data/books/computers.json"), "computers")
					.setRegistryName("book_computers").setUnlocalizedName("book_computers");
	public static final Item BOOK_CONCRETE = new ItemRecipeBook(
			new ResourceLocation(SevenDaysToMine.MODID, "data/books/concrete.json"), "concrete")
					.setRegistryName("book_concrete").setUnlocalizedName("book_concrete");
	public static final Item BOOK_ELECTRICITY = new ItemRecipeBook(
			new ResourceLocation(SevenDaysToMine.MODID, "data/books/electricity.json"), "electricity")
					.setRegistryName("book_electricity").setUnlocalizedName("book_electricity");
	public static final Item BOOK_CHEMISTRY = new ItemRecipeBook(
			new ResourceLocation(SevenDaysToMine.MODID, "data/books/chemistry.json"), "chemistry")
					.setRegistryName("book_chemistry").setUnlocalizedName("book_chemistry");
	public static final Item BOOK_METALWORKING = new ItemRecipeBook(
			new ResourceLocation(SevenDaysToMine.MODID, "data/books/metalworking.json"), "metalworking")
					.setRegistryName("book_metalworking").setUnlocalizedName("book_metalworking");
	public static final Item BOOK_PISTOL = new ItemRecipeBook(
			new ResourceLocation(SevenDaysToMine.MODID, "data/books/pistol.json"), "pistol")
					.setRegistryName("book_pistol").setUnlocalizedName("book_pistol");
	public static final Item BOOK_MINIBIKE = new ItemRecipeBook(
			new ResourceLocation(SevenDaysToMine.MODID, "data/books/minibike.json"), "minibike")
					.setRegistryName("book_minibike").setUnlocalizedName("book_minibike");

	public static final Item SHOTGUN_SCHEMATICS = new ItemBlueprint(
			new ResourceLocation(SevenDaysToMine.MODID, "data/books/shotgun.json"), "shotgun")
					.setRegistryName("book_shotgun").setUnlocalizedName("book_shotgun");
	public static final Item SNIPER_SCHEMATICS = new ItemBlueprint(
			new ResourceLocation(SevenDaysToMine.MODID, "data/books/sniper.json"), "sniper")
					.setRegistryName("book_sniper").setUnlocalizedName("book_sniper");
	public static final Item MAGNUM_SCHEMATICS = new ItemBlueprint(
			new ResourceLocation(SevenDaysToMine.MODID, "data/books/magnum.json"), "magnum")
					.setRegistryName("book_magnum").setUnlocalizedName("book_magnum");
	public static final Item MP5_SCHEMATICS = new ItemBlueprint(
			new ResourceLocation(SevenDaysToMine.MODID, "data/books/mp5.json"), "mp5").setRegistryName("book_mp5")
					.setUnlocalizedName("book_mp5");
	public static final Item HUNTING_RIFLE_SCHEMATICS = new ItemBlueprint(
			new ResourceLocation(SevenDaysToMine.MODID, "data/books/hunting_rifle.json"), "hunting_rifle").setRegistryName("book_hunting_rifle")
					.setUnlocalizedName("book_hunting_rifle");
	
	public static final Item AUGER_SCHEMATICS = new ItemBlueprint(
			new ResourceLocation(SevenDaysToMine.MODID, "data/books/auger.json"), "auger").setRegistryName("book_auger")
					.setUnlocalizedName("book_auger");
	
	public static final Item ROCKET_SCHEMATICS = new ItemBlueprint(
			new ResourceLocation(SevenDaysToMine.MODID, "data/books/rocket.json"), "rocket").setRegistryName("book_rocket")
					.setUnlocalizedName("book_rocket");

	public static final Item MICROPHONE = new Item().setRegistryName("microphone").setUnlocalizedName("microphone");
	public static final Item BULLET_TIP = new ItemScrapable(EnumMaterial.LEAD, 1).setRegistryName("bullet_tip")
			.setUnlocalizedName("bullet_tip").setCreativeTab(SevenDaysToMine.TAB_FORGING);
	public static final Item BULLET_CASING = new ItemScrapable(EnumMaterial.BRASS, 1).setRegistryName("bullet_casing")
			.setUnlocalizedName("bullet_casing").setCreativeTab(SevenDaysToMine.TAB_FORGING);
	public static final Item MOLDY_BREAD = new ItemFood(2, 0.3F, false)
			.setPotionEffect(new PotionEffect(MobEffects.POISON, 100, 0), 0.6F).setRegistryName("moldy_bread")
			.setUnlocalizedName("moldy_bread").setMaxStackSize(1);

	public static final Item SLEDGEHAMMER = new ItemClub(SevenDaysToMine.SLEDGEHAMMER, -3.6d)
			.setRegistryName("sledgehammer").setUnlocalizedName("sledgehammer");

	public static Item PISTOL_SLIDE = new ItemGunPart("pistol_slide",EnumMaterial.IRON,2);
	public static Item PISTOL_TRIGGER = new ItemGunPart("pistol_trigger",EnumMaterial.IRON,4);
	public static Item PISTOL_GRIP = new ItemGunPart("pistol_grip",EnumMaterial.IRON,3);
	public static Item SNIPER_RIFLE_BARREL = new ItemGunPart("sniper_rifle_barrel",EnumMaterial.IRON,3);
	public static Item SNIPER_RIFLE_TRIGGER = new ItemGunPart("sniper_rifle_trigger",EnumMaterial.IRON,6);
	public static Item SNIPER_RIFLE_SCOPE = new ItemGunPart("sniper_rifle_scope",EnumMaterial.IRON,4);
	public static Item SNIPER_RIFLE_PARTS = new ItemGunPart("sniper_rifle_parts",EnumMaterial.IRON,2).setCreativeTab(null);
	public static Item SNIPER_RIFLE_STOCK = new ItemGunPart("sniper_rifle_stock",EnumMaterial.IRON,6);

	public static Item SHOTGUN_RECEIVER = new ItemGunPart("shotgun_receiver",EnumMaterial.IRON,4);
	public static Item SHOTGUN_STOCK = new ItemGunPart("shotgun_stock",EnumMaterial.WOOD,3);
	public static Item SHOTGUN_STOCK_SHORT = new ItemGunPart("shotgun_stock_short",EnumMaterial.WOOD,2);
	public static Item SHOTGUN_PARTS = new ItemGunPart("shotgun_parts",EnumMaterial.IRON,2);
	public static Item SHOTGUN_BARREL = new ItemGunPart("shotgun_barrel",EnumMaterial.IRON,3);
	public static Item SHOTGUN_BARREL_SHORT = new ItemGunPart("shotgun_barrel_short",EnumMaterial.IRON,2);

	public static Item MAGNUM_FRAME = new ItemGunPart("magnum_frame",EnumMaterial.IRON,4);
	public static Item MAGNUM_CYLINDER = new ItemGunPart("magnum_cylinder",EnumMaterial.IRON,4);
	public static Item MAGNUM_GRIP = new ItemGunPart("magnum_grip",EnumMaterial.IRON,2);

	public static Item MP5_BARREL = new ItemGunPart("mp5_barrel",EnumMaterial.IRON,4);
	public static Item MP5_TRIGGER = new ItemGunPart("mp5_trigger",EnumMaterial.IRON,6);
	public static Item MP5_STOCK = new ItemGunPart("mp5_stock",EnumMaterial.IRON,4);
	
	public static Item HUNTING_RIFLE_BARREL = new ItemGunPart("hunting_rifle_barrel",EnumMaterial.IRON,3);
	public static Item HUNTING_RIFLE_STOCK = new ItemGunPart("hunting_rifle_stock",EnumMaterial.WOOD,4);
	public static Item HUNTING_RIFLE_PARTS = new ItemGunPart("hunting_rifle_parts",EnumMaterial.IRON,2);
	public static Item HUNTING_RIFLE_BOLT = new ItemGunPart("hunting_rifle_bolt",EnumMaterial.IRON,4);

	public static final Item SCRAP_CHESTPLATE = new ItemArmorBase(SevenDaysToMine.SCRAP_ARMOR, 0,
			EntityEquipmentSlot.CHEST, "scrap_chestplate");
	public static final Item SCRAP_LEGGINGS = new ItemArmorBase(SevenDaysToMine.SCRAP_ARMOR, 1,
			EntityEquipmentSlot.LEGS, "scrap_leggings");
	public static final Item SCRAP_BOOTS = new ItemArmorBase(SevenDaysToMine.SCRAP_ARMOR, 0, EntityEquipmentSlot.FEET,
			"scrap_boots");
	public static final Item SCRAP_HELMET = new ItemArmorBase(SevenDaysToMine.SCRAP_ARMOR, 0, EntityEquipmentSlot.HEAD,
			"scrap_helmet");

	public static final Item SNIPER_RIFLE = new ItemSniperRifle().setRegistryName("sniper_rifle")
			.setUnlocalizedName("sniper_rifle");
	public static final Item SHOTGUN = new ItemShotgun().setRegistryName("shotgun").setUnlocalizedName("shotgun");
	public static final Item SHOTGUN_SAWED_OFF = new ItemShotgunShort().setRegistryName("shotgun_short")
			.setUnlocalizedName("shotgun_short");

	public static final Item BELLOWS = new Item().setMaxStackSize(1).setCreativeTab(SevenDaysToMine.TAB_FORGING)
			.setRegistryName("bellows").setUnlocalizedName("bellows");
	public static final Item MAGNET = new Item().setMaxStackSize(16).setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY)
			.setRegistryName("magnet").setUnlocalizedName("magnet");
	public static final Item PHOTO_CELL = new Item().setMaxStackSize(16).setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY)
			.setRegistryName("photo_cell").setUnlocalizedName("photo_cell");
	public static final Item POTASSIUM = new ItemScrap(EnumMaterial.POTASSIUM).setMaxStackSize(64)
			.setCreativeTab(SevenDaysToMine.TAB_MATERIALS).setRegistryName("potassium").setUnlocalizedName("potassium");

	public static final Item GAS_CANISTER = new ItemScrap(EnumMaterial.GASOLINE).setMaxStackSize(64)
			.setCreativeTab(SevenDaysToMine.TAB_MATERIALS).setRegistryName("gas_canister")
			.setUnlocalizedName("gas_canister");
	public static final Item AUGER_BLADE = new ItemQuality().setMaxStackSize(1)
			.setCreativeTab(SevenDaysToMine.TAB_FORGING).setRegistryName("auger_blade")
			.setUnlocalizedName("auger_blade");

	public static final Item NIGHT_VISION_DEVICE = new ItemNightVisionDevice(SevenDaysToMine.SCRAP_ARMOR, 0,
			EntityEquipmentSlot.HEAD).setMaxStackSize(1).setCreativeTab(CreativeTabs.COMBAT)
					.setRegistryName("night_vision_device").setUnlocalizedName("night_vision_device");

	public static final Item CHRISTMAS_HAT = new ItemChristmasHat(SevenDaysToMine.FIBER, 0, EntityEquipmentSlot.HEAD)
			.setMaxStackSize(1).setCreativeTab(SevenDaysToMine.TAB_CLOTHING).setRegistryName("christmas_hat")
			.setUnlocalizedName("christmas_hat");

	public static final Item BERET = new ItemBeret(SevenDaysToMine.FIBER, 0, EntityEquipmentSlot.HEAD)
			.setMaxStackSize(1).setCreativeTab(SevenDaysToMine.TAB_CLOTHING).setRegistryName("beret")
			.setUnlocalizedName("beret");

	public static final Item STETHOSCOPE = new ItemStethoscope().setMaxStackSize(1).setCreativeTab(CreativeTabs.TOOLS)
			.setRegistryName("stethoscope").setUnlocalizedName("stethoscope");

	public static final Item BLOODMOON = new Item().setMaxStackSize(1).setRegistryName("bloodmoon")
			.setUnlocalizedName("bloodmoon");
	
	public static final Item SCRAP_HOE = new ItemQualityHoe(SevenDaysToMine.SCRAP_TOOLS).setRegistryName("scrap_hoe")
			.setUnlocalizedName("scrap_hoe");
	public static final Item COPPER_HOE = new ItemQualityHoe(SevenDaysToMine.COPPER_TOOLS).setRegistryName("copper_hoe")
			.setUnlocalizedName("copper_hoe");
	public static final Item BRONZE_HOE = new ItemQualityHoe(SevenDaysToMine.BRONZE_TOOLS).setRegistryName("bronze_hoe")
			.setUnlocalizedName("bronze_hoe");
	public static final Item IRON_HOE = new ItemQualityHoe(SevenDaysToMine.IRON_TOOLS).setRegistryName("iron_hoe")
			.setUnlocalizedName("iron_hoe");
	public static final Item STEEL_HOE = new ItemQualityHoe(SevenDaysToMine.STEEL_TOOLS).setRegistryName("steel_hoe")
			.setUnlocalizedName("steel_hoe");
	
	public static final Item AUGER_HANDLES = new ItemQualityScrapable(EnumMaterial.IRON, 4)
			.setRegistryName("auger_handles").setUnlocalizedName("auger_handles")
			.setCreativeTab(SevenDaysToMine.TAB_FORGING).setMaxStackSize(1);
	
	public static final Item EMPTY_JAR_MOLD = new ItemMold().setRegistryName("empty_jar_mold")
			.setUnlocalizedName("empty_jar_mold");
	public static final Item HUNTING_RIFLE_BARREL_MOLD = new ItemMold().setRegistryName("hunting_rifle_barrel_mold")
			.setUnlocalizedName("hunting_rifle_barrel_mold");
	public static final Item HUNTING_RIFLE_BOLT_MOLD = new ItemMold().setRegistryName("hunting_rifle_bolt_mold")
			.setUnlocalizedName("hunting_rifle_bolt_mold");
	public static final Item MP5_BARREL_MOLD = new ItemMold().setRegistryName("mp5_barrel_mold")
			.setUnlocalizedName("mp5_barrel_mold");
	public static final Item MP5_STOCK_MOLD = new ItemMold().setRegistryName("mp5_stock_mold")
			.setUnlocalizedName("mp5_stock_mold");
	public static final Item MP5_TRIGGER_MOLD = new ItemMold().setRegistryName("mp5_trigger_mold")
			.setUnlocalizedName("mp5_trigger_mold");
	public static final Item PISTOL_BARREL_MOLD = new ItemMold().setRegistryName("pistol_barrel_mold")
			.setUnlocalizedName("pistol_barrel_mold");
	public static final Item PISTOL_TRIGGER_MOLD = new ItemMold().setRegistryName("pistol_trigger_mold")
			.setUnlocalizedName("pistol_trigger_mold");
	public static final Item SNIPER_RIFLE_STOCK_MOLD = new ItemMold().setRegistryName("sniper_rifle_stock_mold")
			.setUnlocalizedName("sniper_rifle_stock_mold");
	public static final Item SNIPER_RIFLE_TRIGGER_MOLD = new ItemMold().setRegistryName("sniper_rifle_trigger_mold")
			.setUnlocalizedName("sniper_rifle_trigger_mold");
	public static final Item SHOTGUN_BARREL_MOLD = new ItemMold().setRegistryName("shotgun_barrel_mold")
			.setUnlocalizedName("shotgun_barrel_mold");
	public static final Item SHOTGUN_RECEIVER_MOLD = new ItemMold().setRegistryName("shotgun_receiver_mold")
			.setUnlocalizedName("shotgun_receiver_mold");
	public static final Item SHOTGUN_SHORT_BARREL_MOLD = new ItemMold().setRegistryName("shotgun_short_barrel_mold")
			.setUnlocalizedName("shotgun_short_barrel_mold");
	
	public static final Item SALT = new Item().setCreativeTab(SevenDaysToMine.TAB_MATERIALS).setRegistryName("salt")
			.setUnlocalizedName("salt");
	
	public static final Item CHLORINE_TANK = new Item().setCreativeTab(SevenDaysToMine.TAB_MATERIALS).setRegistryName("chlorine_tank")
			.setUnlocalizedName("chlorine_tank");
	
	public static final Item NATRIUM_TANK = new Item().setCreativeTab(SevenDaysToMine.TAB_MATERIALS).setRegistryName("natrium_tank")
			.setUnlocalizedName("natrium_tank");
	
	public static final Item CHLORINE_GRENADE = new ItemChlorineGrenade().setCreativeTab(CreativeTabs.COMBAT).setRegistryName("chlorine_grenade")
			.setUnlocalizedName("chlorine_grenade");
	public static final Item FRAGMENTATION_GRENADE = new ItemFragmentationGrenade().setCreativeTab(CreativeTabs.COMBAT).setRegistryName("fragmentation_grenade")
			.setUnlocalizedName("fragmentation_grenade");
	public static final Item MOLOTOV = new ItemMolotov().setCreativeTab(CreativeTabs.COMBAT).setRegistryName("molotov")
			.setUnlocalizedName("molotov");
	
	public static final Item RIOT_SHIELD = new ItemRiotShield().setRegistryName("riot_shield")
			.setUnlocalizedName("riot_shield");
	
	public static final Item CRUDE_BOW = new ItemQualityBow(1.45,1f).setMaxDamage(20).setRegistryName("crude_bow")
			.setUnlocalizedName("crude_bow");
	public static final Item COMPOUND_BOW = new ItemQualityBow(2.4,1.33f).setMaxDamage(50).setRegistryName("compound_bow")
			.setUnlocalizedName("compound_bow");
	
	public static final Item SAND_DUST = new ItemScrap(EnumMaterial.SAND).setCreativeTab(SevenDaysToMine.TAB_MATERIALS).setRegistryName("sand_dust")
			.setUnlocalizedName("sand_dust");
	
	public static final Item VOMIT = new Item().setRegistryName("vomit")
			.setUnlocalizedName("vomit");
	public static final Item FLARE = new ItemFlare().setRegistryName("flare")
			.setUnlocalizedName("flare");
	public static final Item SODA = new ItemCoffeeDrink(0, 300, 150,0,3000).setRegistryName("soda")
			.setUnlocalizedName("soda").setContainerItem(EMPTY_CAN);
}