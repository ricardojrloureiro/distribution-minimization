package Models;

import java.awt.*;

public class Factory {

    private String name;
    private Point position;
    private Integer production;

    public Factory(String name, Point position, Integer prod) {
        this.name = name;
        this.position = position;
        this.production = prod;
    }

}
