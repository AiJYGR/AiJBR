package com.aijygr.Events.Game.Ring;

import com.aijygr.Events.Game.Game;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber()
public class RingDamage {
    private static final String DATA_LastHurtTick = "AiJBR:LastHurtTick";

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.side == LogicalSide.SERVER) {
            Player player = event.player;
            if(player!=null) {
                double distance =  player.level().getWorldBorder().getDistanceToBorder(player);
                CompoundTag data = player.getPersistentData();
                if(distance < 0)
                {

                    long lasthurttick = data.getLong(DATA_LastHurtTick);
                    if(lasthurttick == -1)
                    {
                        data.putLong((DATA_LastHurtTick),Game.gametime);
                        return ;//刚进入毒圈的时候不受伤害
                    }

                    if(Game.gametime - lasthurttick >= Game.damage_tickingtime)
                    {
                        float damage = (float)((-distance)*Game.damage_per_block +Game.basic_damage);
                        player.hurt(player.damageSources().outOfBorder(),damage);
                        data.putLong((DATA_LastHurtTick), Game.gametime);
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
