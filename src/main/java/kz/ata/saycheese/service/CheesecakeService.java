package kz.ata.saycheese.service;

import kz.ata.saycheese.model.CheesecakeModel;
import kz.ata.saycheese.repository.CheesecakeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheesecakeService {

    @Autowired
    private CheesecakeRepository cheesecakeRepository;

    public List<CheesecakeModel> findAll(){
        return cheesecakeRepository.findAll();
    }
}
