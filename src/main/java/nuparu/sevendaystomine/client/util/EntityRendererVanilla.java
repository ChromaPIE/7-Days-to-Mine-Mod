package nuparu.sevendaystomine.client.util;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.gson.JsonSyntaxException;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MouseFilter;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//Vanilla EnittyRenderer methods to be saved from Optifine's vaccination
@SideOnly(Side.CLIENT)
public class EntityRendererVanilla implements IResourceManagerReloadListener {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final ResourceLocation RAIN_TEXTURES = new ResourceLocation("textures/environment/rain.png");
	private static final ResourceLocation SNOW_TEXTURES = new ResourceLocation("textures/environment/snow.png");
	public static boolean anaglyphEnable;
	/** Anaglyph field (0=R, 1=GB) */
	public static int anaglyphField;
	/** A reference to the Minecraft object. */
	private final Minecraft mc;
	private final IResourceManager resourceManager;
	private final Random random = new Random();
	private float farPlaneDistance;
	public final ItemRenderer itemRenderer;
	private final MapItemRenderer mapItemRenderer;
	/** Entity renderer update count */
	private int rendererUpdateCount;
	/** Pointed entity */
	private Entity pointedEntity;
	private final MouseFilter mouseFilterXAxis = new MouseFilter();
	private final MouseFilter mouseFilterYAxis = new MouseFilter();
	private final float thirdPersonDistance = 4.0F;
	/** Previous third person distance */
	private float thirdPersonDistancePrev = 4.0F;
	/** Smooth cam yaw */
	private float smoothCamYaw;
	/** Smooth cam pitch */
	private float smoothCamPitch;
	/** Smooth cam filter X */
	private float smoothCamFilterX;
	/** Smooth cam filter Y */
	private float smoothCamFilterY;
	/** Smooth cam partial ticks */
	private float smoothCamPartialTicks;
	/** FOV modifier hand */
	private float fovModifierHand;
	/** FOV modifier hand prev */
	private float fovModifierHandPrev;
	private float bossColorModifier;
	private float bossColorModifierPrev;
	/** Cloud fog mode */
	private boolean cloudFog;
	private boolean renderHand = true;
	private boolean drawBlockOutline = true;
	private long timeWorldIcon;
	/** Previous frame time in milliseconds */
	private long prevFrameTime = Minecraft.getSystemTime();
	/** End time of last render (ns) */
	private long renderEndNanoTime;
	/**
	 * The texture id of the blocklight/skylight texture used for lighting effects
	 */
	private final DynamicTexture lightmapTexture;
	/**
	 * Colors computed in updateLightmap() and loaded into the lightmap emptyTexture
	 */
	private final int[] lightmapColors;
	private final ResourceLocation locationLightMap;
	/**
	 * Is set, updateCameraAndRender() calls updateLightmap(); set by
	 * updateTorchFlicker()
	 */
	private boolean lightmapUpdateNeeded;
	/** Torch flicker X */
	private float torchFlickerX;
	private float torchFlickerDX;
	/** Rain sound counter */
	private int rainSoundCounter;
	private final float[] rainXCoords = new float[1024];
	private final float[] rainYCoords = new float[1024];
	/** Fog color buffer */
	private final FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
	private float fogColorRed;
	private float fogColorGreen;
	private float fogColorBlue;
	/** Fog color 2 */
	private float fogColor2;
	/** Fog color 1 */
	private float fogColor1;
	private int debugViewDirection;
	private boolean debugView;
	private double cameraZoom = 1.0D;
	private double cameraYaw;
	private double cameraPitch;
	private ItemStack itemActivationItem;
	private int itemActivationTicks;
	private float itemActivationOffX;
	private float itemActivationOffY;
	private ShaderGroup shaderGroup;
	private static final ResourceLocation[] SHADERS_TEXTURES = new ResourceLocation[] {
			new ResourceLocation("shaders/post/notch.json"), new ResourceLocation("shaders/post/fxaa.json"),
			new ResourceLocation("shaders/post/art.json"), new ResourceLocation("shaders/post/bumpy.json"),
			new ResourceLocation("shaders/post/blobs2.json"), new ResourceLocation("shaders/post/pencil.json"),
			new ResourceLocation("shaders/post/color_convolve.json"),
			new ResourceLocation("shaders/post/deconverge.json"), new ResourceLocation("shaders/post/flip.json"),
			new ResourceLocation("shaders/post/invert.json"), new ResourceLocation("shaders/post/ntsc.json"),
			new ResourceLocation("shaders/post/outline.json"), new ResourceLocation("shaders/post/phosphor.json"),
			new ResourceLocation("shaders/post/scan_pincushion.json"), new ResourceLocation("shaders/post/sobel.json"),
			new ResourceLocation("shaders/post/bits.json"), new ResourceLocation("shaders/post/desaturate.json"),
			new ResourceLocation("shaders/post/green.json"), new ResourceLocation("shaders/post/blur.json"),
			new ResourceLocation("shaders/post/wobble.json"), new ResourceLocation("shaders/post/blobs.json"),
			new ResourceLocation("shaders/post/antialias.json"), new ResourceLocation("shaders/post/creeper.json"),
			new ResourceLocation("shaders/post/spider.json") };
	public static final int SHADER_COUNT = SHADERS_TEXTURES.length;
	private int shaderIndex;
	private boolean useShader;
	private int frameCount;

	public EntityRendererVanilla(Minecraft mcIn, IResourceManager resourceManagerIn) {
		this.shaderIndex = SHADER_COUNT;
		this.mc = mcIn;
		this.resourceManager = resourceManagerIn;
		this.itemRenderer = mcIn.getItemRenderer();
		this.mapItemRenderer = new MapItemRenderer(mcIn.getTextureManager());
		this.lightmapTexture = new DynamicTexture(16, 16);
		this.locationLightMap = mcIn.getTextureManager().getDynamicTextureLocation("lightMap", this.lightmapTexture);
		this.lightmapColors = this.lightmapTexture.getTextureData();
		this.shaderGroup = null;

		for (int i = 0; i < 32; ++i) {
			for (int j = 0; j < 32; ++j) {
				float f = (float) (j - 16);
				float f1 = (float) (i - 16);
				float f2 = MathHelper.sqrt(f * f + f1 * f1);
				this.rainXCoords[i << 5 | j] = -f1 / f2;
				this.rainYCoords[i << 5 | j] = f / f2;
			}
		}
	}

	public boolean isShaderActive() {
		return OpenGlHelper.shadersSupported && this.shaderGroup != null;
	}

	public void stopUseShader() {
		if (this.shaderGroup != null) {
			this.shaderGroup.deleteShaderGroup();
		}

		this.shaderGroup = null;
		this.shaderIndex = SHADER_COUNT;
	}

	public void switchUseShader() {
		this.useShader = !this.useShader;
	}

	/**
	 * What shader to use when spectating this entity
	 */
	public void loadEntityShader(@Nullable Entity entityIn) {
		if (OpenGlHelper.shadersSupported) {
			if (this.shaderGroup != null) {
				this.shaderGroup.deleteShaderGroup();
			}

			this.shaderGroup = null;

			if (entityIn instanceof EntityCreeper) {
				this.loadShader(new ResourceLocation("shaders/post/creeper.json"));
			} else if (entityIn instanceof EntitySpider) {
				this.loadShader(new ResourceLocation("shaders/post/spider.json"));
			} else if (entityIn instanceof EntityEnderman) {
				this.loadShader(new ResourceLocation("shaders/post/invert.json"));
			}
		}
	}

	public void loadShader(ResourceLocation resourceLocationIn) {
		try {
			this.shaderGroup = new ShaderGroup(this.mc.getTextureManager(), this.resourceManager,
					this.mc.getFramebuffer(), resourceLocationIn);
			this.shaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
			this.useShader = true;
		} catch (IOException ioexception) {
			LOGGER.warn("Failed to load shader: {}", resourceLocationIn, ioexception);
			this.shaderIndex = SHADER_COUNT;
			this.useShader = false;
		} catch (JsonSyntaxException jsonsyntaxexception) {
			LOGGER.warn("Failed to load shader: {}", resourceLocationIn, jsonsyntaxexception);
			this.shaderIndex = SHADER_COUNT;
			this.useShader = false;
		}
	}
	
	public void loadShader(ResourceLocation resourceLocationIn, Framebuffer framebuffer, int width, int height) {
		try {
			this.shaderGroup = new ShaderGroup(this.mc.getTextureManager(), this.resourceManager,framebuffer, resourceLocationIn);
			this.shaderGroup.createBindFramebuffers(width, height);
			this.useShader = true;
		} catch (IOException ioexception) {
			LOGGER.warn("Failed to load shader: {}", resourceLocationIn, ioexception);
			this.shaderIndex = SHADER_COUNT;
			this.useShader = false;
		} catch (JsonSyntaxException jsonsyntaxexception) {
			LOGGER.warn("Failed to load shader: {}", resourceLocationIn, jsonsyntaxexception);
			this.shaderIndex = SHADER_COUNT;
			this.useShader = false;
		}
	}

	public void onResourceManagerReload(IResourceManager resourceManager) {
		if (this.shaderGroup != null) {
			this.shaderGroup.deleteShaderGroup();
		}

		this.shaderGroup = null;

		if (this.shaderIndex == SHADER_COUNT) {
			this.loadEntityShader(this.mc.getRenderViewEntity());
		} else {
			this.loadShader(SHADERS_TEXTURES[this.shaderIndex]);
		}
	}

	/**
	 * Updates the entity renderer
	 */
	public void updateRenderer() {
		if (OpenGlHelper.shadersSupported && ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
			ShaderLinkHelper.setNewStaticShaderLinkHelper();
		}

		this.updateFovModifierHand();
		this.updateTorchFlicker();
		this.fogColor2 = this.fogColor1;
		this.thirdPersonDistancePrev = 4.0F;

		if (this.mc.gameSettings.smoothCamera) {
			float f = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
			float f1 = f * f * f * 8.0F;
			this.smoothCamFilterX = this.mouseFilterXAxis.smooth(this.smoothCamYaw, 0.05F * f1);
			this.smoothCamFilterY = this.mouseFilterYAxis.smooth(this.smoothCamPitch, 0.05F * f1);
			this.smoothCamPartialTicks = 0.0F;
			this.smoothCamYaw = 0.0F;
			this.smoothCamPitch = 0.0F;
		} else {
			this.smoothCamFilterX = 0.0F;
			this.smoothCamFilterY = 0.0F;
			this.mouseFilterXAxis.reset();
			this.mouseFilterYAxis.reset();
		}

		if (this.mc.getRenderViewEntity() == null) {
			this.mc.setRenderViewEntity(this.mc.player);
		}

		float f3 = this.mc.world.getLightBrightness(new BlockPos(this.mc.getRenderViewEntity().getPositionEyes(1F))); // Forge:
																														// fix
																														// MC-51150
		float f4 = (float) this.mc.gameSettings.renderDistanceChunks / 32.0F;
		float f2 = f3 * (1.0F - f4) + f4;
		this.fogColor1 += (f2 - this.fogColor1) * 0.1F;
		++this.rendererUpdateCount;
		this.itemRenderer.updateEquippedItem();
		this.addRainParticles();
		this.bossColorModifierPrev = this.bossColorModifier;

		if (this.mc.ingameGUI.getBossOverlay().shouldDarkenSky()) {
			this.bossColorModifier += 0.05F;

			if (this.bossColorModifier > 1.0F) {
				this.bossColorModifier = 1.0F;
			}
		} else if (this.bossColorModifier > 0.0F) {
			this.bossColorModifier -= 0.0125F;
		}

		if (this.itemActivationTicks > 0) {
			--this.itemActivationTicks;

			if (this.itemActivationTicks == 0) {
				this.itemActivationItem = null;
			}
		}
	}

	public ShaderGroup getShaderGroup() {
		return this.shaderGroup;
	}

	public void updateShaderGroupSize(int width, int height) {
		if (OpenGlHelper.shadersSupported) {
			if (this.shaderGroup != null) {
				this.shaderGroup.createBindFramebuffers(width, height);
			}

			//this.mc.renderGlobal.createBindEntityOutlineFbs(width, height);
		}
	}

	/**
	 * Gets the block or object that is being moused over.
	 */
	public void getMouseOver(float partialTicks) {
		Entity entity = this.mc.getRenderViewEntity();

		if (entity != null) {
			if (this.mc.world != null) {
				this.mc.mcProfiler.startSection("pick");
				this.mc.pointedEntity = null;
				double d0 = (double) this.mc.playerController.getBlockReachDistance();
				this.mc.objectMouseOver = entity.rayTrace(d0, partialTicks);
				Vec3d vec3d = entity.getPositionEyes(partialTicks);
				boolean flag = false;
				int i = 3;
				double d1 = d0;

				if (this.mc.playerController.extendedReach()) {
					d1 = 6.0D;
					d0 = d1;
				} else {
					if (d0 > 3.0D) {
						flag = true;
					}
				}

				if (this.mc.objectMouseOver != null) {
					d1 = this.mc.objectMouseOver.hitVec.distanceTo(vec3d);
				}

				Vec3d vec3d1 = entity.getLook(1.0F);
				Vec3d vec3d2 = vec3d.addVector(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0);
				this.pointedEntity = null;
				Vec3d vec3d3 = null;
				float f = 1.0F;
				List<Entity> list = this.mc.world.getEntitiesInAABBexcluding(
						entity, entity.getEntityBoundingBox().expand(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0)
								.grow(1.0D, 1.0D, 1.0D),
						Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
							public boolean apply(@Nullable Entity p_apply_1_) {
								return p_apply_1_ != null && p_apply_1_.canBeCollidedWith();
							}
						}));
				double d2 = d1;

				for (int j = 0; j < list.size(); ++j) {
					Entity entity1 = list.get(j);
					AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox()
							.grow((double) entity1.getCollisionBorderSize());
					RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);

					if (axisalignedbb.contains(vec3d)) {
						if (d2 >= 0.0D) {
							this.pointedEntity = entity1;
							vec3d3 = raytraceresult == null ? vec3d : raytraceresult.hitVec;
							d2 = 0.0D;
						}
					} else if (raytraceresult != null) {
						double d3 = vec3d.distanceTo(raytraceresult.hitVec);

						if (d3 < d2 || d2 == 0.0D) {
							if (entity1.getLowestRidingEntity() == entity.getLowestRidingEntity()
									&& !entity1.canRiderInteract()) {
								if (d2 == 0.0D) {
									this.pointedEntity = entity1;
									vec3d3 = raytraceresult.hitVec;
								}
							} else {
								this.pointedEntity = entity1;
								vec3d3 = raytraceresult.hitVec;
								d2 = d3;
							}
						}
					}
				}

				if (this.pointedEntity != null && flag && vec3d.distanceTo(vec3d3) > 3.0D) {
					this.pointedEntity = null;
					this.mc.objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, vec3d3, (EnumFacing) null,
							new BlockPos(vec3d3));
				}

				if (this.pointedEntity != null && (d2 < d1 || this.mc.objectMouseOver == null)) {
					this.mc.objectMouseOver = new RayTraceResult(this.pointedEntity, vec3d3);

					if (this.pointedEntity instanceof EntityLivingBase
							|| this.pointedEntity instanceof EntityItemFrame) {
						this.mc.pointedEntity = this.pointedEntity;
					}
				}

				this.mc.mcProfiler.endSection();
			}
		}
	}

	/**
	 * Update FOV modifier hand
	 */
	private void updateFovModifierHand() {
		float f = 1.0F;

		if (this.mc.getRenderViewEntity() instanceof AbstractClientPlayer) {
			AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer) this.mc.getRenderViewEntity();
			f = abstractclientplayer.getFovModifier();
		}

		this.fovModifierHandPrev = this.fovModifierHand;
		this.fovModifierHand += (f - this.fovModifierHand) * 0.5F;

		if (this.fovModifierHand > 1.5F) {
			this.fovModifierHand = 1.5F;
		}

		if (this.fovModifierHand < 0.1F) {
			this.fovModifierHand = 0.1F;
		}
	}

	/**
	 * Changes the field of view of the player depending on if they are underwater
	 * or not
	 */
	private float getFOVModifier(float partialTicks, boolean useFOVSetting) {
		if (this.debugView) {
			return 90.0F;
		} else {
			Entity entity = this.mc.getRenderViewEntity();
			float f = 70.0F;

			if (useFOVSetting) {
				f = this.mc.gameSettings.fovSetting;
			}

			if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getHealth() <= 0.0F) {
				float f1 = (float) ((EntityLivingBase) entity).deathTime + partialTicks;
				f /= (1.0F - 500.0F / (f1 + 500.0F)) * 2.0F + 1.0F;
			}

			IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, entity,
					partialTicks);

			if (iblockstate.getMaterial() == Material.WATER) {
				f = f * 60.0F / 70.0F;
			}

			return f;
		}
	}

	/**
	 * sets up player's eye (or camera in third person mode)
	 */
	private void orientCamera(float partialTicks) {
		Entity entity = this.mc.getRenderViewEntity();
		float f = entity.getEyeHeight();
		double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks;
		double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialTicks + (double) f;
		double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks;

		if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isPlayerSleeping()) {
			f = (float) ((double) f + 1.0D);
			GlStateManager.translate(0.0F, 0.3F, 0.0F);

			if (!this.mc.gameSettings.debugCamEnable) {
				BlockPos blockpos = new BlockPos(entity);
				IBlockState iblockstate = this.mc.world.getBlockState(blockpos);
				net.minecraftforge.client.ForgeHooksClient.orientBedCamera(this.mc.world, blockpos, iblockstate,
						entity);

				GlStateManager.rotate(
						entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F,
						0.0F, -1.0F, 0.0F);
				GlStateManager.rotate(
						entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks,
						-1.0F, 0.0F, 0.0F);
			}
		} else if (this.mc.gameSettings.thirdPersonView > 0) {
			double d3 = (double) (this.thirdPersonDistancePrev + (4.0F - this.thirdPersonDistancePrev) * partialTicks);

			if (this.mc.gameSettings.debugCamEnable) {
				GlStateManager.translate(0.0F, 0.0F, (float) (-d3));
			} else {
				float f1 = entity.rotationYaw;
				float f2 = entity.rotationPitch;

				if (this.mc.gameSettings.thirdPersonView == 2) {
					f2 += 180.0F;
				}

				double d4 = (double) (-MathHelper.sin(f1 * 0.017453292F) * MathHelper.cos(f2 * 0.017453292F)) * d3;
				double d5 = (double) (MathHelper.cos(f1 * 0.017453292F) * MathHelper.cos(f2 * 0.017453292F)) * d3;
				double d6 = (double) (-MathHelper.sin(f2 * 0.017453292F)) * d3;

				for (int i = 0; i < 8; ++i) {
					float f3 = (float) ((i & 1) * 2 - 1);
					float f4 = (float) ((i >> 1 & 1) * 2 - 1);
					float f5 = (float) ((i >> 2 & 1) * 2 - 1);
					f3 = f3 * 0.1F;
					f4 = f4 * 0.1F;
					f5 = f5 * 0.1F;
					RayTraceResult raytraceresult = this.mc.world
							.rayTraceBlocks(new Vec3d(d0 + (double) f3, d1 + (double) f4, d2 + (double) f5), new Vec3d(
									d0 - d4 + (double) f3 + (double) f5, d1 - d6 + (double) f4, d2 - d5 + (double) f5));

					if (raytraceresult != null) {
						double d7 = raytraceresult.hitVec.distanceTo(new Vec3d(d0, d1, d2));

						if (d7 < d3) {
							d3 = d7;
						}
					}
				}

				if (this.mc.gameSettings.thirdPersonView == 2) {
					GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				}

				GlStateManager.rotate(entity.rotationPitch - f2, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(entity.rotationYaw - f1, 0.0F, 1.0F, 0.0F);
				GlStateManager.translate(0.0F, 0.0F, (float) (-d3));
				GlStateManager.rotate(f1 - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(f2 - entity.rotationPitch, 1.0F, 0.0F, 0.0F);
			}
		} else {
			GlStateManager.translate(0.0F, 0.0F, 0.05F);
		}

		if (!this.mc.gameSettings.debugCamEnable) {
			float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F;
			float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
			float roll = 0.0F;
			if (entity instanceof EntityAnimal) {
				EntityAnimal entityanimal = (EntityAnimal) entity;
				yaw = entityanimal.prevRotationYawHead
						+ (entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * partialTicks + 180.0F;
			}
			IBlockState state = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, entity, partialTicks);
			GlStateManager.rotate(roll, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
		}

		GlStateManager.translate(0.0F, -f, 0.0F);
		d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks;
		d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialTicks + (double) f;
		d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks;
		this.cloudFog = this.mc.renderGlobal.hasCloudFog(d0, d1, d2, partialTicks);
	}

	/**
	 * sets up projection, view effects, camera position/rotation
	 */
	private void setupCameraTransform(float partialTicks, int pass) {
		this.farPlaneDistance = (float) (this.mc.gameSettings.renderDistanceChunks * 16);
		GlStateManager.matrixMode(5889);
		GlStateManager.loadIdentity();
		float f = 0.07F;

		if (this.mc.gameSettings.anaglyph) {
			GlStateManager.translate((float) (-(pass * 2 - 1)) * 0.07F, 0.0F, 0.0F);
		}

		if (this.cameraZoom != 1.0D) {
			GlStateManager.translate((float) this.cameraYaw, (float) (-this.cameraPitch), 0.0F);
			GlStateManager.scale(this.cameraZoom, this.cameraZoom, 1.0D);
		}

		Project.gluPerspective(this.getFOVModifier(partialTicks, true),
				(float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F,
				this.farPlaneDistance * MathHelper.SQRT_2);
		GlStateManager.matrixMode(5888);
		GlStateManager.loadIdentity();

		if (this.mc.gameSettings.anaglyph) {
			GlStateManager.translate((float) (pass * 2 - 1) * 0.1F, 0.0F, 0.0F);
		}

		this.orientCamera(partialTicks);

		if (this.debugView) {
			switch (this.debugViewDirection) {
			case 0:
				GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
				break;
			case 1:
				GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				break;
			case 2:
				GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
				break;
			case 3:
				GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				break;
			case 4:
				GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
			}
		}
	}

	public void disableLightmap() {
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	public void enableLightmap() {
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.matrixMode(5890);
		GlStateManager.loadIdentity();
		float f = 0.00390625F;
		GlStateManager.scale(0.00390625F, 0.00390625F, 0.00390625F);
		GlStateManager.translate(8.0F, 8.0F, 8.0F);
		GlStateManager.matrixMode(5888);
		this.mc.getTextureManager().bindTexture(this.locationLightMap);
		GlStateManager.glTexParameteri(3553, 10241, 9729);
		GlStateManager.glTexParameteri(3553, 10240, 9729);
		GlStateManager.glTexParameteri(3553, 10242, 10496);
		GlStateManager.glTexParameteri(3553, 10243, 10496);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	/**
	 * Recompute a random value that is applied to block color in updateLightmap()
	 */
	private void updateTorchFlicker() {
		this.torchFlickerDX = (float) ((double) this.torchFlickerDX
				+ (Math.random() - Math.random()) * Math.random() * Math.random());
		this.torchFlickerDX = (float) ((double) this.torchFlickerDX * 0.9D);
		this.torchFlickerX += this.torchFlickerDX - this.torchFlickerX;
		this.lightmapUpdateNeeded = true;
	}

	private void updateLightmap(float partialTicks) {
		if (this.lightmapUpdateNeeded) {
			this.mc.mcProfiler.startSection("lightTex");
			World world = this.mc.world;

			if (world != null) {
				float f = world.getSunBrightness(1.0F);
				float f1 = f * 0.95F + 0.05F;

				for (int i = 0; i < 256; ++i) {
					float f2 = world.provider.getLightBrightnessTable()[i / 16] * f1;
					float f3 = world.provider.getLightBrightnessTable()[i % 16] * (this.torchFlickerX * 0.1F + 1.5F);

					if (world.getLastLightningBolt() > 0) {
						f2 = world.provider.getLightBrightnessTable()[i / 16];
					}

					float f4 = f2 * (f * 0.65F + 0.35F);
					float f5 = f2 * (f * 0.65F + 0.35F);
					float f6 = f3 * ((f3 * 0.6F + 0.4F) * 0.6F + 0.4F);
					float f7 = f3 * (f3 * f3 * 0.6F + 0.4F);
					float f8 = f4 + f3;
					float f9 = f5 + f6;
					float f10 = f2 + f7;
					f8 = f8 * 0.96F + 0.03F;
					f9 = f9 * 0.96F + 0.03F;
					f10 = f10 * 0.96F + 0.03F;

					if (this.bossColorModifier > 0.0F) {
						float f11 = this.bossColorModifierPrev
								+ (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
						f8 = f8 * (1.0F - f11) + f8 * 0.7F * f11;
						f9 = f9 * (1.0F - f11) + f9 * 0.6F * f11;
						f10 = f10 * (1.0F - f11) + f10 * 0.6F * f11;
					}

					if (world.provider.getDimensionType().getId() == 1) {
						f8 = 0.22F + f3 * 0.75F;
						f9 = 0.28F + f6 * 0.75F;
						f10 = 0.25F + f7 * 0.75F;
					}

					float[] colors = { f8, f9, f10 };
					world.provider.getLightmapColors(partialTicks, f, f2, f3, colors);
					f8 = colors[0];
					f9 = colors[1];
					f10 = colors[2];

					// Forge: fix MC-58177
					f8 = MathHelper.clamp(f8, 0f, 1f);
					f9 = MathHelper.clamp(f9, 0f, 1f);
					f10 = MathHelper.clamp(f10, 0f, 1f);

					if (this.mc.player.isPotionActive(MobEffects.NIGHT_VISION)) {
						float f15 = this.getNightVisionBrightness(this.mc.player, partialTicks);
						float f12 = 1.0F / f8;

						if (f12 > 1.0F / f9) {
							f12 = 1.0F / f9;
						}

						if (f12 > 1.0F / f10) {
							f12 = 1.0F / f10;
						}

						f8 = f8 * (1.0F - f15) + f8 * f12 * f15;
						f9 = f9 * (1.0F - f15) + f9 * f12 * f15;
						f10 = f10 * (1.0F - f15) + f10 * f12 * f15;
					}

					if (f8 > 1.0F) {
						f8 = 1.0F;
					}

					if (f9 > 1.0F) {
						f9 = 1.0F;
					}

					if (f10 > 1.0F) {
						f10 = 1.0F;
					}

					float f16 = this.mc.gameSettings.gammaSetting;
					float f17 = 1.0F - f8;
					float f13 = 1.0F - f9;
					float f14 = 1.0F - f10;
					f17 = 1.0F - f17 * f17 * f17 * f17;
					f13 = 1.0F - f13 * f13 * f13 * f13;
					f14 = 1.0F - f14 * f14 * f14 * f14;
					f8 = f8 * (1.0F - f16) + f17 * f16;
					f9 = f9 * (1.0F - f16) + f13 * f16;
					f10 = f10 * (1.0F - f16) + f14 * f16;
					f8 = f8 * 0.96F + 0.03F;
					f9 = f9 * 0.96F + 0.03F;
					f10 = f10 * 0.96F + 0.03F;

					if (f8 > 1.0F) {
						f8 = 1.0F;
					}

					if (f9 > 1.0F) {
						f9 = 1.0F;
					}

					if (f10 > 1.0F) {
						f10 = 1.0F;
					}

					if (f8 < 0.0F) {
						f8 = 0.0F;
					}

					if (f9 < 0.0F) {
						f9 = 0.0F;
					}

					if (f10 < 0.0F) {
						f10 = 0.0F;
					}

					int j = 255;
					int k = (int) (f8 * 255.0F);
					int l = (int) (f9 * 255.0F);
					int i1 = (int) (f10 * 255.0F);
					this.lightmapColors[i] = -16777216 | k << 16 | l << 8 | i1;
				}

				this.lightmapTexture.updateDynamicTexture();
				this.lightmapUpdateNeeded = false;
				this.mc.mcProfiler.endSection();
			}
		}
	}

	private float getNightVisionBrightness(EntityLivingBase entitylivingbaseIn, float partialTicks) {
		int i = entitylivingbaseIn.getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration();
		return i > 200 ? 1.0F : 0.7F + MathHelper.sin(((float) i - partialTicks) * (float) Math.PI * 0.2F) * 0.3F;
	}

	public void updateCameraAndRender(float partialTicks, long nanoTime, Framebuffer frameBuffer, int width, int height) {

		//loadShader(new ResourceLocation("shaders/post/green.json"));
		if (!this.mc.skipRenderWorld) {
			updateRenderer();
			anaglyphEnable = this.mc.gameSettings.anaglyph;
			final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
			int i1 = scaledresolution.getScaledWidth();
			int j1 = scaledresolution.getScaledHeight();
			final int k1 = Mouse.getX() * i1 / width;
			final int l1 = j1 - Mouse.getY() * j1 / height - 1;
			int i2 = this.mc.gameSettings.limitFramerate;

			if (this.mc.world != null) {
				this.mc.mcProfiler.startSection("level");
				int j = Math.min(Minecraft.getDebugFPS(), i2);
				j = Math.max(j, 60);
				long k = System.nanoTime() - nanoTime;
				long l = Math.max((long) (1000000000 / j / 4) - k, 0L);
				this.renderWorld(partialTicks, System.nanoTime() + l);

				if (OpenGlHelper.shadersSupported) {

					if (this.shaderGroup != null && this.useShader) {
						GlStateManager.matrixMode(5890);
						GlStateManager.pushMatrix();
						GlStateManager.loadIdentity();
					    this.shaderGroup.render(partialTicks);
						GlStateManager.popMatrix();
					}

				}

				this.mc.getFramebuffer().bindFramebuffer(true);
				this.renderEndNanoTime = System.nanoTime();

				this.mc.mcProfiler.endSection();
			}
		}
	}

	public void renderWorld(float partialTicks, long finishTimeNano) {
		this.updateLightmap(partialTicks);

		if (this.mc.getRenderViewEntity() == null) {
			this.mc.setRenderViewEntity(this.mc.player);
		}

		this.getMouseOver(partialTicks);
		GlStateManager.enableDepth();
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(516, 0.5F);
		this.mc.mcProfiler.startSection("center");

		if (this.mc.gameSettings.anaglyph) {
			anaglyphField = 0;
			GlStateManager.colorMask(false, true, true, false);
			this.renderWorldPass(0, partialTicks, finishTimeNano);
			anaglyphField = 1;
			GlStateManager.colorMask(true, false, false, false);
			this.renderWorldPass(1, partialTicks, finishTimeNano);
			GlStateManager.colorMask(true, true, true, false);
		} else {
			this.renderWorldPass(2, partialTicks, finishTimeNano);
		}

		this.mc.mcProfiler.endSection();
	}

	private void renderWorldPass(int pass, float partialTicks, long finishTimeNano) {
		RenderGlobal renderglobal = this.mc.renderGlobal;
		ParticleManager particlemanager = this.mc.effectRenderer;
		GlStateManager.enableCull();
		this.mc.mcProfiler.endStartSection("clear");
		GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
		this.updateFogColor(partialTicks);
		GlStateManager.clear(16640);
		this.mc.mcProfiler.endStartSection("camera");
		this.setupCameraTransform(partialTicks, pass);
		ActiveRenderInfo.updateRenderInfo(this.mc.getRenderViewEntity(), this.mc.gameSettings.thirdPersonView == 2); 
		this.mc.mcProfiler.endStartSection("frustum");
		ClippingHelperImpl.getInstance();
		this.mc.mcProfiler.endStartSection("culling");
		ICamera icamera = new Frustum();
		Entity entity = this.mc.getRenderViewEntity();
		double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks;
		double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks;
		double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks;
		icamera.setPosition(d0, d1, d2);

		if (this.mc.gameSettings.renderDistanceChunks >= 4) {
			this.setupFog(-1, partialTicks);
			this.mc.mcProfiler.endStartSection("sky");
			GlStateManager.matrixMode(5889);
			GlStateManager.loadIdentity();
			Project.gluPerspective(this.getFOVModifier(partialTicks, true),
					(float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
			GlStateManager.matrixMode(5888);
			renderglobal.renderSky(partialTicks, pass);
			GlStateManager.matrixMode(5889);
			GlStateManager.loadIdentity();
			Project.gluPerspective(this.getFOVModifier(partialTicks, true),
					(float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F,
					this.farPlaneDistance * MathHelper.SQRT_2);
			GlStateManager.matrixMode(5888);
		}

		this.setupFog(0, partialTicks);
		GlStateManager.shadeModel(7425);

		if (entity.posY + (double) entity.getEyeHeight() < 128.0D) {
			this.renderCloudsCheck(renderglobal, partialTicks, pass, d0, d1, d2);
		}
		this.mc.mcProfiler.endStartSection("prepareterrain");
		this.setupFog(0, partialTicks);
		this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		RenderHelper.disableStandardItemLighting();
		this.mc.mcProfiler.endStartSection("terrain_setup");
		renderglobal.setupTerrain(entity, (double) partialTicks, icamera, this.frameCount++,
				this.mc.player.isSpectator());

		if (pass == 0 || pass == 2) {
			this.mc.mcProfiler.endStartSection("updatechunks");
			this.mc.renderGlobal.updateChunks(finishTimeNano);
		}

		this.mc.mcProfiler.endStartSection("terrain");
		GlStateManager.matrixMode(5888);
		GlStateManager.pushMatrix();
		GlStateManager.disableAlpha();
		renderglobal.renderBlockLayer(BlockRenderLayer.SOLID, (double) partialTicks, pass, entity);
		GlStateManager.enableAlpha();
		this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false,
				this.mc.gameSettings.mipmapLevels > 0); // FORGE: fix flickering leaves when mods mess up the blurMipmap
														// settings
		renderglobal.renderBlockLayer(BlockRenderLayer.CUTOUT_MIPPED, (double) partialTicks, pass, entity);
		this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
		this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
		renderglobal.renderBlockLayer(BlockRenderLayer.CUTOUT, (double) partialTicks, pass, entity);
		this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
		GlStateManager.shadeModel(7424);
		GlStateManager.alphaFunc(516, 0.1F);

		if (!this.debugView) {
			GlStateManager.matrixMode(5888);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			RenderHelper.enableStandardItemLighting();
			this.mc.mcProfiler.endStartSection("entities");
			net.minecraftforge.client.ForgeHooksClient.setRenderPass(0);
			renderglobal.renderEntities(entity, icamera, partialTicks);
			net.minecraftforge.client.ForgeHooksClient.setRenderPass(0);
			RenderHelper.disableStandardItemLighting();
			this.disableLightmap();
		}

		GlStateManager.matrixMode(5888);
		GlStateManager.popMatrix();

		if (this.mc.debugRenderer.shouldRender()) {
			this.mc.debugRenderer.renderDebug(partialTicks, finishTimeNano);
		}

		this.mc.mcProfiler.endStartSection("destroyProgress");
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE,
				GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
		renderglobal.drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getBuffer(), entity,
				partialTicks);
		this.mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
		GlStateManager.disableBlend();

		if (!this.debugView) {
			this.enableLightmap();
			this.mc.mcProfiler.endStartSection("litParticles");
			particlemanager.renderLitParticles(entity, partialTicks);
			RenderHelper.disableStandardItemLighting();
			this.setupFog(0, partialTicks);
			this.mc.mcProfiler.endStartSection("particles");
			particlemanager.renderParticles(entity, partialTicks);
			this.disableLightmap();
		}

		GlStateManager.depthMask(false);
		GlStateManager.enableCull();
		this.mc.mcProfiler.endStartSection("weather");
		this.renderRainSnow(partialTicks);
		GlStateManager.depthMask(true);
		renderglobal.renderWorldBorder(entity, partialTicks);
		GlStateManager.disableBlend();
		GlStateManager.enableCull();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
		GlStateManager.alphaFunc(516, 0.1F);
		this.setupFog(0, partialTicks);
		GlStateManager.enableBlend();
		GlStateManager.depthMask(false);
		this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.shadeModel(7425);
		this.mc.mcProfiler.endStartSection("translucent");
		renderglobal.renderBlockLayer(BlockRenderLayer.TRANSLUCENT, (double) partialTicks, pass, entity);
		if (!this.debugView) // Only render if render pass 0 happens as well.
		{
			RenderHelper.enableStandardItemLighting();
			this.mc.mcProfiler.endStartSection("entities");
			net.minecraftforge.client.ForgeHooksClient.setRenderPass(1);
			renderglobal.renderEntities(entity, icamera, partialTicks);
			// restore blending function changed by RenderGlobal.preRenderDamagedBlocks
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
					GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
					GlStateManager.DestFactor.ZERO);
			net.minecraftforge.client.ForgeHooksClient.setRenderPass(-1);
			RenderHelper.disableStandardItemLighting();
		}
		GlStateManager.shadeModel(7424);
		GlStateManager.depthMask(true);
		GlStateManager.enableCull();
		GlStateManager.disableBlend();
		GlStateManager.disableFog();

		if (entity.posY + (double) entity.getEyeHeight() >= 128.0D) {
			this.mc.mcProfiler.endStartSection("aboveClouds");
			this.renderCloudsCheck(renderglobal, partialTicks, pass, d0, d1, d2);
		}

		this.mc.mcProfiler.endStartSection("forge_render_last");
		net.minecraftforge.client.ForgeHooksClient.dispatchRenderLast(renderglobal, partialTicks);
	}

	private void renderCloudsCheck(RenderGlobal renderGlobalIn, float partialTicks, int pass, double x, double y,
			double z) {
		if (this.mc.gameSettings.shouldRenderClouds() != 0) {
			this.mc.mcProfiler.endStartSection("clouds");
			GlStateManager.matrixMode(5889);
			GlStateManager.loadIdentity();
			Project.gluPerspective(this.getFOVModifier(partialTicks, true),
					(float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, this.farPlaneDistance * 4.0F);
			GlStateManager.matrixMode(5888);
			GlStateManager.pushMatrix();
			this.setupFog(0, partialTicks);
			renderGlobalIn.renderClouds(partialTicks, pass, x, y, z);
			GlStateManager.disableFog();
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5889);
			GlStateManager.loadIdentity();
			Project.gluPerspective(this.getFOVModifier(partialTicks, true),
					(float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F,
					this.farPlaneDistance * MathHelper.SQRT_2);
			GlStateManager.matrixMode(5888);
		}
	}

	private void addRainParticles() {
		float f = this.mc.world.getRainStrength(1.0F);

		if (!this.mc.gameSettings.fancyGraphics) {
			f /= 2.0F;
		}

		if (f != 0.0F) {
			this.random.setSeed((long) this.rendererUpdateCount * 312987231L);
			Entity entity = this.mc.getRenderViewEntity();
			World world = this.mc.world;
			BlockPos blockpos = new BlockPos(entity);
			int i = 10;
			double d0 = 0.0D;
			double d1 = 0.0D;
			double d2 = 0.0D;
			int j = 0;
			int k = (int) (100.0F * f * f);

			if (this.mc.gameSettings.particleSetting == 1) {
				k >>= 1;
			} else if (this.mc.gameSettings.particleSetting == 2) {
				k = 0;
			}

			for (int l = 0; l < k; ++l) {
				BlockPos blockpos1 = world
						.getPrecipitationHeight(blockpos.add(this.random.nextInt(10) - this.random.nextInt(10), 0,
								this.random.nextInt(10) - this.random.nextInt(10)));
				Biome biome = world.getBiome(blockpos1);
				BlockPos blockpos2 = blockpos1.down();
				IBlockState iblockstate = world.getBlockState(blockpos2);

				if (blockpos1.getY() <= blockpos.getY() + 10 && blockpos1.getY() >= blockpos.getY() - 10
						&& biome.canRain() && biome.getTemperature(blockpos1) >= 0.15F) {
					double d3 = this.random.nextDouble();
					double d4 = this.random.nextDouble();
					AxisAlignedBB axisalignedbb = iblockstate.getBoundingBox(world, blockpos2);

					if (iblockstate.getMaterial() != Material.LAVA && iblockstate.getBlock() != Blocks.MAGMA) {
						if (iblockstate.getMaterial() != Material.AIR) {
							++j;

							if (this.random.nextInt(j) == 0) {
								d0 = (double) blockpos2.getX() + d3;
								d1 = (double) ((float) blockpos2.getY() + 0.1F) + axisalignedbb.maxY - 1.0D;
								d2 = (double) blockpos2.getZ() + d4;
							}

							this.mc.world.spawnParticle(EnumParticleTypes.WATER_DROP, (double) blockpos2.getX() + d3,
									(double) ((float) blockpos2.getY() + 0.1F) + axisalignedbb.maxY,
									(double) blockpos2.getZ() + d4, 0.0D, 0.0D, 0.0D, new int[0]);
						}
					} else {
						this.mc.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double) blockpos1.getX() + d3,
								(double) ((float) blockpos1.getY() + 0.1F) - axisalignedbb.minY,
								(double) blockpos1.getZ() + d4, 0.0D, 0.0D, 0.0D, new int[0]);
					}
				}
			}

			if (j > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
				this.rainSoundCounter = 0;

				if (d1 > (double) (blockpos.getY() + 1)
						&& world.getPrecipitationHeight(blockpos).getY() > MathHelper.floor((float) blockpos.getY())) {
					this.mc.world.playSound(d0, d1, d2, SoundEvents.WEATHER_RAIN_ABOVE, SoundCategory.WEATHER, 0.1F,
							0.5F, false);
				} else {
					this.mc.world.playSound(d0, d1, d2, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.2F, 1.0F,
							false);
				}
			}
		}
	}

	/**
	 * Render rain and snow
	 */
	protected void renderRainSnow(float partialTicks) {
		net.minecraftforge.client.IRenderHandler renderer = this.mc.world.provider.getWeatherRenderer();
		if (renderer != null) {
			renderer.render(partialTicks, this.mc.world, mc);
			return;
		}

		float f = this.mc.world.getRainStrength(partialTicks);

		if (f > 0.0F) {
			this.enableLightmap();
			Entity entity = this.mc.getRenderViewEntity();
			World world = this.mc.world;
			int i = MathHelper.floor(entity.posX);
			int j = MathHelper.floor(entity.posY);
			int k = MathHelper.floor(entity.posZ);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			GlStateManager.disableCull();
			GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
					GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
					GlStateManager.DestFactor.ZERO);
			GlStateManager.alphaFunc(516, 0.1F);
			double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks;
			double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks;
			double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks;
			int l = MathHelper.floor(d1);
			int i1 = 5;

			if (this.mc.gameSettings.fancyGraphics) {
				i1 = 10;
			}

			int j1 = -1;
			float f1 = (float) this.rendererUpdateCount + partialTicks;
			bufferbuilder.setTranslation(-d0, -d1, -d2);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

			for (int k1 = k - i1; k1 <= k + i1; ++k1) {
				for (int l1 = i - i1; l1 <= i + i1; ++l1) {
					int i2 = (k1 - k + 16) * 32 + l1 - i + 16;
					double d3 = (double) this.rainXCoords[i2] * 0.5D;
					double d4 = (double) this.rainYCoords[i2] * 0.5D;
					blockpos$mutableblockpos.setPos(l1, 0, k1);
					Biome biome = world.getBiome(blockpos$mutableblockpos);

					if (biome.canRain() || biome.getEnableSnow()) {
						int j2 = world.getPrecipitationHeight(blockpos$mutableblockpos).getY();
						int k2 = j - i1;
						int l2 = j + i1;

						if (k2 < j2) {
							k2 = j2;
						}

						if (l2 < j2) {
							l2 = j2;
						}

						int i3 = j2;

						if (j2 < l) {
							i3 = l;
						}

						if (k2 != l2) {
							this.random
									.setSeed((long) (l1 * l1 * 3121 + l1 * 45238971 ^ k1 * k1 * 418711 + k1 * 13761));
							blockpos$mutableblockpos.setPos(l1, k2, k1);
							float f2 = biome.getTemperature(blockpos$mutableblockpos);

							if (world.getBiomeProvider().getTemperatureAtHeight(f2, j2) >= 0.15F) {
								if (j1 != 0) {
									if (j1 >= 0) {
										tessellator.draw();
									}

									j1 = 0;
									this.mc.getTextureManager().bindTexture(RAIN_TEXTURES);
									bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
								}

								double d5 = -((double) (this.rendererUpdateCount + l1 * l1 * 3121 + l1 * 45238971
										+ k1 * k1 * 418711 + k1 * 13761 & 31) + (double) partialTicks) / 32.0D
										* (3.0D + this.random.nextDouble());
								double d6 = (double) ((float) l1 + 0.5F) - entity.posX;
								double d7 = (double) ((float) k1 + 0.5F) - entity.posZ;
								float f3 = MathHelper.sqrt(d6 * d6 + d7 * d7) / (float) i1;
								float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * f;
								blockpos$mutableblockpos.setPos(l1, i3, k1);
								int j3 = world.getCombinedLight(blockpos$mutableblockpos, 0);
								int k3 = j3 >> 16 & 65535;
								int l3 = j3 & 65535;
								bufferbuilder.pos((double) l1 - d3 + 0.5D, (double) l2, (double) k1 - d4 + 0.5D)
										.tex(0.0D, (double) k2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4)
										.lightmap(k3, l3).endVertex();
								bufferbuilder.pos((double) l1 + d3 + 0.5D, (double) l2, (double) k1 + d4 + 0.5D)
										.tex(1.0D, (double) k2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4)
										.lightmap(k3, l3).endVertex();
								bufferbuilder.pos((double) l1 + d3 + 0.5D, (double) k2, (double) k1 + d4 + 0.5D)
										.tex(1.0D, (double) l2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4)
										.lightmap(k3, l3).endVertex();
								bufferbuilder.pos((double) l1 - d3 + 0.5D, (double) k2, (double) k1 - d4 + 0.5D)
										.tex(0.0D, (double) l2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4)
										.lightmap(k3, l3).endVertex();
							} else {
								if (j1 != 1) {
									if (j1 >= 0) {
										tessellator.draw();
									}

									j1 = 1;
									this.mc.getTextureManager().bindTexture(SNOW_TEXTURES);
									bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
								}

								double d8 = (double) (-((float) (this.rendererUpdateCount & 511) + partialTicks)
										/ 512.0F);
								double d9 = this.random.nextDouble()
										+ (double) f1 * 0.01D * (double) ((float) this.random.nextGaussian());
								double d10 = this.random.nextDouble()
										+ (double) (f1 * (float) this.random.nextGaussian()) * 0.001D;
								double d11 = (double) ((float) l1 + 0.5F) - entity.posX;
								double d12 = (double) ((float) k1 + 0.5F) - entity.posZ;
								float f6 = MathHelper.sqrt(d11 * d11 + d12 * d12) / (float) i1;
								float f5 = ((1.0F - f6 * f6) * 0.3F + 0.5F) * f;
								blockpos$mutableblockpos.setPos(l1, i3, k1);
								int i4 = (world.getCombinedLight(blockpos$mutableblockpos, 0) * 3 + 15728880) / 4;
								int j4 = i4 >> 16 & 65535;
								int k4 = i4 & 65535;
								bufferbuilder.pos((double) l1 - d3 + 0.5D, (double) l2, (double) k1 - d4 + 0.5D)
										.tex(0.0D + d9, (double) k2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5)
										.lightmap(j4, k4).endVertex();
								bufferbuilder.pos((double) l1 + d3 + 0.5D, (double) l2, (double) k1 + d4 + 0.5D)
										.tex(1.0D + d9, (double) k2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5)
										.lightmap(j4, k4).endVertex();
								bufferbuilder.pos((double) l1 + d3 + 0.5D, (double) k2, (double) k1 + d4 + 0.5D)
										.tex(1.0D + d9, (double) l2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5)
										.lightmap(j4, k4).endVertex();
								bufferbuilder.pos((double) l1 - d3 + 0.5D, (double) k2, (double) k1 - d4 + 0.5D)
										.tex(0.0D + d9, (double) l2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5)
										.lightmap(j4, k4).endVertex();
							}
						}
					}
				}
			}

			if (j1 >= 0) {
				tessellator.draw();
			}

			bufferbuilder.setTranslation(0.0D, 0.0D, 0.0D);
			GlStateManager.enableCull();
			GlStateManager.disableBlend();
			GlStateManager.alphaFunc(516, 0.1F);
			this.disableLightmap();
		}
	}

	/**
	 * calculates fog and calls glClearColor
	 */
	private void updateFogColor(float partialTicks) {
		World world = this.mc.world;
		Entity entity = this.mc.getRenderViewEntity();
		float f = 0.25F + 0.75F * (float) this.mc.gameSettings.renderDistanceChunks / 32.0F;
		f = 1.0F - (float) Math.pow((double) f, 0.25D);
		Vec3d vec3d = world.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
		float f1 = (float) vec3d.x;
		float f2 = (float) vec3d.y;
		float f3 = (float) vec3d.z;
		Vec3d vec3d1 = world.getFogColor(partialTicks);
		this.fogColorRed = (float) vec3d1.x;
		this.fogColorGreen = (float) vec3d1.y;
		this.fogColorBlue = (float) vec3d1.z;
		

		if (this.mc.gameSettings.renderDistanceChunks >= 4) {
			double d0 = MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) > 0.0F ? -1.0D : 1.0D;
			Vec3d vec3d2 = new Vec3d(d0, 0.0D, 0.0D);
			float f5 = (float) entity.getLook(partialTicks).dotProduct(vec3d2);

			if (f5 < 0.0F) {
				f5 = 0.0F;
			}

			if (f5 > 0.0F) {
				float[] afloat = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks),
						partialTicks);

				if (afloat != null) {
					f5 = f5 * afloat[3];
					this.fogColorRed = this.fogColorRed * (1.0F - f5) + afloat[0] * f5;
					this.fogColorGreen = this.fogColorGreen * (1.0F - f5) + afloat[1] * f5;
					this.fogColorBlue = this.fogColorBlue * (1.0F - f5) + afloat[2] * f5;
				}
			}
		}

		this.fogColorRed += (f1 - this.fogColorRed) * f;
		this.fogColorGreen += (f2 - this.fogColorGreen) * f;
		this.fogColorBlue += (f3 - this.fogColorBlue) * f;
		float f8 = world.getRainStrength(partialTicks);

		if (f8 > 0.0F) {
			float f4 = 1.0F - f8 * 0.5F;
			float f10 = 1.0F - f8 * 0.4F;
			this.fogColorRed *= f4;
			this.fogColorGreen *= f4;
			this.fogColorBlue *= f10;
		}

		float f9 = world.getThunderStrength(partialTicks);

		if (f9 > 0.0F) {
			float f11 = 1.0F - f9 * 0.5F;
			this.fogColorRed *= f11;
			this.fogColorGreen *= f11;
			this.fogColorBlue *= f11;
		}

		IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, entity, partialTicks);

		if (this.cloudFog) {
			Vec3d vec3d3 = world.getCloudColour(partialTicks);
			this.fogColorRed = (float) vec3d3.x;
			this.fogColorGreen = (float) vec3d3.y;
			this.fogColorBlue = (float) vec3d3.z;
		} else {
			// Forge Moved to Block.
			Vec3d viewport = ActiveRenderInfo.projectViewFromEntity(entity, partialTicks);
			BlockPos viewportPos = new BlockPos(viewport);
			IBlockState viewportState = this.mc.world.getBlockState(viewportPos);
			Vec3d inMaterialColor = viewportState.getBlock().getFogColor(this.mc.world, viewportPos, viewportState,
					entity, new Vec3d(fogColorRed, fogColorGreen, fogColorBlue), partialTicks);
			this.fogColorRed = (float) inMaterialColor.x;
			this.fogColorGreen = (float) inMaterialColor.y;
			this.fogColorBlue = (float) inMaterialColor.z;
		}

		float f13 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * 0;
		this.fogColorRed *= f13;
		this.fogColorGreen *= f13;
		this.fogColorBlue *= f13;
		double d1 = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks)
				* world.provider.getVoidFogYFactor();

		if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isPotionActive(MobEffects.BLINDNESS)) {
			int i = ((EntityLivingBase) entity).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();

			if (i < 20) {
				d1 *= (double) (1.0F - (float) i / 20.0F);
			} else {
				d1 = 0.0D;
			}
		}

		if (d1 < 1.0D) {
			if (d1 < 0.0D) {
				d1 = 0.0D;
			}

			d1 = d1 * d1;
			this.fogColorRed = (float) ((double) this.fogColorRed * d1);
			this.fogColorGreen = (float) ((double) this.fogColorGreen * d1);
			this.fogColorBlue = (float) ((double) this.fogColorBlue * d1);
		}

		if (this.bossColorModifier > 0.0F) {
			float f14 = this.bossColorModifierPrev
					+ (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
			this.fogColorRed = this.fogColorRed * (1.0F - f14) + this.fogColorRed * 0.7F * f14;
			this.fogColorGreen = this.fogColorGreen * (1.0F - f14) + this.fogColorGreen * 0.6F * f14;
			this.fogColorBlue = this.fogColorBlue * (1.0F - f14) + this.fogColorBlue * 0.6F * f14;
		}

		if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isPotionActive(MobEffects.NIGHT_VISION)) {
			float f15 = this.getNightVisionBrightness((EntityLivingBase) entity, partialTicks);
			float f6 = 1.0F / this.fogColorRed;

			if (f6 > 1.0F / this.fogColorGreen) {
				f6 = 1.0F / this.fogColorGreen;
			}

			if (f6 > 1.0F / this.fogColorBlue) {
				f6 = 1.0F / this.fogColorBlue;
			}

			// Forge: fix MC-4647 and MC-10480
			if (Float.isInfinite(f6))
				f6 = Math.nextAfter(f6, 0.0);

			this.fogColorRed = this.fogColorRed * (1.0F - f15) + this.fogColorRed * f6 * f15;
			this.fogColorGreen = this.fogColorGreen * (1.0F - f15) + this.fogColorGreen * f6 * f15;
			this.fogColorBlue = this.fogColorBlue * (1.0F - f15) + this.fogColorBlue * f6 * f15;
		}

		if (this.mc.gameSettings.anaglyph) {
			float f16 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
			float f17 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
			float f7 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
			this.fogColorRed = f16;
			this.fogColorGreen = f17;
			this.fogColorBlue = f7;
		}

		net.minecraftforge.client.event.EntityViewRenderEvent.FogColors event = new net.minecraftforge.client.event.EntityViewRenderEvent.FogColors(
				mc.entityRenderer, entity, iblockstate, partialTicks, this.fogColorRed, this.fogColorGreen,
				this.fogColorBlue);
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);

		this.fogColorRed = event.getRed();
		this.fogColorGreen = event.getGreen();
		this.fogColorBlue = event.getBlue();

		GlStateManager.clearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
	}

	/**
	 * Sets up the fog to be rendered. If the arg passed in is -1 the fog starts at
	 * 0 and goes to 80% of far plane distance and is used for sky rendering.
	 */
	private void setupFog(int startCoords, float partialTicks) {
		Entity entity = this.mc.getRenderViewEntity();
		this.setupFogColor(false);
		GlStateManager.glNormal3f(0.0F, -1.0F, 0.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.world, entity, partialTicks);
		float hook = net.minecraftforge.client.ForgeHooksClient.getFogDensity(mc.entityRenderer, entity, iblockstate,
				partialTicks, 0.1F);
		if (hook >= 0)
			GlStateManager.setFogDensity(hook);
		else if (entity instanceof EntityLivingBase
				&& ((EntityLivingBase) entity).isPotionActive(MobEffects.BLINDNESS)) {
			float f1 = 5.0F;
			int i = ((EntityLivingBase) entity).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();

			if (i < 20) {
				f1 = 5.0F + (this.farPlaneDistance - 5.0F) * (1.0F - (float) i / 20.0F);
			}

			GlStateManager.setFog(GlStateManager.FogMode.LINEAR);

			if (startCoords == -1) {
				GlStateManager.setFogStart(0.0F);
				GlStateManager.setFogEnd(f1 * 0.8F);
			} else {
				GlStateManager.setFogStart(f1 * 0.25F);
				GlStateManager.setFogEnd(f1);
			}

			if (GLContext.getCapabilities().GL_NV_fog_distance) {
				GlStateManager.glFogi(34138, 34139);
			}
		} else if (this.cloudFog) {
			GlStateManager.setFog(GlStateManager.FogMode.EXP);
			GlStateManager.setFogDensity(0.1F);
		} else if (iblockstate.getMaterial() == Material.WATER) {
			GlStateManager.setFog(GlStateManager.FogMode.EXP);

			if (entity instanceof EntityLivingBase) {
				if (((EntityLivingBase) entity).isPotionActive(MobEffects.WATER_BREATHING)) {
					GlStateManager.setFogDensity(0.01F);
				} else {
					GlStateManager.setFogDensity(
							0.1F - (float) EnchantmentHelper.getRespirationModifier((EntityLivingBase) entity) * 0.03F);
				}
			} else {
				GlStateManager.setFogDensity(0.1F);
			}
		} else if (iblockstate.getMaterial() == Material.LAVA) {
			GlStateManager.setFog(GlStateManager.FogMode.EXP);
			GlStateManager.setFogDensity(2.0F);
		} else {
			float f = this.farPlaneDistance;
			GlStateManager.setFog(GlStateManager.FogMode.LINEAR);

			if (startCoords == -1) {
				GlStateManager.setFogStart(0.0F);
				GlStateManager.setFogEnd(f);
			} else {
				GlStateManager.setFogStart(f * 0.75F);
				GlStateManager.setFogEnd(f);
			}

			if (GLContext.getCapabilities().GL_NV_fog_distance) {
				GlStateManager.glFogi(34138, 34139);
			}

			if (this.mc.world.provider.doesXZShowFog((int) entity.posX, (int) entity.posZ)
					|| this.mc.ingameGUI.getBossOverlay().shouldCreateFog()) {
				GlStateManager.setFogStart(f * 0.05F);
				GlStateManager.setFogEnd(Math.min(f, 192.0F) * 0.5F);
			}
			net.minecraftforge.client.ForgeHooksClient.onFogRender(mc.entityRenderer, entity, iblockstate, partialTicks,
					startCoords, f);
		}

		GlStateManager.enableColorMaterial();
		GlStateManager.enableFog();
		GlStateManager.colorMaterial(1028, 4608);
	}

	public void setupFogColor(boolean black) {
		if (black) {
			GlStateManager.glFog(2918, this.setFogColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
		} else {
			GlStateManager.glFog(2918,
					this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
		}
	}

	/**
	 * Update and return fogColorBuffer with the RGBA values passed as arguments
	 */
	private FloatBuffer setFogColorBuffer(float red, float green, float blue, float alpha) {
		this.fogColorBuffer.clear();
		this.fogColorBuffer.put(red).put(green).put(blue).put(alpha);
		this.fogColorBuffer.flip();
		return this.fogColorBuffer;
	}

	public void resetData() {
		this.itemActivationItem = null;
		this.mapItemRenderer.clearLoadedMaps();
	}

	public MapItemRenderer getMapItemRenderer() {
		return this.mapItemRenderer;
	}

}