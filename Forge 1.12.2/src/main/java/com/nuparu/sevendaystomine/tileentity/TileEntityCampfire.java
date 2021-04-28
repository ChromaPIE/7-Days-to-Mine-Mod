package com.nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.block.BlockCampfire;
import com.nuparu.sevendaystomine.block.BlockCookware;
import com.nuparu.sevendaystomine.crafting.campfire.CampfireRecipeManager;
import com.nuparu.sevendaystomine.crafting.campfire.ICampfireRecipe;
import com.nuparu.sevendaystomine.inventory.ContainerCampfire;
import com.nuparu.sevendaystomine.inventory.ContainerForge;
import com.nuparu.sevendaystomine.inventory.IContainerCallbacks;
import com.nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import com.nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import com.nuparu.sevendaystomine.inventory.itemhandler.wraper.NameableCombinedInvWrapper;
import com.nuparu.sevendaystomine.item.ItemCookware;
import com.nuparu.sevendaystomine.item.ItemMold;
import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class TileEntityCampfire extends TileEntity implements IContainerCallbacks, ILootContainer, ITickable {

	private static final ITextComponent DEFAULT_NAME = new TextComponentTranslation("container.campfire");
	
	public enum EnumSlots {
		INPUT_SLOT, INPUT_SLOT2, INPUT_SLOT3, INPUT_SLOT4, OUTPUT_SLOT, FUEL_SLOT, POT_SLOT
	}

	private int burnTime;
	private int currentItemBurnTime;
	private int cookTime;
	private int totalCookTime;
	
	public ICampfireRecipe currentRecipe = null;
	
	private final ItemHandlerNameable HANDLER_INPUT = new ItemHandlerNameable(4, DEFAULT_NAME);
	private final ItemHandlerNameable HANDLER_OUTPUT = new ItemHandlerNameable(1, DEFAULT_NAME);
	private final ItemHandlerNameable HANDLER_FUEL = new ItemHandlerNameable(1, DEFAULT_NAME){
		@Override
	    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
	    {
	        return !stack.isEmpty();
	    }
	};
	private final ItemHandlerNameable HANDLER_POT = new ItemHandlerNameable(1, DEFAULT_NAME) {
		@Override
	    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
	    {
	        return !stack.isEmpty() && ((stack.getItem() instanceof ItemBlock && ((ItemBlock)stack.getItem()).getBlock() instanceof BlockCookware) || stack.getItem() instanceof ItemCookware || stack.getItem() instanceof ItemBucket);
	    }
	};

	public TileEntityCampfire() {

	}

	@Override
	public void update() {
		boolean flag = this.isBurning();
		boolean flag1 = false;

		if (this.isBurning()) {
			--this.burnTime;
		}

		if (!this.world.isRemote) {
			ItemStack itemstack = this.getInventory().getStackInSlot(EnumSlots.FUEL_SLOT.ordinal());
            if (this.isBurning()) {
            	AxisAlignedBB AABB = new AxisAlignedBB(pos.getX() + 0.2, pos.getY(), pos.getZ() + 0.2, pos.getX() + 0.8,
    					pos.getY() + 0.8, pos.getZ() + 0.8);
    			List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, AABB);
    			for (EntityLivingBase entity : list) {
    				if (world.rand.nextInt(4) == 0) {
    					entity.setFire(5 + world.rand.nextInt(5));
    				}
    			}
            }
            if (this.isBurning() || hasPot() && !isInputEmpty()) {
				if (!this.isBurning() && this.canSmelt()) {
					this.burnTime = Utils.getItemBurnTime(itemstack);
					this.currentItemBurnTime = this.burnTime;

					if (this.isBurning()) {
						flag1 = true;

						if (!itemstack.isEmpty()) {
							Item item = itemstack.getItem();
							itemstack.shrink(1);

							if (itemstack.isEmpty()) {
								ItemStack item1 = item.getContainerItem(itemstack);
								this.getInventory().setStackInSlot(EnumSlots.FUEL_SLOT.ordinal(), item1);
							}
						}
					}
					this.totalCookTime = this.getCookTime(null);
				}

				if (this.isBurning() && this.canSmelt()) {
					++this.cookTime;
					if (this.cookTime == this.totalCookTime) {
						this.cookTime = 0;
						this.totalCookTime = this.getCookTime(null);
						this.smeltItem();
						flag1 = true;
					}
				} else {
					this.cookTime = 0;
				}
			} else if (!this.isBurning() && this.cookTime > 0) {
				this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
			}

			if (flag != this.isBurning()) {
				flag1 = true;
				BlockCampfire.setState(this.isBurning(), this.world, this.pos);
			}
		}

		if (flag1) {
			world.markBlockRangeForRenderUpdate(pos, pos);
			world.notifyBlockUpdate(pos, getState(), getState(), 3);
			world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
			markDirty();
		}



	}

	private boolean canSmelt() {
		if (!getOutputSlot().isEmpty() && getOutputSlot().getCount() > Math.min(getOutputSlot().getItem().getItemStackLimit(getOutputSlot()),64))
			return false;

		if (isInputEmpty() || !hasPot())
			return false;

		for (ICampfireRecipe recipe : CampfireRecipeManager.getInstance().getRecipes()) {
			if (recipe.matches(this, this.world)) {
				if (!getOutputSlot().isEmpty() && !ItemStack.areItemsEqual(getOutputSlot(), recipe.getOutput(this)))
					return false;
				if (!ItemStack.areItemsEqual(getPotSlot(), recipe.getPot()))
					return false;
				if (getOutputSlot().getCount() + recipe.getResult().getCount() <= Math.min(getOutputSlot().getItem().getItemStackLimit(getOutputSlot()),64)) {
					currentRecipe = recipe;
					return true;
				}
			}
		}
		return false;
	}

	public void smeltItem() {

		ICampfireRecipe recipeToUse = null;
		for (ICampfireRecipe recipe : CampfireRecipeManager.getInstance().getRecipes()) {
			if (recipe.matches(this, this.world)) {
				recipeToUse = recipe;
				break;
			}
		}
		if (recipeToUse != null) {

			ItemStack currentOutput = getOutputSlot();
			if (ItemStack.areItemsEqual(getPotSlot(), recipeToUse.getPot())) {

				if (currentOutput.isEmpty()) {
					getInventory().setStackInSlot(EnumSlots.OUTPUT_SLOT.ordinal(), recipeToUse.getResult());

				} else {
					if (ItemStack.areItemsEqual(currentOutput, recipeToUse.getResult()) && currentOutput.getCount()
							+ recipeToUse.getResult().getCount() <= Math.min(getOutputSlot().getItem().getItemStackLimit(getOutputSlot()),64)) {

						currentOutput.grow(recipeToUse.getResult().getCount());
						getInventory().setStackInSlot(EnumSlots.OUTPUT_SLOT.ordinal(), currentOutput);
					}
				}
				consumeInput(recipeToUse);
			}
		}
	}

	public void consumeInput(ICampfireRecipe recipe) {
		recipe.consumeInput(this);
	}

	public boolean isInputEmpty() {
		return (getInventory().getStackInSlot(EnumSlots.INPUT_SLOT.ordinal()).isEmpty()
				&& getInventory().getStackInSlot(EnumSlots.INPUT_SLOT2.ordinal()).isEmpty()
				&& getInventory().getStackInSlot(EnumSlots.INPUT_SLOT3.ordinal()).isEmpty()
				&& getInventory().getStackInSlot(EnumSlots.INPUT_SLOT4.ordinal()).isEmpty());
	}

	public boolean hasPot() {
		return !getInventory().getStackInSlot(EnumSlots.POT_SLOT.ordinal()).isEmpty();
	}

	public boolean hasFuel() {
		return !getInventory().getStackInSlot(EnumSlots.FUEL_SLOT.ordinal()).isEmpty();
	}

	public ItemStack getOutputSlot() {
		return getInventory().getStackInSlot(EnumSlots.OUTPUT_SLOT.ordinal());
	}

	public ItemStack getPotSlot() {
		return getInventory().getStackInSlot(EnumSlots.POT_SLOT.ordinal());
	}

	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("HandlerInput")) {
			HANDLER_INPUT.deserializeNBT(compound.getCompoundTag("HandlerInput"));
		}
		if (compound.hasKey("HandlerOutput")) {
			HANDLER_OUTPUT.deserializeNBT(compound.getCompoundTag("HandlerOutput"));
		}
		if (compound.hasKey("HandlerFuel")) {
			HANDLER_FUEL.deserializeNBT(compound.getCompoundTag("HandlerFuel"));
		}
		if (compound.hasKey("HandlerMold")) {
			HANDLER_POT.deserializeNBT(compound.getCompoundTag("HandlerMold"));
		}
		
		this.burnTime = compound.getInteger("BurnTime");
		this.cookTime = compound.getInteger("CookTime");
		this.totalCookTime = compound.getInteger("CookTimeTotal");
		this.currentItemBurnTime = Utils.getItemBurnTime(this.getInventory().getStackInSlot(1));
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("HandlerInput", HANDLER_INPUT.serializeNBT());
		compound.setTag("HandlerOutput", HANDLER_OUTPUT.serializeNBT());
		compound.setTag("HandlerFuel", HANDLER_FUEL.serializeNBT());
		compound.setTag("HandlerMold", HANDLER_POT.serializeNBT());
		
		compound.setInteger("BurnTime", (short) this.burnTime);
		compound.setInteger("CookTime", (short) this.cookTime);
		compound.setInteger("CookTimeTotal", (short) this.totalCookTime);

		return compound;
	}

	public int getCookTime(ItemStack stack) {
		return 600;
	}

	public List<ItemStack> getActiveInventory() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		list.add(getInventory().getStackInSlot(EnumSlots.INPUT_SLOT.ordinal()));
		list.add(getInventory().getStackInSlot(EnumSlots.INPUT_SLOT2.ordinal()));
		list.add(getInventory().getStackInSlot(EnumSlots.INPUT_SLOT3.ordinal()));
		list.add(getInventory().getStackInSlot(EnumSlots.INPUT_SLOT4.ordinal()));
		return list;
	}

	public ItemStack[][] getActiveInventoryAsArray() {
		ItemStack[][] array = new ItemStack[2][2];

		array[0][0] = getInventory().getStackInSlot(EnumSlots.INPUT_SLOT.ordinal());
		array[0][1] = getInventory().getStackInSlot(EnumSlots.INPUT_SLOT2.ordinal());
		array[1][0] = getInventory().getStackInSlot(EnumSlots.INPUT_SLOT3.ordinal());
		array[1][1] = getInventory().getStackInSlot(EnumSlots.INPUT_SLOT4.ordinal());

		return array;
	}
	
	public ICampfireRecipe getCurrentRecipe() {
		return this.currentRecipe;
	}


	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		} else {
			return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
					(double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}
	

	private IBlockState getState() {
		return world.getBlockState(pos);
	}


	@SideOnly(Side.CLIENT)
	public static boolean isBurning(TileEntityCampfire campfire) {
		return campfire.getField(0) > 0;
	}

	public boolean isBurning() {
		return this.burnTime > 0;
	}



	public static boolean isItemFuel(ItemStack stack) {
		return Utils.getItemBurnTime(stack) > 0;
	}

	public CombinedInvWrapper getInventory() {
		return new CombinedInvWrapper(this.HANDLER_INPUT, this.HANDLER_OUTPUT, this.HANDLER_FUEL, this.HANDLER_POT);
	}
	
	public NonNullList<ItemStack> getDrops() {
		return Utils.dropItemHandlerContents(getInventory(), getWorld().rand);
	}

	public void setDisplayName(String displayName) {
		HANDLER_INPUT.setDisplayName(new TextComponentString(displayName));
	}
	
	public ITextComponent getDisplayName() {
		return HANDLER_INPUT.getDisplayName();
	}

	public int getField(int id) {
		switch (id) {
		case 0:
			return this.burnTime;
		case 1:
			return this.currentItemBurnTime;
		case 2:
			return this.cookTime;
		case 3:
			return this.totalCookTime;
		default:
			return 0;
		}
	}

	public void setField(int id, int value) {
		switch (id) {
		case 0:
			this.burnTime = value;
			break;
		case 1:
			this.currentItemBurnTime = value;
			break;
		case 2:
			this.cookTime = value;
			break;
		case 3:
			this.totalCookTime = value;
		}
	}

	public int getFieldCount() {
		return 4;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			switch(facing) {
			case UP : return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(HANDLER_INPUT);
			case DOWN : return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(HANDLER_OUTPUT);
			case NORTH :
			case WEST :
			case SOUTH :
			case EAST : return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new CombinedInvWrapper(HANDLER_FUEL,HANDLER_POT));
			}
		}

		return null;
	}

	@Override
	public ResourceLocation getLootTable() {
		return null;
	}

	@Override
	public void onContainerOpened(EntityPlayer player) {
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
	}
	
	public ContainerCampfire createContainer(EntityPlayer player) {
		final IItemHandlerModifiable playerInventory = (IItemHandlerModifiable) player
				.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		final IItemHandlerNameable playerInventoryWrapper = new NameableCombinedInvWrapper(player.inventory,
				playerInventory);

		return new ContainerCampfire(playerInventoryWrapper, getInventory(), this, player);
	}

}
