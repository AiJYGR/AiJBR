package com.aijygr.AiJGame.Ring;

import com.aijygr.AiJGame.Game;
import com.aijygr.LIB;
import com.aijygr.Reg;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.event.TickEvent;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.aijygr.ModDamageSource.getRingDamageSource;


@Mod.EventBusSubscriber()
public class RingDamage {
    private static final String DATA_LastHurtTick = "AiJBR_LastRingHurtTick";
    public static final double PLAYER_HITBOXFIX = 0.3d;
    public static void hurtPlayer(ServerPlayer player, float damage, boolean isClientSoundOnly) {
        if(player.isCreative() || player.isSpectator())
            return;
        if(isClientSoundOnly && player.getHealth() >= damage)
        {
            player.setHealth(player.getHealth()-damage);
            player.playNotifySound(Reg.RING_DAMAGE_SOUND.get(), SoundSource.AMBIENT, 1.0F, 1.0F);//客户端声音
            //player.playSound(Reg.RING_DAMAGE_SOUND.get(), 1.0F, 1.0F);             //这个不知道为啥用不了 没声音
            //player.level().playSound(null, player.getX(), player.getY(), player.getZ(), Reg.RING_DAMAGE_SOUND.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        else
        {
            player.hurt(getRingDamageSource(player.level()),damage);
            player.invulnerableTime = 0;
            player.hurtTime = 0;
        }
    }
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (LIB.SV1TK(event))
        {
            if(event.player instanceof ServerPlayer player)
            {
                double distance =  player.level().getWorldBorder().getDistanceToBorder(player) - PLAYER_HITBOXFIX;//玩家碰撞箱宽度为0.3
                CompoundTag data = player.getPersistentData();
                long lasthurttick = data.getLong(DATA_LastHurtTick);
                if(distance < 0)
                {
                    if(lasthurttick == -1)
                    {
                        data.putLong((DATA_LastHurtTick),Game.gametime);
                        return ;//刚进入毒圈的时候不受伤害
                    }
                    if(Game.gametime - lasthurttick >= Game.damage_tickingtime)
                    {
                        float damage = (float)((-distance)*Game.sv_damage_per_block +Game.sv_basicdamage);
                        hurtPlayer(player,damage,true);
                        data.putLong((DATA_LastHurtTick), Game.gametime);
                    }
                }
                else
                {
                    if(Game.gametime - lasthurttick >= Game.damage_tickingtime)
                        data.putLong(DATA_LastHurtTick, -1);
                }
            }
        }
    }
}
