package com.cydeo.review;

import com.cydeo.dto.RoleDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Role;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.KeycloakService;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.impl.UserServiceImpl;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ProjectService projectService;
    @Mock
    private TaskService taskService;
    @Mock
    private KeycloakService keycloakService;
    @InjectMocks
    private UserServiceImpl userService;
    @Spy // since UserMapper created by Spring, we can not define "then" return type, so can not use mock annotation
    private UserMapper userMapper = new UserMapper(new ModelMapper());

    User user;
    UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserName("user");
        user.setPassWord("Abc1");
        user.setEnabled(true);
        user.setRole(new Role("Manager"));

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setUserName("user");
        userDTO.setPassWord("Abc1");
        userDTO.setEnabled(true);

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setDescription("Manager");

        userDTO.setRole(roleDTO);
    }

    private List<User> getUsers() {
        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Emily");

        return List.of(user, user2);
    }

    private List<UserDTO> getUserDTOs() {
        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(2L);
        userDTO2.setFirstName("Emily");
        return List.of(userDTO, userDTO2);
    }

    @Test
    public void should_list_all_users() {
        //stub
        when(userRepository.findAllByIsDeletedOrderByFirstNameDesc(false)).thenReturn(getUsers());

        List<UserDTO> expectedList = getUserDTOs();

        List<UserDTO> actualList = userService.listAllUsers();

//        Assertions.assertEquals(expectedList,actualList);
        //AssertJ
        assertThat(actualList).usingRecursiveComparison().isEqualTo(expectedList);
    }

    @Test
    void should_find_user_by_username() {
        //stub
        when(userRepository.findByUserNameAndIsDeleted(anyString(), anyBoolean())).thenReturn(user);
        // we can use either raw hard-coded both side for example ("user", false) or any as used. Otherwise, it fails.

        UserDTO actualUserDTO = userService.findByUserName("user");

        assertThat(actualUserDTO).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(userDTO);
        // asserThat compares compare fields, not object
    }

    @Test
    void should_throw_exception_when_user_not_found(){
//        when(userRepository.findByUserNameAndIsDeleted("", false)).thenThrow(new NoSuchElementException("User Not Found"));
//
//        Throwable throwable = assertThrows(NoSuchElementException.class, () -> userService.findByUserName(""));
//        assertEquals("User Not Found", throwable.getMessage());
        // mo stub needed, first line will be null and will assigned to the user field.

        Throwable throwable = catchThrowable(() ->
                userService.findByUserName("someUserName"));
        AssertionsForClassTypes.assertThat(throwable).isInstanceOf(RuntimeException.class);
        AssertionsForClassTypes.assertThat(throwable).hasMessage("User not found.");
    }
    @Test
    void should_save_user(){
        /*
        user.setEnabled(true);

        User obj = userMapper.convertToEntity(user);

        User savedUser = userRepository.save(obj);// save in the db

        keycloakService.userCreate(user); // call this method to connect to keycloak, save in the keycloak as well

        return userMapper.convertToDto(savedUser);
         */
        when(userRepository.save(user)).thenReturn(user);
        UserDTO actualDTO = userService.save(userDTO);
        assertThat(actualDTO).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(userDTO);
        verify(keycloakService, atLeast(1)).userCreate(any());
    }
}
