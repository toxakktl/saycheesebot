package kz.ata.saycheese.service;

import kz.ata.saycheese.model.FoodModel;
import kz.ata.saycheese.repository.StorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StorageService {

    @Autowired
    private StorageRepository storageRepository;

    public List<FoodModel> findAll() {
        return storageRepository.findAll();
    }
}
