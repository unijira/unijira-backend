package it.unical.unijira.services.common.impl;

import it.unical.unijira.data.dao.NotifyRepository;
import it.unical.unijira.data.models.Notify;
import it.unical.unijira.data.models.User;
import it.unical.unijira.services.common.EmailService;
import it.unical.unijira.services.common.NotifyService;
import it.unical.unijira.utils.Locale;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public record NotifyServiceImpl(NotifyRepository notifyRepository, EmailService emailService, Locale locale) implements NotifyService {

    @Override
    public void send(User user, String title, String message, @Nullable URL target, Notify.Priority priority, @Nullable Notify.Mask mask) {

        Objects.requireNonNull(user);
        Objects.requireNonNull(title);
        Objects.requireNonNull(message);
        Objects.requireNonNull(priority);

        if(!user.getNotificationMask().contains(mask)) {

            var n = new Notify();
            n.setTitle(title);
            n.setMessage(message);
            n.setPriority(priority);
            n.setUser(user);
            n.setTarget(target);

            this.create(n);

        }

        emailService.send (
                user.getUsername(),
                locale.get("NOTIFICATION_TITLE", title),
                locale.get("NOTIFICATION_MESSAGE", message)
        );

    }


    @Override
    public Optional<Notify> create(Notify notify) {

        var n = new Notify();
        n.setTitle(notify.getTitle());
        n.setMessage(notify.getMessage());
        n.setPriority(notify.getPriority());
        n.setUser(notify.getUser());
        n.setTarget(notify.getTarget());

        return Optional.of(notifyRepository.saveAndFlush(n));

    }

    @Override
    public Optional<Notify> update(Long id, Notify notify) {

        return notifyRepository.findById(id)
                .stream()
                .peek(n -> n.setRead(notify.isRead()))
                .findFirst()
                .map(notifyRepository::saveAndFlush);

    }

    @Override
    public Optional<Notify> findById(Long id) {
        return notifyRepository.findById(id);
    }

    @Override
    public List<Notify> findAllByUserId(Long userId, int page, int size) {
        return notifyRepository.findByUserIdOrderByReadAscPriorityAscDateDesc(userId, PageRequest.of(page, size));
    }

    @Override
    public List<Notify> findAllByUserId(Long userId) {
        return notifyRepository.findByUserIdOrderByReadAscPriorityAscDateDesc(userId);
    }

    
}
