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
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testLogin_Success() throws Exception {

        RegisterDto registerDto = ConstantGeneratorFotTest.registerDtoGenerator();
        authService.register(registerDto);

        JSONObject loginDto = new JSONObject();
        loginDto.put("password", NEW_USER_PASSWORD);
        loginDto.put("username", NEW_USER_EMAIL);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .content(loginDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
                //.andExpect(MockMvcResultMatchers.jsonPath("$.password").value(NEW_USER_PASSWORD))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.username").value(NEW_USER_EMAIL));
    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testLogin_UnauthorizedException_IncorrectLogin() throws Exception {

        RegisterDto registerDto = ConstantGeneratorFotTest.registerDtoGenerator();
        authService.register(registerDto);

        JSONObject loginDto = new JSONObject();
        loginDto.put("password", NEW_USER_PASSWORD);
        loginDto.put("username", USER_EMAIL);
        // Введен некорректный логин

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .content(loginDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testLogin_UnauthorizedException_IncorrectPassword() throws Exception {

        RegisterDto registerDto = ConstantGeneratorFotTest.registerDtoGenerator();
        authService.register(registerDto);

        JSONObject loginDto = new JSONObject();
        loginDto.put("password", USER_PASSWORD);
        // Введен некорректный пароль
        loginDto.put("username", NEW_USER_EMAIL);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .content(loginDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }
}
