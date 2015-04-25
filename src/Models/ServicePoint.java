package Models;


import Engine.Partials;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
        String representation = "";

        Iterator it = prodReceipts.entrySet().iterator();
        Integer temp=0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            Factory tempFactory = (Factory) pair.getKey();
            if(tempFactory.getProduction()>temp) {
                temp = tempFactory.getProduction();
            }
            it.remove(); // avoids a ConcurrentModificationException
        }

        Integer size = Integer.toBinaryString(temp).length();

        Iterator it2 = prodReceipts.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry pair = (Map.Entry)it2.next();

            Integer tempProduction = (Integer) pair.getValue();
            representation+= Partials.integerModified(tempProduction,size);

            it2.remove(); // avoids a ConcurrentModificationException
        }

        return representation;
    }
}
