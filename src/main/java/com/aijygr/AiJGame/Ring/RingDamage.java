package com.aijygr.AiJGame.Ring;

import com.aijygr.AiJGame.Game;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import static com.aijygr.DamageSource.getRingDamageSource;


@Mod.EventBusSubscriber()
public class RingDamage {
    private static final String DATA_LastHurtTick = "AiJBR_LastRingHurtTick";
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.side == LogicalSide.SERVER) {
            Player player = event.player;
            if(player!=null) {
                double distance =  player.level().getWorldBorder().getDistanceToBorder(player) - 0.3;//玩家碰撞箱宽度为0.3
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
                        //player.sendSystemMessage(Component.translatable(player.getName().getString()+String.format(" %3.2f",damage)));
                        if(damage - player.getHealth() > 0) {
                            //player.hurt(player.damageSources().outOfBorder(),damage);//kill the player
                            player.hurt(getRingDamageSource(player.level()),damage);
                        }
                        else {

                            //player.setHealth(player.getHealth()-damage);
                            player.hurt(getRingDamageSource(player.level()),damage);
                            player.invulnerableTime = 0;
                            player.hurtTime = 0;
                            //if(player instanceof ServerPlayer)
                            //{
                            //    player.playSound(SoundEvents.PLAYER_HURT_DROWN, 0.8F, 0.8F);
                            //    player.playNotifySound(SoundEvents.PLAYER_HURT_DROWN, SoundSource.PLAYERS,0.8F, 0.8F);
                            //    System.out.println("hurt");
                            //}
                        }

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
