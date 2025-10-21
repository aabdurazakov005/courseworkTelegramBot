package org.skypro.Reminder005Bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final TelegramBot telegramBot;
    private final NotificationTaskRepository notificationTaskRepository;
    private final Pattern pattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}:\\d{2})\\s+(.+)");

    public TelegramBotUpdatesListener(@Value("${bot.token}") String token,
                                      NotificationTaskRepository notificationTaskRepository) {
        this.telegramBot = new TelegramBot(token);
        this.notificationTaskRepository = notificationTaskRepository;
        this.telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            if (update.message() != null && update.message().text() != null) {
                Long chatId = update.message().chat().id();
                String text = update.message().text();

                if ("/start".equals(text)) {
                    sendWelcomeMessage(chatId);
                } else {
                    processReminderMessage(chatId, text);
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void processReminderMessage(Long chatId, String text) {
        Matcher matcher = pattern.matcher(text);

        if (matcher.matches()) {
            try {
                String dateTimeStr = matcher.group(1);
                String reminderText = matcher.group(2);

                LocalDateTime notificationDateTime = LocalDateTime.parse(
                        dateTimeStr,
                        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                );

                // Проверяем, что дата в будущем
                if (notificationDateTime.isBefore(LocalDateTime.now())) {
                    sendMessage(chatId, " Нельзя установить напоминание на прошедшее время!");
                    return;
                }

                NotificationTask task = new NotificationTask(chatId, reminderText, notificationDateTime);
                notificationTaskRepository.save(task);

                String response = String.format(
                        " Напоминание установлено на %s\n Текст: %s",
                        dateTimeStr, reminderText
                );
                sendMessage(chatId, response);

            } catch (DateTimeParseException e) {
                sendMessage(chatId, " Неверный формат даты или времени! Используйте: ДД.ММ.ГГГГ ЧЧ:MM");
            }
        } else {
            sendMessage(chatId, """
                     Неверный формат сообщения!
                    
                     Правильный формат:
                    ДД.ММ.ГГГГ ЧЧ:MM Ваш текст напоминания
                    
                    Например:
                    01.01.2025 20:00 Сделать домашнюю работу
                    """);
        }
    }

    private void sendWelcomeMessage(Long chatId) {
        String welcomeText = """
                 Добро пожаловать в бот-напоминалку!
                
                Я помогу вам не забывать о важных делах.
                
                 Формат добавления напоминания:
                ДД.ММ.ГГГГ ЧЧ:MM Ваш текст напоминания
                
                Например:
                01.01.2025 20:00 Сделать домашнюю работу
                
                Я пришлю вам напоминание в указанное время!
                """;
        sendMessage(chatId, welcomeText);
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        telegramBot.execute(message);
    }
}