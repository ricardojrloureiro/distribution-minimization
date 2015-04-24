package Engine;

import Models.Chromosome;

import java.awt.*;

public class Partials {

    public double getDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
    }

    public static void representSolution(Chromosome solution) {
        System.out.println("Melo rocks");

    }
}
