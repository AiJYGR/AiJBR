package com.aijygr.Events.Game.Ring;

import com.aijygr.Main;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class RingEventSubscriber {
    private static int r_waiting_tick[] = { 1, 300, 200, 150, 1000000, 1200, 1200, 10000000 };
    private static int r_moving_tick[] ={ 200, 200, 150, 100, 1000000, 1200, 1200, 10000000 };
    private static double r_size[] = { 30, 30, 20, 15, 10, 64, 32, 16 };
    private static int max_round = 8;
    private static long starttick = 0;
    private static long svtick = 0;
    private static long tick = 0;
    private static boolean isGameStart = false;
    private static boolean isRingClosing = false;
    private static double next_x = 0;
    private static double next_z = 0;
    private static double curr_x = 0;
    private static double curr_z = 0;
    private static int round = 0;

    private static void generate_next_ring() {
        double randnum = (double)Math.random()*2 - 1.0d;//生成[-1,1]的随机数
        curr_x=next_x;
        curr_z=next_z;

        next_x =
        (long)Math.ceil((double)curr_x+((double)r_size[round]/8.0d)*randnum);
        randnum = Math.random()*2 - 1.0d;
        next_z =
        (long)Math.ceil((double)curr_z+((double)r_size[round]/8.0d)*randnum);
    }

    private static void set_ring(double process, Level level)
    {
        var wb = level.getWorldBorder();

        double x = ((double) (next_x - curr_x) * process) + (double) curr_x;
        double y = ((double) (next_z - curr_z) * process) + (double) curr_z;
        wb.setCenter(x, y);
        wb.setSize(
                (double) ((double) r_size[round + 1] + (double) (r_size[round] - r_size[round + 1]) * (1.0 - process)));
        Main.LOGGER
                .info("ratio" + process + " x=" + wb.getCenterX() + " z=" + wb.getCenterZ() + " size=" + wb.getSize());
    }

    private static boolean ifTick(long tickin) {
        if (svtick < tickin) {
            svtick = tickin;
            tick = svtick - starttick;
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public static void onServerStart(ServerStartedEvent event) {
        MinecraftForge.EVENT_BUS.post(new GameInitEvent(event.getServer().overworld(), null));
    }

    @SubscribeEvent
    public static void onGameStart(GameStartEvent event) {
        Player player = event.getPlayer();
        if (isGameStart) {
            player.sendSystemMessage(Component.translatable("[FAIL] : ").withStyle(style -> style.withColor(0xff5555))
                    .append(Component.translatable("aijbr.command.err.game_started")
                            .withStyle(style -> style.withColor(0xffffff))));
            return;
        } else {
            isGameStart = true;
            starttick = event.getLevel().getGameTime();
            player.sendSystemMessage(Component.translatable("aijbr.command.executed"));
        }
    }

    @SubscribeEvent
    public static void onGameInit(GameInitEvent event) {

        Player player = event.getPlayer();
        event.getLevel().getWorldBorder().setSize(r_size[0]);
        event.getLevel().getWorldBorder().setCenter(0.0, 0.0);
        isGameStart = false;
        starttick = -1;
        next_x = 0;
        next_z = 0;
        curr_x = 0;
        curr_z = 0;
        if (player != null)
            player.sendSystemMessage(Component.translatable("aijbr.command.executed"));
    }

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent event) {
        if ((event.phase == TickEvent.Phase.START) && (event.level.dimension().equals(Level.OVERWORLD))
                && (!event.level.isClientSide())) {
            if (isGameStart && ifTick(event.level.getGameTime())) {
                long ctick = tick;
                for (int i = 0; i <= max_round; i++)// 计算当前所在的游戏阶段(回合数，是否在缩圈)
                {
                    if (ctick >= r_waiting_tick[i])
                        ctick -= r_waiting_tick[i];
                    else {
                        round = i;
                        isRingClosing = false;
                        break;
                    }
                    if (ctick >= r_moving_tick[i])
                        ctick -= r_moving_tick[i];
                    else {
                        round = i;
                        isRingClosing = true;
                        break;
                    }
                }
                if (ctick == 0&&round!=0)// 在游戏阶段变化时 执行命令
                {
                    if (isRingClosing)
                    {
                        Main.LOGGER.info("Ring Starts to move.");
                    } else {
                        Main.LOGGER.info("Next round.");
                        generate_next_ring();
                        Main.LOGGER.info("cur:(" + curr_x + "," + curr_z + ")");
                        Main.LOGGER.info("next:(" + next_x + "," + next_z + ")");
                    }
                }
                Main.LOGGER.info("tick=" + tick + " round=" + round + " isRingClosing=" + isRingClosing + " currxz:"
                        + curr_x + "," + curr_z + " nextxz=" + next_x + "," + next_z);
                if (isRingClosing&&round!=0) {
                    set_ring((double) ((double) ctick / (double) r_moving_tick[round]), event.level);
                }
            }
        }
    }
}
