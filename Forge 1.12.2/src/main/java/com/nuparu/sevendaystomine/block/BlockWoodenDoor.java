package com.nuparu.sevendaystomine.block;

import java.util.Random;

import com.nuparu.sevendaystomine.client.sound.SoundHelper;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.item.ItemUpgrader;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockWoodenDoor extends BlockDoorBase implements IUpgradeable {

	public BlockWoodenDoor() {
		super(Material.WOOD);
		this.setHardness(1.7F);
		this.setResistance(3.0F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomStateMapper() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IStateMapper getStateMapper() {
		return new StateMap.Builder().ignore(POWERED).build();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomItemMesh() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemMeshDefinition getItemMesh() {
		return null;
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(ModItems.WOODEN_DOOR_ITEM);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER ? net.minecraft.init.Items.AIR
				: ModItems.WOODEN_DOOR_ITEM;
	}

	@Override
	public ItemStack[] getItems() {
		return new ItemStack[] { new ItemStack(ModItems.PLANK_WOOD, 6) };
	}

	@Override
	public SoundEvent getSound() {
		return SoundHelper.UPGRADE_WOOD;
	}

	@Override
	public IBlockState getPrev(World world, BlockPos pos) {
		return Blocks.AIR.getDefaultState();
	}

	@Override
	public IBlockState getResult(World world, BlockPos pos) {
		IBlockState oldState = world.getBlockState(pos);

		return ModBlocks.WOODEN_DOOR_REINFORCED.getDefaultState().withProperty(FACING, oldState.getValue(FACING))
				.withProperty(OPEN, oldState.getValue(OPEN)).withProperty(HINGE, oldState.getValue(HINGE))
				.withProperty(POWERED, oldState.getValue(POWERED)).withProperty(HALF, oldState.getValue(HALF));
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.getHeldItem(hand).getItem() instanceof ItemUpgrader && playerIn.isSneaking()) {
			return true;
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public void onUpgrade(World world, BlockPos pos, IBlockState oldState) {
		EnumDoorHalf half = oldState.getValue(HALF);
		if (half == EnumDoorHalf.LOWER) {
			BlockPos blockPos = pos.up();
			IBlockState state = world.getBlockState(blockPos);
			if (state.getBlock() == net.minecraft.init.Blocks.AIR || state.getBlock() instanceof BlockDoorBase) {
				world.setBlockState(blockPos,getResult(world,blockPos));
			}
		}
		else if (half == EnumDoorHalf.UPPER) {
			BlockPos blockPos = pos.down();
			IBlockState state = world.getBlockState(blockPos);
			if (state.getBlock() == net.minecraft.init.Blocks.AIR || state.getBlock() instanceof BlockDoorBase) {
				world.setBlockState(blockPos,getResult(world,blockPos));
			}
		}
	}

}
