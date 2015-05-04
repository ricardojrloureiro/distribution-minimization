package Models;

import java.awt.font.NumericShaper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Chromosome {

    private ArrayList<ServicePoint> servicePoints;
    private String representation;

    public Chromosome(ArrayList<ServicePoint> serv, Integer maxProduction) {

        this.servicePoints = serv;
        representation = createRepresentation(maxProduction);
    }

    /**
     * Gets the information for all the service points and gathers in one chromossome
     *
     * @return the string created
     */
    public String createRepresentation(Integer representationNumber) {

        String chromossome = "";
        for (int i = 0; i < servicePoints.size(); i++) {
            chromossome += servicePoints.get(i).getBinaryRepresentation(representationNumber);
        }
        return chromossome;
    }

    public String getRepresentation() {
        return this.representation;
    }

    /**
     * @return the adaptability of this chromosome
     */
    public double getPenalization(int maxRepresentation) {
        int numberOfServicePoints = servicePoints.size();
        double adapt = 0;
        String servicePointString;
        String factoryString;
        for (int i = 0; i < numberOfServicePoints; i++) {
            servicePointString = (this.representation).substring(i * (servicePoints.get(i).getFactories().size() * maxRepresentation), (i + 1) * (servicePoints.get(i).getFactories().size() * maxRepresentation));
            for (int j = 0; j < servicePoints.get(i).getFactories().size(); j++) {
                factoryString = servicePointString.substring(j * maxRepresentation, (j + 1) * maxRepresentation);
                adapt += binaryToInteger(factoryString);
            }
            adapt -= servicePoints.get(i).getRequired();
        }
        return adapt - getPenalization(maxRepresentation);
    }

    /**
     * @return the penalization of this chromosome
     */
    public double getAdaptability(int maxRepresentation) {
        double distance = 0;
        String servicePointString;
        String factoryString;
        int numberOfServicePoints = servicePoints.size();

        for (int i = 0; i < numberOfServicePoints; i++) {
            servicePointString = (this.representation).substring(i * (servicePoints.get(i).getFactories().size() * maxRepresentation), (i + 1) * (servicePoints.get(i).getFactories().size() * maxRepresentation));
            for (int j = 0; j < servicePoints.get(i).getFactories().size(); j++) {
                factoryString = servicePointString.substring(j * maxRepresentation, (j + 1) * maxRepresentation);
                if (binaryToInteger(factoryString) != 0) {
                    distance += servicePoints.get(i).distanceToFactory(servicePoints.get(i).getFactories().get(j));
                }
            }
        }
        if (distance == 0) {
            return 0;
        }
        return 1/distance;
    }


    public void setRepresentation(String representation) {
        this.representation = representation;
    }

    public int binaryToInteger(String binary) {
        char[] numbers = binary.toCharArray();
        int result = 0;
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] == '1') {
                result += Math.pow(2,numbers.length-i-1);
            }
        }
        return result;
    }
}


