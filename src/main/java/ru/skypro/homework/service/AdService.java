package ru.skypro.homework.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.impl.SecurityServiceImpl;
import ru.skypro.homework.util.UploadImage;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.exception.NotFoundException;

/**
 * Сервис по работе с объявлениями
 */
public interface AdService {

    /**
     * Получение всех объявлений
     * Метод использует:
     * {@link AdRepository#findAll()}
     * {@link AdMapper#toDto(Ad)}
     * @return AdsDto - модель списка объявлений {@link AdsDto}
     */
    AdsDto getAds();

    /**
     * Создание объявления
     * Метод использует:
     * {@link SecurityServiceImpl#getAuthenticatedUserName()}
     * {@link UserService#getUserByEmailFromDb(String)}
     * {@link AdMapper#updateAdFromUpdateAdDto(CreateOrUpdateAdDto, Ad)}
     * {@link AdRepository#save(Object)}
     * {@link UploadImage#uploadImage(MultipartFile)}
     * {@link AdRepository#save(Object)}
     * {@link AdMapper#toDto(Ad)}
     * @param createAd - модель создания или обновления объявления
     * @param image - изображение (картинка) для объявления
     * @throws IOException - исключение, выбрасываемое при возникновении ошибки при загрузке файла
     * @return AdDTO - модель объявления {@link AdDto}
     */
    AdDto addAd(CreateOrUpdateAdDto createAd, MultipartFile image);

    /**
     * Получение полного объявления
     * Метод использует:
     * {@link AdService#findAdById(Long)}
     * {@link AdMapper#toExtendedDto(Ad)}
     * @param id - идентификатор объявления
     * @return ExtendedAdDto - модель полного объявления {@link ExtendedAdDto}
     */
    ExtendedAdDto getExtendedAd(Long id);

    /**
     * Удаление объявления
     * Метод использует:
     * {@link AdService#findAdById(Long)}
     * {@link java.nio.file.Path#of(String, String...)}
     * {@link java.nio.file.Files#deleteIfExists(Path)}
     * {@link AdRepository#deleteById(Object)}
     * @param id - идентификатор объявления
     */
    void removeAd(Long id) throws IOException;

    /**
     * Обновление информации об объявлении
     * Метод использует:
     * {@link AdService#findAdById(Long)} 
     * {@link AdMapper#updateAdFromUpdateAdDto(CreateOrUpdateAdDto, Ad)} 
     * {@link AdRepository#save(Object)}
     * {@link AdMapper#toDto(Ad)}
     * @param id - идентификатор объявления
     * @param ad - модель создания или обновления объявления
     * @return AdDTO - модель объявления {@link AdDto}
     */
    AdDto updateAd(Long id, CreateOrUpdateAdDto ad);

    /**
     * Получение объявлений авторизованного пользователя
     * Метод использует:
     * {@link SecurityServiceImpl#getAuthenticatedUserName()}
     * {@link UserService#getUserByEmailFromDb(String)}
     * {@link AdRepository#findAllByUserId(Long)}
     * {@link AdMapper#toDtos(List)}
     * @return AdsDto - модель списка объявлений {@link AdsDto}
     */
    AdsDto getAdsByAuthenticatedUser();

    /**
     * Обновление картинки объявления
     * Метод использует:
     * {@link AdService#findAdById(Long)}
     * {@link UploadImage#uploadImage(MultipartFile)}
     * {@link AdRepository#save(Object)}
     * {@link AdMapper#toDto(Ad)}
     * @param id - идентификатор объявления
     * @param file - загружаемая в файловую систему картинка для объявления
     * @return AdDto - модель объявления {@link AdDto}
     */
    AdDto updateImageAd(Long id, MultipartFile file);

    /**
     * Поиск объявления по идентификатору
     * Метод использует {@link AdRepository#findById(Object)}
     * @param id - идентификатор объявления
     * @throws NotFoundException - исключение выбрасывается, если объявление не найдено в базе данных
     * @return объявление (объект класса Ad)
     */
    Ad findAdById(Long id);

    /**
     * Проверка, является ли пользователь создателем объявления или администратором
     * Метод использует:
     * {@link AdService#findAdById(Long)}
     * {@link SecurityServiceImpl#getAuthenticatedUserName()}
     * {@link UserService#getUserByEmailFromDb(String)}
     * {@link Role#ADMIN
     * {@link Ad#getAuthor()}
     * {@link User#getEmail()}
     * @param adId - идентификатор объявления
     * @return true - если пользователь является создателем объявления или админом; false - в противоположном случае
     */
    boolean isAdCreatorOrAdmin(Long id);

}
