package com.github.gitvoytenko.jrtb.service;

import com.github.gitvoytenko.jrtb.bot.JavarushTelegramBot;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Implementation of {@link SendBotMessageService} interface.
 */
@Service
public class SendBotMessageServiceIpml implements SendBotMessageService{

    private final JavarushTelegramBot javarushBot;

    @Autowired
    public SendBotMessageServiceIpml(JavarushTelegramBot javarushBot){
        this.javarushBot = javarushBot;
    }


    @Override
    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        sendMessage.setText(message);

        try{
            javarushBot.execute(sendMessage);
            } catch (TelegramApiException e){
            // to-do add logging to the project.
            e.printStackTrace();
        }
    }
}
