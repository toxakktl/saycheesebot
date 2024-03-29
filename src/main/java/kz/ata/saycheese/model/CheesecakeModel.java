package kz.ata.saycheese.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "cheesecakes")
@SequenceGenerator(name = "id_gen", sequenceName = "cheesecake_seq", initialValue = 1000, allocationSize = 1)
public class CheesecakeModel extends BaseModel {

    @Column(name = "name")
    private String name;

    @Column(name = "price_per_kg")
    private BigDecimal pricePerKg;

    @Column(name = "actual_price_per_kg")
    private BigDecimal actualPricePerKg;
//
//    @OneToMany(mappedBy = "cheesecake", fetch = FetchType.EAGER)
//    private Set<OrderModel> order;

    @OneToOne
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private RecipeModel recipe;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPricePerKg() {
        return pricePerKg;
    }

    public void setPricePerKg(BigDecimal pricePerKg) {
        this.pricePerKg = pricePerKg;
    }

    public BigDecimal getActualPricePerKg() {
        return actualPricePerKg;
    }

    public void setActualPricePerKg(BigDecimal actualPricePerKg) {
        this.actualPricePerKg = actualPricePerKg;
    }

    public RecipeModel getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeModel recipe) {
        this.recipe = recipe;
    }
}
