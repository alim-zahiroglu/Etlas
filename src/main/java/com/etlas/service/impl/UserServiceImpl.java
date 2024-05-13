package com.etlas.service.impl;

import com.etlas.dto.UserDto;
import com.etlas.entity.User;
import com.etlas.exception.UserNotFoundException;
import com.etlas.mapper.MapperUtil;
import com.etlas.repository.UserRepository;
import com.etlas.service.BalanceService;
import com.etlas.service.TicketService;
import com.etlas.service.UserService;
import com.etlas.service.VisaService;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final MapperUtil mapper;
    private final PasswordEncoder passwordEncoder;
    private final TicketService ticketService;
    private final VisaService visaService;
    private final BalanceService balanceService;

    public UserServiceImpl(UserRepository repository, MapperUtil mapper, PasswordEncoder passwordEncoder, @Lazy TicketService ticketService, VisaService visaService, BalanceService balanceService) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.ticketService = ticketService;
        this.visaService = visaService;
        this.balanceService = balanceService;
    }

    @Transactional
    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setPassWord(passwordEncoder.encode(userDto.getPassWord()));
        User savedUser = repository.save(mapper.convert(userDto,new User()));
        return mapper.convert(savedUser, new UserDto());
//        TODO send verification email if user verify is ture

    }

    @Override
    public UserDto findById(long id) {
        User user =  repository.findById(id).orElseThrow(()-> new UserNotFoundException("No user found with id: " + id));
        return mapper.convert(user,new UserDto());
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = repository.findAll();
        return users.stream()
                .map(user -> mapper.convert(user, new UserDto()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findByUsername(String username) {
        User foundUser  = repository.findByUserNameAndIsDeleted(username,false);
        if (foundUser == null) throw new UserNotFoundException("No user found with name: " + username);
        return mapper.convert(foundUser,new UserDto());
    }

    @Transactional
    @Override
    public UserDto deleteUser(String username) {
        User user = repository.findByUserNameAndIsDeleted(username,false);
        user.setDeleted(true);
        user.setUserName(user.getUserName() + "_"+ LocalDateTime.now());
        repository.save(user);
        return mapper.convert(user, new UserDto());
    }

    @Override
    public List<UserDto> findAllUsers() {
        return repository.findAllByIsDeletedOrderByLastUpdateDateTimeDesc(false).stream()
                .map(user -> mapper.convert(user,new UserDto()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserByUserName(String userName) {
        User user = repository.findByUserNameAndIsDeleted(userName,false);
        if (user == null) throw new UserNotFoundException("No user found with name: " + userName);
        return mapper.convert(user, new UserDto());
    }

    @Transactional
    @Override
    public UserDto saveUpdatedUser(UserDto userToUpdate) {
        UserDto oldUser = findById(userToUpdate.getId());
        if (userToUpdate.getPassWord().isEmpty()) {
            userToUpdate.setPassWord(oldUser.getPassWord());
        }else {
            userToUpdate.setPassWord(passwordEncoder.encode(userToUpdate.getPassWord()));
        }
        repository.save(mapper.convert(userToUpdate, new User()));
        return mapper.convert(oldUser, new UserDto());
//        TODO send verification email if user verify is ture
    }

    @Override
    public BindingResult validateNewUser(UserDto userDto, BindingResult bindingResult) {
        boolean isUserNameExist = repository.existsByUserName(userDto.getUserName());
        if (isUserNameExist){
            bindingResult.addError(new FieldError("userDto", "userName", "this user already exist"));
        }
        return bindingResult;
    }

    @Override
    public BindingResult validateUpdatedUser(UserDto userToBeUpdate, BindingResult bindingResult) {
        User oldUser = repository.findById(userToBeUpdate.getId()).orElseThrow(NoSuchElementException::new);

        if (!oldUser.getUserName().equals(userToBeUpdate.getUserName())) {
            boolean isUserExist = repository.existsByUserName(userToBeUpdate.getUserName());
            if (isUserExist) {
                bindingResult.addError(new FieldError("userToBeUpdate", "userName", "this user already exist"));
            }
        }
        if (userToBeUpdate.isUseCurrentPassword()){
            List<FieldError> errorsToKeep = bindingResult.getFieldErrors().stream()
                    .filter(fer -> !fer.getField().equals("passWord"))
                    .toList();
            UserDto userDto = new UserDto();
            BindingResult newBindingResult = new BeanPropertyBindingResult(userDto,"userDto");

            for (FieldError fieldError : errorsToKeep) {
                newBindingResult.addError(fieldError);
            }
            return newBindingResult;
        }
        return bindingResult;
    }

    @Override
    public boolean isUserDeletable(String username) {
        User userToBeDelete = repository.findByUserNameAndIsDeleted(username,false);
        if (userToBeDelete != null){
            boolean isOnlyAdmin = false;
            if (userToBeDelete.getRole().getDescription().equals("Admin")) {
                List<User> users = repository.findAllByRoleAndIsDeleted(userToBeDelete.getRole(),false);
                isOnlyAdmin = users.size() == 1;
            }
            boolean isUserUsedInTicket = ticketService.isUserBoughtTicket(userToBeDelete.getUserName());
            boolean isUserUsedInVisa = visaService.isUserBoughtTicket(userToBeDelete.getUserName());
            boolean isUserUsedInBalanceRecord = balanceService.isUserReceivedMoney(userToBeDelete.getUserName());

            return !isUserUsedInTicket && !isOnlyAdmin && !isUserUsedInVisa && !isUserUsedInBalanceRecord;
        }
        return false;
    }
}
