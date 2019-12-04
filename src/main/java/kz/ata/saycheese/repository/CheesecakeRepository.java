package kz.ata.saycheese.repository;

import kz.ata.saycheese.model.CheesecakeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CheesecakeRepository extends JpaRepository<CheesecakeModel, Long> {
}
