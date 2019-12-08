package kz.ata.saycheese.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ingredients")
@SequenceGenerator(name = "id_gen", sequenceName = "ingredient_seq", initialValue = 1000, allocationSize = 1)
public class IngredientModel extends BaseModel {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL)
    private Set<RecipeIngredientModel> recipesAssoc = new HashSet<>();

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

    public Set<RecipeIngredientModel> getRecipesAssoc() {
        return recipesAssoc;
    }

    public void setRecipesAssoc(Set<RecipeIngredientModel> recipesAssoc) {
        this.recipesAssoc = recipesAssoc;
    }
}
