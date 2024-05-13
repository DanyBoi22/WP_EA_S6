/*
 * Evolutionary Algorithms Framework
 *
 * Copyright (c) 2023 Christian Lins <christian.lins@haw-hamburg.de>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.heaal.eaf.algorithm;

import de.heaal.eaf.base.Algorithm;
import de.heaal.eaf.base.Individual;
import de.heaal.eaf.base.IndividualFactory;
import de.heaal.eaf.base.VecN;
import de.heaal.eaf.crossover.Combination;
import de.heaal.eaf.evaluation.ComparatorIndividual;
import de.heaal.eaf.evaluation.MinimizeFunctionComparator;
import de.heaal.eaf.mutation.Mutation;
import de.heaal.eaf.mutation.MutationOptions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import static de.heaal.eaf.selection.SelectionUtils.selectNormal;

/**
 * Implementation of the Hill Climbing algorithm.
 * 
 * @author Christian Lins <christian.lins@haw-hamburg.de>
 */
public class GeneticAlgorithm extends Algorithm {

    private final IndividualFactory indFac;
    private final ComparatorIndividual terminationCriterion;
    private final Combination combination;
    private final int populationSize;

    public GeneticAlgorithm(float[] min, float[] max,
                            Comparator<Individual> comparator, Mutation mutator, int populationSize,
                            Combination combination, ComparatorIndividual terminationCriterion)
    {
        super(comparator, mutator);
        this.indFac = new ParticleFactory(min, max);
        this.terminationCriterion = terminationCriterion;
        this.combination = combination;
        this.populationSize = populationSize;
    }
    
    @Override
    public void nextGeneration() {
        super.nextGeneration();

        //Step 1 calculate the fitness of each Parent in the Population
        //and sort the Population in descending order
        Comparator<Individual> cmp = new MinimizeFunctionComparator(FitnessFunction);

        population.sort(cmp);

        // While |Children| < |Parents|
        List<Individual> children = new ArrayList<>();

        while(children.size() != population.size()) {
            //Step 2 select a pair of different parents
            Individual[] parents = new Individual[2];
            parents[0] = selectNormal(population, new Random(), null);
            parents[1] = selectNormal(population, new Random(), parents[0]);

            //Step 3 mate the parent
            children.add(combination.combine(parents));
        }
        //Loop

        //Step 4 randomly mutate kids
        //Only one feature is allowed to mutate
        MutationOptions opt = new MutationOptions();
        opt.put(MutationOptions.KEYS.MUTATION_PROBABILITY, 0.1f);

        for(int i = 0; i < population.size(); i++) {
            Individual ind = children.get(i);
            mutator.mutate(ind, opt);
        }

        //Step 5 set the children as the new population and exterminate the parents
        for(int i = 0; i < population.size(); i++) {
            population.set(i, children.get(i));
        }
    }

    /**
     * Fitness Function.
     * If we are trying to maximize f(x), then we calculate the fitness of each xi
     * by computing f(xi). -> |x| + |y|
     * If we are trying to minimize f(x), then we calculate the fitness of each xi
     * by computing the negative of f(xi). -> -|x| - |y|; alternatively 1/|x| + 1/|x| (watch out for zero division)
     * But because we are using minimising Comparator to sort the population we need to maximize the fitness
     */
    public Function<Individual, Float> FitnessFunction = (x) -> {
        float x1 = x.getGenome().array()[0] < 0 ? -x.getGenome().array()[0] : x.getGenome().array()[0];
        float x2 = x.getGenome().array()[1] < 0 ? -x.getGenome().array()[1] : x.getGenome().array()[1];
        return x1 + x2;};
  
    @Override
    public boolean isTerminationCondition() {
        boolean termination = false;

        for(int i = 0; i < population.size(); i++) {
            if (comparator.compare(population.get(i), terminationCriterion) > 0)
                termination = true;
        }

        return termination;
    }

    @Override
    public void run() {
        initialize(indFac, populationSize);
        int count = 0;
        while(!isTerminationCondition()) {
            System.out.println("Gen: " + count);
             /*
            for(int i = 0; i < population.size(); i++) {
                System.out.println("Genome " + i + ": " + population.get(i).getGenome());
                System.out.println("Cache: " + population.get(i).getCache());
            }
             */
            nextGeneration();
            count++;
        }

        /* print the entire population
        for(int i = 0; i < population.size(); i++) {
            System.out.println("Genome " + i + ": " + population.get(i).getGenome());
            System.out.println("Cache: " + population.get(i).getCache());
        }
        */
        VecN TerminationGenome = null;
        float TerminationCache = 0.0f;

        for(int i = 0; i < population.size(); i++) {
            if (comparator.compare(population.get(i), terminationCriterion) > 0) {
                TerminationGenome = population.get(i).getGenome();
                TerminationCache = population.get(i).getCache();
                break;
            }
        }
        System.out.println("Termination Genome: " + TerminationGenome);
        System.out.println("Cache: " + TerminationCache);

    }

}
