package com.github.gitvoytenko.jrtb.command;

import com.github.gitvoytenko.jrtb.repository.entity.GroupSub;
import com.github.gitvoytenko.jrtb.repository.entity.TelegramUser;
import com.github.gitvoytenko.jrtb.service.GroupSubService;
import com.github.gitvoytenko.jrtb.service.SendBotMessageService;
import com.github.gitvoytenko.jrtb.service.TelegramUserService;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.gitvoytenko.jrtb.command.CommandName.DELETE_GROUP_SUB;
import static com.github.gitvoytenko.jrtb.command.CommandUtils.*;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isNumeric;

/**
 * Delete Group subscription {@link Command}.
 */
public class DeleteGroupSubCommand implements Command{

    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;
    private final GroupSubService groupSubService;

    public DeleteGroupSubCommand(SendBotMessageService sendBotMessageService, GroupSubService groupSubService,
                                 TelegramUserService telegramUserService){
        this.sendBotMessageService = sendBotMessageService;
        this.groupSubService = groupSubService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        if(getMessage(update).equalsIgnoreCase(DELETE_GROUP_SUB.getCommandName())){
            sendGroupIdList(getChatId(update));
            return;
        }
        String groupId = getMessage(update).split(SPACE)[1];
        Long chatId = getChatId(update);

        if(isNumeric(groupId)) {
            Optional<GroupSub> optionalGroupSub = groupSubService.findById(Integer.valueOf(groupId));
            if(optionalGroupSub.isPresent()){
                GroupSub groupSub = optionalGroupSub.get();
                TelegramUser telegramUser = telegramUserService.findByChatId(chatId).orElseThrow(NotFoundException::new);
                groupSub.getUsers().remove(telegramUser);
                groupSubService.save(groupSub);
                sendBotMessageService.sendMessage(chatId, format("Удалил подписку на группу: %s", groupSub.getTitle()));
            } else {
                sendBotMessageService.sendMessage(chatId, "Не нашел такой группы =/");
            }
        } else {
            sendBotMessageService.sendMessage(chatId, "Неправильный формат ID группы.\n" +
                    "ID должно быть целым положительным числом");
        }

    }
        private void sendGroupIdList(Long chatId) {
            String message;
            List<GroupSub> groupSubs = telegramUserService.findByChatId(chatId)
                    .orElseThrow(NotFoundException::new).getGroupSubs();
            if(CollectionUtils.isEmpty(groupSubs)){
                message = "Пока нет подписок на группы. Чтобы добавить подписку напиши /addgroupsub";
            } else {
            String userGroupSubData = groupSubs.stream()
                    .map(group -> format("%s - %s \n", group.getTitle(), group.getId()))
                    .collect(Collectors.joining());

                message = String.format("Чтобы удалить подписку на группу - передай команду вместе с ID группы. \n" +
                        "Например: /deletegroupsub 16 \n\n" +
                        "я подготовил список всех групп, на которые ты подписан \n\n" +
                        "имя группы - ID группы \n\n" +
                        "%s", userGroupSubData);
            }
            sendBotMessageService.sendMessage(chatId, message);
        }
}
