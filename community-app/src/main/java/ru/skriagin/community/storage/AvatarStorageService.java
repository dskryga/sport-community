package ru.skriagin.community.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface AvatarStorageService {

    String store(Long userId, MultipartFile file);

    Resource load(String key);

    void delete(String key);
}
