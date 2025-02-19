package nuparu.sevendaystomine.world.gen.city.building;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.city.EnumCityType;

public class BuildingPoliceStation extends Building {

	private ResourceLocation JAIL = new ResourceLocation(SevenDaysToMine.MODID, "police_station_jail");
	private ResourceLocation PROJECTOR = new ResourceLocation(SevenDaysToMine.MODID, "police_station_projector");

	public BuildingPoliceStation(int weight) {
		this(weight, 0);
	}

	public BuildingPoliceStation(int weight, int yOffset) {
		super(null, weight, yOffset);
		canBeMirrored = false;
		setAllowedCityTypes(EnumCityType.CITY, EnumCityType.TOWN);
	}

	@Override
	public void generate(World world, BlockPos pos, EnumFacing facing, boolean mirror, Random rand) {
		if (!world.isRemote) {
			WorldServer worldserver = (WorldServer) world;
			MinecraftServer minecraftserver = world.getMinecraftServer();
			TemplateManager templatemanager = worldserver.getStructureTemplateManager();

			Template template = templatemanager.getTemplate(minecraftserver, PROJECTOR);
			if (template == null) {
				return;
			}

			Rotation rot = Utils.facingToRotation(facing.rotateYCCW());
			pos = pos.up(yOffset).offset(facing,4);

			PlacementSettings placementsettings = (new PlacementSettings())
					.setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot).setIgnoreEntities(false)
					.setChunk((ChunkPos) null).setReplacedBlock((Block) null).setIgnoreStructureBlock(false);

			template.addBlocksToWorld(world, pos, placementsettings);

			Map<BlockPos, String> map = template.getDataBlocks(pos, placementsettings);

			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), mirror);
			}
			generatePedestal(world, pos, template, facing, mirror);

			template = templatemanager.getTemplate(minecraftserver, JAIL);
			if (template == null) {
				return;
			}
			pos = pos.offset(facing, -16);
			template.addBlocksToWorld(world, pos, placementsettings);
			map = template.getDataBlocks(pos, placementsettings);
			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), mirror);
			}
			generatePedestal(world, pos, template, facing, mirror);
			coverWithSand(world, pos, template, facing, mirror,rand);
		}
	}

	@Override
	public BlockPos getDimensions(World world, EnumFacing facing) {
		return new BlockPos(32, 22, 32);
	}

	@Override
	public void handleDataBlock(World world, EnumFacing facing, BlockPos pos, String data, boolean mirror) {
		switch (data) {
		case "map": {
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			for(int i = -1; i < 2; i++) {
				for(int j = 0; j < 2; j++) {
					BlockPos pos2 = pos.down(1+j).offset(facing,i);
					List<EntityItemFrame> list = world.getEntitiesWithinAABB(EntityItemFrame.class, new AxisAlignedBB(pos2,pos2.add(1,1,1)));
					if(list.isEmpty()) continue;
					EntityItemFrame frame = list.get(0);
					int x = pos.getX()+(256*i*(mirror ? -1 : 1));
					int z = pos.getZ()+(256*j);
					ItemStack stack = ItemMap.setupNewMap(world, x, z,(byte)1, true, true);
					Utils.renderBiomePreviewMap(world, stack);
					MapData.addTargetDecoration(stack, pos, "+", MapDecoration.Type.TARGET_X);
					frame.setDisplayedItem(stack);
				}
			}
			break;
		}
		default:
			super.handleDataBlock(world, facing, pos, data, mirror);
		}
	}
}
