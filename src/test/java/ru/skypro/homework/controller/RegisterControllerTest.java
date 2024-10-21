package ru.skypro.homework.controller;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.skypro.homework.ConstantGeneratorFotTest;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.service.AuthService;

import static ru.skypro.homework.ConstantGeneratorFotTest.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testRegister_Success() throws Exception {

        JSONObject registerDto = new JSONObject();
        registerDto.put("username", USER_EMAIL);
        registerDto.put("password", USER_PASSWORD);
        registerDto.put("firstName", USER_FIRST_NAME);
        registerDto.put("lastName", USER_LAST_NAME);
        registerDto.put("phone", USER_PHONE);
        registerDto.put("role", USER_ROLE);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .content(registerDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
                //.andExpect(MockMvcResultMatchers.jsonPath("$.email").value(USER_EMAIL))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(USER_FIRST_NAME));
                //.andExpect(MockMvcResultMatchers.jsonPath("$.role").value(USER_ROLE.name()));
    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testRegister_UserAlreadyRegisteredException() throws Exception {

        RegisterDto registerDto1 = ConstantGeneratorFotTest.registerDtoGenerator();
        authService.register(registerDto1);

        JSONObject registerDto2 = new JSONObject();
        registerDto2.put("username", NEW_USER_EMAIL);
        registerDto2.put("password", USER_PASSWORD);
        registerDto2.put("firstName", USER_FIRST_NAME);
        registerDto2.put("lastName", USER_LAST_NAME);
        registerDto2.put("phone", USER_PHONE);
        registerDto2.put("role", USER_ROLE);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .content(registerDto2.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testRegister_InvalidData() throws Exception {

        JSONObject registerDto = new JSONObject();
        registerDto.put("username", USER_INVALID_EMAIL);
        // Введенный логин не соответствует паттерну
        registerDto.put("password", USER_INVALID_PASSWORD);
        // Введенный пароль содержит 5 символов
        registerDto.put("firstName", USER_FIRST_NAME);
        registerDto.put("lastName", USER_LAST_NAME);
        registerDto.put("phone", USER_INVALID_PHONE);
        // Введенный номер не соответствует паттерну
        registerDto.put("role", USER_ROLE);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .content(registerDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
