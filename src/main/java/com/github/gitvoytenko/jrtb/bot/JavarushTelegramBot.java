package com.github.gitvoytenko.jrtb.bot;

import com.github.gitvoytenko.jrtb.command.CommandContainer;
import com.github.gitvoytenko.jrtb.service.SendBotMessageServiceIpml;
import com.github.gitvoytenko.jrtb.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


import static com.github.gitvoytenko.jrtb.command.CommandName.NO;

/**
 * Telegrambot for JavaRush Community from JavaRush community.
 */
@Component
public class JavarushTelegramBot extends TelegramLongPollingBot {

    public static String COMMAND_PREFIX = "/";

    @Value("${bot.username}")
    private String username;

    @Value("${bot.token}")
    private String token;

    private final CommandContainer commandContainer;

    @Autowired
    public JavarushTelegramBot(TelegramUserService telegramUserService) {
        this.commandContainer = new CommandContainer(new SendBotMessageServiceIpml(this), telegramUserService);
    }

    @Override
    public void onUpdateReceived(Update update){
            if(update.hasMessage() && update.getMessage().hasText()) {
                String message = update.getMessage().getText().trim();
                if(message.startsWith(COMMAND_PREFIX)){
                    String commandIdentifier = message.split(" ")[0].toLowerCase();
                    commandContainer.retrieveCommand(commandIdentifier).execute(update);
                } else {
                    commandContainer.retrieveCommand(NO.getCommandName()).execute(update);
                }
            }
    }

    @Override
    public String getBotUsername(){
        return username;
    }

    @Override
    public String getBotToken(){
        return token;
    }
}
