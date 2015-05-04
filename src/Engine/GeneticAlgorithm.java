package Engine;

import Models.Chromosome;
import Models.Factory;
import Models.ServicePoint;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GeneticAlgorithm extends Thread {

    private final Integer elitistNumber;
    private ArrayList<Chromosome> chromosomes;
    private Boolean elitism;
    private double crossoverProbability;
    private double mutationProbability;
    private Integer maxProduction;
    Integer factoriesNumber;
    Integer servicePointsNumber;

    public GeneticAlgorithm(boolean elitism, Integer number,
                            double crossProb, double mutationProb){
    	factoriesNumber = 2;
    	servicePointsNumber = 5;
        this.chromosomes = generatePopulation(3);
        this.elitism = elitism;
        this.elitistNumber = number;
        this.crossoverProbability = crossProb;
        this.mutationProbability = mutationProb;
    }

    public void run() {

        
        boolean required=true;

        // while required to keep moving into next generations
        while(required) {
            ArrayList<Chromosome> newGeneration = chromosomes;
            
            //secures the best
            if(elitism) {
                for(int i=0;i<elitistNumber;i++) {
                    newGeneration.add(mostAdapted());
                }
            }

            // picking
            ArrayList<Chromosome> elementsToCross = picking();

            //doing crossover
            ArrayList<Chromosome> afterCrossover = crossover(elementsToCross);

            //adding the crossoved to the elitist
            for(int i=0;i<afterCrossover.size();i++) {
                newGeneration.add(afterCrossover.get(i));
            }

            //aplying mutation
            ArrayList<Chromosome> afterMutation = mutation(newGeneration);


            Partials.representSolution(afterMutation);
            required = false;

        }

    }

    /**
     * Function that will pick the chromosomes to pass to the next generation
     * @return an array list of the passing chromosomes.
     */
    private ArrayList<Chromosome> picking() {

        double generalAdaptability = 0;
        ArrayList<Double> chromosomeAdaptability = new ArrayList<Double>();

        for (int i = 0; i < chromosomes.size(); i++) {
            generalAdaptability += chromosomes.get(i).getAdaptability(maxProduction);
        }


        for(int i = 0;i < chromosomes.size();i++) {
            chromosomeAdaptability.add(chromosomes.get(i).getAdaptability(maxProduction)/generalAdaptability);
        }

        for(int i = 1; i < chromosomes.size(); i++) {
            chromosomeAdaptability.set(i, chromosomeAdaptability.get(i) + chromosomeAdaptability.get(i - 1));
        }

        // GENERATES A NUMBER BETWEEN 0-1 chromosomes.size() times.

        ArrayList<Double> randomNumbers = Partials.generateRandom(chromosomes.size());
        ArrayList<Chromosome> newOrder = new ArrayList<Chromosome>();

        for(int i=0;i<randomNumbers.size();i++) {
            double currentNumber = randomNumbers.get(i);
            for(int j=0;j<chromosomeAdaptability.size();j++) {
                if(currentNumber>chromosomeAdaptability.get(j)) {
                    newOrder.add(chromosomes.get(j));
                    continue;
                }
            }
        }

        return newOrder;
    }

    /**
     * Generates an initial population to start the problem
     * @return an array list of the current population
     */

    private ArrayList<Chromosome> generatePopulation(Integer populationNumber){
        ArrayList<Chromosome> population = new ArrayList<Chromosome>();
        System.out.println("Starting to generate population");

        for(int x=0;x<populationNumber;x++) {
            System.out.println("Start a specific chromosome");
            
            ArrayList<Factory> factories = new ArrayList<Factory>();
            Random r = new Random();
            for(int i=0;i<factoriesNumber;i++) {
            	
                Integer xCord = r.nextInt(10);
                Integer yCord = r.nextInt(10);
                Point coords = new Point(xCord,yCord);
                Integer production = r.nextInt(maxProduction)+1;

                Factory factory = new Factory("factory"+i,coords,production);
                factories.add(factory);
            }
            
            ArrayList<ServicePoint> servicePoints = new ArrayList<ServicePoint>();
            
            for(int i=0;i<servicePointsNumber;i++) {
                Integer requiredProduction = r.nextInt(maxProduction);
                Integer xCord = r.nextInt(10);
                Integer yCord = r.nextInt(10);
                Point coords = new Point(xCord,yCord);

                HashMap<Factory,Integer> prodReceived = new HashMap<Factory,Integer>();
                for(int j=0;j<factories.size();j++) {
                    prodReceived.put(
                      factories.get(j), r.nextInt(factories.get(j).getProduction())
                    );
                }

                ServicePoint servicePoint = new ServicePoint("servicepoint"+i,coords,requiredProduction,prodReceived);
                servicePoints.add(servicePoint);
            }

            Chromosome chrom = new Chromosome(servicePoints);
            chrom.createRepresentation();

            System.out.println("Generated a chromosome with the representation of:");
            System.out.println(chrom.getRepresentation());
            System.out.println("____________________");

            population.add(chrom);
        }

        System.out.println("\nFinal generation");
        for(int i=0;i<population.size();i++){
            System.out.println(population.get(i).getRepresentation());
        }
        return population;
    }

    private int getMaxProduction(Integer bitsRequired) {
        int maxValue=0;
        for(int i=0;i<bitsRequired;i++) 
        	maxValue += Math.pow(2, i);
        return maxValue;
    }

    /**
     * Cross the chosen chromosomes
     * @param chromosomes
     * @return the current crossoved population
     */
    private ArrayList<Chromosome> crossover(ArrayList<Chromosome> chromosomes) {

        ArrayList<Chromosome> chromosomesClone = new ArrayList<Chromosome>();
        for(int i=0;i<chromosomes.size();i++) {
            chromosomesClone.add(chromosomes.get(i));
        }

        ArrayList<Double> randomNumbers = Partials.generateRandom(chromosomes.size());

        ArrayList<Chromosome> toCross = new ArrayList<Chromosome>();

        for(int i=0;i<randomNumbers.size();i++){
            if(randomNumbers.get(i)<this.crossoverProbability) {
                toCross.add(chromosomes.get(i));
                chromosomesClone.remove(chromosomes.get(i));
            }
        }

        for(int i=1;i<toCross.size();i+=+2) {
                // adiciona os elementos cruzados 2 a 2
            ArrayList<Chromosome> crossed = officialCrossover(toCross.get(i),toCross.get(i-1));
            for(int j=0;j<crossed.size();j++) {
                chromosomesClone.add(crossed.get(j));
            }

            // adiciona o ultimo elemento caso seja impar
            
            if(toCross.get(i+2) == null && toCross.get(i+1) != null) {
                chromosomesClone.add(toCross.get(i+1));
            }
        }

        return chromosomesClone;
    }

    /**
     * Crosses two chromosomes
     * @param chrom1
     * @param chrom2
     * @return
     */
    private ArrayList<Chromosome> officialCrossover(Chromosome chrom1, Chromosome chrom2) {
        String representation1 = chrom1.getRepresentation();
        String representation2 = chrom2.getRepresentation();

        Random r = new Random();
        Integer crossBit = r.nextInt(representation1.length());

        String tempRep1FirstHalf,tempRep1SecondHalf;
        tempRep1FirstHalf = representation1.substring(0,crossBit);
        tempRep1SecondHalf = representation1.substring(crossBit,representation1.length());

        String tempRep2FirstHalf,tempRep2SecondHalf;
        tempRep2FirstHalf = representation2.substring(0,crossBit);
        tempRep2SecondHalf = representation2.substring(crossBit,representation1.length());

        String finalRep1, finalRep2;

        finalRep1 = tempRep1FirstHalf + tempRep2SecondHalf;
        finalRep2 = tempRep2FirstHalf + tempRep1SecondHalf;

        chrom1.setRepresentation(finalRep1);
        chrom2.setRepresentation(finalRep2);

        ArrayList<Chromosome> crossed = new ArrayList<Chromosome>();
        crossed.add(chrom1);crossed.add(chrom2);

        return crossed;
    }

    /**
     * generates a random mutation in order to improve the general population
     * @return the current mutated population
     * @param newGeneration
     */
    private ArrayList<Chromosome> mutation(ArrayList<Chromosome> newGeneration) {
        return new ArrayList<Chromosome>();
    }

    /**
     * Returns the most adapted chromosome of the current population
     * @return
     */
    private Chromosome mostAdapted() {
    	/*				TESTE
    	if (chromosomes.size() == 0) {
    		return null;
    	}*/
        Chromosome temp=chromosomes.get(0);
        for(int i=1;i<chromosomes.size();i++) {
            if(temp.getAdaptability(maxProduction)<chromosomes.get(i).getAdaptability(maxProduction)) {
                temp = chromosomes.get(i);
            }
        }
        chromosomes.remove(temp);
        return temp;
    }


    public double getGenerationAdaptability(ArrayList<Chromosome> currentGeneration) {
        double adapt=0;
        for(int i=0;i<currentGeneration.size();i++) {
            adapt+=currentGeneration.get(i).getAdaptability(maxProduction);
        }
        return adapt;
    }
}
