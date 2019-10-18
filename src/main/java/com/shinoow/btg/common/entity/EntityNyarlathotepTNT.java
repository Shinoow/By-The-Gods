package com.shinoow.btg.common.entity;

import com.shinoow.btg.common.util.NyarlathotepExplosion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityNyarlathotepTNT extends Entity {

	/** How long the fuse is */
	public int fuse;
	private EntityLivingBase tntPlacedBy;

	public EntityNyarlathotepTNT(World par1World)
	{
		super(par1World);
		fuse = 40;
		preventEntitySpawning = true;
		setSize(0.98F, 0.98F);
	}

	public EntityNyarlathotepTNT(World par1World, double par2, double par4, double par6, EntityLivingBase par8EntityLivingBase)
	{
		this(par1World);
		setPosition(par2, par4, par6);
		float var8 = (float)(Math.random() * Math.PI * 2.0D);
		motionX = -((float)Math.sin(var8)) * 0.02F;
		motionY = 0.20000000298023224D;
		motionZ = -((float)Math.cos(var8)) * 0.02F;
		fuse = 40;
		prevPosX = par2;
		prevPosY = par4;
		prevPosZ = par6;
		tntPlacedBy = par8EntityLivingBase;
	}

	@Override
	protected void entityInit() {}

	@Override
	protected boolean canTriggerWalking()
	{
		return true;
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return !isDead;
	}

	@Override
	public void onUpdate()
	{
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		motionY -= 0.03999999910593033D;
		move(MoverType.SELF,motionX, motionY, motionZ);
		motionX *= 0.9800000190734863D;
		motionY *= 0.9800000190734863D;
		motionZ *= 0.9800000190734863D;

		if (onGround)
		{
			motionX *= 0.699999988079071D;
			motionZ *= 0.699999988079071D;
			motionY *= -0.5D;
		}

		if (fuse-- <= 0)
		{
			setDead();

			explode();

		} else {
			handleWaterMovement();
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX, posY + 0.5D, posZ, 0.0D, 0.0D, 0.0D, new int[0]);
		}
	}

	private void explode()
	{
		if(!world.isRemote){
			NyarlathotepExplosion explosion = new NyarlathotepExplosion(world, this, posX, posY, posZ, 16, false, true);
			if(net.minecraftforge.event.ForgeEventFactory.onExplosionStart(world, explosion)) return;
			explosion.doExplosionA();
			explosion.doExplosionB(true);

			if(world instanceof WorldServer)
				for (EntityPlayer entityplayer : ((WorldServer)world).playerEntities)
					if (entityplayer.getDistanceSq(posX, posY, posZ) < 4096.0D)
						((EntityPlayerMP)entityplayer).connection.sendPacket(new SPacketExplosion(posX, posY, posZ , 16, explosion.getAffectedBlockPositions(), explosion.getPlayerKnockbackMap().get(entityplayer)));

		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		par1NBTTagCompound.setByte("Fuse", (byte)fuse);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		fuse = par1NBTTagCompound.getByte("Fuse");
	}

	/**
	 * returns null or the entityliving it was placed or ignited by
	 */
	public EntityLivingBase getTntPlacedBy()
	{
		return tntPlacedBy;
	}

	@Override
	public float getEyeHeight()
	{
		return 0.0F;
	}
}