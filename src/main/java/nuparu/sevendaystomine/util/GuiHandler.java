package nuparu.sevendaystomine.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import nuparu.sevendaystomine.capability.ExtendedInventoryProvider;
import nuparu.sevendaystomine.client.gui.GuiBook;
import nuparu.sevendaystomine.client.gui.GuiCodeSafeLocked;
import nuparu.sevendaystomine.client.gui.GuiDialogue;
import nuparu.sevendaystomine.client.gui.GuiKeySafeLocked;
import nuparu.sevendaystomine.client.gui.GuiMonitor;
import nuparu.sevendaystomine.client.gui.inventory.GuiBackpack;
import nuparu.sevendaystomine.client.gui.inventory.GuiCamera;
import nuparu.sevendaystomine.client.gui.inventory.GuiCampfire;
import nuparu.sevendaystomine.client.gui.inventory.GuiCar;
import nuparu.sevendaystomine.client.gui.inventory.GuiChemistryStation;
import nuparu.sevendaystomine.client.gui.inventory.GuiCombustionGenerator;
import nuparu.sevendaystomine.client.gui.inventory.GuiComputer;
import nuparu.sevendaystomine.client.gui.inventory.GuiContainerAirdrop;
import nuparu.sevendaystomine.client.gui.inventory.GuiContainerBig;
import nuparu.sevendaystomine.client.gui.inventory.GuiContainerLootableEntity;
import nuparu.sevendaystomine.client.gui.inventory.GuiContainerSmallOld;
import nuparu.sevendaystomine.client.gui.inventory.GuiContainerTiny;
import nuparu.sevendaystomine.client.gui.inventory.GuiFlamethrower;
import nuparu.sevendaystomine.client.gui.inventory.GuiForge;
import nuparu.sevendaystomine.client.gui.inventory.GuiGasGenerator;
import nuparu.sevendaystomine.client.gui.inventory.GuiMinibike;
import nuparu.sevendaystomine.client.gui.inventory.GuiProjector;
import nuparu.sevendaystomine.client.gui.inventory.GuiSeparator;
import nuparu.sevendaystomine.client.gui.inventory.GuiTurretAdvanced;
import nuparu.sevendaystomine.client.gui.inventory.GuiTurretBase;
import nuparu.sevendaystomine.client.gui.inventory.GuiWorkbench;
import nuparu.sevendaystomine.client.gui.inventory.GuiWorkbenchUncrafting;
import nuparu.sevendaystomine.entity.EntityAirdrop;
import nuparu.sevendaystomine.entity.EntityCar;
import nuparu.sevendaystomine.entity.EntityHuman;
import nuparu.sevendaystomine.entity.EntityLootableCorpse;
import nuparu.sevendaystomine.entity.EntityMinibike;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.inventory.ContainerAirdrop;
import nuparu.sevendaystomine.inventory.ContainerBig;
import nuparu.sevendaystomine.inventory.ContainerCampfire;
import nuparu.sevendaystomine.inventory.ContainerChemistryStation;
import nuparu.sevendaystomine.inventory.ContainerComputer;
import nuparu.sevendaystomine.inventory.ContainerForge;
import nuparu.sevendaystomine.inventory.ContainerLootableCorpse;
import nuparu.sevendaystomine.inventory.ContainerMonitor;
import nuparu.sevendaystomine.inventory.ContainerProjector;
import nuparu.sevendaystomine.inventory.ContainerSmall;
import nuparu.sevendaystomine.inventory.ContainerTiny;
import nuparu.sevendaystomine.inventory.container.ContainerBackpack;
import nuparu.sevendaystomine.inventory.container.ContainerCamera;
import nuparu.sevendaystomine.inventory.container.ContainerCar;
import nuparu.sevendaystomine.inventory.container.ContainerFlamethrower;
import nuparu.sevendaystomine.inventory.container.ContainerGenerator;
import nuparu.sevendaystomine.inventory.container.ContainerMinibike;
import nuparu.sevendaystomine.inventory.container.ContainerSeparator;
import nuparu.sevendaystomine.inventory.container.ContainerTurretAdvanced;
import nuparu.sevendaystomine.inventory.container.ContainerTurretBase;
import nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import nuparu.sevendaystomine.inventory.itemhandler.wraper.NameableCombinedInvWrapper;
import nuparu.sevendaystomine.item.ItemGuide;
import nuparu.sevendaystomine.tileentity.TileEntityCampfire;
import nuparu.sevendaystomine.tileentity.TileEntityChemistryStation;
import nuparu.sevendaystomine.tileentity.TileEntityForge;
import nuparu.sevendaystomine.tileentity.TileEntityItemHandler;
import nuparu.sevendaystomine.tileentity.TileEntityMonitor;
import nuparu.sevendaystomine.tileentity.TileEntityWorkbench;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		ItemStack stack = player.getHeldItemMainhand();
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		if (tileEntity instanceof TileEntityItemHandler && !(tileEntity instanceof TileEntityWorkbench)) {
			return ((TileEntityItemHandler<?>) tileEntity).createContainer(player);
		}

		if (ID == 9) {
			Entity entity = world.getEntityByID(y);
			if (entity != null && entity instanceof EntityLootableCorpse) {
				EntityLootableCorpse lootable = (EntityLootableCorpse) entity;
				return new ContainerLootableCorpse(player.inventory, lootable);
			}
		}
		if (ID == 17) {
			Entity entity = world.getEntityByID(y);
			if (entity != null) {
				if (entity instanceof EntityMinibike) {
					EntityMinibike minibike = (EntityMinibike) entity;
					return new ContainerMinibike(player.inventory, minibike);
				} else if (entity instanceof EntityCar) {
					EntityCar car = (EntityCar) entity;
					return new ContainerCar(player.inventory, car);
				}
			}
		}
		if (ID == 18) {
			if (stack.getItem() == ModItems.BACKPACK) {
				final IItemHandlerModifiable playerInventory = (IItemHandlerModifiable) player
						.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
				final IItemHandlerNameable playerInventoryWrapper = new NameableCombinedInvWrapper(player.inventory,
						playerInventory);
				return new ContainerBackpack(playerInventoryWrapper,
						stack.getCapability(ExtendedInventoryProvider.EXTENDED_INV_CAP, EnumFacing.UP));
			}
		}
		if (ID == 21) {
			Entity entity = world.getEntityByID(y);
			if (entity != null && entity instanceof EntityAirdrop) {
				EntityAirdrop airdrop = (EntityAirdrop) entity;
				return new ContainerAirdrop(player.inventory, airdrop);
			}
		}
		
		switch (ID) {
		case 0:
			return ((TileEntityCampfire) tileEntity).createContainer(player);
		case 1:
			return ((TileEntityForge) tileEntity).createContainer(player);
		case 4:
			return null;
		case 5:
			return new ContainerSmall(player.inventory, (IInventory) tileEntity);
		case 6:
			return new ContainerBig(player.inventory, (IInventory) tileEntity);
		case 7:
			return new ContainerComputer(player.inventory, (IInventory) tileEntity);
		case 8:
			return new ContainerMonitor(player, (TileEntityMonitor) tileEntity);
		case 10:
			return new ContainerTiny(player.inventory, (IInventory) tileEntity);
		case 11:
			return ((TileEntityWorkbench) tileEntity).createContainer(player,true);
		case 12:
			return ((TileEntityChemistryStation) tileEntity).createContainer(player);
		case 13:
			return new ContainerProjector(player.inventory, (IInventory) tileEntity);
		case 15:
			return null;
		case 22:
			return null;
		case 27:
			if (stack.getItem() == ModItems.ANALOG_CAMERA) {
				final IItemHandlerModifiable playerInventory = (IItemHandlerModifiable) player
						.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
				final IItemHandlerNameable playerInventoryWrapper = new NameableCombinedInvWrapper(player.inventory,
						playerInventory);
				return new ContainerCamera(playerInventoryWrapper,
						stack.getCapability(ExtendedInventoryProvider.EXTENDED_INV_CAP, EnumFacing.UP));
			}
		case 28:
			Container c = ((TileEntityWorkbench) tileEntity).createContainer(player,false);
			return c;
		}

		return null;

	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		ItemStack stack = player.getHeldItemMainhand();
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		if (ID == 9) {
			Entity entity = world.getEntityByID(y);
			if (entity != null && entity instanceof EntityLootableCorpse) {
				EntityLootableCorpse lootable = (EntityLootableCorpse) entity;
				return new GuiContainerLootableEntity(player.inventory, lootable,
						new ContainerLootableCorpse(player.inventory, lootable));
			}
		}
		if (ID == 15) {
			Entity entity = world.getEntityByID(x);
			if (entity == null || !(entity instanceof EntityHuman))
				return null;
			return new GuiDialogue((EntityHuman) entity);
		}
		if (ID == 17) {
			Entity entity = world.getEntityByID(y);
			if (entity != null) {
				if (entity instanceof EntityMinibike) {
					EntityMinibike minibike = (EntityMinibike) entity;
					return new GuiMinibike(player.inventory, minibike,
							new ContainerMinibike(player.inventory, minibike));
				} else if (entity instanceof EntityCar) {
					EntityCar car = (EntityCar) entity;
					return new GuiCar(player.inventory, car, new ContainerCar(player.inventory, car));
				}
			}
		}
		if (ID == 18) {
			if (stack.getItem() == ModItems.BACKPACK) {
				final IItemHandlerModifiable playerInventory = (IItemHandlerModifiable) player
						.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
				final IItemHandlerNameable playerInventoryWrapper = new NameableCombinedInvWrapper(player.inventory,
						playerInventory);
				return new GuiBackpack(
						new ContainerBackpack(playerInventoryWrapper,
								stack.getCapability(ExtendedInventoryProvider.EXTENDED_INV_CAP, EnumFacing.UP)),
						stack.getDisplayName());
			}

		}
		if (ID == 21) {
			Entity entity = world.getEntityByID(y);
			if (entity != null && entity instanceof EntityAirdrop) {
				EntityAirdrop airdrop = (EntityAirdrop) entity;
				return new GuiContainerAirdrop(player.inventory, airdrop,
						new ContainerAirdrop(player.inventory, airdrop));
			}
		}

		switch (ID) {
		case 0:
			return new GuiCampfire((ContainerCampfire) ((TileEntityCampfire) tileEntity).createContainer(player));
		case 1:
			return new GuiForge((ContainerForge) ((TileEntityForge) tileEntity).createContainer(player));
		case 2:
			if (tileEntity instanceof TileEntityItemHandler && !(tileEntity instanceof TileEntityWorkbench)) {
				return new nuparu.sevendaystomine.client.gui.inventory.GuiContainerSmall(
						(nuparu.sevendaystomine.inventory.container.ContainerSmall) ((TileEntityItemHandler<?>) tileEntity)
								.createContainer(player));
			}
		case 3:
			return new GuiCodeSafeLocked(tileEntity, new BlockPos(x, y, z));
		case 4:
			return new GuiKeySafeLocked(tileEntity, new BlockPos(x, y, z));
		case 5:
			return new GuiContainerSmallOld(player.inventory, (IInventory) tileEntity,
					new ContainerSmall(player.inventory, (IInventory) tileEntity));
		case 6:
			return new GuiContainerBig(player.inventory, (IInventory) tileEntity,
					new ContainerBig(player.inventory, (IInventory) tileEntity));
		case 7:
			return new GuiComputer(player.inventory, (IInventory) tileEntity);
		case 8:
			return new GuiMonitor(player, (TileEntityMonitor) tileEntity);
		case 10:
			return new GuiContainerTiny(player.inventory, (IInventory) tileEntity,
					new ContainerTiny(player.inventory, (IInventory) tileEntity));
		case 11:
			return new GuiWorkbench(player.inventory, ((TileEntityWorkbench) tileEntity).createContainer(player,true));
		case 12:
			return new GuiChemistryStation(
					(ContainerChemistryStation) ((TileEntityChemistryStation) tileEntity).createContainer(player));
		case 13:
			if (tileEntity instanceof TileEntityItemHandler) {
				return new GuiGasGenerator(
						(ContainerGenerator) ((TileEntityItemHandler<?>) tileEntity).createContainer(player));
			}
		case 14:
			return new GuiProjector(player.inventory, (IInventory) tileEntity,
					new ContainerProjector(player.inventory, (IInventory) tileEntity));
		case 16:
			if (tileEntity instanceof TileEntityItemHandler) {

				return new nuparu.sevendaystomine.client.gui.inventory.GuiContainerSmall(
						(nuparu.sevendaystomine.inventory.container.ContainerSmall) ((TileEntityItemHandler<?>) tileEntity)
								.createContainer(player));
			}
		case 19:
			if (tileEntity instanceof TileEntityItemHandler) {

				return new nuparu.sevendaystomine.client.gui.inventory.GuiBatteryStation(
						(nuparu.sevendaystomine.inventory.container.ContainerBatteryStation) ((TileEntityItemHandler<?>) tileEntity)
								.createContainer(player));
			}
		case 20:
			if (tileEntity instanceof TileEntityItemHandler) {
				return new GuiCombustionGenerator(
						(ContainerGenerator) ((TileEntityItemHandler<?>) tileEntity).createContainer(player));
			}
		case 22:
			return new GuiBook(((ItemGuide) stack.getItem()).data);

		case 23:
			if (tileEntity instanceof TileEntityItemHandler) {

				return new GuiSeparator(
						(ContainerSeparator) ((TileEntityItemHandler<?>) tileEntity).createContainer(player));
			}
		case 24:
			if (tileEntity instanceof TileEntityItemHandler) {

				return new GuiTurretBase(
						(ContainerTurretBase) ((TileEntityItemHandler<?>) tileEntity).createContainer(player));
			}
		case 25:
			if (tileEntity instanceof TileEntityItemHandler) {

				return new GuiTurretAdvanced(
						(ContainerTurretAdvanced) ((TileEntityItemHandler<?>) tileEntity).createContainer(player));
			}
		case 26:
			if (tileEntity instanceof TileEntityItemHandler) {

				return new GuiFlamethrower(
						(ContainerFlamethrower) ((TileEntityItemHandler<?>) tileEntity).createContainer(player));
			}
		case 27:
			if (stack.getItem() == ModItems.ANALOG_CAMERA) {
				final IItemHandlerModifiable playerInventory = (IItemHandlerModifiable) player
						.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
				final IItemHandlerNameable playerInventoryWrapper = new NameableCombinedInvWrapper(player.inventory,
						playerInventory);
				return new GuiCamera(
						new ContainerCamera(playerInventoryWrapper,
								stack.getCapability(ExtendedInventoryProvider.EXTENDED_INV_CAP, EnumFacing.UP)),
						stack.getDisplayName());
			}
		case 28:
			return new GuiWorkbenchUncrafting(player.inventory, ((TileEntityWorkbench) tileEntity).createContainer(player,false));
		}

		return null;
	}

}
