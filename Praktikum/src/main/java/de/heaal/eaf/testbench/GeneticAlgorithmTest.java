package de.heaal.eaf.testbench;

import de.heaal.eaf.algorithm.Particle;
import de.heaal.eaf.base.Individual;
import de.heaal.eaf.base.Population;
import de.heaal.eaf.base.VecN;
import de.heaal.eaf.evaluation.MinimizeFunctionComparator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.Random;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class GeneticAlgorithmTest {

    public Function<Individual, Float> FitnessFunction = (x) -> {
        float x1 = x.getGenome().array()[0] < 0 ? -x.getGenome().array()[0] : x.getGenome().array()[0];
        float x2 = x.getGenome().array()[1] < 0 ? -x.getGenome().array()[1] : x.getGenome().array()[1];
        return x1 + x2;};

    @Test
    public void testFitnessFunction() {

        Individual ind = new Particle(new VecN(new float[]{1.0f, 1.0f}));
        float res1 = FitnessFunction.apply(ind);
        assertEquals(2.0f, res1, 0.0001f);

        ind = new Particle(new VecN(new float[]{1.0f, 0.0f}));
        float res2 = FitnessFunction.apply(ind);
        assertEquals(1.0f, res2, 0.0001f);

        ind = new Particle(new VecN(new float[]{-1.0f, 2.0f}));
        float res3 = FitnessFunction.apply(ind);
        assertEquals(3.0f, res3, 0.0001f);

        ind = new Particle(new VecN(new float[]{-1.0f, -3.0f}));
        float res4 = FitnessFunction.apply(ind);
        assertEquals(4.0f, res4, 0.0001f);

        ind = new Particle(new VecN(new float[]{0.0f, 0.0f}));
        float res5 = FitnessFunction.apply(ind);
        assertEquals(0.0f, res5, 0.0001f);
    }

    @Test
    public void testPopulationSort() {
        Comparator<Individual> cmp = new MinimizeFunctionComparator(FitnessFunction);
        Population pop = new Population(0);

        Individual ind1 = new Particle(new VecN(new float[]{0.0f, 1.0f}));
        pop.add(ind1);
        Individual ind2 = new Particle(new VecN(new float[]{0.0f, 0.0f}));
        pop.add(ind2);
        Individual ind3 = new Particle(new VecN(new float[]{1.0f, 1.0f}));
        pop.add(ind3);
        Individual ind4 = new Particle(new VecN(new float[]{-2.0f, -2.0f}));
        pop.add(ind4);

        pop.sort(cmp);

        Population popExpected = new Population(0);

        popExpected.add(ind4);
        popExpected.add(ind3);
        popExpected.add(ind1);
        popExpected.add(ind2);

        assertEquals(popExpected.get(0), pop.get(0));
        assertEquals(popExpected.get(1), pop.get(1));
        assertEquals(popExpected.get(2), pop.get(2));
        assertEquals(popExpected.get(3), pop.get(3));
    }
}
