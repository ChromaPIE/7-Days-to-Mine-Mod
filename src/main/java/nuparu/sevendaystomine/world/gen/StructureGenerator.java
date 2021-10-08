package nuparu.sevendaystomine.world.gen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.init.ModBiomes;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.city.CitySavedData;
import nuparu.sevendaystomine.world.gen.city.building.Building;
import nuparu.sevendaystomine.world.gen.city.building.BuildingAirplane;
import nuparu.sevendaystomine.world.gen.city.building.BuildingAirport;
import nuparu.sevendaystomine.world.gen.city.building.BuildingCargoShip;
import nuparu.sevendaystomine.world.gen.city.building.BuildingFactory;
import nuparu.sevendaystomine.world.gen.city.building.BuildingHelicopter;
import nuparu.sevendaystomine.world.gen.city.building.BuildingLandfill;
import nuparu.sevendaystomine.world.gen.city.building.BuildingLargeBanditCamp;
import nuparu.sevendaystomine.world.gen.city.building.BuildingMilitaryBase;
import nuparu.sevendaystomine.world.gen.city.building.BuildingSettlement;
import nuparu.sevendaystomine.world.gen.city.building.BuildingWindTurbine;

public class StructureGenerator implements IWorldGenerator {

	public static List<Building> buildings = new ArrayList<Building>();

	public static void loadBuildings() {
		buildings.add(new BuildingFactory(new ResourceLocation(SevenDaysToMine.MODID, "factory_garage"), 30)
				.setAllowedBiomes(ModBiomes.BURNT_FOREST, ModBiomes.WASTELAND, ModBiomes.WASTELAND_FOREST, ModBiomes.WASTELAND_DESERT));
		buildings.add(new BuildingLandfill(30)
				.setAllowedBiomes(ModBiomes.BURNT_FOREST, ModBiomes.WASTELAND, ModBiomes.WASTELAND_FOREST, ModBiomes.WASTELAND_DESERT));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "lookout_birch"), 280)
				.setAllowedBiomes(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST).stream().toArray(Biome[]::new))
				.setAllowedBlocks(Blocks.GRASS));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "lookout_dark_oak"), 280)
				.setAllowedBiomes(
						BiomeDictionary.getBiomes(BiomeDictionary.Type.CONIFEROUS).stream().toArray(Biome[]::new))
				.setAllowedBlocks(Blocks.GRASS));
		buildings.add((new Building(new ResourceLocation(SevenDaysToMine.MODID, "lookout_burnt"), 280))
				.setAllowedBiomes(ModBiomes.BURNT_FOREST, ModBiomes.BURNT_JUNGLE, ModBiomes.BURNT_TAIGA,
						ModBiomes.WASTELAND_FOREST, ModBiomes.WASTELAND)
				.setAllowedBlocks(Blocks.GRASS));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "burnt_house"), 300, -5)
				.setAllowedBiomes(ModBiomes.BURNT_FOREST, ModBiomes.BURNT_JUNGLE, ModBiomes.BURNT_TAIGA,
						ModBiomes.WASTELAND_FOREST, ModBiomes.WASTELAND)
				.setAllowedBlocks(Blocks.GRASS).setPedestal(Blocks.STONE.getDefaultState()));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "ruined_house"), 300)
				.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new))
				.setAllowedBlocks(Blocks.GRASS));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "shack"), 120, 0)
				.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new))
				.setAllowedBlocks(Blocks.GRASS));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "ruined_house_1"), 300)
				.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new))
				.setAllowedBlocks(Blocks.GRASS));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "bandit_camp"), 80, 0,
				Blocks.STONE.getDefaultState())
						.setAllowedBiomes(Utils
								.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
										BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
										BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH))
								.stream().toArray(Biome[]::new))
						.setAllowedBlocks(Blocks.GRASS));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "ruined_house_2"), 300)
				.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new))
				.setAllowedBlocks(Blocks.GRASS));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "ruined_house_icy_2"), 300)
				.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.SNOWY)).stream()
						.toArray(Biome[]::new)));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "ruined_house_icy"), 300)
				.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.SNOWY)).stream()
						.toArray(Biome[]::new)));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "ruined_house_desert_1"), 300)
				.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.SANDY)).stream()
						.toArray(Biome[]::new)));

		buildings.add(new BuildingHelicopter(new ResourceLocation(SevenDaysToMine.MODID, "helicopter"), 50, -3)
				.setHasPedestal(false)
				.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new)));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "observatory"), 150)
				.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new))
				.setAllowedBlocks(Blocks.GRASS));
		buildings.add(new BuildingWindTurbine(200, 0, ModBlocks.MARBLE.getDefaultState())
				.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new))
				.setAllowedBlocks(Blocks.GRASS));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "well_bunker"), 40, -23)
				.setHasPedestal(false)
				.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new)));
		buildings.add(new BuildingSettlement(25, -3)
				.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new))
				.setAllowedBlocks(Blocks.GRASS));

		buildings.add(new BuildingHelicopter(new ResourceLocation(SevenDaysToMine.MODID, "tank_01"), 300, -1)
				.setHasPedestal(false)
				.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.SWAMP),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.COLD),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new)));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "yacht"), 250, -4).setAllowedBiomes(
				Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.OCEAN)).stream().toArray(Biome[]::new))
				.setHasPedestal(false));

		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "campsite"), 70, -3)
				.setPedestal(Blocks.STONE.getDefaultState())
				.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA)).stream().toArray(Biome[]::new))
				.setAllowedBlocks(Blocks.GRASS));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "ruins_0"), 150,-1)
				.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new))
				.setAllowedBlocks(Blocks.GRASS));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "ruins_1"), 200,-1)
				.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new))
				.setAllowedBlocks(Blocks.GRASS));
		// buildings.add(new BuildingFarmProcedural(600));

		if (!ModConfig.worldGen.smallStructuresOnly) {
			buildings
					.add(new BuildingAirport(80).setAllowedBiomes(Utils
							.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
									BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
									BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH))
							.stream().toArray(Biome[]::new)).setAllowedBlocks(Blocks.GRASS));
			buildings.add(new BuildingSettlement(15, -3,
					new ResourceLocation(SevenDaysToMine.MODID, "abandoned_settlement_farm"),
					new ResourceLocation(SevenDaysToMine.MODID, "abandoned_settlement_houses"),
					new ResourceLocation(SevenDaysToMine.MODID, "abandoned_settlement_barracks"),
					new ResourceLocation(SevenDaysToMine.MODID, "abandoned_settlement_pub"))
							.setAllowedBiomes(Utils
									.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
											BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
											BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH))
									.stream().toArray(Biome[]::new))
							.setAllowedBlocks(Blocks.GRASS));
			buildings.add(
					new BuildingAirplane(40, -4, new ResourceLocation(SevenDaysToMine.MODID, "airplane_tail_desert"),
							new ResourceLocation(SevenDaysToMine.MODID, "airplane_right_wing_desert"),
							new ResourceLocation(SevenDaysToMine.MODID, "airplane_left_wing_desert"),
							new ResourceLocation(SevenDaysToMine.MODID, "airplane_front_desert"))
									.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.DRY))
											.stream().toArray(Biome[]::new)));
			buildings.add(new BuildingAirplane(40, -4, new ResourceLocation(SevenDaysToMine.MODID, "airplane_tail"),
					new ResourceLocation(SevenDaysToMine.MODID, "airplane_right_wing"),
					new ResourceLocation(SevenDaysToMine.MODID, "airplane_left_wing"),
					new ResourceLocation(SevenDaysToMine.MODID, "airplane_front"))
							.setAllowedBiomes(Utils
									.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
											BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
											BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH),
											BiomeDictionary.getBiomes(BiomeDictionary.Type.BEACH),
											BiomeDictionary.getBiomes(BiomeDictionary.Type.COLD))
									.stream().toArray(Biome[]::new)));
			buildings.add(new BuildingCargoShip(60, -4).setAllowedBiomes(Utils
					.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.OCEAN)).stream().toArray(Biome[]::new)));

			;
			buildings
					.add(new BuildingLargeBanditCamp(20).setAllowedBiomes(Utils
							.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
									BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
									BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH))
							.stream().toArray(Biome[]::new)).setAllowedBlocks(Blocks.GRASS));
			buildings
					.add(new BuildingMilitaryBase(20, -3).setAllowedBiomes(Utils
							.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
									BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
									BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH))
							.stream().toArray(Biome[]::new)).setAllowedBlocks(Blocks.GRASS));
		}
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		switch (world.provider.getDimension()) {
		case -1:
			generateNether(world, random, chunkX, chunkZ);
			break;
		case 0:
			generateOverworld(world, random, chunkX, chunkZ);
			break;
		case 1:
			generateEnd(world, random, chunkX, chunkZ);
			break;
		}
	}

	private void generateNether(World world, Random rand, int chunkX, int blockZ) {
	}

	private void generateOverworld(World world, Random rand, int chunkX, int chunkZ) {
		if (world.getWorldType() == WorldType.FLAT) {
			return;
		}

		int blockX = chunkX * 16;
		int blockZ = chunkZ * 16;

		CitySavedData data = CitySavedData.get(world);
		if (Utils.isCityInArea(world, chunkX, chunkZ, ModConfig.worldGen.minScatteredDistanceFromCities)
				|| data.isScatteredNearby(new BlockPos(blockX, 128, blockZ), ModConfig.worldGen.minScatteredDistanceSq))
			return;
		Building building = getRandomBuilding(rand);
		generateStructure(building, world, rand, blockX, blockZ, 20);

	}

	private void generateEnd(World world, Random rand, int chunkX, int blockZ) {

	}

	private boolean generateStructure(Building building, World world, Random rand, int blockX, int blockZ, int chance) {
		int c = (int) (chance * ModConfig.worldGen.scattereedStructureChanceModifier);
		if (c == 0 || rand.nextInt(c) != 0)
			return false;
		BlockPos pos = Utils.getTopGroundBlock(new BlockPos(blockX, 64, blockZ), world, true);
		if (pos.getY() < 0)
			return false;
		if (building.allowedBlocks == null || building.allowedBlocks.length == 0
				|| Arrays.asList(building.allowedBlocks).contains(world.getBlockState(pos).getBlock())) {

			Biome biome = world.provider.getBiomeForCoords(pos);
			if (building.allowedBiomes == null || building.allowedBiomes.isEmpty()
					|| building.allowedBiomes.contains(biome)) {
				EnumFacing facing = EnumFacing.getHorizontal(rand.nextInt(4));
				boolean mirror = building.canBeMirrored ? rand.nextBoolean() : false;
				BlockPos dimensions = building.getDimensions(world, facing);
				BlockPos end = pos
						.offset(facing, facing.getAxis() == EnumFacing.Axis.Z ? dimensions.getZ() : dimensions.getX())
						.offset(mirror ? facing.rotateY() : facing.rotateYCCW(),
								facing.getAxis() == EnumFacing.Axis.X ? dimensions.getZ() : dimensions.getX());
				end = Utils.getTopGroundBlock(end, world, true);
				BlockPos pos2 = new BlockPos(pos.getX(),
						end.getY() < 0 ? pos.getY() : MathUtils.lerp(pos.getY(), end.getY(), 0.5f), pos.getZ());
				building.generate(world, pos2, facing, mirror, rand);
				CitySavedData.get(world).addScattered(pos);
				return true;
			}
		}
		return false;
	}

	public int getBuildingsCount() {
		return buildings.size();
	}

	public int getBuildingIndex(Building building) {
		return buildings.indexOf(building);
	}

	public Building getBuildingByIndex(int i) {
		return buildings.get(MathHelper.clamp(i, 0, buildings.size()));
	}

	public static Building getRandomBuilding(Random rand) {
		int total = 0;
		for (Building building : buildings) {
			total += building.weight;
		}
		int i = rand.nextInt(total);
		for (Building building : buildings) {
			i -= building.weight;
			if (i <= 0) {
				return building;
			}
		}
		// Failsafe
		return buildings.get(rand.nextInt(buildings.size()));
	}

	public static List<Building> getBuildings() {
		List<Building> list = new ArrayList<Building>(buildings);
		return list;
	}
}
