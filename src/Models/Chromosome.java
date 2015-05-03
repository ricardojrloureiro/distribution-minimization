package Models;

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
	public double getAdaptability() {
		double adapt = 0;

		for(int i = 0; i < servicePoints.size(); i++) {
			System.out.println("\n\n" + servicePoints.get(i).getName() +  ": \n");
			HashMap factories = servicePoints.get(i).getProdReceipts();
			Iterator it = (Iterator) factories.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				System.out.println(((Factory) pair.getKey()).getName() + " = " + pair.getValue());
				adapt += (double) ((Integer) pair.getValue()).intValue();
			}
			adapt -= servicePoints.get(i).getRequired();
		}
		adapt -= getPenalization();
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
		
		return Math.pow(distance,2);
	}

	public void setRepresentation(String representation) {
		this.representation = representation;
	}
}
