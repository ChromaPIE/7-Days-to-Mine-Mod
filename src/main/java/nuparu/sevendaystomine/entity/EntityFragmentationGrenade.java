package nuparu.sevendaystomine.entity;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;
import javax.vecmath.Vector3d;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityFragmentationGrenade extends Entity implements IProjectile {

	private int age = 0;

	private int xTile;
	private int yTile;
	private int zTile;
	private Block inTile;
	protected boolean inGround;
	public int throwableShake;
	/** The entity that threw this throwable item. */
	protected EntityLivingBase thrower;
	private String throwerName;
	private int ticksInGround;
	private int ticksInAir;
	public Entity ignoreEntity;
	private int ignoreTime;

	public EntityFragmentationGrenade(World worldIn) {
		super(worldIn);
		this.xTile = -1;
		this.yTile = -1;
		this.zTile = -1;
		this.setSize(0.3f, 0.3f);
	}

	public EntityFragmentationGrenade(World worldIn, EntityLivingBase throwerIn) {
		this(worldIn, throwerIn.posX, throwerIn.posY + (double) throwerIn.getEyeHeight() - 0.10000000149011612D,
				throwerIn.posZ);
		this.thrower = throwerIn;
	}

	public EntityFragmentationGrenade(World worldIn, double x, double y, double z) {
		this(worldIn);
		this.setPosition(x, y, z);

	}

	protected void entityInit() {
	}

	/**
	 * Checks if the entity is in range to render.
	 */
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance) {
		double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;

		if (Double.isNaN(d0)) {
			d0 = 4.0D;
		}

		d0 = d0 * 64.0D;
		return distance < d0 * d0;
	}

	/**
	 * Sets throwable heading based on an entity that's throwing it
	 */
	public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset,
			float velocity, float inaccuracy) {
		float f = -MathHelper.sin(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
		float f1 = -MathHelper.sin((rotationPitchIn + pitchOffset) * 0.017453292F);
		float f2 = MathHelper.cos(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
		this.shoot((double) f, (double) f1, (double) f2, velocity, inaccuracy);
		this.motionX += entityThrower.motionX;
		this.motionZ += entityThrower.motionZ;

		if (!entityThrower.onGround) {
			this.motionY += entityThrower.motionY;
		}
	}

	/**
	 * Similar to setArrowHeading, it's point the throwable entity to a x, y, z
	 * direction.
	 */
	public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
		float f = MathHelper.sqrt(x * x + y * y + z * z);
		x = x / (double) f;
		y = y / (double) f;
		z = z / (double) f;
		x = x + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		y = y + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		z = z + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		x = x * (double) velocity;
		y = y * (double) velocity;
		z = z * (double) velocity;
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		float f1 = MathHelper.sqrt(x * x + z * z);
		this.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
		this.rotationPitch = (float) (MathHelper.atan2(y, (double) f1) * (180D / Math.PI));
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
		this.ticksInGround = 0;
	}

	/**
	 * Updates the entity motion clientside, called by packets from the server
	 */
	@SideOnly(Side.CLIENT)
	public void setVelocity(double x, double y, double z) {
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;

		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt(x * x + z * z);
			this.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
			this.rotationPitch = (float) (MathHelper.atan2(y, (double) f) * (180D / Math.PI));
			this.prevRotationYaw = this.rotationYaw;
			this.prevRotationPitch = this.rotationPitch;
		}
	}

	@Override
	public void onUpdate() {

		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;
		super.onUpdate();

		Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
		Vec3d vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d, vec3d1);
		vec3d = new Vec3d(this.posX, this.posY, this.posZ);
		vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

		if (raytraceresult != null) {
			vec3d1 = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
		}

		Entity entity = null;
		List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this,
				this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D));
		double d0 = 0.0D;
		boolean flag = false;

		for (int i = 0; i < list.size(); ++i) {
			Entity entity1 = list.get(i);

			if (entity1.canBeCollidedWith()) {
				if (entity1 == this.ignoreEntity) {
					flag = true;
				} else if (this.thrower != null && this.ticksExisted < 2 && this.ignoreEntity == null) {
					this.ignoreEntity = entity1;
					flag = true;
				} else {
					flag = false;
					AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
					RayTraceResult raytraceresult1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);

					if (raytraceresult1 != null) {
						double d1 = vec3d.squareDistanceTo(raytraceresult1.hitVec);

						if (d1 < d0 || d0 == 0.0D) {
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}
		}

		if (this.ignoreEntity != null) {
			if (flag) {
				this.ignoreTime = 2;
			} else if (this.ignoreTime-- <= 0) {
				this.ignoreEntity = null;
			}
		}

		if (entity != null) {
			raytraceresult = new RayTraceResult(entity);
		}

		if (raytraceresult != null) {
			if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK
					&& this.world.getBlockState(raytraceresult.getBlockPos()).getBlock() == Blocks.PORTAL) {
				this.setPortal(raytraceresult.getBlockPos());
			} else if (!net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
				this.onImpact(raytraceresult);
			}
		}

		float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));

		for (this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f)
				* (180D / Math.PI)); this.rotationPitch
						- this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
			;
		}

		while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
		this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
		float f1 = 0.99F;
		float f2 = this.getGravityVelocity();

		if (this.isInWater()) {
			for (int j = 0; j < 4; ++j) {
				float f3 = 0.25F;
				this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D,
						this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY,
						this.motionZ);
			}

			f1 = 0.8F;
		}

		this.motionX *= (double) f1;
		this.motionY *= (double) f1;
		this.motionZ *= (double) f1;

		if (!this.hasNoGravity()) {
			this.motionY -= (double) f2;
		}
		// this.setPosition(this.posX+motionX, this.posY+motionY, this.posZ+motionZ);
		this.move(MoverType.SELF, motionX, motionY, motionZ);
		if (++age >= 70) {
			this.setDead();
			if (!world.isRemote) {
				world.createExplosion(getThrower(), posX + width / 2, posY, posZ + width / 2, 2, true);
			}
			return;
		}

	}

	protected float getGravityVelocity() {
		return 0.03F;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		this.xTile = compound.getInteger("xTile");
		this.yTile = compound.getInteger("yTile");
		this.zTile = compound.getInteger("zTile");

		if (compound.hasKey("inTile", 8)) {
			this.inTile = Block.getBlockFromName(compound.getString("inTile"));
		} else {
			this.inTile = Block.getBlockById(compound.getByte("inTile") & 255);
		}

		this.throwableShake = compound.getByte("shake") & 255;
		this.inGround = compound.getByte("inGround") == 1;
		this.thrower = null;
		this.throwerName = compound.getString("ownerName");

		if (this.throwerName != null && this.throwerName.isEmpty()) {
			this.throwerName = null;
		}

		this.thrower = this.getThrower();
		if (compound.hasKey("age")) {
			age = compound.getInteger("age");
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		compound.setInteger("xTile", this.xTile);
		compound.setInteger("yTile", this.yTile);
		compound.setInteger("zTile", this.zTile);
		ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(this.inTile);
		compound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
		compound.setByte("shake", (byte) this.throwableShake);
		compound.setByte("inGround", (byte) (this.inGround ? 1 : 0));

		if ((this.throwerName == null || this.throwerName.isEmpty()) && this.thrower instanceof EntityPlayer) {
			this.throwerName = this.thrower.getName();
		}

		compound.setString("ownerName", this.throwerName == null ? "" : this.throwerName);

		compound.setInteger("age", age);
	}

	protected void doBlockCollisions() {
		AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
		BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos
				.retain(axisalignedbb.minX + 0.001D, axisalignedbb.minY + 0.001D, axisalignedbb.minZ + 0.001D);
		BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos1 = BlockPos.PooledMutableBlockPos
				.retain(axisalignedbb.maxX - 0.001D, axisalignedbb.maxY - 0.001D, axisalignedbb.maxZ - 0.001D);
		BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos2 = BlockPos.PooledMutableBlockPos.retain();

		if (this.world.isAreaLoaded(blockpos$pooledmutableblockpos, blockpos$pooledmutableblockpos1)) {
			for (int i = blockpos$pooledmutableblockpos.getX(); i <= blockpos$pooledmutableblockpos1.getX(); ++i) {
				for (int j = blockpos$pooledmutableblockpos.getY(); j <= blockpos$pooledmutableblockpos1.getY(); ++j) {
					for (int k = blockpos$pooledmutableblockpos.getZ(); k <= blockpos$pooledmutableblockpos1
							.getZ(); ++k) {
						blockpos$pooledmutableblockpos2.setPos(i, j, k);
						IBlockState iblockstate = this.world.getBlockState(blockpos$pooledmutableblockpos2);

						try {
							iblockstate.getBlock().onEntityCollidedWithBlock(this.world,
									blockpos$pooledmutableblockpos2, iblockstate, this);
							this.onInsideBlock(iblockstate);
						} catch (Throwable throwable) {
							CrashReport crashreport = CrashReport.makeCrashReport(throwable,
									"Colliding entity with block");
							CrashReportCategory crashreportcategory = crashreport
									.makeCategory("Block being collided with");
							CrashReportCategory.addBlockInfo(crashreportcategory, blockpos$pooledmutableblockpos2,
									iblockstate);
							throw new ReportedException(crashreport);
						}
					}
				}
			}
		}

		blockpos$pooledmutableblockpos.release();
		blockpos$pooledmutableblockpos1.release();
		blockpos$pooledmutableblockpos2.release();
	}

	protected void onImpact(RayTraceResult result) {
		if (world.isRemote)
			return;
		if (result.entityHit != null) {

		} else {
			IBlockState state = world.getBlockState(result.getBlockPos());
			SoundType soundType = state.getBlock().getSoundType(state, world, result.getBlockPos(), null);
			this.world.playSound(null, posX, posY, posZ, soundType.getHitSound(), SoundCategory.BLOCKS,
					0.9f + this.rand.nextFloat() * 0.2f, 0.9f + this.rand.nextFloat() * 0.2f);
			Vector3d motion = new Vector3d(motionX, motionY, motionZ);
			/*
			 * double speedSq = motion.x*motion.x + motion.y*motion.y+motion.z*motion.z;
			 * if(speedSq < 0.1) { motionX = 0; motionY = 0; motionZ = 0; return; }
			 */
			EnumFacing facing = result.sideHit;
			Vec3i dir = facing.getDirectionVec();
			Vector3d normal = new Vector3d(dir.getX(), dir.getY(), dir.getZ());
			normal.normalize();
			Vector3d bounce = new Vector3d(motionX, motionY, motionZ);
			double dot = motion.dot(normal);
			/*
			 * bounce.x -= 2 * dot * normal.x; bounce.y -= 2 * dot * normal.y; bounce.z -= 2
			 * * dot * normal.z;
			 */
			normal.scale(2 * dot);
			bounce.sub(normal);
			this.motionX = bounce.x * 0.66;
			this.motionY = bounce.y * 0.66;
			this.motionZ = bounce.z * 0.66;

			if (this.motionX < 0.1) {
				this.motionX = 0;
			}
			if (this.motionY < 0.1) {
				this.motionY = 0;
			}
			if (this.motionZ < 0.1) {
				this.motionZ = 0;
			}
			this.markVelocityChanged();
			doBlockCollisions();
		}
	}

	@Nullable
	public EntityLivingBase getThrower() {
		if (this.thrower == null && this.throwerName != null && !this.throwerName.isEmpty()) {
			this.thrower = this.world.getPlayerEntityByName(this.throwerName);

			if (this.thrower == null && this.world instanceof WorldServer) {
				try {
					Entity entity = ((WorldServer) this.world).getEntityFromUuid(UUID.fromString(this.throwerName));

					if (entity instanceof EntityLivingBase) {
						this.thrower = (EntityLivingBase) entity;
					}
				} catch (Throwable var2) {
					this.thrower = null;
				}
			}
		}

		return this.thrower;
	}

}
