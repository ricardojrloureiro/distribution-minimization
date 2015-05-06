package Engine;

import Models.Chromosome;
import Models.Factory;
import Models.ServicePoint;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Partials {

    public double getDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
    }

    public static void representSolution(ArrayList<Chromosome> solution, ArrayList<Factory> factories, ArrayList<ServicePoint> servicePoints, int maxRepresentation) {
        for (int i = 0; i < solution.size(); i++) {
        	System.out.println("\nSolution #" + i + ":");
        	solution.get(i).printFactories(factories.size(), servicePoints.size(), factories);
        }
        Chromosome temp = solution.get(0);
        int mostAdapted = 0;
        for (int i = 1; i < solution.size(); i++) {
        	if (solution.get(i).getAdaptability(maxRepresentation) >= temp.getAdaptability(maxRepresentation)) {
        		temp = solution.get(i);
        		mostAdapted = i;
        	}
        }
        System.out.println("\n\n Best Solution was chromossome #" + mostAdapted + ", adaptability: " + temp.getAdaptability(maxRepresentation));
        
    }

    public static ArrayList<Double> generateRandom(int size) {
        ArrayList<Double> randomNumbers = new ArrayList<Double>();
        for(int i=0;i<size;i++) {
            Random r = new Random();
            double generated = (r.nextDouble());
            randomNumbers.add(generated);
        }
        return randomNumbers;
    }

    public static String integerModified(Integer tempProduction, Integer size) {
        String representation = Integer.toBinaryString(tempProduction);
        while(representation.length()<size) {
            representation = "0" + representation;
        }
        return representation;
    }
}
