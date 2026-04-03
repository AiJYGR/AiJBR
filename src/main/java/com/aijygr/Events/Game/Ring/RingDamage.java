package com.aijygr.Events.Game.Ring;

import com.aijygr.Main;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;



@Mod.EventBusSubscriber()
public class RingDamage {

    private static long gametime;
    private static short damagetickingtime = 30;
    //private static long damagetick;

    private static float basic_damage = 0.5f;
    private static float damage_per_block =  0.01f;
    private static final String DATA_LastHurtTime = "AiJBR:LastHurtTick";

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if((event.phase == TickEvent.Phase.START) && (event.level.dimension().equals(Level.OVERWORLD))
                && (!event.level.isClientSide()))
        {
            gametime = event.level.getGameTime();
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.side == LogicalSide.SERVER) {
            Player player = event.player;
            if(player!=null) {
                double distance =  player.level().getWorldBorder().getDistanceToBorder(player);
                CompoundTag data = player.getPersistentData();
                if(distance < 0)
                {
                    long lasthurttick = data.getLong(DATA_LastHurtTime);
                    if(lasthurttick == -1)
                    {
                        data.putLong((DATA_LastHurtTime), gametime);
                        return ;//刚进入毒圈的时候不受伤害
                    }
                    if(gametime - lasthurttick >= damagetickingtime)
                    {
                        float damage = (float)((-distance)*damage_per_block+basic_damage);
                        player.hurt(player.damageSources().outOfBorder(),damage);
                        data.putLong((DATA_LastHurtTime), gametime);
                    }

                }
                else
                {
                    data.putLong("AiJBR_LastHurtTick", -1);
                }
            }
        }
    }
}
