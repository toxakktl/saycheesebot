package kz.ata.saycheese.repository;

import kz.ata.saycheese.model.FoodModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<FoodModel, Long> {
}
