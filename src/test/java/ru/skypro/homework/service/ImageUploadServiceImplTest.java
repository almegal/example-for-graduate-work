package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.ImageUploadServiceImpl;
import ru.skypro.homework.service.impl.SecurityServiceImpl;
import ru.skypro.homework.service.impl.UserSecurityDetails;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static ru.skypro.homework.ConstantGeneratorFotTest.USER_IMAGE;

@ExtendWith(MockitoExtension.class)
public class ImageUploadServiceImplTest {


    @InjectMocks
    private ImageUploadServiceImpl imageUploadService;


    @Test
    void getImageByAdId() throws IOException {

        Path path = Path.of("images/" + USER_IMAGE);
        String name = USER_IMAGE.substring(0, USER_IMAGE.indexOf("."));
        String contentType = Files.probeContentType(path);
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile image = new MockMultipartFile(name, USER_IMAGE, contentType, content);

    }
}
