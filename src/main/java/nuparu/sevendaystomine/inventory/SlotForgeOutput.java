package nuparu.sevendaystomine.inventory;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.tileentity.TileEntityForge;

public class SlotForgeOutput extends SlotItemHandler {
	/** The player that is using the GUI where this slot resides. */
	private final EntityPlayer player;
	private int removeCount;

	public SlotForgeOutput(EntityPlayer player, IItemHandler inventoryIn, int slotIndex, int xPosition, int yPosition) {
		super(inventoryIn, slotIndex, xPosition, yPosition);
		this.player = player;
	}

	@Override
	public void onSlotChange(ItemStack p_75220_1_, ItemStack p_75220_2_) {
		int i = p_75220_2_.getCount() - p_75220_1_.getCount();
		this.onTake(player,p_75220_1_);
	}

	/**
	 * Check if the stack is allowed to be placed in this slot, used for armor slots
	 * as well as furnace fuel.
	 */
	public boolean isItemValid(ItemStack stack) {
		return false;
	}

	/**
	 * Decrease the size of the stack in slot (first int arg) by the amount of the
	 * second int arg. Returns the new stack.
	 */
	public ItemStack decrStackSize(int amount) {
		if (this.getHasStack()) {
			this.removeCount += Math.min(amount, this.getStack().getCount());
		}

		return super.decrStackSize(amount);
	}

	public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
		this.onCrafting(stack);
		super.onTake(thePlayer, stack);
		return stack;
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not
	 * ore and wood. Typically increases an internal count then calls
	 * onCrafting(item).
	 */
	protected void onCrafting(ItemStack stack, int amount) {
		this.removeCount += amount;
		this.onCrafting(stack);
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not
	 * ore and wood.
	 */
	protected void onCrafting(ItemStack stack) {
		stack.onCrafting(this.player.world, this.player, this.removeCount);

		if (!this.player.world.isRemote && inventory instanceof TileEntityForge) {
			TileEntityForge tileEntity = (TileEntityForge) inventory;

			int i = this.removeCount;
			float f = tileEntity.getCurrentRecipe() == null ? 5 : tileEntity.getCurrentRecipe().intGetXP(this.player);

			if (f == 0.0F) {
				i = 0;
			} else if (f < 1.0F) {
				int j = MathHelper.floor((float) i * f);

				if (j < MathHelper.ceil((float) i * f) && Math.random() < (double) ((float) i * f - (float) j)) {
					++j;
				}

				i = j;
			}

			while (i > 0) {
				int k = EntityXPOrb.getXPSplit(i);
				i -= k;
				this.player.world.spawnEntity(new EntityXPOrb(this.player.world, this.player.posX,
						this.player.posY + 0.5D, this.player.posZ + 0.5D, k));
			}
		}

		this.removeCount = 0;
		net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerSmeltedEvent(player, stack);
	}
}
