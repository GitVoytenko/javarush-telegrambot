package com.github.gitvoytenko.jrtb.command;

import com.github.gitvoytenko.jrtb.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Start {@link Command}.
 */
public class StartCommand implements Command{

    private final SendBotMessageService sendBotMessageService;

    public final static String START_MESSAGE = "Привет. Я JavaRush Telgram Bot. Я помогу тебе быть в курсе последних " +
            "статей тех авторов, которые тебе интересны. Я еще маленький и только учусь.";

    // Здесь не добовляем сервис через получение из Application Context.
    // Потому что если это сделать так, то будет циклическая зависимость, которая ломает работу приложения.
    public StartCommand(SendBotMessageService sendBotMessageService){
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), START_MESSAGE);
    }
}
