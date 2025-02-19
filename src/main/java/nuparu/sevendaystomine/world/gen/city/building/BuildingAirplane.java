package nuparu.sevendaystomine.world.gen.city.building;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.block.BlockAirplaneRotor;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.util.Utils;

public class BuildingAirplane extends Building{
	private ResourceLocation TAIL = new ResourceLocation(SevenDaysToMine.MODID, "airplane_tail");
	private ResourceLocation RIGHT_WING = new ResourceLocation(SevenDaysToMine.MODID, "airplane_right_wing");
	private ResourceLocation LEFT_WING = new ResourceLocation(SevenDaysToMine.MODID, "airplane_left_wing");
	private ResourceLocation FRONT = new ResourceLocation(SevenDaysToMine.MODID, "airplane_front");
	
	public BuildingAirplane(int weight, ResourceLocation tail, ResourceLocation right_wing, ResourceLocation left_wing, ResourceLocation front) {
		this(weight, 0, tail, right_wing,left_wing,front);
	}

	public BuildingAirplane(int weight, int yOffset, ResourceLocation tail, ResourceLocation right_wing, ResourceLocation left_wing, ResourceLocation front) {
		super(null, weight, yOffset);
		this.pedestalState = null;
		this.TAIL = tail;
		this.FRONT = front;
		this.LEFT_WING = left_wing;
		this.RIGHT_WING = right_wing;
	}

	@Override
	public void generate(World world, BlockPos pos, EnumFacing facing, boolean mirror, Random rand) {

		if (!world.isRemote) {

			WorldServer worldserver = (WorldServer) world;
			MinecraftServer minecraftserver = world.getMinecraftServer();
			TemplateManager templatemanager = worldserver.getStructureTemplateManager();

			Template template = templatemanager.getTemplate(minecraftserver, TAIL);
			if (template == null) {
				return;
			}

			Rotation rot = Utils.facingToRotation(facing);
			pos = pos.up(yOffset).offset(facing,42).offset(facing.rotateY(),8);
			


			PlacementSettings placementsettings = (new PlacementSettings())
					.setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot).setIgnoreEntities(false)
					.setChunk((ChunkPos) null).setReplacedBlock((Block) null).setIgnoreStructureBlock(false);

			template.addBlocksToWorld(world, pos, placementsettings);
			Map<BlockPos, String> map = template.getDataBlocks(pos, placementsettings);
			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), mirror);
			}

			coverWithSand(world, pos, template, facing, mirror,rand);
			
			pos = pos.offset(facing,32* (mirror ? -1 : 1)).offset(facing.rotateY(),9);
			template = templatemanager.getTemplate(minecraftserver, RIGHT_WING);
			if (template == null) {
				return;
			}
			template.addBlocksToWorld(world, pos, placementsettings);
			map = template.getDataBlocks(pos, placementsettings);
			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), mirror);
			}
			coverWithSand(world, pos, template, facing, mirror,rand);
			
			pos = pos.offset(facing.rotateY(),-19);
			template = templatemanager.getTemplate(minecraftserver, LEFT_WING);
			if (template == null) {
				return;
			}
			template.addBlocksToWorld(world, pos, placementsettings);
			map = template.getDataBlocks(pos, placementsettings);
			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), mirror);
			}
			coverWithSand(world, pos, template, facing, mirror,rand);
			
			pos = pos.offset(facing,11* (mirror ? -1 : 1)).offset(facing.rotateY(),17);
			template = templatemanager.getTemplate(minecraftserver, FRONT);
			if (template == null) {
				return;
			}
			template.addBlocksToWorld(world, pos, placementsettings);
			map = template.getDataBlocks(pos, placementsettings);
			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), mirror);
			}
			coverWithSand(world, pos, template, facing, mirror,rand);
		}
	}


	public BlockPos getDimensions(World world, EnumFacing facing) {
		return BlockPos.ORIGIN;
	}
	
	@Override
	public void handleDataBlock(World world, EnumFacing facing, BlockPos pos, String data, boolean mirror) {
		switch (data) {
		case "engine": {
			IBlockState state = Blocks.AIR.getDefaultState();
			int r = world.rand.nextInt(10);
			if (r == 0) {
				state = ModBlocks.AIRPLANE_ROTOR.getDefaultState().withProperty(BlockAirplaneRotor.FACING,
						facing.getOpposite());
			}

			world.setBlockState(pos, state);
			break;
		}
		default:
			super.handleDataBlock(world, facing, pos, data, mirror);
		}
	}

}
