package Models;

import java.awt.font.NumericShaper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


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
	public double getAdaptability(int maxRepresentation) {
		int numberOfServicePoints = servicePoints.size();
		double adapt = 0;
		String servicePointString;
		String factoryString;
		for (int i = 0; i < numberOfServicePoints; i++) {
			servicePointString = (this.representation).substring(i*numberOfServicePoints,(i+1)*numberOfServicePoints);
			for (int j = 0; j < maxRepresentation; j++) {
				factoryString = servicePointString.substring(j*maxRepresentation, (j+1)*maxRepresentation);
				adapt += binaryToInteger(factoryString);
			}
			adapt -= servicePoints.get(i).getRequired();
		}
		return adapt;
	}

	/**
	 * @return the penalization of this chromosome
	 */
	public double getPenalization() {
		double distance = 0;
		
		for(int i = 0; i < servicePoints.size(); i++) {
			HashMap factories = servicePoints.get(i).getProdReceipts();
			Iterator it = (Iterator) factories.entrySet().iterator();
			
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				Factory factory = (Factory) pair.getKey();
				
				if ((double) ((Integer) pair.getValue()).intValue() != 0) {
					distance += servicePoints.get(i).distanceToFactory(factory);
				}
			}
		}
		return distance;
	}

	public void setRepresentation(String representation) {
		this.representation = representation;
	}
	
	public int binaryToInteger(String binary) {
	    char[] numbers = binary.toCharArray();
	    int result = 0;
	    for (int i=numbers.length; i==0; i--) {
	        if (numbers[i]=='1') {
	          result += (numbers.length-i+1)*2;
	        }
	    }
	    return result;
	}
}


