package Engine;

import Models.Chromosome;
import java.util.ArrayList;

public class GeneticAlgorithm extends Thread {

    private ArrayList<Chromosome> chromosomes;
    private Chromosome solution;
    private Integer minimumAdaptability;

    public GeneticAlgorithm(Integer adaptability){
        this.minimumAdaptability = adaptability;
        this.chromosomes = generatePopulation();
    }

    public void run() {

        boolean required=true;

        // while required to keep moving into next generations
        while(required) {

            // calls crossover function with the current best chromosomes to pass to the next generation
            this.chromosomes = crossover(picking());
            this.chromosomes = mutation();

            solution = mostAdapted();

            //checks how good is the current solution
            if((solution.getAdaptability()-solution.getPenalization())>minimumAdaptability) {
                required=false;
            }

        }

        Partials.representSolution(solution);
    }

    /**
     * Function that will pick the chromosomes to pass to the next generation
     * @return an array list of the passing chromosomes.
     */
    private ArrayList<Chromosome> picking() {
        return new ArrayList<Chromosome>();
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
     * @param chromosomes to crossover
     * @return the current crossoved population
     */
    private ArrayList<Chromosome> crossover(ArrayList<Chromosome> chromosomes) {


        return new ArrayList<Chromosome>();
    }

    /**
     * generates a random mutation in order to improve the general population
     * @return the current mutated population
     */
    private ArrayList<Chromosome> mutation() {
        return new ArrayList<Chromosome>();
    }

    /**
     * Returns the most adapted chromosome of the current population
     * @return
     */
    private Chromosome mostAdapted() {
        return chromosomes.get(1);
    }

}
