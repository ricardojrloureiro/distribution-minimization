package Models;

import java.util.ArrayList;

public class Chromosome {

    private ArrayList<ServicePoint> servicePoints;
    private String representation;

    public Chromosome(ArrayList<ServicePoint> serv) {

        this.servicePoints = serv;
        representation=createRepresentation();
    }

    /**
     * Gets the information for all the service points and gathers in one chromossome
     * @return the string created
     */
    public String createRepresentation() {

        String chromossome="";
        for(int i=0;i<servicePoints.size();i++){
            chromossome+=servicePoints.get(i).getBinaryRepresentation();
        }
        return chromossome;
    }

    public String getRepresentation() {
        return this.representation;
    }

    /**
     * @return the adaptability of this chromosome
     */
    public double getAdaptability() {

        return 0;
    }

    /**
     * @return the penalization of this chromosome
     */
    public double getPenalization() {

        return 0;
    }

    public void setRepresentation(String representation) {
        this.representation = representation;
    }
}
