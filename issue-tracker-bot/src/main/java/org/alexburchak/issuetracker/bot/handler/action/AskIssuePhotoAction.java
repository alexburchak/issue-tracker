package org.alexburchak.issuetracker.bot.handler.action;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.alexburchak.issuetracker.bot.config.IssueTrackerBotProperties;
import org.alexburchak.issuetracker.data.domain.jpa.Issue;
import org.alexburchak.issuetracker.data.domain.jpa.Photo;
import org.alexburchak.issuetracker.data.repository.jpa.IssueRepository;
import org.alexburchak.issuetracker.data.state.Context;
import org.alexburchak.issuetracker.data.state.IssueContext;
import org.alexburchak.issuetracker.data.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author alexburchak
 */
@Component
@ActionBinding(State.ASKING_ISSUE_PHOTO)
public class AskIssuePhotoAction extends Action<IssueContext> {
    @Autowired
    private IssueTrackerBotProperties botProperties;
    @Autowired
    private IssueRepository issueRepository;

    @Override
    public Action<? extends Context> prompt(TelegramBot bot, Chat chat) {
        InlineKeyboardButton finishButton = new InlineKeyboardButton("Finish")
                .callbackData(State.MENU.name());
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(new InlineKeyboardButton[]{finishButton});

        bot.execute(new SendMessage(chat.id(), "Please attach photo and/or finish issue registration")
                .replyMarkup(keyboard)
        );

        return this;
    }

    @Override
    public Action<? extends Context> command(TelegramBot bot, Update update) {
        Message message = update.message();

        PhotoSize photoSizes[] = message.photo();
        if (photoSizes != null && photoSizes.length > 0) {
            PhotoSize photoSize = Arrays.stream(photoSizes)
                    .max(Comparator.comparingInt(PhotoSize::height)
                            .thenComparingInt(PhotoSize::width)
                            .thenComparingInt(PhotoSize::fileSize)
                    ).get();

            GetFile getFile = new RestTemplate()
                    .getForObject(String.format("https://api.telegram.org/bot%s/getFile?file_id=%s", botProperties.getApiKey(), photoSize.fileId()), GetFile.class);

            if (getFile != null && getFile.isOk()) {
                GetFile.Result result = getFile.getResult();
                if (result != null && result.getFilePath() != null) {
                    List<String> photos = context().getPhotos();
                    if (photos == null) {
                        photos = new ArrayList<>();
                        context().setPhotos(photos);
                    }
                    photos.add(result.getFilePath());
                }
            }
        }

        return super.command(bot, update);
    }

    @Override
    public Action<? extends Context> callback(TelegramBot bot, CallbackQuery callbackQuery) {
        if (callbackQuery != null && callbackQuery.data() != null) {
            IssueContext context = context();

            Issue issue = new Issue();
            issue.setName(context.getName());
            issue.setDescription(context.getDescription());
            Optional.ofNullable(context.getPhotos())
                    .ifPresent(ps -> ps
                            .forEach(p -> {
                                byte bytes[] = new RestTemplate()
                                        .getForObject(String.format("https://api.telegram.org/file/bot%s/%s", botProperties.getApiKey(), p), byte[].class);
                                Photo photo = new Photo();
                                photo.setIssue(issue);
                                photo.setData(bytes);
                                issue.getPhotos().add(photo);
                            })
                    );
            issueRepository.save(issue);

            bot.execute(new SendMessage(callbackQuery.message().chat().id(), "Thank you. Your issue has been recorded"));

            return action(State.valueOf(callbackQuery.data()))
                    .context(null);
        }

        return this;
    }
}
