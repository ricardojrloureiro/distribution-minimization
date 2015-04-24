package Models;


import java.awt.*;
import java.util.HashMap;

public class ServicePoint {

    private String name;
    private Point position;
    private Integer required;
    private HashMap<Factory,Integer> prodReceipts;


    public ServicePoint(String name, Point position, Integer required,HashMap<Factory,Integer> prod) {
        this.name = name;
        this.position = position;
        this.required = required;
        this.prodReceipts = prod;
    }

    /**
     * Depending of the quantity received between the different factories generates a specific string
     * @return the string of a specific service point
     */
    public String getBinaryRepresentation() {
        //TODO implementar a passagem do HashMap para binario (ciclo for no prodReceipts)
        return "101";
    }

}
