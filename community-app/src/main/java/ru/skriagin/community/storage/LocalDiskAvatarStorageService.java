package ru.skriagin.community.storage;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skriagin.community.exception.EntityNotFoundException;
import ru.skriagin.community.exception.InvalidFileException;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class LocalDiskAvatarStorageService implements AvatarStorageService {

    private static final Map<String, String> ALLOWED_CONTENT_TYPES = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp"
    );

    private final Path baseDir;

    public LocalDiskAvatarStorageService(@Value("${app.storage.avatars.base-dir}") String baseDir) {
        this.baseDir = Path.of(baseDir).toAbsolutePath().normalize();
    }

    @PostConstruct
    void init() {
        try {
            Files.createDirectories(baseDir);
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось создать директорию для аватаров: " + baseDir, e);
        }
    }

    @Override
    public String store(Long userId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("Файл аватара не может быть пустым");
        }

        String extension = ALLOWED_CONTENT_TYPES.get(file.getContentType());
        if (extension == null) {
            throw new InvalidFileException("Недопустимый тип файла: " + file.getContentType()
                    + ". Разрешены: " + ALLOWED_CONTENT_TYPES.keySet());
        }

        String key = UUID.randomUUID() + extension;
        Path target = resolveWithinBaseDir(key);

        try {
            file.transferTo(target);
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось сохранить файл аватара", e);
        }

        return key;
    }

    @Override
    public Resource load(String key) {
        Path path = resolveWithinBaseDir(key);
        if (!Files.isRegularFile(path)) {
            throw new EntityNotFoundException("Файл аватара не найден");
        }

        try {
            return new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void delete(String key) {
        try {
            Files.deleteIfExists(resolveWithinBaseDir(key));
        } catch (IOException e) {
            log.warn("Не удалось удалить файл аватара с ключом {}: {}", key, e.getMessage());
        }
    }

    private Path resolveWithinBaseDir(String key) {
        Path resolved = baseDir.resolve(key).normalize();
        if (!resolved.startsWith(baseDir)) {
            throw new InvalidFileException("Некорректный ключ файла");
        }
        return resolved;
    }
}
