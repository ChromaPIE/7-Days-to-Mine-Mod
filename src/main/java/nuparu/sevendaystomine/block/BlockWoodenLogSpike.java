package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.item.IScrapable;
import nuparu.sevendaystomine.tileentity.TileEntityWoodenLogSpike;

public class BlockWoodenLogSpike extends BlockTileProvider<TileEntityWoodenLogSpike> implements IScrapable {

	private EnumMaterial material = EnumMaterial.WOOD;
	private int weight = 2;

	public BlockWoodenLogSpike() {
		super(Material.WOOD);
		this.setSoundType(SoundType.WOOD);
		this.setHardness(2f);
		this.setResistance(2f);
		this.setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (!(entityIn instanceof EntityLivingBase) || entityIn.isDead) {
			return;
		}
		float fallDistance = entityIn.fallDistance;
		entityIn.setInWeb();
		if (entityIn instanceof EntityPlayer) {
			if (((EntityPlayer) entityIn).isCreative() || ((EntityPlayer) entityIn).isSpectator()) {
				return;
			}
		}
		if(fallDistance < 1) return;
		entityIn.attackEntityFrom(DamageSource.GENERIC, 1.5f*fallDistance);
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && te instanceof TileEntityWoodenLogSpike) {
			TileEntityWoodenLogSpike tileEntity = (TileEntityWoodenLogSpike) te;
			tileEntity.dealDamage((int) Math.ceil(0.5*fallDistance));
		}
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return null;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public void setMaterial(EnumMaterial mat) {
		material = mat;
	}

	@Override
	public EnumMaterial getMaterial() {
		return material;
	}

	@Override
	public void setWeight(int newWeight) {
		weight = newWeight;
	}

	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public boolean canBeScraped() {
		return true;
	}

	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return super.canPlaceBlockAt(worldIn, pos) ? this.canBlockStay(worldIn, pos) : false;
	}

	public boolean canBlockStay(World worldIn, BlockPos pos) {
		IBlockState state = worldIn.getBlockState(pos.down());
		return state.isBlockNormalCube() && !worldIn.getBlockState(pos.up()).getMaterial().isLiquid();
	}

	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (!this.canBlockStay(worldIn, pos)) {
			dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 1);
			worldIn.setBlockToAir(pos);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityWoodenLogSpike();
	}

	@Override
	public TileEntityWoodenLogSpike createTileEntity(World world, IBlockState state) {
		return new TileEntityWoodenLogSpike();
	}

	public static void degradeBlock(BlockPos pos, World world) {
		Block block = world.getBlockState(pos).getBlock();
		world.destroyBlock(pos, false);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

}
