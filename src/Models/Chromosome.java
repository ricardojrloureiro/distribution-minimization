package Models;

import java.util.ArrayList;

public class Chromosome {

    private ArrayList<ServicePoint> servicePoints;

    public Chromosome(ArrayList<ServicePoint> serv) {
        this.servicePoints = serv;
    }

    /**
     * Gets the information for all the service points and gathers in one chromossome
     * @return the string created
     */
    public String getChromossomeRepresentation() {

        String chromossome="";
        for(int i=0;i<servicePoints.size();i++){
            chromossome+=servicePoints.get(i).getBinaryRepresentation();
        }
        return chromossome;
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


}
