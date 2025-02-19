package nuparu.sevendaystomine.util;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.mojang.authlib.GameProfile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockLog;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.IItemHandler;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.block.IUpgradeable;
import nuparu.sevendaystomine.block.repair.BreakData;
import nuparu.sevendaystomine.block.repair.BreakSavedData;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.entity.EntityMountableBlock;
import nuparu.sevendaystomine.item.ItemGun;
import nuparu.sevendaystomine.item.ItemGun.EnumWield;
import nuparu.sevendaystomine.util.dialogue.Dialogues;
import nuparu.sevendaystomine.util.dialogue.DialoguesRegistry;
import nuparu.sevendaystomine.world.gen.RoadDecoratorWorldGen;

public class Utils {
	private static Logger logger;

	public static Logger getLogger() {
		if (logger == null) {
			logger = LogManager.getLogger(SevenDaysToMine.MODID);
		}
		return logger;
	}

	public static boolean mountBlock(World world, BlockPos pos, EntityPlayer player) {
		return mountBlock(world, pos, player, 0);
	}

	public static boolean mountBlock(World world, BlockPos pos, EntityPlayer player, double deltaY) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		List<EntityMountableBlock> list = world.getEntitiesWithinAABB(EntityMountableBlock.class,
				new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D).grow(1D));
		for (EntityMountableBlock mount : list) {
			if (mount.getBlockPos() == pos) {
				if (!mount.isBeingRidden()) {
					player.startRiding(mount);
				}
				return true;
			}
		}
		if (list.size() == 0) {
			EntityMountableBlock mount = new EntityMountableBlock(world, x, y + deltaY, z);
			world.spawnEntity(mount);
			player.startRiding(mount);
			return true;
		}
		return false;
	}

	public static void tryOpenWebsite(String URL) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI(URL));
			} catch (IOException e) {
				e.printStackTrace();
				return;
			} catch (URISyntaxException e) {
				e.printStackTrace();
				return;
			}
		} else {
			return;
		}
		return;
	}

	public static boolean isPlayerDualWielding(EntityPlayer player) {
		if (player == null) {
			return false;
		}
		ItemStack main = player.getHeldItemMainhand();
		ItemStack sec = player.getHeldItemOffhand();
		if ((main == null || main.isEmpty()) && (sec == null || sec.isEmpty())) {
			return false;
		}
		Item item_main = main.getItem();
		Item item_sec = sec.getItem();
		if (item_main == null && item_sec == null) {
			return false;
		}

		ItemGun gun_main = null;
		ItemGun gun_sec = null;

		if (item_main instanceof ItemGun) {
			gun_main = (ItemGun) item_main;
		}

		if (item_sec instanceof ItemGun) {
			gun_sec = (ItemGun) item_sec;
		}

		if (gun_main == null && gun_sec == null) {
			return false;
		}

		if (gun_main != null && gun_main.getWield() == EnumWield.DUAL) {
			if (gun_sec == null) {
				return false;
			}
			if (gun_sec.getWield() == EnumWield.DUAL) {
				return true;
			}
		}
		return false;
	}

	public static double getCrosshairSpread(EntityPlayer player) {
		if (player == null) {
			return -1;
		}
		ItemStack main = player.getHeldItemMainhand();
		ItemStack sec = player.getHeldItemOffhand();
		if ((main == null || main.isEmpty()) && (sec == null || sec.isEmpty())) {
			return -1;
		}
		Item item_main = main.getItem();
		Item item_sec = sec.getItem();
		if (item_main == null && item_sec == null) {
			return -1;
		}

		ItemGun gun_main = null;
		ItemGun gun_sec = null;

		if (item_main instanceof ItemGun) {
			gun_main = (ItemGun) item_main;
		}

		if (item_sec instanceof ItemGun) {
			gun_sec = (ItemGun) item_sec;
		}

		if (gun_main == null && gun_sec == null) {
			return -1;
		}

		if (gun_main != null && gun_sec == null) {
			return gun_main.getCross(player, EnumHand.MAIN_HAND);
		} else if (gun_main == null && gun_sec != null) {
			return gun_sec.getCross(player, EnumHand.OFF_HAND);
		} else {
			return Math.max(gun_sec.getCross(player, EnumHand.MAIN_HAND), gun_main.getCross(player, EnumHand.OFF_HAND));
		}

	}

	public static boolean hasGunInAnyHand(EntityPlayer player) {
		if (player == null) {
			return false;
		}
		ItemStack main = player.getHeldItemMainhand();
		ItemStack sec = player.getHeldItemOffhand();
		if ((main == null || main.isEmpty()) && (sec == null || sec.isEmpty())) {
			return false;
		}
		Item item_main = main.getItem();
		Item item_sec = sec.getItem();
		if (item_main == null && item_sec == null) {
			return false;
		}
		if (item_main instanceof ItemGun) {
			return true;
		}

		if (item_sec instanceof ItemGun) {
			return true;
		}
		return false;
	}

	public static float angularDistance(float alpha, float beta) {
		float phi = Math.abs(beta - alpha) % 360;
		float distance = phi > 180 ? 360 - phi : phi;
		return distance;
	}

	/**
	 * By Choonster
	 * 
	 * Returns <code>null</code>.
	 * <p>
	 * This is used in the initialisers of <code>static final</code> fields instead
	 * of using <code>null</code> directly to suppress the "Argument might be null"
	 * warnings from IntelliJ IDEA's "Constant conditions &amp; exceptions"
	 * inspection.
	 * <p>
	 * Based on diesieben07's solution <a href=
	 * "http://www.minecraftforge.net/forum/topic/60980-solved-disable-%E2%80%9Cconstant-conditions-exceptions%E2%80%9D-inspection-for-field-in-intellij-idea/?do=findCommentcomment=285024">here</a>.
	 *
	 * @param <T> The field's type.
	 * @return null
	 */
	public static <T> T Null() {
		return null;
	}

	/*
	 * By Choonster
	 */
	public static NonNullList<ItemStack> dropItemHandlerContents(IItemHandler itemHandler, Random random) {
		final NonNullList<ItemStack> drops = NonNullList.create();

		for (int slot = 0; slot < itemHandler.getSlots(); ++slot) {
			while (!itemHandler.getStackInSlot(slot).isEmpty()) {
				final int amount = random.nextInt(21) + 10;

				if (itemHandler.extractItem(slot, amount, true) != null) {
					final ItemStack itemStack = itemHandler.extractItem(slot, amount, false);
					drops.add(itemStack);
				}
			}
		}

		return drops;
	}

	public static BlockPos getTopGroundBlock(BlockPos pos, World world, boolean solid) {
		return getTopGroundBlock(pos, world, solid, true);
	}

	public static BlockPos getTopGroundBlock(BlockPos pos, World world, boolean solid, boolean ignoreFoliage) {
		Chunk chunk = world.getChunkFromBlockCoords(pos);
		BlockPos blockpos;
		BlockPos blockpos1;

		for (blockpos = new BlockPos(pos.getX(), chunk.getTopFilledSegment() + 16, pos.getZ()); blockpos
				.getY() >= 0; blockpos = blockpos1) {
			blockpos1 = blockpos.down();
			IBlockState state = chunk.getBlockState(blockpos1);
			Block block = state.getBlock();
			Material material = state.getMaterial();

			if (solid && material.isLiquid()) {
				blockpos = blockpos1;
				break;
			}
			if (solid && state.isFullCube() && material.blocksMovement()
					&& (!ignoreFoliage || (!block.isLeaves(state, world, blockpos1)
							&& !block.isFoliage(world, blockpos1) && !(block instanceof BlockLog)))
					&& material != Material.SNOW) {
				blockpos = blockpos1;
				break;
			} else if (material != Material.AIR) {
				blockpos = blockpos1;
				break;
			}
		}

		return blockpos;
	}

	public static BlockPos getTopBiomeGroundBlock(BlockPos pos, World world) {
		Chunk chunk = world.getChunkFromBlockCoords(pos);
		BlockPos blockpos;
		BlockPos blockpos1;

		Biome biome = world.getBiome(pos);

		for (blockpos = new BlockPos(pos.getX(), chunk.getTopFilledSegment() + 16, pos.getZ()); blockpos
				.getY() >= 0; blockpos = blockpos1) {
			blockpos1 = blockpos.down();
			IBlockState state = chunk.getBlockState(blockpos1);
			Block block = state.getBlock();
			Material material = state.getMaterial();

			if (state == biome.topBlock) {
				blockpos = blockpos1;
				break;
			}
			if (blockpos1.getY() <= 63) {
				return new BlockPos(0, -1, 0);
			}
		}

		return blockpos;
	}

	public static int getTopSolidGroundHeight(BlockPos pos, World world) {
		Chunk chunk = world.getChunkFromBlockCoords(pos);
		BlockPos blockpos;
		BlockPos blockpos1;

		for (blockpos = new BlockPos(pos.getX(), chunk.getTopFilledSegment() + 16, pos.getZ()); blockpos
				.getY() >= 0; blockpos = blockpos1) {
			blockpos1 = blockpos.down();
			IBlockState state = chunk.getBlockState(blockpos1);
			Block block = state.getBlock();
			Material material = state.getMaterial();

			if (material.isLiquid()) {
				break;
			}
			if (state.isFullCube() && material.blocksMovement() && !block.isLeaves(state, world, blockpos1)
					&& !block.isFoliage(world, blockpos1) && material != Material.SNOW) {
				break;
			}
		}

		return blockpos != null ? blockpos.getY() : -1;
	}

	public static boolean isSolid(World world, BlockPos pos, IBlockState state) {
		Material material = state.getMaterial();
		Block block = state.getBlock();
		if (!material.isLiquid() && state.isFullCube() && material.blocksMovement()
				&& !block.isLeaves(state, world, pos) && !block.isFoliage(world, pos) && material != Material.SNOW) {
			return true;
		}
		return false;
	}

	public static float lerp(float a, float b, float f) {
		return (a * (1.0f - f)) + (b * f);
	}

	// returns true if the block has been destroyed
	public static boolean damageBlock(World world, BlockPos pos, float damage, boolean breakBlock) {
		return damageBlock(world, pos, damage, breakBlock, true);
	}

	// returns true if the block has been destroyed
	public static boolean damageBlock(World world, BlockPos pos, float damage, boolean breakBlock, boolean sound) {
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		float hardness = state.getBlockHardness(world, pos);
		if (hardness <= 0) {
			return false;
		}
		if (block.isAir(state, world, pos)) {
			return false;
		}
		if (block instanceof IFluidBlock || block instanceof BlockLiquid) {
			return false;
		}
		if (hardness == 0) {
			world.destroyBlock(pos, false);
			return true;
		}

		BreakSavedData data = BreakSavedData.get(world);
		if (data == null) {
			return false;
		}
		SoundType soundType = block.getSoundType(state, world, pos, null);
		if (sound) {
			world.playSound(null, pos, soundType.getHitSound(), SoundCategory.NEUTRAL,
					(soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
		}
		data.addBreakData(pos, world, damage / hardness);
		BreakData breakData = data.getBreakData(pos, world.provider.getDimension());
		if (breakData != null && breakData.getState() >= 1f && breakBlock) {
			data.setBreakData(pos, world, 0);
			if (block instanceof IUpgradeable) {
				IBlockState prev = ((IUpgradeable) block).getPrev(world, pos, state);
				if (prev != null) {
					world.setBlockState(pos, prev);
					((IUpgradeable) block).onDowngrade(world, pos, state);
				}
				return true;
			}
			world.destroyBlock(pos, false);
			return true;
		}

		return false;
	}

	public static boolean isOperator(EntityPlayer player) {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getOppedPlayers()
				.getEntry(player.getGameProfile()) != null;
	}

	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static Entity getEntityByNBTAndResource(ResourceLocation resourceLocation, NBTTagCompound nbt, World world) {

		if (nbt != null && resourceLocation != null) {
			Class<? extends Entity> clazz = EntityList.getClass(resourceLocation);
			Entity entity = EntityList.newEntity(clazz, world);
			entity.readFromNBT(nbt);
			entity.setUniqueId(MathHelper.getRandomUUID(entity.world.rand));
			return entity;
		}
		return null;
	}

	public static int getWorldMinutes(World world) {
		int time = (int) Math.abs((world.getWorldTime() + 6000) % 24000);
		return (time % 1000) * 6 / 100;
	}

	public static int getWorldHours(World world) {
		int time = (int) Math.abs((world.getWorldTime() + 6000) % 24000);
		return (int) ((float) time / 1000F);
	}

	public static boolean isInArea(double mouseX, double mouseY, double x, double y, double width, double height) {
		return (mouseX >= x && mouseY >= y) && (mouseX <= x + width && mouseY <= y + height);
	}

	public static boolean isInAreaAbs(double mouseX, double mouseY, double x1, double y1, double x2, double y2) {
		return (mouseX >= x1 && mouseY >= y1) && (mouseX <= x2 && mouseY <= y2);
	}

	public static boolean isInsideBlock(Entity entity, Block block) {
		double d0 = entity.posY + (double) entity.getEyeHeight();
		BlockPos blockpos = new BlockPos(entity.posX, d0, entity.posZ);
		IBlockState iblockstate = entity.world.getBlockState(blockpos);
		if (iblockstate.getBlock() == block) {
			return true;
		}
		return false;
	}

	public static MinecraftServer getServer() {
		WorldServer worldServer = DimensionManager.getWorld(0);
		GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "FakePlayer");
		FakePlayer fakePlayer = new FakePlayer(worldServer, gameProfile);
		MinecraftServer server = fakePlayer.mcServer;
		return server;
	}

	public static InputStream getInsideFileStream(String path) {
		return Utils.class.getResourceAsStream(new StringBuilder().append("/assets/").append(path).toString());
	}

	public static File tempFileFromStream(InputStream in, String name, String extension) throws IOException {
		final File tempFile = File.createTempFile(name, extension);
		tempFile.deleteOnExit();
		try (FileOutputStream out = new FileOutputStream(tempFile)) {
			IOUtils.copy(in, out);
		}
		return tempFile;
	}

	public static byte[] getBytes(BufferedImage img) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(img, "png", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();
			return imageInByte;

		} catch (IOException e) {
			getLogger().debug(e.getMessage());
		}
		return null;
	}

	public static List<byte[]> divideArray(byte[] source, int chunksize) {

		List<byte[]> result = new ArrayList<byte[]>();
		int start = 0;
		while (start < source.length) {
			int end = Math.min(source.length, start + chunksize);
			result.add(Arrays.copyOfRange(source, start, end));
			start += chunksize;
		}

		return result;
	}

	public static boolean compareBlockPos(BlockPos a, BlockPos b) {
		return a != null && b != null && a.getX() == b.getX() && a.getY() == b.getY() && a.getZ() == b.getZ();
	}

	public static TextFormatting dyeColorToTextFormatting(EnumDyeColor color) {
		switch (color) {
		default:
		case WHITE:
			return TextFormatting.WHITE;
		case ORANGE:
			return TextFormatting.GOLD;
		case MAGENTA:
			return TextFormatting.AQUA;
		case LIGHT_BLUE:
			return TextFormatting.BLUE;
		case YELLOW:
			return TextFormatting.YELLOW;
		case LIME:
			return TextFormatting.GREEN;
		case PINK:
			return TextFormatting.LIGHT_PURPLE;
		case GRAY:
			return TextFormatting.DARK_GRAY;
		case SILVER:
			return TextFormatting.GRAY;
		case CYAN:
			return TextFormatting.DARK_AQUA;
		case PURPLE:
			return TextFormatting.DARK_PURPLE;
		case BLUE:
			return TextFormatting.DARK_BLUE;
		case BROWN:
			return TextFormatting.GOLD;
		case GREEN:
			return TextFormatting.DARK_GREEN;
		case RED:
			return TextFormatting.DARK_RED;
		case BLACK:
			return TextFormatting.BLACK;

		}
	}

	public static final DataSerializer<Dialogues> DIALOGUES = new DataSerializer<Dialogues>() {
		public void write(PacketBuffer buf, Dialogues value) {
			if (buf == null || value == null || value.getKey() == null)
				return;
			buf.writeResourceLocation(value.getKey());
		}

		public Dialogues read(PacketBuffer buf) throws IOException {
			return DialoguesRegistry.INSTANCE.getByRes(buf.readResourceLocation());
		}

		public DataParameter<Dialogues> createKey(int id) {
			return new DataParameter<Dialogues>(id, this);
		}

		public Dialogues copyValue(Dialogues value) {
			return value;
		}
	};

	public static byte[] convertToBytes(Object object) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = new ObjectOutputStream(bos);
		out.writeObject(object);
		return bos.toByteArray();

	}

	public static Object convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInput in = new ObjectInputStream(bis);
		return in.readObject();
	}

	@SuppressWarnings("rawtypes")
	public static boolean isClassFromPackageOrChild(Class clazz, String path) {
		return containsIgnoreCase(clazz.toString(), path);
	}

	public static boolean containsIgnoreCase(String str, String subString) {
		return str.toLowerCase().contains(subString.toLowerCase());
	}

	public static void stackTrace(int length) {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		for (int i = 0; i < Math.min(length, stackTraceElements.length); i++) {
			StackTraceElement ste = stackTraceElements[i];
			
		}
	}

	public static int facingToInt(EnumFacing facing) {
		switch (facing) {
		case SOUTH:
			return 0;
		case EAST:
			return 90;
		case NORTH:
			return 180;
		case WEST:
			return 270;
		default:
			return 0;
		}
	}

	public static Rotation intToRotation(int angle) {
		int remainder = angle % 360;

		switch (remainder) {
		case 0:
			return Rotation.NONE;
		case 90:
			return Rotation.CLOCKWISE_90;
		case 180:
			return Rotation.CLOCKWISE_180;
		case 270:
			return Rotation.COUNTERCLOCKWISE_90;
		default:
			return Rotation.NONE;
		}
	}

	public static Rotation facingToRotation(EnumFacing facing) {

		switch (facing) {
		default:
		case SOUTH:
			return Rotation.NONE;
		case WEST:
			return Rotation.CLOCKWISE_90;
		case NORTH:
			return Rotation.CLOCKWISE_180;
		case EAST:
			return Rotation.COUNTERCLOCKWISE_90;
		}
	}

	public static int getDay(World world) {
		return (int) Math.round(world.getWorldTime() / 24000) + 1;
	}

	public static boolean isBloodmoon(World world) {
		return ModConfig.world.bloodmoonFrequency > 0 && Utils.getDay(world) % ModConfig.world.bloodmoonFrequency == 0;
	}

	public static boolean isBloodmoonProper(World world) {
		return isBloodmoon(world) && !world.isDaytime();
	}

	public static boolean isBloodmoon(int day) {
		return ModConfig.world.bloodmoonFrequency > 0 && day % ModConfig.world.bloodmoonFrequency == 0;
	}

	public static boolean isWolfHorde(World world) {
		return ModConfig.world.wolfHordeFrequency > 0 && Utils.getDay(world) % ModConfig.world.wolfHordeFrequency == 0;
	}

	public static int getItemBurnTime(ItemStack stack) {
		if (stack.isEmpty()) {
			return 0;
		} else {
			int burnTime = net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack);
			if (burnTime >= 0)
				return burnTime;
			Item item = stack.getItem();

			if (item == Item.getItemFromBlock(Blocks.WOODEN_SLAB)) {
				return 150;
			} else if (item == Item.getItemFromBlock(Blocks.WOOL)) {
				return 100;
			} else if (item == Item.getItemFromBlock(Blocks.CARPET)) {
				return 67;
			} else if (item == Item.getItemFromBlock(Blocks.LADDER)) {
				return 300;
			} else if (item == Item.getItemFromBlock(Blocks.WOODEN_BUTTON)) {
				return 100;
			} else if (Block.getBlockFromItem(item).getDefaultState().getMaterial() == Material.WOOD) {
				return 300;
			} else if (item == Item.getItemFromBlock(Blocks.COAL_BLOCK)) {
				return 16000;
			} else if (item instanceof ItemTool && "WOOD".equals(((ItemTool) item).getToolMaterialName())) {
				return 200;
			} else if (item instanceof ItemSword && "WOOD".equals(((ItemSword) item).getToolMaterialName())) {
				return 200;
			} else if (item instanceof ItemHoe && "WOOD".equals(((ItemHoe) item).getMaterialName())) {
				return 200;
			} else if (item == Items.STICK) {
				return 100;
			} else if (item != Items.BOW && item != Items.FISHING_ROD) {
				if (item == Items.SIGN) {
					return 200;
				} else if (item == Items.COAL) {
					return 1600;
				} else if (item == Items.LAVA_BUCKET) {
					return 20000;
				} else if (item != Item.getItemFromBlock(Blocks.SAPLING) && item != Items.BOWL) {
					if (item == Items.BLAZE_ROD) {
						return 2400;
					} else if (item instanceof ItemDoor && item != Items.IRON_DOOR) {
						return 200;
					} else {
						return item instanceof ItemBoat ? 400 : 0;
					}
				} else {
					return 100;
				}
			} else {
				return 300;
			}
		}
	}

	public static BlockPos getAirdropPos(World world) {
		List<EntityPlayer> players = world.playerEntities;
		double xSum = 0;
		double zSum = 0;

		if (players.size() == 1) {
			EntityPlayer player = players.get(0);

			double angle = 2.0 * Math.PI * world.rand.nextDouble();
			double dist = 256 + world.rand.nextDouble() * 256;
			double x = player.posX + dist * Math.cos(angle);
			double z = player.posZ + dist * Math.sin(angle);

			return new BlockPos(x, 255, z);
		}

		for (EntityPlayer player : players) {
			xSum += player.posX;
			zSum += player.posZ;
		}

		double angle = 2.0 * Math.PI * world.rand.nextDouble();
		double dist = 256 + world.rand.nextDouble() * 256;
		double x = (double) xSum / players.size() + dist * Math.cos(angle);
		double z = (double) zSum / players.size() + dist * Math.sin(angle);

		return new BlockPos(x, 255, z);
	}

	public static BlockPos getAirDropStartPoint(World world, BlockPos centerPoint) {
		List<EntityPlayer> players = world.playerEntities;
		if (players.size() == 0)
			return null;

		BlockPos theMostDistant = null;
		double lastDistance = Double.MAX_VALUE;
		if (players.size() == 1) {
			EntityPlayer player = players.get(0);
			theMostDistant = new BlockPos(player.posX + (world.rand.nextDouble() * 256) - 128, 255,
					player.posZ + (world.rand.nextDouble() * 256) - 128);
			lastDistance = centerPoint.distanceSq(theMostDistant);
		}

		if (theMostDistant == null) {
			for (EntityPlayer player : players) {
				BlockPos bp = new BlockPos(player.posX, 255, player.posZ);
				double dist = bp.distanceSq(centerPoint);
				if (dist < lastDistance) {
					lastDistance = dist;
					theMostDistant = bp;
				}
			}
		}
		double x = 0;
		double z = 0;
		double d = (double) (lastDistance + 32) / lastDistance;

		x = ((1 - d) * centerPoint.getX()) + (d * theMostDistant.getX());
		z = ((1 - d) * centerPoint.getZ()) + (d * theMostDistant.getY());
		return new BlockPos(x, 255, z);

	}

	@SafeVarargs
	@SuppressWarnings("unchecked")
	public static <T> Set<T> combine(Set<T>... sets) {
		return Stream.of(sets).flatMap(Set::stream).collect(Collectors.toSet());
	}

	public static void infectPlayer(EntityPlayer player, int time) {
		IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
		if (!iep.isInfected()) {
			iep.setInfectionTime(time);
		}
	}

	public static int getItemCount(InventoryPlayer inv, Item item) {
		int count = 0;
		for (int slot = 0; slot < inv.getSizeInventory(); slot++) {
			ItemStack stack = inv.getStackInSlot(slot);
			if (stack != null && stack.getItem() == item) {
				count += stack.getCount();
			}
		}
		return count;
	}

	public static void renderBiomePreviewMap(World worldIn, ItemStack map) {
		if (map.getItem() instanceof ItemMap) {
			MapData mapdata = ((ItemMap) map.getItem()).getMapData(map, worldIn);

			if (mapdata != null) {
				if (worldIn.provider.getDimension() == mapdata.dimension) {
					int i = 1 << mapdata.scale;
					int j = mapdata.xCenter;
					int k = mapdata.zCenter;

					int x = (j / i - 64) * i;
					int z = (k / i - 64) * i;

					Biome[] abiome = worldIn.getBiomeProvider().getBiomes((Biome[]) null, x, z, 128 * i, 128 * i,
							false);

					for (int l = 0; l < 128; ++l) {
						for (int i1 = 0; i1 < 128; ++i1) {
							int j1 = l * i;
							int k1 = i1 * i;
							Biome biome = abiome[j1 + k1 * 128 * i];
							MapColor mapcolor = MapColor.AIR;

							if (j1 % 64 == 0 || k1 % 64 == 0) {
								mapcolor = MapColor.BLACK;
							}

							int l1 = 3;
							int i2 = 8;

							if (l > 0 && i1 > 0 && l < 127 && i1 < 127) {
								if (abiome[(l - 1) * i + (i1 - 1) * i * 128 * i].getBaseHeight() >= 0.0F) {
									--i2;
								}

								if (abiome[(l - 1) * i + (i1 + 1) * i * 128 * i].getBaseHeight() >= 0.0F) {
									--i2;
								}

								if (abiome[(l - 1) * i + i1 * i * 128 * i].getBaseHeight() >= 0.0F) {
									--i2;
								}

								if (abiome[(l + 1) * i + (i1 - 1) * i * 128 * i].getBaseHeight() >= 0.0F) {
									--i2;
								}

								if (abiome[(l + 1) * i + (i1 + 1) * i * 128 * i].getBaseHeight() >= 0.0F) {
									--i2;
								}

								if (abiome[(l + 1) * i + i1 * i * 128 * i].getBaseHeight() >= 0.0F) {
									--i2;
								}

								if (abiome[l * i + (i1 - 1) * i * 128 * i].getBaseHeight() >= 0.0F) {
									--i2;
								}

								if (abiome[l * i + (i1 + 1) * i * 128 * i].getBaseHeight() >= 0.0F) {
									--i2;
								}

								if (biome.getBaseHeight() < 0.0F) {
									mapcolor = MapColor.ADOBE;

									if (i2 > 7 && i1 % 2 == 0) {
										l1 = (l + (int) (MathHelper.sin((float) i1 + 0.0F) * 7.0F)) / 8 % 5;

										if (l1 == 3) {
											l1 = 1;
										} else if (l1 == 4) {
											l1 = 0;
										}
									} else if (i2 > 7) {
										mapcolor = MapColor.AIR;
									} else if (i2 > 5) {
										l1 = 1;
									} else if (i2 > 3) {
										l1 = 0;
									} else if (i2 > 1) {
										l1 = 0;
									}
								} else if (i2 > 0) {
									mapcolor = MapColor.BROWN;

									if (i2 > 3) {
										l1 = 1;
									} else {
										l1 = 3;
									}
								}
							}

							if (!BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN)
									&& Math.abs(RoadDecoratorWorldGen.getNoiseValue(x + j1, z + k1, 0)) < 0.005d) {
								mapcolor = MapColor.BROWN_STAINED_HARDENED_CLAY;
							}

							if (mapcolor != MapColor.AIR) {
								mapdata.colors[l + i1 * 128] = (byte) (mapcolor.colorIndex * 4 + l1);
								mapdata.updateMapData(l, i1);
							}
						}
					}
				}
			}
		}
	}

	public static void consumeXp(EntityPlayer player, float amount) {
		if (player.experienceTotal - amount <= 0) {
			player.experienceLevel = 0;
			player.experience = 0;
			player.experienceTotal = 0;
			return;
		}

		player.experienceTotal -= amount;

		if (player.experience * (float) player.xpBarCap() <= amount) {
			amount -= player.experience * (float) player.xpBarCap();
			player.experience = 1.0f;
			player.experienceLevel--;
		}

		while (player.xpBarCap() < amount) {
			amount -= player.xpBarCap();
			player.experienceLevel--;
		}

		player.experience -= amount / (float) player.xpBarCap();
	}

	public static boolean canCityBeGeneratedHere(World world, int chunkX, int chunkZ) {
		if (chunkX % ModConfig.worldGen.citySpacing != 0 || chunkZ % ModConfig.worldGen.citySpacing != 0)
			return false;
		Random rand = new Random(world.getSeed());
		long k = rand.nextLong() / 2L * 2L + 1L;
		long l = rand.nextLong() / 2L * 2L + 1L;
		rand.setSeed((long) chunkX * k + (long) chunkZ * l ^ world.getSeed());

		Biome biome = world.getChunkFromChunkCoords(chunkX, chunkZ)
				.getBiome(new BlockPos(16 * chunkX + 8, 255, 16 * chunkZ + 8), world.getBiomeProvider());
		if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN))
			return false;
		return rand.nextInt(16) == 0;
	}

	public static List<ChunkPos> getClosestCities(World world, int chunkX, int chunkZ, int dst) {
		List<ChunkPos> poses = new ArrayList<ChunkPos>();
		for (int i = -dst; i < dst; i++) {
			for (int j = -dst; j < dst; j++) {
				int cX = chunkX + i;
				int cZ = chunkZ + j;
				if (Utils.canCityBeGeneratedHere(world, cX, cZ)) {
					poses.add(new ChunkPos(cX, cZ));
				}
			}
		}
		return poses;
	}

	public static boolean isCityInArea(World world, int chunkX, int chunkZ, int dst) {
		for (int i = -dst; i < dst; i++) {
			for (int j = -dst; j < dst; j++) {
				int cX = chunkX + i;
				int cZ = chunkZ + j;
				if (Utils.canCityBeGeneratedHere(world, cX, cZ)) {
					return true;
				}
			}
		}
		return false;
	}

	public static ChunkPos getClosestCity(World world, int chunkX, int chunkZ) {

		ChunkPos nearest = null;
		double nearestDst = Double.MAX_VALUE;
		List<ChunkPos> cities = getClosestCities(world, chunkX, chunkZ, 128);
		for (ChunkPos pos : cities) {
			double dst = (pos.x - chunkX) + (pos.z - chunkZ);
			if (dst < nearestDst) {
				nearestDst = dst;
				nearest = pos;
			}
		}

		return nearest;
	}

	public static void generateDiagonal(World world, BlockPos start, BlockPos end) {
		int minX = Math.min(start.getX(), end.getX());
		int minZ = Math.min(start.getZ(), end.getZ());

		int maxX = Math.max(start.getX(), end.getX());
		int maxZ = Math.max(start.getZ(), end.getZ());

		Vec3d dif = new Vec3d(maxX - minX, 0, maxZ - minZ);
		Vec3d vec = dif.scale(1d / dif.lengthVector());

		for (double d = 0; d < dif.lengthVector(); d++) {
			Vec3d vec2 = vec.scale(d);
			world.setBlockState(new BlockPos(minX + vec2.x, 200, minZ + vec2.z), Blocks.IRON_BLOCK.getDefaultState());
		}
		world.setBlockState(new BlockPos(minX, 50, minZ), Blocks.REDSTONE_BLOCK.getDefaultState());
		world.setBlockState(new BlockPos(maxX, 50, maxZ), Blocks.GOLD_BLOCK.getDefaultState());

	}

	public static RayTraceResult raytraceEntities(Entity entity, double dst) {

		Entity pointedEntity = null;

		Vec3d vec3d = entity.getPositionEyes(1);

		RayTraceResult r = rayTraceServer(entity, dst, 1);
		double d1 = dst;
		if (r != null && r.typeOfHit == RayTraceResult.Type.BLOCK) {
			d1 = r.hitVec.distanceTo(vec3d);
		}

		Vec3d vec3d1 = entity.getLook(1.0F);
		Vec3d vec3d2 = vec3d.addVector(vec3d1.x * dst, vec3d1.y * dst, vec3d1.z * dst);
		List<Entity> list = entity.world
				.getEntitiesInAABBexcluding(
						entity, entity.getEntityBoundingBox().expand(vec3d1.x * dst, vec3d1.y * dst, vec3d1.z * dst)
								.grow(1.0D, 1.0D, 1.0D),
						Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
							public boolean apply(@Nullable Entity p_apply_1_) {
								return p_apply_1_ != null && p_apply_1_.canBeCollidedWith();
							}
						}));

		Vec3d vec3d3 = null;
		double d2 = d1;
		for (int j = 0; j < list.size(); ++j) {
			Entity entity1 = list.get(j);
			AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox()
					.grow((double) entity1.getCollisionBorderSize());
			RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);

			if (axisalignedbb.contains(vec3d)) {
				if (d2 >= 0.0D) {
					pointedEntity = entity1;
					vec3d3 = raytraceresult == null ? vec3d : raytraceresult.hitVec;
					d2 = 0.0D;
				}
			} else if (raytraceresult != null) {
				double d3 = vec3d.distanceTo(raytraceresult.hitVec);

				if (d3 < d2 || d2 == 0.0D) {
					if (entity1.getLowestRidingEntity() == entity.getLowestRidingEntity()
							&& !entity1.canRiderInteract()) {
						if (d2 == 0.0D) {
							pointedEntity = entity1;
							vec3d3 = raytraceresult.hitVec;
						}
					} else {
						pointedEntity = entity1;
						vec3d3 = raytraceresult.hitVec;
						d2 = d3;
					}
				}
			}
		}

		if (pointedEntity != null) {
			return new RayTraceResult(pointedEntity, vec3d3);
		}
		vec3d3 = new Vec3d(0, -1, 0);
		return new RayTraceResult(RayTraceResult.Type.MISS, vec3d3, (EnumFacing) null, new BlockPos(vec3d3));
	}

	public static RayTraceResult rayTraceServer(Entity entity, double blockReachDistance, float partialTicks) {
		Vec3d vec3 = getPositionEyesServer(entity, partialTicks);
		Vec3d vec31 = entity.getLook(partialTicks);
		Vec3d vec32 = vec3.addVector(vec31.x * blockReachDistance, vec31.y * blockReachDistance,
				vec31.z * blockReachDistance);
		return entity.world.rayTraceBlocks(vec3, vec32, false, false, true);
	}

	public static Vec3d getPositionEyesServer(Entity entity, float partialTicks) {
		if (partialTicks == 1.0F) {
			return new Vec3d(entity.posX, entity.posY + (double) entity.getEyeHeight(), entity.posZ);
		} else {
			double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks;
			double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialTicks
					+ (double) entity.getEyeHeight();
			double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks;
			return new Vec3d(d0, d1, d2);
		}
	}

	public static boolean hasItemStack(EntityPlayer player, ItemStack itemStack) {
		int count = 0;
		for (int slot = 0; slot < player.inventory.getSizeInventory(); slot++) {
			ItemStack stack = player.inventory.getStackInSlot(slot);
			if (stack != null && stack.getItem() == itemStack.getItem()) {
				count += stack.getCount();
			}
		}
		return count >= itemStack.getCount();
	}

	public static void removeItemStack(InventoryPlayer inv, ItemStack itemStack) {
		int count = itemStack.getCount();
		for (int slot = 0; slot < inv.getSizeInventory(); slot++) {
			ItemStack stack = inv.getStackInSlot(slot);
			if (stack != null && stack.getItem() == itemStack.getItem()) {
				int decrease = Math.min(count, stack.getCount());
				inv.decrStackSize(slot, decrease);
				count -= decrease;
				if (count <= 0) {
					break;
				}
			}
		}
	}

	@Nullable
	public static IRecipe getRecipesForStack(ItemStack stack, World worldIn) {
		for (IRecipe irecipe : ForgeRegistries.RECIPES) {
			if (ItemStack.areItemsEqualIgnoreDurability(stack, irecipe.getRecipeOutput())) {
				return irecipe;
			}
		}

		return null;
	}
	
	public static EntityPlayer getPlayerFromUUID(UUID parUUID) 
	{
	    if (parUUID == null) 
	    {
	        return null;
	    }
	    List<EntityPlayerMP> allPlayers = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
	    for (EntityPlayerMP player : allPlayers) 
	    {
	        if (player.getPersistentID().equals(parUUID)) 
	        {
	            return player;
	        }
	    }
	    return null;
	}
}
