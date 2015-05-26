package Engine;

import Models.Chromosome;
import Models.Factory;
import Models.ServicePoint;

import java.awt.*;
import java.util.*;

public class GeneticAlgorithm extends Thread {

	private final Integer elitistNumber;
	private ArrayList<Chromosome> chromosomes = new ArrayList<Chromosome>();
	private Boolean elitism;
	private double crossoverProbability;
	private double mutationProbability;
	Integer factoriesNumber;
	Integer servicePointsNumber;
	Integer maxProduction;
	Integer generationsNumber;
	ArrayList<Factory> factories;
	ArrayList<ServicePoint> servicePoints;

	public GeneticAlgorithm(boolean elitism, Integer number, double crossProb, double mutationProb) {

		this.elitism = elitism;
		this.elitistNumber = number;
		this.crossoverProbability = crossProb;
		this.mutationProbability = mutationProb;
	}

	public void run() {

		generatePopulation();
		for (int n = 0; n < generationsNumber; n++) {
			ArrayList<Chromosome> newGeneration = new ArrayList<Chromosome>();

			if (elitism) {
				newGeneration = mostAdapted(elitistNumber);
			}

			ArrayList<Chromosome> elementsToCross = picking();


			//doing crossover
			if (elementsToCross.size() != 0) {
				ArrayList<Chromosome> afterCrossover = crossover(elementsToCross);

				//adding the crossoved to the elitist
				for (int i = 0; i < afterCrossover.size(); i++) {
					newGeneration.add(afterCrossover.get(i));
				}
			}

			//applying mutation
			this.chromosomes = mutation(newGeneration);

			Partials.representSolution(this.chromosomes, factories, servicePoints, maxProduction);
		}
	}

	/**
	 * Function that will pick the chromosomes to pass to the next generation
	 *
	 * @return an array list of the passing chromosomes.
	 */
	private ArrayList<Chromosome> picking() {

		double generalAdaptability = 0;
		ArrayList<Double> chromosomeAdaptability = new ArrayList<Double>();


		for (int i = 0; i < chromosomes.size(); i++) {
			generalAdaptability += chromosomes.get(i).getAdaptability(maxProduction);
		}

		for (int i = 0; i < chromosomes.size(); i++) {
			chromosomeAdaptability.add(chromosomes.get(i).getAdaptability(maxProduction) / generalAdaptability);
		}

		for (int i = 1; i < chromosomes.size(); i++) {
			chromosomeAdaptability.set(i, chromosomeAdaptability.get(i) + chromosomeAdaptability.get(i - 1));
		}

		// Generates a number between 0-1 for each chromossome.
		ArrayList<Double> randomNumbers = Partials.generateRandom(chromosomes.size() - elitistNumber);

		ArrayList<Chromosome> newOrder = new ArrayList<Chromosome>();

		for (int i = 0; i < randomNumbers.size(); i++) {
			double currentNumber = randomNumbers.get(i);
			for (int j = 0; j < chromosomeAdaptability.size(); j++) {
				if (currentNumber < chromosomeAdaptability.get(j)) {
					newOrder.add(chromosomes.get(j));
					break;
				}
			}
		}

		return newOrder;
	}

	/**
	 * Generates an initial population to start the Algorithm
	 *
	 * @return an array list of the current population
	 */

	private void generatePopulation() {

		Scanner in = new Scanner(System.in);
		System.out.print("Number of factories: ");
		this.factoriesNumber = Integer.valueOf(in.nextLine());

		factories = new ArrayList<Factory>();
		for (int i = 0; i < factoriesNumber; i++) {
			System.out.print("cord X from factory #" + i + ": ");
			Integer cordX = Integer.valueOf(in.nextLine());
			System.out.print("cord Y from factory #" + i + ": ");
			Integer cordY = Integer.valueOf(in.nextLine());
			Point cords = new Point(cordX, cordY);

			System.out.print("Factory production of the current factory: ");
			Integer value = Integer.valueOf(in.nextLine());

			Factory factory = new Factory("factory" + i, cords, value);
			factories.add(factory);
		}

		Integer maxValue = 0;
		for (int i = 0; i < factories.size(); i++) {
			if (factories.get(i).getProduction() > maxValue) {
				maxValue = factories.get(i).getProduction();
			}
		}
		String representation = Integer.toBinaryString(maxValue);
		maxProduction = representation.length();

		System.out.print("Number of service points: ");
		this.servicePointsNumber = Integer.valueOf(in.nextLine());

		servicePoints = new ArrayList<ServicePoint>();
		for (int i = 0; i < servicePointsNumber; i++) {
			System.out.print("cord X from service point #" + i + ": ");
			Integer cordX = Integer.valueOf(in.nextLine());
			System.out.print("cord Y from service point #" + i + ": ");
			Integer cordY = Integer.valueOf(in.nextLine());
			Point cords = new Point(cordX, cordY);

			System.out.print("Required quantity for the current service point: ");
			Integer value = Integer.valueOf(in.nextLine());

			ServicePoint servicePoint = new ServicePoint("service" + i, cords, value);
			servicePoint.setFactories(factories);
			servicePoints.add(servicePoint);
		}

		System.out.print("Number of Chromossomes: ");
		Integer chromosomes = Integer.valueOf(in.nextLine());
		Random r = new Random();
		for (int i = 0; i < chromosomes; i++) {
			for (int j = 0; j < servicePoints.size(); j++) {
				HashMap<Factory, Integer> prod = new HashMap<Factory, Integer>();
				for (int n = 0; n < factories.size(); n++) {
					prod.put(factories.get(n), r.nextInt(factories.get(n).getProduction()));
				}
				servicePoints.get(j).setProdReceipts(prod);
			}
			Chromosome current = new Chromosome(servicePoints, maxProduction);
			current.createRepresentation(maxProduction);
			this.chromosomes.add(current);
		}

		System.out.print("How many generations you want? ");
		generationsNumber = Integer.valueOf(in.nextLine());
	}

	/**
	 * Sends the chosen chromosomes to crossing function
	 *
	 * @param chromosomes
	 * @return the current crossoved population
	 */
	private ArrayList<Chromosome> crossover(ArrayList<Chromosome> chromosomes) {

		ArrayList<Chromosome> chromosomesClone = new ArrayList<Chromosome>();
		for (int i = 0; i < chromosomes.size(); i++) {
			chromosomesClone.add(chromosomes.get(i));
		}

		ArrayList<Double> randomNumbers = Partials.generateRandom(chromosomes.size());

		ArrayList<Chromosome> toCross = new ArrayList<Chromosome>();

		for (int i = 0; i < randomNumbers.size(); i++) {
			if (randomNumbers.get(i) < this.crossoverProbability) {
				toCross.add(chromosomes.get(i));
				chromosomesClone.remove(chromosomes.get(i));
			}
		}

		for (int i = 0; i < toCross.size(); ) {
			if (toCross.size() == 1) {
				//For uneven number of Chromossomes ads the last on without crossing
				chromosomesClone.add(toCross.get(0));
				toCross.remove(0);
			} else {
				//Crosses 2 Chromossomes
				ArrayList<Chromosome> crossed = officialCrossover(toCross.get(i), toCross.get(i + 1));
				for (int j = 0; j < crossed.size(); j++) {
					chromosomesClone.add(crossed.get(j));
				}
				toCross.remove(0);
				toCross.remove(0);
			}
		}

		return chromosomesClone;
	}

	/**
	 * Crosses two chromosomes
	 *
	 * @param chrom1
	 * @param chrom2
	 * @return
	 */
	private ArrayList<Chromosome> officialCrossover(Chromosome chrom1, Chromosome chrom2) {
		Random r = new Random();
		Integer crossBit = r.nextInt(chromosomes.get(0).getRepresentation().length() - 1) + 1;
		String representation1 = chrom1.getRepresentation();
		String representation2 = chrom2.getRepresentation();

		String tempRep1FirstHalf, tempRep1SecondHalf;
		tempRep1FirstHalf = representation1.substring(0, crossBit);
		tempRep1SecondHalf = representation1.substring(crossBit, representation1.length());

		String tempRep2FirstHalf, tempRep2SecondHalf;
		tempRep2FirstHalf = representation2.substring(0, crossBit);
		tempRep2SecondHalf = representation2.substring(crossBit, representation1.length());

		String finalRep1, finalRep2;

		finalRep1 = tempRep1FirstHalf + tempRep2SecondHalf;
		finalRep2 = tempRep2FirstHalf + tempRep1SecondHalf;

		chrom1.setRepresentation(finalRep1);
		chrom2.setRepresentation(finalRep2);

		ArrayList<Chromosome> crossed = new ArrayList<Chromosome>();
		crossed.add(chrom1);
		crossed.add(chrom2);

		return crossed;
	}

	/**
	 * generates a random mutation to the Population of Chromossomes
	 *
	 * @param newGeneration
	 * @return the current mutated population
	 */
	private ArrayList<Chromosome> mutation(ArrayList<Chromosome> newGeneration) {
		ArrayList<Chromosome> afterMutation = new ArrayList<Chromosome>();
		ArrayList<Double> mutationProb = new ArrayList<Double>();
		mutationProb = Partials.generateRandom(newGeneration.size() * newGeneration.get(0).getRepresentation().length());

		for (int i = 0; i < newGeneration.size(); i++) {
			Chromosome temp = new Chromosome(newGeneration.get(i).getServicePoints(), newGeneration.get(i).getMaxProduction());
			temp.setRepresentation(newGeneration.get(i).getRepresentation());
			afterMutation.add(temp);

		}

		for (int i = 0; i < mutationProb.size(); i++) {
			if (mutationProb.get(i) < mutationProbability) {
				Integer chromosomeLength = afterMutation.get(0).getRepresentation().length();
				Integer indexChromosome = (i / chromosomeLength);
				Integer indexMutation = i % chromosomeLength;

				String newRepresentation = "";
				String oldRepresentation = afterMutation.get(indexChromosome).getRepresentation();
				newRepresentation = oldRepresentation.substring(0, indexMutation);
				newRepresentation += (oldRepresentation.charAt(indexMutation) + 1) % 2;
				newRepresentation += oldRepresentation.substring(indexMutation + 1, chromosomeLength);

				afterMutation.get(indexChromosome).setRepresentation(newRepresentation);
				
				/*System.out.println("Mutation in bit:" + indexMutation + " in the chromosome #" + indexChromosome);
				System.out.println("Old representation: " + oldRepresentation + " new representation: " + newRepresentation);*/
			}
		}
		return afterMutation;
	}

	/**
	 * Returns the most adapted chromosome(s) of the current population
	 *
	 * @return
	 */
	private ArrayList<Chromosome> mostAdapted(Integer elitistNumber) {

		ArrayList<Chromosome> tempChromosomes = new ArrayList<Chromosome>();
		ArrayList<Chromosome> elitChromosomes = new ArrayList<Chromosome>();

		for (int i = 0; i < this.chromosomes.size(); i++) {
			tempChromosomes.add(this.chromosomes.get(i));
		}

		for (int i = 0; i < elitistNumber; i++) {
			Chromosome temp = tempChromosomes.get(0);
			for (int j = 1; j < tempChromosomes.size(); j++) {
				if (temp.getAdaptability(maxProduction) <= tempChromosomes.get(j).getAdaptability(maxProduction)) {
					temp = tempChromosomes.get(j);
				}
			}
			tempChromosomes.remove(temp);
			elitChromosomes.add(temp);
		}
		return elitChromosomes;
	}
}
