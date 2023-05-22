package ua.foxstudent.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.foxstudent.domain.user.User;
import ua.foxstudent.repository.UserRepository;
import ua.foxstudent.service.UserService;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Long save(User user) {
        var savedUser = userRepository.save(user);

        return savedUser.getId();
    }
}
