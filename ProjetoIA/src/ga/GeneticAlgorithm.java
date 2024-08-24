package ga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import nn.NeuralNetwork;
import space.Board;
import space.Commons;
import space.SpaceInvaders;

public class GeneticAlgorithm {

	private final int populationSize;
	private final double mutationRate;
	private final NeuralNetwork prototype;
	private final Random random = new Random();
	private int elitismCount;
	private List<NeuralNetwork> population = new ArrayList<>();

	public GeneticAlgorithm(int populationSize, double mutationRate, NeuralNetwork prototype, int elitismCount) {
		this.populationSize = populationSize;
		this.mutationRate = mutationRate;
		this.prototype = prototype;
		this.elitismCount=elitismCount;
	}

	public void run(int generations) {
		createPopulation(populationSize, prototype);
		for (int i = 0; i < generations; i++) {
//			System.out.println("GEN: " +i);
			evaluatePopulation(i);
			List<NeuralNetwork> parents = selectParents();
			List<NeuralNetwork> offspring = generateOffspring(parents);
			mutatePopulation();
			population = selectSurvivors(offspring);
			
		}
//		System.out.println("GEN: " +generations);
		evaluatePopulation(generations);
		System.out.println("Best solution found: " + Collections.max(population).getFitness());
		SpaceInvaders.showControllerPlaying(Collections.max(population), 1);
	}

	public void createPopulation(int size, NeuralNetwork prototype) {
		for (int i = 0; i < size; i++) {
			NeuralNetwork individual = new NeuralNetwork(prototype.getInputDim(), prototype.getHiddenDim(),prototype.getHiddenDim2(),prototype.getOutputDim());			
			individual.initializeWeights();
			population.add(individual);
		}
	}
	
	public void evaluatePopulation(int g) {
		int i=0;
		for (NeuralNetwork individual : population) {
			SpaceInvaders si = new SpaceInvaders(individual,false);
			Board b = si.getBoard();
			b.setSeed(1);
			b.run();
			Double fitness = b.getFitness();
			individual.setFitness(fitness);
//			System.out.println("NN= " + i + "  Fitness:" + individual.toString());
			i++;
		}
		Collections.sort(population, Comparator.reverseOrder());
		System.out.println("GEN: " +g + " MaxFitness :" + population.get(0).getFitness());
	}
		

	public List<NeuralNetwork> selectParents() {
	    List<NeuralNetwork> parents = new ArrayList<>();
	    double maxFitness = 0.0;
	    for (NeuralNetwork nn : population) {
	        if (nn.getFitness() > maxFitness)
	            maxFitness = nn.getFitness();
	    }
	    //Mating pool
	    List<NeuralNetwork> matingPool = new ArrayList<>();
	    for (NeuralNetwork nn : population) {
	        double fitnessNormalized = nn.getFitness() / maxFitness;
	        int n = (int) Math.floor(fitnessNormalized * 100);
	        for (int i = 0; i < n; i++) {
	            matingPool.add(nn);
	        }
	    }
	    //roulette wheel selection na mating pool
	    double totalFitness = 0.0;
	    for (NeuralNetwork nn : matingPool) {
	        totalFitness += nn.getFitness();
	    }
	    double slice = random.nextDouble() * totalFitness;
	    double fitnessSoFar = 0.0;
	    NeuralNetwork parent1 = null;
	    for (NeuralNetwork nn : matingPool) {
	        fitnessSoFar += nn.getFitness();
	        if (fitnessSoFar >= slice) {
	            parent1 = nn;
	            break;
	        }
	    }
	    //Parent 1
	    matingPool.remove(parent1);
	    totalFitness = 0.0;
	    for (NeuralNetwork nn : matingPool) {
	        totalFitness += nn.getFitness();
	    }
	    // Parent 2
	    slice = random.nextDouble() * totalFitness;
	    fitnessSoFar = 0.0;
	    NeuralNetwork parent2 = null;
	    for (NeuralNetwork nn : matingPool) {
	        fitnessSoFar += nn.getFitness();
	        if (fitnessSoFar >= slice) {
	            parent2 = nn;
	            break;
	        }
	    }
	    parents.add(parent1);
	    parents.add(parent2);
	    return parents;
	}

	public List<NeuralNetwork> generateOffspring(List<NeuralNetwork> parents) {
	    List<NeuralNetwork> offspring = new ArrayList<>();
	    int numOffspring = population.size();
	    for (int i = 0; i < numOffspring; i++) {
	        NeuralNetwork parent1 = parents.get(random.nextInt(parents.size()));
	        NeuralNetwork parent2 = parents.get(random.nextInt(parents.size()));
	        NeuralNetwork child = twoPointCrossover(parent1, parent2);
	        mutateIndividual(child);
	        offspring.add(child);
	    }
	    offspring.sort(Comparator.comparing(NeuralNetwork::getFitness).reversed());
	    return offspring;
	}


	public List<NeuralNetwork> selectSurvivors(List<NeuralNetwork> offspring) {
		List<NeuralNetwork> mergedPopulation = new ArrayList<>(population);
		mergedPopulation.addAll(offspring);
		Collections.sort(mergedPopulation, Comparator.reverseOrder());
		return mergedPopulation.subList(0, populationSize);
	}
	
	
	
	public static NeuralNetwork twoPointCrossover(NeuralNetwork parent1, NeuralNetwork parent2) {
	    Random random = new Random();
	    double[] chrom1 = parent1.getChromossome();
	    double[] chrom2 = parent2.getChromossome();
	    int point1 = random.nextInt(chrom1.length);
	    int point2 = random.nextInt(chrom1.length);
	    if (point1 > point2) {
	        int temp = point1;
	        point1 = point2;
	        point2 = temp;
	    }
	    double[] childChrom = new double[chrom1.length];
	    for (int i = 0; i < chrom1.length; i++) {
	        if (i <= point1 || i >= point2) {
	            childChrom[i] = chrom1[i];
	        } else {
	            childChrom[i] = chrom2[i];
	        }
	    }
	    NeuralNetwork child = new NeuralNetwork(parent1.getInputDim(), parent1.getHiddenDim(),parent1.getHiddenDim2(), parent1.getOutputDim(), childChrom);
	    return child;
	}
	
	public void mutatePopulation() {
		for (NeuralNetwork individual : population) {
			if (random.nextDouble() < mutationRate) {
//				System.out.println("I mutated to " + individual.getFitness());
				mutateIndividual(individual);
			}
		}
	}

	public void mutateIndividual(NeuralNetwork individual) {
		double mutationRate = 0.01;
		Random random = new Random();
		double[] chromosome = individual.getChromossome();
		for (int i = 0; i < chromosome.length; i++) {
			if (random.nextDouble() < mutationRate) {
				chromosome[i] += random.nextGaussian() * 0.1;
			}
		}
		individual.initializeWeights();
		individual.setChromosome(chromosome);
	}

	


}
