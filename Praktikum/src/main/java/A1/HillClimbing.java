package A1;

import de.heaal.eaf.base.Individual;
import de.heaal.eaf.evaluation.ComparatorIndividual;
import de.heaal.eaf.mutation.Mutation;

import java.util.Comparator;

public class HillClimbing {
    // Fields
    private int myField;

    // Constructor
    public HillClimbing(int myField) {
        this.myField = myField;

        float[] min;
        float[] max;
        Comparator<Individual> comparator;
        Mutation mutation;
        ComparatorIndividual terminationCriterion;
        //new HillClimbingAlgorithm(min, max, comparator, mutation, terminationCriterion);
        printit();

    }

    // Method
    public void printit() {
        System.out.println(myField + "Hello from myMethod!");
    }
}



