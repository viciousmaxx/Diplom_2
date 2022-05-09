package diplom_2.orders;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Order {
    public ArrayList<String> ingredients;

    public Order(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public Order() {
    }
}
