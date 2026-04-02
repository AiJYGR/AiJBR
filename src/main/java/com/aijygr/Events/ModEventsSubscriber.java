package com.aijygr.Events;

import com.aijygr.Main;
import com.aijygr.Commands.InitCommand;
import com.aijygr.Commands.ReloadCommand;
import com.aijygr.Commands.ScrCommand;
import com.aijygr.Commands.StartCommand;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEventsSubscriber
{
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event)
    {
        new StartCommand(event.getDispatcher());
        new InitCommand(event.getDispatcher());
        new ScrCommand(event.getDispatcher());
        new ReloadCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }
}