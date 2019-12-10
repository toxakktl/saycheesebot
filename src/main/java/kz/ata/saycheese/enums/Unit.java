package kz.ata.saycheese.enums;

public enum Unit {
    KG("кг"),
    G("г"),
    ML("мл"),
    L("л");

    private String value;

    Unit(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
