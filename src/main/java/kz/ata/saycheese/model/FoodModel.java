package kz.ata.saycheese.model;

import kz.ata.saycheese.enums.Unit;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "food")
@SequenceGenerator(name = "id_gen", sequenceName = "food_seq", initialValue = 1000, allocationSize = 1)
public class FoodModel extends BaseModel {

    @Column(name = "name")
    private String name;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "unit")
    @Enumerated(EnumType.STRING)
    private Unit unit;

    @OneToOne(mappedBy = "food")
    private IngredientModel ingredient;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public IngredientModel getIngredient() {
        return ingredient;
    }

    public void setIngredient(IngredientModel ingredient) {
        this.ingredient = ingredient;
    }
}
