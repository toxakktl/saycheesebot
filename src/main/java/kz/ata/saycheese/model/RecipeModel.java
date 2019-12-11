package kz.ata.saycheese.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "recipes")
@SequenceGenerator(name = "id_gen", sequenceName = "recipe_seq", initialValue = 1000, allocationSize = 1)
public class RecipeModel extends BaseModel {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToOne(mappedBy = "recipe")
    private CheesecakeModel cheesecake;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<RecipeIngredientModel> ingredients;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<RecipeIngredientModel> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<RecipeIngredientModel> ingredients) {
        this.ingredients = ingredients;
    }

    public CheesecakeModel getCheesecake() {
        return cheesecake;
    }

    public void setCheesecake(CheesecakeModel cheesecake) {
        this.cheesecake = cheesecake;
    }
}
