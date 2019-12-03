package kz.ata.saycheese.service;

import kz.ata.saycheese.model.FoodModel;
import kz.ata.saycheese.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    public List<FoodModel> findAllFood(){
        return foodRepository.findAll();
    }
}
