package com.nuparu.sevendaystomine.tileentity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.computer.process.BootingProcess;
import com.nuparu.sevendaystomine.computer.process.CreateAccountProcess;
import com.nuparu.sevendaystomine.computer.process.DesktopProcess;
import com.nuparu.sevendaystomine.computer.process.HardDrive;
import com.nuparu.sevendaystomine.computer.process.ProcessRegistry;
import com.nuparu.sevendaystomine.computer.process.TickingProcess;
import com.nuparu.sevendaystomine.computer.process.WindowedProcess;
import com.nuparu.sevendaystomine.computer.process.WindowsDesktopProcess;
import com.nuparu.sevendaystomine.computer.process.WindowsLoginProcess;
import com.nuparu.sevendaystomine.electricity.network.INetwork;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.network.PacketManager;
import com.nuparu.sevendaystomine.network.packets.SyncTileEntityMessage;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

public class TileEntityComputer extends TileEntityLockableLoot implements ISidedInventory, ITickable, INetwork {
	private NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(7, ItemStack.EMPTY);

	private TileEntityMonitor monitorTE = null;

	// Machine variables
	private EnumState state = EnumState.OFF;
	private EnumSystem system = EnumSystem.NONE;

	// processes
	private ArrayList<TickingProcess> processes = new ArrayList<TickingProcess>();
	// Persistent data
	private boolean registered = false;
	private String username = "";
	private String password = "";
	private String hint = "";

	// hard drive
	private HardDrive hardDrive = new HardDrive(this);

	private ArrayList<BlockPos> network = new ArrayList<BlockPos>();

	public TileEntityComputer() {

	}

	public boolean isOn() {
		return state != EnumState.OFF;
	}

	public void setState(EnumState state) {
		this.state = state;
		this.markDirty();
	}

	public EnumSystem getSystem() {
		return this.system;
	}

	public EnumState getState() {
		return this.state;
	}

	public HardDrive getHardDrive() {
		return this.hardDrive;
	}

	public void installSystem(EnumSystem system) {
		this.system = system;
	}

	public void startComputer() {
		setState(EnumState.BOOT_SCREEN);
		processes.clear();
		startProcess(new BootingProcess());
	}

	public void turnOff() {
		setState(EnumState.OFF);
		processes.clear();
		this.markDirty();
	}

	@SuppressWarnings("unchecked")
	public void startProcess(TickingProcess process) {
		process.setComputer(this);

		if (process instanceof WindowedProcess) {
			int order = 0;
			for (TickingProcess tp : (ArrayList<TickingProcess>) this.getProcessesList().clone()) {
				if (tp instanceof WindowedProcess) {
					order++;
				}
			}
			if (((WindowedProcess) process).getWindowOrder() == -1) {
				((WindowedProcess) process).setWindowOrder(order);
			}
		}
		processes.add(process);
		this.markDirty();
	}

	public void startProcess(NBTTagCompound nbt, boolean sync) {
		if (isCompleted() && isOn()) {
			ArrayList<TickingProcess> processes2 = getProcessesList();

			if (!nbt.hasKey(ProcessRegistry.RES_KEY)) {
				return;
			}

			boolean flag = false;

			for (TickingProcess process : processes2) {
				if (nbt.hasUniqueId("id") && process.getId().equals(nbt.getUniqueId("id"))) {
					process.readFromNBT(nbt);
					process.setComputer(this);
					flag = true;
					break;
				}
			}

			if (flag) {
				this.markDirty();
				return;
			}
			if(sync) {
				return;
			}
			String res = nbt.getString(ProcessRegistry.RES_KEY);
			TickingProcess process = ProcessRegistry.INSTANCE.getByRes(new ResourceLocation(res));
			if (process != null) {
				process.readFromNBT(nbt);
				startProcess(process);
			}
		}

	}

	public void killProcess(TickingProcess process) {
		processes.remove(process);
		this.markDirty();
	}

	public TickingProcess getProcessByUUID(UUID id) {
		for (TickingProcess process : getProcessesList()) {
			if (process.getId().equals(id)) {
				return process;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<TickingProcess> getProcessesList() {
		return (ArrayList<TickingProcess>) (processes.clone());
	}

	public void onBootFinished() {
		setState(EnumState.LOGIN);
		if (!isRegistered()) {
			startProcess(new CreateAccountProcess());
		} else {
			if (password.isEmpty()) {
				setState(EnumState.NORMAL);
				startProcess(new WindowsDesktopProcess());
			} else {
				startProcess(new WindowsLoginProcess());
			}
		}
	}

	public void onLogin(WindowsLoginProcess process) {
		setState(EnumState.NORMAL);
		startProcess(new WindowsDesktopProcess());
		killProcess(process);
	}

	public void onAccountCreated(CreateAccountProcess process) {
		setState(EnumState.NORMAL);
		setRegistered(true);
		startProcess(new WindowsDesktopProcess());
		this.username = process.username;
		this.password = process.password;
		this.hint = process.hint;
		killProcess(process);
	}

	@Override
	public void update() {

		if (world.isRemote) {
			return;
		}
		if (!isCompleted() && isOn()) {
			turnOff();
			return;
		}
		if (!world.isRemote) {
			for (TickingProcess process : getProcessesList()) {
				process.tick();
			}
		}

	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.inventory = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.inventory);
		this.state = EnumState.getEnum(compound.getString("state"));
		this.system = EnumSystem.getEnum(compound.getString("system"));

		this.username = compound.getString("username");
		this.password = compound.getString("password");
		this.hint = compound.getString("hint");
		this.setRegistered(compound.getBoolean("registered"));

		NBTTagList list = compound.getTagList("processes", Constants.NBT.TAG_COMPOUND);

		ArrayList<TickingProcess> processes2 = getProcessesList();

		processes.clear();

		for (int i = 0; i < list.tagCount(); ++i) {
			NBTTagCompound nbt = list.getCompoundTagAt(i);
			if (!nbt.hasKey(ProcessRegistry.RES_KEY)) {
				continue;
			}

			boolean flag = false;

			for (TickingProcess process : processes2) {
				if (nbt.hasKey("id") && process.getId().compareTo(nbt.getUniqueId("id")) == 0) {
					process.readFromNBT(nbt);
					startProcess(process);
					flag = true;
					break;
				}
			}

			if (flag) {
				continue;
			}

			String res = nbt.getString(ProcessRegistry.RES_KEY);
			TickingProcess process = ProcessRegistry.INSTANCE.getByRes(new ResourceLocation(res));
			if (process != null) {
				process.readFromNBT(nbt);
				startProcess(process);
			}
		}

		if (compound.hasKey("drive")) {
			if (this.hardDrive != null) {
				this.hardDrive.readFromNBT(compound.getCompoundTag("drive"));
			} else {
				this.hardDrive = new HardDrive(this);
				this.hardDrive.readFromNBT(compound.getCompoundTag("drive"));
			}
		}

		network.clear();
		NBTTagList list2 = compound.getTagList("network", Constants.NBT.TAG_LONG);
		for (int i = 0; i < list2.tagCount(); ++i) {
			NBTTagLong nbt = (NBTTagLong) list2.get(i);
			BlockPos blockPos = BlockPos.fromLong(nbt.getLong());
			network.add(blockPos);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		ItemStackHelper.saveAllItems(compound, this.inventory);
		compound.setString("state", state.getName());
		compound.setString("system", system.getName());

		compound.setString("username", username);
		compound.setString("password", password);
		compound.setString("hint", hint);

		compound.setBoolean("registered", isRegistered());
		NBTTagList list = new NBTTagList();
		for (TickingProcess process : (ArrayList<TickingProcess>) processes.clone()) {
			list.appendTag(process.writeToNBT(new NBTTagCompound()));
		}
		compound.setTag("processes", list);

		if (hardDrive != null) {
			compound.setTag("drive", hardDrive.writeToNBT(new NBTTagCompound()));
		}

		NBTTagList list2 = new NBTTagList();
		for (BlockPos net : getConnections()) {
			list2.appendTag(new NBTTagLong(net.toLong()));
		}

		compound.setTag("network", list2);

		return compound;
	}

	public boolean isCompleted() {
		for (int i = 0; i < 5; i++) {
			if (isPartCorrect(i) == false) {
				return false;
			}
		}
		return true;
	}

	private boolean isPartCorrect(int slot) {

		ItemStack stack = inventory.get(slot);
		if (stack.isEmpty()) {
			return false;
		}
		Item item = stack.getItem();

		switch (slot) {
		case 0:
			return item == ModItems.POWER_SUPPLY;
		case 1:
			return item == ModItems.MOTHERBOARD;
		case 2:
			return item == ModItems.CPU;
		case 3:
			return item == ModItems.GPU;
		case 4:
			return item == ModItems.HDD;
		case 5:
			return item == ModItems.RAM;
		}
		return false;
	}

	@Override
	public int getSizeInventory() {
		return this.inventory.size();
	}

	public NonNullList<ItemStack> getInventory() {
		return this.inventory;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.inventory) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.inventory.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.inventory, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.inventory, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.inventory.set(index, stack);
		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
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

	@Override
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		this.inventory.clear();
	}

	@Override
	public String getName() {
		return "container.computer";
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return null;
	}

	@Override
	public String getGuiID() {
		return SevenDaysToMine.MODID + ":computer";
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return null;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return false;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(net.minecraft.network.NetworkManager net,
			net.minecraft.network.play.server.SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
		world.notifyBlockUpdate(pos, Blocks.AIR.getDefaultState(), world.getBlockState(pos), 1);
	}

	@Override
	public void markDirty() {
		super.markDirty();
		if (monitorTE != null && !world.isRemote) {
			for (EntityPlayer player : monitorTE.getLookingPlayers()) {
				PacketManager.syncTileEntity.sendTo(new SyncTileEntityMessage(writeToNBT(new NBTTagCompound()), pos),
						(EntityPlayerMP) player);
			}
		}
	}

	public TileEntityMonitor getMonitorTE() {
		return monitorTE;
	}

	public void setMonitorTE(TileEntityMonitor monitorTE) {
		this.monitorTE = monitorTE;
	}

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public String getUsername() {
		return username;
	}

	public String getHint() {
		return hint;
	}

	public boolean verifyPassword(String s) {
		return s.equals(password);
	}

	public enum EnumState {
		OFF("off"), BOOT_SCREEN("boot"), STARTING("starting"), SIGNUP("signup"), LOGIN("login"), NORMAL("normal"),
		CRASH("crash");

		private String name;

		private EnumState(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public static EnumState getEnum(String value) {
			for (EnumState st : EnumState.values()) {
				if (st.name.equalsIgnoreCase(value)) {
					return st;
				}
			}
			return EnumState.OFF;
		}
	}

	public enum EnumSystem {
		NONE("none", ""), MAC("mac", "Mac OS X"), LINUX("linux", "Linux"), WIN98("win98", "Windows 98"),
		WINXP("winXp", "Windows XP"), WIN7("win7", "Windows 7"), WIN8("win8", "Windows 8"),
		WIN10("win10", "Windows 10");

		private String name;
		private String readable;

		private EnumSystem(String name, String readable) {
			this.name = name;
			this.readable = readable;
		}

		public String getName() {
			return name;
		}

		public String getReadeable() {
			return this.readable;
		}

		public static EnumSystem getEnum(String value) {
			for (EnumSystem st : EnumSystem.values()) {
				if (st.name.equalsIgnoreCase(value)) {
					return st;
				}
			}
			return EnumSystem.NONE;
		}
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return inventory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BlockPos> getConnections() {
		return (List<BlockPos>) network.clone();
	}

	@Override
	public void connectTo(INetwork toConnect) {
		if (!isConnectedTo(toConnect)) {
			network.add(toConnect.getPosition());
			toConnect.connectTo(this);
			markDirty();
		}
	}

	@Override
	public void disconnect(INetwork toDisconnect) {
		if (isConnectedTo(toDisconnect)) {
			network.remove(toDisconnect.getPosition());
			toDisconnect.disconnect(this);
			markDirty();
		}
	}

	@Override
	public boolean isConnectedTo(INetwork net) {
		return network.contains(net.getPosition());
	}

	@Override
	public void disconnectAll() {
		for (BlockPos pos : getConnections()) {
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof INetwork) {
				((INetwork) te).disconnect(this);
			}
		}
	}

	@Override
	public BlockPos getPosition() {
		return this.getPos();
	}

}
