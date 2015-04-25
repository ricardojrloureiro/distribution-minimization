package Engine;

import Models.Chromosome;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class GeneticAlgorithm extends Thread {

    private final Integer elitistNumber;
    private ArrayList<Chromosome> chromosomes;
    private Boolean elitism;
    private double crossoverProbability;
    private double mutationProbability;

    public GeneticAlgorithm(boolean elitism, Integer number,
                            double crossProb, double mutationProb){
        this.chromosomes = generatePopulation();
        this.elitism = elitism;
        this.elitistNumber = number;
        this.crossoverProbability = crossProb;
        this.mutationProbability = mutationProb;
    }

    public void run() {

        boolean required=true;

        // while required to keep moving into next generations
        while(required) {
            ArrayList<Chromosome> newGeneration = new ArrayList<Chromosome>();

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
            generalAdaptability += chromosomes.get(i).getAdaptability();
        }


        for(int i = 0;i < chromosomes.size();i++) {
            chromosomeAdaptability.add(chromosomes.get(i).getAdaptability()/generalAdaptability);
        }

        for(int i = 1; i < chromosomes.size(); i++) {
            chromosomeAdaptability.set(i,chromosomeAdaptability.get(i) +  chromosomeAdaptability.get(i-1));
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

    private ArrayList<Chromosome> generatePopulation(){
        return new ArrayList<Chromosome>();
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

            // adiciona o ultimo elemento caso seja ímpar
            if(toCross.get(i+2)==null) {
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

        Chromosome temp=chromosomes.get(0);
        for(int i=1;i<chromosomes.size();i++) {
            if(temp.getAdaptability()<chromosomes.get(i).getAdaptability()) {
                temp = chromosomes.get(i);
            }
        }
        chromosomes.remove(temp);
        return temp;
    }


    public double getGenerationAdaptability(ArrayList<Chromosome> currentGeneration) {
        double adapt=0;
        for(int i=0;i<currentGeneration.size();i++) {
            adapt+=currentGeneration.get(i).getAdaptability();
        }
        return adapt;
    }
}
