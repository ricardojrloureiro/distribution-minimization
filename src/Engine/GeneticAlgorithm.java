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

    public GeneticAlgorithm(boolean elitism, Integer number,
                            double crossProb, double mutationProb) {

        this.elitism = elitism;
        this.elitistNumber = number;
        this.crossoverProbability = crossProb;
        this.mutationProbability = mutationProb;
    }

    public void run() {
        generatePopulation();
        ArrayList<Chromosome> newGeneration = new ArrayList<Chromosome>();

        if (elitism) {
            newGeneration = mostAdapted(elitistNumber);
        }

        System.out.println("size#"+chromosomes.size());

        ArrayList<Chromosome> elementsToCross = picking();

        for(int i=0;i<elementsToCross.size();i++ ) {
            System.out.println(elementsToCross.get(i).getRepresentation());
        }

        //doing crossover
        ArrayList<Chromosome> afterCrossover = crossover(elementsToCross);

        System.out.println("Elistists: " + newGeneration);
        for (int i = 0; i < newGeneration.size(); i++) {
            System.out.println(i + ". " + newGeneration.get(i).getRepresentation());
        }

        System.out.println("After CrossOver: ");
        for (int i = 0; i < afterCrossover.size(); i++) {
            System.out.println(i + ". " + afterCrossover.get(i).getRepresentation());
        }

        //adding the crossoved to the elitist
        for (int i = 0; i < afterCrossover.size(); i++) {
            newGeneration.add(afterCrossover.get(i));
        }

        System.out.println("After Junction: ");
        for (int i = 0; i < newGeneration.size(); i++) {
            System.out.println(i + ". " + newGeneration.get(i).getRepresentation());
        }

        //applying mutation
         ArrayList<Chromosome> afterMutation = mutation(newGeneration);
        System.out.println("After Mutation: ");
        for (int i = 0; i < afterMutation.size(); i++) {
            System.out.println(i + ". " + afterMutation.get(i).getRepresentation());
        }

       // Partials.representSolution(afterMutation);
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
        // GENERATES A NUMBER BETWEEN 0-1 chromosomes.size() times.

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
     * Generates an initial population to start the problem
     *
     * @return an array list of the current population
     */

    private void generatePopulation() {

        Scanner in = new Scanner(System.in);
        System.out.print("Number of factories #");
        this.factoriesNumber = Integer.valueOf(in.nextLine());

        ArrayList<Factory> factories = new ArrayList<Factory>();
        for (int i = 0; i < factoriesNumber; i++) {
            System.out.print("cord X from factory #" + i + ":");
            Integer cordX = Integer.valueOf(in.nextLine());
            System.out.print("cord Y from factory #" + i + ":");
            Integer cordY = Integer.valueOf(in.nextLine());
            Point cords = new Point(cordX, cordY);

            System.out.print("Factory production of the current factory #");
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

        System.out.print("Number of service points #");
        this.servicePointsNumber = Integer.valueOf(in.nextLine());

        ArrayList<ServicePoint> servicePoints = new ArrayList<ServicePoint>();
        for (int i = 0; i < servicePointsNumber; i++) {
            System.out.print("cord X from service point #" + i + ":");
            Integer cordX = Integer.valueOf(in.nextLine());
            System.out.print("cord Y from service point #" + i + ":");
            Integer cordY = Integer.valueOf(in.nextLine());
            Point cords = new Point(cordX, cordY);

            System.out.print("Service point requirement of the current service point #");
            Integer value = Integer.valueOf(in.nextLine());

            ServicePoint servicePoint = new ServicePoint("service" + i, cords, value);
            servicePoint.setFactories(factories);
            servicePoints.add(servicePoint);
        }

        System.out.print("How many chromosome ?#");
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

        for (int i = 0; i < this.chromosomes.size(); i++) {
            System.out.println(this.chromosomes.get(i).getRepresentation() + " -> " + this.chromosomes.get(i).getAdaptability(maxProduction));
        }

    }

    /**
     * Cross the chosen chromosomes
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

        System.out.println("MR Random: " + randomNumbers);

        ArrayList<Chromosome> toCross = new ArrayList<Chromosome>();

        for (int i = 0; i < randomNumbers.size(); i++) {
            if (randomNumbers.get(i) < this.crossoverProbability) {
                toCross.add(chromosomes.get(i));
                chromosomesClone.remove(chromosomes.get(i));
            }
        }

        for (int i = 0; i < chromosomesClone.size(); i++) {
            System.out.println(i + " chromosomesClone: " + chromosomesClone.get(i).getRepresentation());
        }

        for (int i = 0; i < toCross.size(); i++) {
            System.out.println(i + " To cross: " + toCross.get(i).getRepresentation());
        }

        Random r = new Random();
        Integer crossBit = r.nextInt(chromosomes.get(0).getRepresentation().length() - 1) + 1;
        for (int i = 1; i < toCross.size(); i += +2) {
            // adiciona os elementos cruzados 2 a 2
            ArrayList<Chromosome> crossed = officialCrossover(toCross.get(i), toCross.get(i - 1), crossBit);
            for (int j = 0; j < crossed.size(); j++) {
                chromosomesClone.add(crossed.get(j));
            }

            // adiciona o ultimo elemento caso seja impar

            try {
                if (toCross.get(i + 2) == null) {
                    chromosomesClone.add(toCross.get(i + 1));
                    System.out.println("ES HERE");
                }
            } catch(IndexOutOfBoundsException e) {
                System.out.println("NO ES HERE");
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
    private ArrayList<Chromosome> officialCrossover(Chromosome chrom1, Chromosome chrom2, Integer crossBit) {
        String representation1 = chrom1.getRepresentation();
        String representation2 = chrom2.getRepresentation();


        System.out.println("Random n: " + crossBit.intValue());

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
     * generates a random mutation in order to improve the general population
     *
     * @param newGeneration
     * @return the current mutated population
     */
    private ArrayList<Chromosome> mutation(ArrayList<Chromosome> newGeneration) {
        Random rand = new Random();
        ArrayList<Double> mutationProb = new ArrayList<Double>();
        mutationProb = Partials.generateRandom(newGeneration.size() * newGeneration.get(0).getRepresentation().length());

        for(int i = 0; i < newGeneration.size(); i++) {
            for (int j = 0; j < newGeneration.get(i).getRepresentation().length(); j++) {
                if (mutationProb.get(((i*newGeneration.get(i).getRepresentation().length())+j)) < mutationProbability) {
                    String newRepresentation = newGeneration.get(i).getRepresentation().substring(0, j) +
                            (newGeneration.get(i).getRepresentation().charAt(j)+1)%2 +
                            newGeneration.get(i).getRepresentation().substring(j+1, newGeneration.get(i).getRepresentation().length());
                    newGeneration.get(i).setRepresentation(newRepresentation);
                }
            }
        }
        return newGeneration;
    }

    /**
     * Returns the most adapted chromosome of the current population
     *
     * @return
     */
    private ArrayList<Chromosome> mostAdapted(Integer elitistNumber) {

        ArrayList<Chromosome> tempChromosomes= new ArrayList<Chromosome>();
        ArrayList<Chromosome> elitChromosomes= new ArrayList<Chromosome>();

        for(int i=0;i<this.chromosomes.size();i++) {
            tempChromosomes.add(this.chromosomes.get(i));
        }

        for(int i=0;i<elitistNumber;i++) {
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
