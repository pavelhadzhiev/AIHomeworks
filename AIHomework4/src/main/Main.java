package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    private static List<Item> items = new ArrayList<>();
    private static int knapsackCapacity;
    private static int numberOfItems;

    private static int populationSize = 1000;
    private static double crossoverProbability = 0.50;
    private static double mutationProbability = 0.25;
    private static int similarBestCount = 250;

    private static int crossoverCount = 0;
    private static int mutationCount = 0;
    private static int generationCount = 0;
    private static int noImprovementCount = 0;

    private static double totalGenerationFitness = 0;
    private static double bestGenerationFitness = 0;
    private static double averageGenerationFitness = 0;
    private static Boolean[] fittestChromosome;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        knapsackCapacity = scan.nextInt();
        numberOfItems = scan.nextInt();
        for (int i = 0; i < numberOfItems; i++) {
            double weight = scan.nextDouble();
            double value = scan.nextDouble();
            items.add(new Item(weight, value));
        }

        long start = System.currentTimeMillis();

        List<Boolean[]> population = generatePopulation();
        setFitnessProperties(population);

        while (noImprovementCount <= similarBestCount) {
            population = generateNextPopulation(population);
            setFitnessProperties(population);
            
            System.out.println(bestGenerationFitness);
        }

        long finish = System.currentTimeMillis();

        System.out.println();
        System.out.println("Best solution: " + bestGenerationFitness);
        System.out.println("Time taken: " + (finish - start));
        System.out.println("Total generations: " + generationCount);
        System.out.println("Population size: " + populationSize);
        System.out.println("Crossovers: " + crossoverCount);
        System.out.println("Crossover probability: " + crossoverProbability);
        System.out.println("Mutations: " + mutationCount);
        System.out.println("Mutation probability: " + mutationProbability);
        System.out.println("Average fitness for final generation: " + averageGenerationFitness);

        scan.close();
    }

    private static void setFitnessProperties(List<Boolean[]> population) {
        double oldBestFitness = bestGenerationFitness;
        totalGenerationFitness = 0;
        for (Boolean[] chromosome : population) {
            double fitness = fitness(chromosome);
            totalGenerationFitness += fitness;
            if (fitness > bestGenerationFitness) {
                bestGenerationFitness = fitness;
                fittestChromosome = chromosome;
            }
        }
        averageGenerationFitness = totalGenerationFitness / population.size();
        if (oldBestFitness == bestGenerationFitness) {
            noImprovementCount++;
        } else {
            noImprovementCount = 0;
        }
        generationCount++;
    }

    private static Boolean[] generateOptimalChromosome() {
        Boolean[] greedyChromosome = new Boolean[items.size()];
        Arrays.fill(greedyChromosome, false);

        List<Item> sortedByRatio = items.stream()
                .sorted()
                .collect(Collectors.toList());
        Collections.reverse(sortedByRatio);

        int weight = 0;
        for (Item item : sortedByRatio) {
            if (weight + item.weight < knapsackCapacity) {
                weight += item.weight;
                greedyChromosome[items.indexOf(item)] = true;
            } else {
                break;
            }
        }

        return greedyChromosome;
    }

    private static double fitness(Boolean[] chromosome) {
        double totalValue = 0;
        double totalWeight = 0;
        for (int i = 0; i < items.size(); i++) {
            if (chromosome[i]) {
                totalValue += items.get(i).value;
                totalWeight += items.get(i).weight;
            }
        }

        return totalWeight < knapsackCapacity ? totalValue : 0;
    }

    private static List<Boolean[]> generatePopulation() {
        List<Boolean[]> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(generateChromosome());
        }
        return population;
    }

    private static Boolean[] generateChromosome() {
        Boolean[] chromosome = new Boolean[numberOfItems];
        Arrays.fill(chromosome, false);
        double totalWeight = 0;

        for (int i = 0; i < numberOfItems; i++) {
            int randomIndex = (int) (Math.random() * numberOfItems);
            double weight = items.get(randomIndex).weight;
            if (totalWeight + weight < knapsackCapacity && !chromosome[randomIndex]) {
                chromosome[randomIndex] = true;
                totalWeight += weight;
            }
        }

        return chromosome;
    }

    private static List<Boolean[]> generateNextPopulation(List<Boolean[]> population) {
        List<Boolean[]> nextPopulation = new ArrayList<>();

        if (populationSize % 2 == 1) {
            nextPopulation.add(fittestChromosome);
        }

        for (int i = 0; i < populationSize / 2; i++) {
            int parentIndex1 = selectParentIndex(population);
            int parentIndex2 = parentIndex1;
            while (parentIndex1 == parentIndex2) {
                parentIndex2 = selectParentIndex(population);
            }

            Boolean[] parent1 = population.get(parentIndex1);
            Boolean[] parent2 = population.get(parentIndex2);

            double crossoverRoll = Math.random();
            if (crossoverRoll <= crossoverProbability) {
                crossoverCount++;
                nextPopulation.addAll(crossover(parent1, parent2));
            } else {
                nextPopulation.add(parent1);
                nextPopulation.add(parent2);
            }

        }

        for (int i = 0; i < nextPopulation.size(); i++) {
            double mutationRoll = Math.random();
            if (mutationRoll <= mutationProbability) {
                mutationCount++;
                mutate(nextPopulation.get(i));
            }
        }

        return nextPopulation;
    }

    private static int selectParentIndex(List<Boolean[]> population) {
        while (true) {
            double fitnessBar = Math.random() * bestGenerationFitness;
            int candidateSearchCount = 0;
            while (true) {
                int randomIndex = (int) (Math.random() * populationSize);
                double fitnessOfElem = fitness(population.get(randomIndex));
                if (fitnessOfElem >= fitnessBar) {
                    return randomIndex;
                }
                candidateSearchCount++;
                if (candidateSearchCount > 100) {
                    break;
                }
            }
        }
    }

    private static List<Boolean[]> crossover(Boolean[] parent1, Boolean[] parent2) {
        List<Boolean[]> crossoverResult = new ArrayList<>();

        int crossoverPoint = (int) (Math.random() * (numberOfItems - 1));
        List<Boolean> child1 = new ArrayList<>(Arrays.asList(parent1));
        List<Boolean> child2 = new ArrayList<>(Arrays.asList(parent2));

        List<Boolean> child1Suffix = new ArrayList<>(child1.subList(crossoverPoint, child1.size()));
        List<Boolean> child2Suffix = new ArrayList<>(child2.subList(crossoverPoint, child2.size()));

        child1 = child1.subList(0, crossoverPoint);
        child1.addAll(crossoverPoint, child2Suffix);

        child2 = child2.subList(0, crossoverPoint);
        child2.addAll(crossoverPoint, child1Suffix);

        crossoverResult.add(child1.toArray(new Boolean[child1.size()]));
        crossoverResult.add(child2.toArray(new Boolean[child1.size()]));

        return crossoverResult;
    }

    private static void mutate(Boolean[] element) {
        int geneIndex = (int) (Math.random() * (element.length - 1));
        if (element[geneIndex]) {
            element[geneIndex] = false;
        } else {
            element[geneIndex] = true;
        }
    }
}