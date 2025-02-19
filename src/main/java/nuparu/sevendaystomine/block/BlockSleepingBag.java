package nuparu.sevendaystomine.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.tileentity.TileEntitySleepingBag;

public class BlockSleepingBag extends BlockHorizontalBase implements ITileEntityProvider {
	public static final PropertyEnum<BlockSleepingBag.EnumPartType> PART = PropertyEnum.<BlockSleepingBag.EnumPartType>create(
			"part", BlockSleepingBag.EnumPartType.class);
	public static final PropertyBool OCCUPIED = PropertyBool.create("occupied");
	protected static final AxisAlignedBB BED_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0756D, 1.0D);

	public BlockSleepingBag() {
		super(Material.CLOTH);
		this.setDefaultState(this.blockState.getBaseState().withProperty(PART, BlockSleepingBag.EnumPartType.FOOT)
				.withProperty(OCCUPIED, Boolean.valueOf(false)));
		this.hasTileEntity = true;
		this.setHardness(0.1f);
		this.setResistance(0.2f);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomStateMapper() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IStateMapper getStateMapper() {
		return new StateMap.Builder().ignore(BlockHorizontalBase.FACING).ignore(PART).ignore(OCCUPIED).build();
	}

	/**
	 * Get the MapColor for this Block and the given BlockState
	 */
	public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		if (state.getValue(PART) == BlockSleepingBag.EnumPartType.FOOT) {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileEntitySleepingBag) {
				EnumDyeColor enumdyecolor = ((TileEntitySleepingBag) tileentity).getColor();
				return MapColor.getBlockColor(enumdyecolor);
			}
		}

		return MapColor.CLOTH;
	}

	/**
	 * Called when the block is right clicked by a player.
	 */
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		EnumDyeColor color = EnumDyeColor.RED;
		if (tileentity instanceof TileEntitySleepingBag) {
			color = ((TileEntitySleepingBag) tileentity).getColor();
		}
		if (worldIn.isRemote) {
			return true;
		} else {
			if (state.getValue(PART) != BlockSleepingBag.EnumPartType.HEAD) {
				pos = pos.offset((EnumFacing) state.getValue(FACING));
				state = worldIn.getBlockState(pos);

				if (state.getBlock() != this) {
					return true;
				}
			}

			net.minecraft.world.WorldProvider.WorldSleepResult sleepResult = worldIn.provider.canSleepAt(playerIn, pos);
			if (sleepResult != net.minecraft.world.WorldProvider.WorldSleepResult.BED_EXPLODES) {
				if (sleepResult == net.minecraft.world.WorldProvider.WorldSleepResult.DENY)
					return true;
				if (((Boolean) state.getValue(OCCUPIED)).booleanValue()) {
					EntityPlayer entityplayer = this.getPlayerInBed(worldIn, pos);

					if (entityplayer != null) {
						playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.occupied", new Object[0]),
								true);
						return true;
					}

					state = state.withProperty(OCCUPIED, Boolean.valueOf(false));
					worldIn.setBlockState(pos, state, 4);
					TileEntity tileentity1 = worldIn.getTileEntity(pos);
					if (tileentity1 instanceof TileEntitySleepingBag) {
						((TileEntitySleepingBag) tileentity1).setColor(color);
						;
					}
				}

				EntityPlayer.SleepResult entityplayer$sleepresult = playerIn.trySleep(pos);

				if (entityplayer$sleepresult == EntityPlayer.SleepResult.OK) {
					state = state.withProperty(OCCUPIED, Boolean.valueOf(true));
					worldIn.setBlockState(pos, state, 4);
					TileEntity tileentity1 = worldIn.getTileEntity(pos);
					if (tileentity1 instanceof TileEntitySleepingBag) {
						((TileEntitySleepingBag) tileentity1).setColor(color);
						;
					}
					return true;
				} else {
					if (entityplayer$sleepresult == EntityPlayer.SleepResult.NOT_POSSIBLE_NOW) {
						playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.noSleep", new Object[0]),
								true);
					} else if (entityplayer$sleepresult == EntityPlayer.SleepResult.NOT_SAFE) {
						playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.notSafe", new Object[0]),
								true);
					} else if (entityplayer$sleepresult == EntityPlayer.SleepResult.TOO_FAR_AWAY) {
						playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.tooFarAway", new Object[0]),
								true);
					}

					return true;
				}
			} else {
				worldIn.setBlockToAir(pos);
				BlockPos blockpos = pos.offset(((EnumFacing) state.getValue(FACING)).getOpposite());

				if (worldIn.getBlockState(blockpos).getBlock() == this) {
					worldIn.setBlockToAir(blockpos);
				}

				worldIn.newExplosion((Entity) null, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
						(double) pos.getZ() + 0.5D, 5.0F, true, true);
				return true;
			}
		}
	}

	@Nullable
	private EntityPlayer getPlayerInBed(World worldIn, BlockPos pos) {
		for (EntityPlayer entityplayer : worldIn.playerEntities) {
			if (entityplayer.isPlayerSleeping() && entityplayer.bedLocation.equals(pos)) {
				return entityplayer;
			}
		}

		return null;
	}

	public boolean isFullCube(IBlockState state) {
		return false;
	}

	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for
	 * render
	 */
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	/**
	 * Block's chance to react to a living entity falling on it.
	 */
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		super.onFallenUpon(worldIn, pos, entityIn, fallDistance * 0.5F);
	}

	/**
	 * Called when an Entity lands on this Block. This method *must* update motionY
	 * because the entity will not do that on its own
	 */
	public void onLanded(World worldIn, Entity entityIn) {
		if (entityIn.isSneaking()) {
			super.onLanded(worldIn, entityIn);
		} else if (entityIn.motionY < 0.0D) {
			entityIn.motionY = -entityIn.motionY * 0.00191101D;

			if (!(entityIn instanceof EntityLivingBase)) {
				entityIn.motionY *= 0.08D;
			}
		}
	}

	/**
	 * Called when a neighboring block was changed and marks that this state should
	 * perform any checks during a neighbor change. Cases may include when redstone
	 * power is updated, cactus blocks popping off due to a neighboring solid block,
	 * etc.
	 */
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);

		if (state.getValue(PART) == BlockSleepingBag.EnumPartType.FOOT) {
			if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() != this) {
				worldIn.setBlockToAir(pos);
			}
		} else if (worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock() != this) {
			if (!worldIn.isRemote) {
				this.dropBlockAsItem(worldIn, pos, state, 0);
			}

			worldIn.setBlockToAir(pos);
		}
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return state.getValue(PART) == BlockSleepingBag.EnumPartType.FOOT ? net.minecraft.init.Items.AIR
				: ModItems.SLEEPING_BAG;
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BED_AABB;
	}

	@SideOnly(Side.CLIENT)
	public boolean hasCustomBreakingProgress(IBlockState state) {
		return true;
	}

	/**
	 * Returns a safe BlockPos to disembark the bed
	 */
	@Nullable
	public static BlockPos getSafeExitLocation(World worldIn, BlockPos pos, int tries) {
		EnumFacing enumfacing = (EnumFacing) worldIn.getBlockState(pos).getValue(FACING);
		int i = pos.getX();
		int j = pos.getY();
		int k = pos.getZ();

		for (int l = 0; l <= 1; ++l) {
			int i1 = i - enumfacing.getFrontOffsetX() * l - 1;
			int j1 = k - enumfacing.getFrontOffsetZ() * l - 1;
			int k1 = i1 + 2;
			int l1 = j1 + 2;

			for (int i2 = i1; i2 <= k1; ++i2) {
				for (int j2 = j1; j2 <= l1; ++j2) {
					BlockPos blockpos = new BlockPos(i2, j, j2);

					if (hasRoomForPlayer(worldIn, blockpos)) {
						if (tries <= 0) {
							return blockpos;
						}

						--tries;
					}
				}
			}
		}

		return null;
	}

	protected static boolean hasRoomForPlayer(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.down()).isTopSolid() && !worldIn.getBlockState(pos).getMaterial().isSolid()
				&& !worldIn.getBlockState(pos.up()).getMaterial().isSolid();
	}

	/**
	 * Spawns this Block's drops into the World as EntityItems.
	 */
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
		if (state.getValue(PART) == BlockSleepingBag.EnumPartType.HEAD) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			EnumDyeColor enumdyecolor = tileentity instanceof TileEntitySleepingBag
					? ((TileEntitySleepingBag) tileentity).getColor()
					: EnumDyeColor.RED;
			spawnAsEntity(worldIn, pos, new ItemStack(ModItems.SLEEPING_BAG, 1, enumdyecolor.getMetadata()));
		}
	}

	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.DESTROY;
	}

	/**
	 * Gets the render layer this block will render on. SOLID for solid blocks,
	 * CUTOUT or CUTOUT_MIPPED for on-off transparency (glass, reeds), TRANSLUCENT
	 * for fully blended transparency (stained glass)
	 */
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	/**
	 * The type of render function called. MODEL for mixed tesr and static model,
	 * MODELBLOCK_ANIMATED for TESR-only, LIQUID for vanilla liquids, INVISIBLE to
	 * skip all rendering
	 */
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		BlockPos blockpos = pos;

		if (state.getValue(PART) == BlockSleepingBag.EnumPartType.FOOT) {
			blockpos = pos.offset((EnumFacing) state.getValue(FACING));
		}

		TileEntity tileentity = worldIn.getTileEntity(blockpos);
		EnumDyeColor enumdyecolor = tileentity instanceof TileEntitySleepingBag
				? ((TileEntitySleepingBag) tileentity).getColor()
				: EnumDyeColor.RED;
		return new ItemStack(ModItems.SLEEPING_BAG, 1, enumdyecolor.getMetadata());
	}

	/**
	 * Called before the Block is set to air in the world. Called regardless of if
	 * the player's tool can actually collect this block
	 */
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (player.capabilities.isCreativeMode && state.getValue(PART) == BlockSleepingBag.EnumPartType.FOOT) {
			BlockPos blockpos = pos.offset((EnumFacing) state.getValue(FACING));

			if (worldIn.getBlockState(blockpos).getBlock() == this) {
				worldIn.setBlockToAir(blockpos);
			}
		}
	}

	/**
	 * Spawns the block's drops in the world. By the time this is called the Block
	 * has possibly been set to air via Block.removedByPlayer
	 */
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te,
			ItemStack stack) {
		if (state.getValue(PART) == BlockSleepingBag.EnumPartType.HEAD && te instanceof TileEntitySleepingBag) {
			TileEntitySleepingBag TileEntitySleepingBag = (TileEntitySleepingBag) te;
			ItemStack itemstack = TileEntitySleepingBag.getItemStack();
			spawnAsEntity(worldIn, pos, itemstack);
		} else {
			super.harvestBlock(worldIn, player, pos, state, (TileEntity) null, stack);
		}
	}

	/**
	 * Called serverside after this block is replaced with another in Chunk, but
	 * before the Tile Entity is updated
	 */
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		super.breakBlock(worldIn, pos, state);
		worldIn.removeTileEntity(pos);
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getHorizontal(meta);
		return (meta & 8) > 0
				? this.getDefaultState().withProperty(PART, BlockSleepingBag.EnumPartType.HEAD)
						.withProperty(FACING, enumfacing).withProperty(OCCUPIED, Boolean.valueOf((meta & 4) > 0))
				: this.getDefaultState().withProperty(PART, BlockSleepingBag.EnumPartType.FOOT).withProperty(FACING,
						enumfacing);
	}

	/**
	 * Get the actual Block state of this Block at the given position. This applies
	 * properties not visible in the metadata, such as fence connections.
	 */
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		if (state.getValue(PART) == BlockSleepingBag.EnumPartType.FOOT) {
			IBlockState iblockstate = worldIn.getBlockState(pos.offset((EnumFacing) state.getValue(FACING)));

			if (iblockstate.getBlock() == this) {
				state = state.withProperty(OCCUPIED, iblockstate.getValue(OCCUPIED));
			}
		}

		return state;
	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If
	 * inapplicable, returns the passed blockstate.
	 */
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If
	 * inapplicable, returns the passed blockstate.
	 */
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(FACING)));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state) {
		int i = 0;
		i = i | ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();

		if (state.getValue(PART) == BlockSleepingBag.EnumPartType.HEAD) {
			i |= 8;

			if (((Boolean) state.getValue(OCCUPIED)).booleanValue()) {
				i |= 4;
			}
		}

		return i;
	}

	/**
	 * Get the geometry of the queried face at the given position and state. This is
	 * used to decide whether things like buttons are allowed to be placed on the
	 * face, or how glass panes connect to the face, among other things.
	 * <p>
	 * Common values are {@code SOLID}, which is the default, and {@code UNDEFINED},
	 * which represents something that does not fit the other descriptions and will
	 * generally cause other things not to connect to the face.
	 * 
	 * @return an approximation of the form of the given face
	 */
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, PART, OCCUPIED });
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the
	 * block.
	 */
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntitySleepingBag();
	}

	@SideOnly(Side.CLIENT)
	public static boolean isHeadPiece(int metadata) {
		return (metadata & 8) != 0;
	}

	@Override
	public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable Entity player) {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, BlockPos pos, net.minecraft.client.particle.ParticleManager manager) {
		TileEntity tileEntity = world.getTileEntity(pos);
		EnumDyeColor color = EnumDyeColor.WHITE;
		if (tileEntity instanceof TileEntitySleepingBag) {
			color = ((TileEntitySleepingBag) tileEntity).getColor();
		}
		for (int i = 0; i < world.rand.nextInt(30) + 18; i++) {
			world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0, 0,
					Block.getStateId(Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, color)));
		}
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(IBlockState state, World world, RayTraceResult target,
			net.minecraft.client.particle.ParticleManager manager) {
		BlockPos pos = target.getBlockPos();
		TileEntity tileEntity = world.getTileEntity(pos);
		EnumDyeColor color = EnumDyeColor.WHITE;
		if (tileEntity instanceof TileEntitySleepingBag) {
			color = ((TileEntitySleepingBag) tileEntity).getColor();
		}
		for (int i = 0; i < world.rand.nextInt(5) + 1; i++) {
			world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0, 0,
					Block.getStateId(Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, color)));
		}
		return true;
	}

	public static enum EnumPartType implements IStringSerializable {
		HEAD("head"), FOOT("foot");

		private final String name;

		private EnumPartType(String name) {
			this.name = name;
		}

		public String toString() {
			return this.name;
		}

		public String getName() {
			return this.name;
		}
	}
}
