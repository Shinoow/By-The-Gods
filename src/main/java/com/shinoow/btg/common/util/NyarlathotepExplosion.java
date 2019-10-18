package com.shinoow.btg.common.util;

import java.util.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.shinoow.btg.common.entity.EntityNyarlathotepTNT;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class NyarlathotepExplosion extends Explosion {

	/** whether or not the explosion sets fire to blocks around it */
	private final boolean isFlaming;
	/** whether or not this explosion spawns smoke particles */
	private final boolean isSmoking;
	private final Random explosionRNG;
	private final World worldObj;
	private final double explosionX;
	private final double explosionY;
	private final double explosionZ;
	private final Entity exploder;
	private final float explosionSize;
	private final List<BlockPos> affectedBlockPositions;
	private final Map<EntityPlayer, Vec3d> playerKnockbackMap;
	private final Vec3d position;

	public NyarlathotepExplosion(World worldIn, Entity entityIn, double x,
			double y, double z, float size, boolean flaming, boolean smoking) {
		super(worldIn, entityIn, x, y, z, size, flaming, smoking);
		explosionRNG = new Random();
		affectedBlockPositions = Lists.<BlockPos>newArrayList();
		playerKnockbackMap = Maps.<EntityPlayer, Vec3d>newHashMap();
		worldObj = worldIn;
		exploder = entityIn;
		explosionSize = size;
		explosionX = x;
		explosionY = y;
		explosionZ = z;
		isFlaming = flaming;
		isSmoking = smoking;
		position = new Vec3d(explosionX, explosionY, explosionZ);
	}

	@Override
	public void doExplosionA()
	{
		Set<BlockPos> set = Sets.<BlockPos>newHashSet();
		for (int j = 0; j < 16; ++j)
			for (int k = 0; k < 16; ++k)
				for (int l = 0; l < 16; ++l)
					if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15)
					{
						double d0 = j / 15.0F * 2.0F - 1.0F;
						double d1 = k / 15.0F * 2.0F - 1.0F;
						double d2 = l / 15.0F * 2.0F - 1.0F;
						double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
						d0 = d0 / d3;
						d1 = d1 / d3;
						d2 = d2 / d3;
						float f = explosionSize * (0.7F + worldObj.rand.nextFloat() * 0.6F);
						double d4 = explosionX;
						double d6 = explosionY;
						double d8 = explosionZ;

						for (; f > 0.0F; f -= 0.22500001F)
						{
							BlockPos blockpos = new BlockPos(d4, d6, d8);
							IBlockState iblockstate = worldObj.getBlockState(blockpos);

							if (iblockstate.getMaterial() != Material.AIR)
							{
								float f2 = exploder != null ? exploder.getExplosionResistance(this, worldObj, blockpos, iblockstate) : iblockstate.getBlock().getExplosionResistance(worldObj, blockpos, (Entity)null, this);
								f -= (f2 + 0.3F) * 0.3F;

								if(f2 < 600000)
									set.add(blockpos);
							}

							d4 += d0 * 0.30000001192092896D;
							d6 += d1 * 0.30000001192092896D;
							d8 += d2 * 0.30000001192092896D;
						}
					}

		affectedBlockPositions.addAll(set);
		float f3 = explosionSize * 2.0F;
		int k1 = MathHelper.floor(explosionX - f3 - 1.0D);
		int l1 = MathHelper.floor(explosionX + f3 + 1.0D);
		int i2 = MathHelper.floor(explosionY - f3 - 1.0D);
		int i1 = MathHelper.floor(explosionY + f3 + 1.0D);
		int j2 = MathHelper.floor(explosionZ - f3 - 1.0D);
		int j1 = MathHelper.floor(explosionZ + f3 + 1.0D);
		List<Entity> list = worldObj.getEntitiesWithinAABBExcludingEntity(exploder, new AxisAlignedBB(k1, i2, j2, l1, i1, j1));
		net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(worldObj, this, list, f3);
		Vec3d vec3d = new Vec3d(explosionX, explosionY, explosionZ);

		for (int k2 = 0; k2 < list.size(); ++k2)
		{
			Entity entity = list.get(k2);

			if (!entity.isImmuneToExplosions())
			{
				double d12 = entity.getDistance(explosionX, explosionY, explosionZ) / f3;

				if (d12 <= 1.0D)
				{
					double d5 = entity.posX - explosionX;
					double d7 = entity.posY + entity.getEyeHeight() - explosionY;
					double d9 = entity.posZ - explosionZ;
					double d13 = MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);

					if (d13 != 0.0D)
					{
						d5 = d5 / d13;
						d7 = d7 / d13;
						d9 = d9 / d13;
						double d14 = worldObj.getBlockDensity(vec3d, entity.getEntityBoundingBox());
						double d10 = (1.0D - d12) * d14;
						entity.attackEntityFrom(PlayerKillUtil.nyarlathotep_explosion, (int)((d10 * d10 + d10) / 2.0D * 1000000000.0D * f3 + 1.0D));
						double d11 = 1.0D;

						if (entity instanceof EntityLivingBase)
							d11 = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase)entity, d10);

						entity.motionX += d5 * d11;
						entity.motionY += d7 * d11;
						entity.motionZ += d9 * d11;

						if (entity instanceof EntityPlayer)
						{
							EntityPlayer entityplayer = (EntityPlayer)entity;

							if (!entityplayer.isSpectator())
								playerKnockbackMap.put(entityplayer, new Vec3d(d5 * d10, d7 * d10, d9 * d10));
						}

						if(entity instanceof EntityItem)
							entity.setDead();
					}
				}
			}
		}
	}

	/**
	 * Does the second part of the explosion (sound, particles, drop spawn)
	 */
	@Override
	public void doExplosionB(boolean spawnParticles)
	{
		worldObj.playSound((EntityPlayer)null, explosionX, explosionY, explosionZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);

		if (explosionSize >= 2.0F && isSmoking)
			worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, explosionX, explosionY, explosionZ, 1.0D, 0.0D, 0.0D, new int[0]);
		else
			worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, explosionX, explosionY, explosionZ, 1.0D, 0.0D, 0.0D, new int[0]);

		if (isSmoking)
			for (BlockPos blockpos : affectedBlockPositions)
			{

				if (spawnParticles)
				{
					double d0 = blockpos.getX() + worldObj.rand.nextFloat();
					double d1 = blockpos.getY() + worldObj.rand.nextFloat();
					double d2 = blockpos.getZ() + worldObj.rand.nextFloat();
					double d3 = d0 - explosionX;
					double d4 = d1 - explosionY;
					double d5 = d2 - explosionZ;
					double d6 = MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
					d3 = d3 / d6;
					d4 = d4 / d6;
					d5 = d5 / d6;
					double d7 = 0.5D / (d6 / explosionSize + 0.1D);
					d7 = d7 * (worldObj.rand.nextFloat() * worldObj.rand.nextFloat() + 0.3F);
					d3 = d3 * d7;
					d4 = d4 * d7;
					d5 = d5 * d7;
					worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (d0 + explosionX) / 2.0D, (d1 + explosionY) / 2.0D, (d2 + explosionZ) / 2.0D, d3, d4, d5, new int[0]);
					worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5, new int[0]);
				}

				worldObj.setBlockToAir(blockpos);
			}

		if (isFlaming)
			for (BlockPos blockpos1 : affectedBlockPositions)
				if (worldObj.getBlockState(blockpos1).getMaterial() == Material.AIR && worldObj.getBlockState(blockpos1.down()).isFullBlock() && explosionRNG.nextInt(3) == 0)
					worldObj.setBlockState(blockpos1, Blocks.FIRE.getDefaultState());
	}

	@Override
	public Map<EntityPlayer, Vec3d> getPlayerKnockbackMap()
	{
		return playerKnockbackMap;
	}

	/**
	 * Returns either the entity that placed the explosive block, the entity that caused the explosion or null.
	 */
	@Override
	public EntityLivingBase getExplosivePlacedBy()
	{
		return exploder == null ? null : exploder instanceof EntityNyarlathotepTNT ? ((EntityNyarlathotepTNT)exploder).getTntPlacedBy() : exploder instanceof EntityLivingBase ? (EntityLivingBase)exploder : null;
	}

	@Override
	public void clearAffectedBlockPositions()
	{
		affectedBlockPositions.clear();
	}

	@Override
	public List<BlockPos> getAffectedBlockPositions()
	{
		return affectedBlockPositions;
	}

	@Override
	public Vec3d getPosition(){ return position; }
}
