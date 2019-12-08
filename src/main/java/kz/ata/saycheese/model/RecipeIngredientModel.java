package kz.ata.saycheese.model;

import kz.ata.saycheese.enums.Unit;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "recipe_ingredients")
public class RecipeIngredientModel implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private RecipeModel recipe;

    @Id
    @ManyToOne
    @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
    private IngredientModel ingredient;

    @Column(name = "quantity")
    private BigDecimal quantiy;

    @Column(name = "unit")
    @Enumerated(EnumType.STRING)
    private Unit unit;

    public RecipeModel getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeModel recipe) {
        this.recipe = recipe;
    }

    public IngredientModel getIngredient() {
        return ingredient;
    }

    public void setIngredient(IngredientModel ingredient) {
        this.ingredient = ingredient;
    }

    public BigDecimal getQuantiy() {
        return quantiy;
    }

    public void setQuantiy(BigDecimal quantiy) {
        this.quantiy = quantiy;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
