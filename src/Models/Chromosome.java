package Models;
import java.util.ArrayList;

public class Chromosome {

	private ArrayList<ServicePoint> servicePoints;
	private String representation;
	private Integer maxProduction;

	public Chromosome(ArrayList<ServicePoint> serv, Integer maxProduction) {

		this.servicePoints = serv;
		this.representation = createRepresentation(maxProduction);
		this.maxProduction = maxProduction;
	}

	public Integer getMaxProduction() {
		return maxProduction;
	}

	public ArrayList<ServicePoint> getServicePoints() {
		return servicePoints;
	}

	public String getRepresentation() {
		return this.representation;
	}

	public void setRepresentation(String representation) {
		this.representation = representation;
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


	/**
	 * Get Chromossome's Penalty
	 * @param maxRepresentation
	 * @return
	 */
	public double getPenalty(int maxRepresentation) {
		int sendingSurplus = 0;
		int numberOfServicePoints = servicePoints.size();
		double receives = 0;
		String servicePointString;
		String factoryString;
		for (int i = 0; i < numberOfServicePoints; i++) {
			servicePointString = (this.representation).substring(i * (servicePoints.get(i).getFactories().size() * maxRepresentation), (i + 1) * (servicePoints.get(i).getFactories().size() * maxRepresentation));
			for (int j = 0; j < servicePoints.get(i).getFactories().size(); j++) {
				factoryString = servicePointString.substring(j * maxRepresentation, (j + 1) * maxRepresentation);
				if(binaryToInteger(factoryString) > servicePoints.get(i).getFactories().get(j).getProduction()) {
					sendingSurplus += binaryToInteger(factoryString) - servicePoints.get(i).getFactories().get(j).getProduction();
				}
				receives += binaryToInteger(factoryString);
			}
			receives -= servicePoints.get(i).getRequired();
		}
		receives = Math.abs(receives);

		if (sendingSurplus < 0) {
			sendingSurplus = 0;
		}

		return 2*(receives + sendingSurplus);
	}

	/**
	 * Return the Chromossome's fitness
	 * 
	 * @param maxRepresentation
	 * @return
	 */
	public double getAdaptability(int maxRepresentation) {
		double distance = 0;
		String servicePointString;
		String factoryString;
		int numberOfServicePoints = servicePoints.size();
		Boolean distanceExists = false;
		double penalty = getPenalty(maxRepresentation);

		for (int i = 0; i < numberOfServicePoints; i++) {
			servicePointString = (this.representation).substring(i * (servicePoints.get(i).getFactories().size() * maxRepresentation), (i + 1) * (servicePoints.get(i).getFactories().size() * maxRepresentation));
			for (int j = 0; j < servicePoints.get(i).getFactories().size(); j++) {
				factoryString = servicePointString.substring(j * maxRepresentation, (j + 1) * maxRepresentation);
				if (binaryToInteger(factoryString) != 0) {
					distance += servicePoints.get(i).distanceToFactory(servicePoints.get(i).getFactories().get(j));
					distanceExists = true;
				}
			}
		}

		if (penalty == 0 && distanceExists) {
			return 1/distance;
		} else if (!distanceExists) {
			return 1/penalty;
		}

		return 1/(distance + penalty);
	}

	/**
	 * Converts Binary to its Integer representation
	 * 
	 * @param binary
	 * @return
	 */
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

	/**
	 * Prints the factories information on the console
	 * 
	 * @param numFactories
	 * @param numServicePoints
	 * @param factories
	 */
	public void printFactories(int numFactories, int numServicePoints, ArrayList<Factory> factories) {
		int servicePoint = (this.getRepresentation().length())/numServicePoints;
		int factory = servicePoint/numFactories;
		for(int j = 0; j<numServicePoints; j++) {
			String tempString1 = this.getRepresentation().substring(j*servicePoint, (j+1)*servicePoint);
			System.out.println("  Service Point #" + j + ":");
			for (int i = 0; i < numFactories; i++) {
				String tempString2 = tempString1.substring(i*factory, (i+1)*factory);
				if (binaryToInteger(this.getRepresentation()) > 0) {
					System.out.println("  Factory #" + i + ": " + "Position [" + factories.get(i).getPosition().getX() + ", " + factories.get(i).getPosition().getY() + "], sends: " + binaryToInteger(tempString2));
				}
			}
		}
	}


}


