package nuparu.sevendaystomine.events;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.block.IUpgradeable;
import nuparu.sevendaystomine.block.repair.BreakData;
import nuparu.sevendaystomine.block.repair.BreakSavedData;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.entity.INoiseListener;
import nuparu.sevendaystomine.entity.Noise;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.item.IScrapable;
import nuparu.sevendaystomine.util.DamageSources;
import nuparu.sevendaystomine.util.VanillaManager;
import nuparu.sevendaystomine.util.VanillaManager.VanillaBlockUpgrade;
import nuparu.sevendaystomine.world.MiscSavedData;
import nuparu.sevendaystomine.world.gen.city.CitySavedData;
import nuparu.sevendaystomine.world.horde.HordeSavedData;

public class WorldEventHandler {
	/*
	 * CALLED WHEN MOB MANUALLY "DIGS" A BLOCK --> HANDLES THE BREAK LOGIC
	 */
	@SubscribeEvent
	public void mobDig(MobBreakEvent event) {
		IBlockState state = event.state;
		Block block = state.getBlock();
		World world = event.world;
		BlockPos pos = event.pos;
		if (state.getBlock() instanceof IUpgradeable) {
			IUpgradeable upgradeable = (IUpgradeable) state.getBlock();
			world.setBlockState(pos, upgradeable.getPrev(world, pos, state));
			upgradeable.onDowngrade(world, pos, state);
			return;
		} else {
			VanillaBlockUpgrade upgrade = VanillaManager.getVanillaUpgrade(state);
			if (upgrade != null) {
				world.setBlockState(pos, upgrade.getPrev());
				return;
			}
		}

		if (!(block instanceof BlockDoublePlant)) {
			block.removedByPlayer(state, world, pos, (EntityPlayer) null, true);
		} else {
			world.setBlockToAir(pos);
		}
		world.notifyBlockUpdate(pos, state, Blocks.AIR.getDefaultState(), 3);
	}

	/*
	 * CALLED WHEN PLAYER GETS A DROP(S) FROM A BLOCK -- EDITING VANILLA
	 */
	@SubscribeEvent
	public void playerdig(BlockEvent.HarvestDropsEvent event) {

		BlockPos pos = event.getPos();
		World world = event.getWorld();
		IBlockState state = event.getState();
		Block block = state.getBlock();
		BreakSavedData breakSavedData = BreakSavedData.get(world);
		BreakData data = breakSavedData.getBreakData(pos, world.provider.getDimension());
		if (block.getHarvestLevel(state) != 0) {
			if (data != null) {
				event.setDropChance(1.0f - data.getState());
				BreakSavedData.get(world).removeBreakData(pos, world);
			}
		} else {
			BreakSavedData.get(world).removeBreakData(pos, world);
		}
		if (state.getBlock() instanceof IUpgradeable && ((IUpgradeable) block).getPrev(world, pos, state) != null
				&& ((IUpgradeable) block).getPrev(world, pos, state).getBlock() != Blocks.AIR) {
			IUpgradeable upgradeable = (IUpgradeable) state.getBlock();
			event.getDrops().clear();
			world.setBlockState(pos, upgradeable.getPrev(world, pos, state));
			upgradeable.onDowngrade(world, pos, state);
			Block prev = upgradeable.getPrev(world, pos, state).getBlock();
			if (!(prev instanceof IUpgradeable))
				return;
			for (ItemStack stack : ((IUpgradeable) prev).getItems()) {
				int count = (int) (stack.getCount() * Math.random());
				if (count > 0) {
					ItemStack s = stack.copy();
					s.setCount(count);
					event.getDrops().add(s);
				}
			}
			return;
		} else {
			VanillaBlockUpgrade upgrade = VanillaManager.getVanillaUpgrade(state);
			if (upgrade != null && upgrade.getPrev() != null && upgrade.getPrev().getBlock() != Blocks.AIR) {
				event.getDrops().clear();
				world.setBlockState(pos, upgrade.getPrev());
				if (upgrade.getPrev().getBlock() instanceof IUpgradeable) {
					ItemStack[] stacks = (upgrade.getPrev().getBlock() instanceof IUpgradeable)
							? (((IUpgradeable) upgrade.getPrev().getBlock()).getItems())
							: ((VanillaManager.getVanillaUpgrade(upgrade.getPrev()) != null)
									? (VanillaManager.getVanillaUpgrade(upgrade.getPrev()).getItems())
									: (null));

					if (stacks != null) {
						for (ItemStack stack : stacks) {
							int count = (int) (stack.getCount() * Math.random());
							if (count > 0) {
								ItemStack s = stack.copy();
								s.setCount(count);
								event.getDrops().add(s);
							}
						}
					}
				}
				return;
			}
		}
		if ((block instanceof BlockTallGrass && state.getValue(BlockTallGrass.TYPE) == BlockTallGrass.EnumType.GRASS)
				|| (block instanceof BlockDoublePlant
						&& state.getValue(BlockDoublePlant.VARIANT) == BlockDoublePlant.EnumPlantType.GRASS)) {
			if (world.rand.nextInt(3) == 0) {
				event.getDrops().add(new ItemStack(ModItems.PLANT_FIBER, 1 + world.rand.nextInt(1)));
			}
		}
	}

	/*
	 * CALLED WHEN BLOCK IS BROKEN - CAN BE BY A PLAYER
	 */
	@SubscribeEvent
	public void onBlockBreakEvent(BlockEvent.BreakEvent event) {
		IBlockState oldState = event.getState();
		World world = event.getWorld();
		if (event.getPlayer() instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) event.getPlayer();
			if (player.interactionManager.survivalOrAdventure()) {
				if (player.getHeldItemMainhand().isEmpty()) {
					if (oldState.getMaterial() == Material.GLASS) {
						if (event.getWorld().rand.nextInt(5) == 0) {
							player.attackEntityFrom(DamageSources.sharpGlass, 2.0F);

						}
					}
				}
			}
			BreakSavedData.get(world).removeBreakData(event.getPos(), world);
		}
		TileEntity te = world.getTileEntity(event.getPos());
		if (te != null && te instanceof IVoltage) {
			IVoltage voltage = (IVoltage) te;
			List<ElectricConnection> inputs = voltage.getInputs();
			if (inputs == null)
				return;
			for (ElectricConnection connection : inputs) {
				IVoltage from = connection.getFrom(world);
				if (from == null)
					continue;
				from.disconnect(voltage);
			}
		}
	}

	/*
	 * CALLED ON WORLD LOAD - HANDLES INIIAL LOADING OF WORLD SAVED DATA
	 */
	@SubscribeEvent
	public void loadWorld(WorldEvent.Load event) {
		World world = event.getWorld();
		if (!world.isRemote) {
			// BREAK SAVED DATA
			BreakSavedData break_data = ((BreakSavedData) world.getPerWorldStorage().getOrLoadData(BreakSavedData.class,
					BreakSavedData.DATA_NAME));
			if (break_data == null) {
				SevenDaysToMine.breakSavedData = new BreakSavedData();
				world.getPerWorldStorage().setData(BreakSavedData.DATA_NAME, SevenDaysToMine.breakSavedData);
			} else {
				SevenDaysToMine.breakSavedData = break_data;
			}

			HordeSavedData horde_data = ((HordeSavedData) world.getPerWorldStorage().getOrLoadData(HordeSavedData.class,
					HordeSavedData.DATA_NAME));
			if (horde_data == null) {
				SevenDaysToMine.hordeSavedData = new HordeSavedData();
				world.getPerWorldStorage().setData(HordeSavedData.DATA_NAME, SevenDaysToMine.hordeSavedData);
			} else {
				SevenDaysToMine.hordeSavedData = horde_data;
			}

			CitySavedData city_data = ((CitySavedData) world.getPerWorldStorage().getOrLoadData(CitySavedData.class,
					CitySavedData.DATA_NAME));
			if (city_data == null) {
				SevenDaysToMine.citySavedData = new CitySavedData();
				world.getPerWorldStorage().setData(CitySavedData.DATA_NAME, SevenDaysToMine.citySavedData);
			} else {
				SevenDaysToMine.citySavedData = city_data;
			}

			MiscSavedData misc_data = ((MiscSavedData) world.getPerWorldStorage().getOrLoadData(MiscSavedData.class,
					MiscSavedData.DATA_NAME));
			if (misc_data == null) {
				SevenDaysToMine.miscSavedData = new MiscSavedData();
				world.getPerWorldStorage().setData(MiscSavedData.DATA_NAME, SevenDaysToMine.miscSavedData);
			} else {
				SevenDaysToMine.miscSavedData = misc_data;
			}

		} else {
			if (ModConfig.client.customSky && world.provider.getDimension() == 0) {
				SevenDaysToMine.proxy.setSkyRenderer(world);
				SevenDaysToMine.proxy.setCloudRenderer(world);
			}
		}
	}

	/*
	 * Syncs block damage
	 */
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		World world = event.player.world;
		if (event.player instanceof EntityPlayerMP && !world.isRemote) {
			EntityPlayerMP player = (EntityPlayerMP) event.player;
			BreakSavedData.get(world).sync(player);
		}
	}

	/*
	 * Replaces vanilla torches with modded ones
	 */
	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public void replaceTorchs(PopulateChunkEvent.Post event) {

		World world = event.getWorld();
		Chunk chunk = world.getChunkFromChunkCoords(event.getChunkX(), event.getChunkZ());
		Block fromBlock = net.minecraft.init.Blocks.TORCH;
		Block toBlock = ModBlocks.TORCH_LIT;

		int i = chunk.x * 16;
		int j = chunk.z * 16;
		for (int x = 0; x < 16; ++x) {
			for (int y = 0; y < 256; ++y) {
				for (int z = 0; z < 16; ++z) {
					BlockPos pos = new BlockPos(x + i, y, z + j);
					IBlockState oldState = world.getBlockState(pos);
					Block oldBlock = oldState.getBlock();
					if (oldBlock == fromBlock) {
						world.setBlockState(pos, toBlock.getDefaultState().withProperty(BlockTorch.FACING,
								oldState.getValue(BlockTorch.FACING)));
						chunk.markDirty();
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlaySoundAtEntity(PlaySoundAtEntityEvent event) {
		Entity entity = event.getEntity();
		if (entity != null) {
			if (!(entity instanceof EntityMob)) {
				if (entity.getEntityBoundingBox() == null)
					return;
				float range = event.getVolume() * 64f * (entity.isSneaking() ? 0.75f : 1f);
				AxisAlignedBB aabb = entity.getEntityBoundingBox().grow(range, range / 2, range);
				List<Entity> entities = entity.world.getEntitiesWithinAABB(Entity.class, aabb);
				for (Entity e : entities) {
					if (e instanceof INoiseListener) {
						INoiseListener noiseListener = (INoiseListener) e;
						noiseListener.addNoise(new Noise(entity, new BlockPos(entity), entity.world, event.getVolume(),
								event.getPitch()));
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayLoundSoundAtEntity(LoudSoundEvent event) {
		if (event.pos != null) {
			float range = event.volume * 6.4f;
			AxisAlignedBB aabb = new AxisAlignedBB(event.pos).grow(range, range / 2, range);
			List<Entity> entities = event.world.getEntitiesWithinAABB(Entity.class, aabb);
			for (Entity e : entities) {
				if (e instanceof INoiseListener) {
					INoiseListener noiseListener = (INoiseListener) e;
					noiseListener.addNoise(new Noise(null, event.pos, event.world, event.volume, 1));
				}
			}
		}
	}

	@SubscribeEvent
	public void onFurnaceBurnTime(FurnaceFuelBurnTimeEvent event) {
		ItemStack fuel = event.getItemStack();
		if (fuel.isEmpty())
			return;
		Item item = fuel.getItem();
		if (item == Items.PAPER || item == Items.FILLED_MAP || item == Items.MAP) {
			event.setBurnTime(40);
		} else if (item == Items.BOOK) {
			event.setBurnTime(80);
		}
		if (item instanceof IScrapable) {
			IScrapable scrapable = (IScrapable) item;
			if (scrapable.getMaterial() == EnumMaterial.WOOD) {
				event.setBurnTime(200 * scrapable.getWeight());
			}
		} else if (item instanceof ItemBlock) {
			ItemBlock itemBlock = (ItemBlock) item;
			Block block = itemBlock.getBlock();
			if (block instanceof IScrapable) {
				IScrapable scrapable = (IScrapable) block;
				if (scrapable.getMaterial() == EnumMaterial.WOOD) {
					event.setBurnTime(200 * scrapable.getWeight());
				}
			}
		} else if (VanillaManager.getVanillaScrapable(item) != null) {
			VanillaManager.VanillaScrapableItem scrapable = VanillaManager.getVanillaScrapable(item);
			if (scrapable.getMaterial() == EnumMaterial.WOOD) {
				event.setBurnTime(200 * scrapable.getWeight());
			}
		} else if (item == ModItems.CRUDE_BOW) {
			event.setBurnTime(300);
		}
	}

}