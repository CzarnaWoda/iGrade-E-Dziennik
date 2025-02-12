package me.igrade.user.mapper;

import me.igrade.user.dto.UserDto;
import me.igrade.user.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto mapUserToUserDto(User user){

        final UserDto userDto = new UserDto();


        BeanUtils.copyProperties(userDto, userDto);

        userDto.setRole(user.getRole().getName());

        return userDto;
    }
}
