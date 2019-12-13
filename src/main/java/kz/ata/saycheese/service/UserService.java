package kz.ata.saycheese.service;

import kz.ata.saycheese.model.UserModel;
import kz.ata.saycheese.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<Long> findAllUserIds(){
        return userRepository.findAll().stream().map(u->u.getTelegramId()).collect(Collectors.toList());
    }

    public boolean checkAccessRights(Long chatId) {
        return findAllUserIds().contains(chatId);
    }
}
