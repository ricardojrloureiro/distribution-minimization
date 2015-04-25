package Engine;


import jdk.internal.util.xml.impl.Pair;

public class Service {

    public static void main(String [] args) {
        //TODO podemos mudar isto para pedir ao utilizador quanto de adaptabilidade quiser

        new GeneticAlgorithm(true,1,0.5,0.1).run();
    }

}
