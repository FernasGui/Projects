package nn;

import java.util.Arrays;
import java.util.Random;

public class NeuralNetwork implements Comparable<NeuralNetwork>{
	private int inputDim;
	private int hiddenDim;
	private int hiddenDim2;
	private int outputDim;
	private double[][] inputWeights;
	private double[] hiddenBiases;
	private double[][] hidden2Weights;
	private double[] hidden2Biases;
	private double[][] outputWeights;
	private double[] outputBiases;

	private double fitness;
	private double[] chromosome;
	
	public NeuralNetwork(int inputDim, int hiddenDim, int hiddenDim2, int outputDim) {
		this.inputDim = inputDim;
		this.hiddenDim = hiddenDim;
		this.hiddenDim2 = hiddenDim2;
		this.outputDim = outputDim;
		this.inputWeights = new double[inputDim][hiddenDim];
		this.hiddenBiases = new double[hiddenDim];
		this.hidden2Weights = new double[hiddenDim][hiddenDim2];
		this.hidden2Biases = new double[hiddenDim2];
		this.outputWeights = new double[hiddenDim2][outputDim];
		this.outputBiases = new double[outputDim];
	}


	public NeuralNetwork(NeuralNetwork other) {
        // Copy the weights and biases arrays
		this.inputDim = other.getInputDim();
		this.hiddenDim = other.getHiddenDim();
		this.hiddenDim2 = other.getHiddenDim2();
		this.outputDim = other.getOutputDim();
        this.inputWeights = other.getInputWeights();
        this.hiddenBiases = other.getHiddenBiases();
        this.hidden2Weights = other.getHidden2Weights();
        this.hidden2Biases = other.getHidden2Biases();
        this.outputWeights = other.getOutputWeights();
        this.outputBiases = other.getOutputBiases();
    }
	
	public int getHiddenDim2() {
		return hiddenDim2;
	}


	public double[][] getHidden2Weights() {
		return hidden2Weights;
	}


	public double[] getHidden2Biases() {
		return hidden2Biases;
	}


	public NeuralNetwork(int inputDim, int hiddenDim,int hiddenDim2 , int outputDim, double[] values) {
		this(inputDim, hiddenDim,hiddenDim2, outputDim);
		this.chromosome=values;
		int offset = 0;
		for (int i = 0; i < inputDim; i++) {
			for (int j = 0; j < hiddenDim; j++) {
				inputWeights[i][j] = values[i * hiddenDim + j];
			}
		}
		offset = inputDim * hiddenDim;
		for (int i = 0; i < hiddenDim; i++) {
			hiddenBiases[i] = values[offset + i];
		}
		offset += hiddenDim;
		
		for (int i = 0; i < hiddenDim; i++) {
			for (int j = 0; j < hiddenDim2; j++) {
				hidden2Weights[i][j] = values[i * hiddenDim2 + j];
			}
		}
		offset += hiddenDim * hiddenDim2;
		for (int i = 0; i < hiddenDim2; i++) {
			hidden2Biases[i] = values[offset + i];
		}
		offset += hiddenDim2;
		
		for (int i = 0; i < hiddenDim2; i++) {
			for (int j = 0; j < outputDim; j++) {
				outputWeights[i][j] = values[offset + i * outputDim + j];
			}
		}
		offset += hiddenDim2 * outputDim;
		for (int i = 0; i < outputDim; i++) {
			outputBiases[i] = values[offset + i];
		}

	}
	

	public int getChromossomeSize() {
		return inputWeights.length * inputWeights[0].length + hiddenBiases.length+ hidden2Weights.length*hidden2Weights[0].length+hidden2Biases.length + outputWeights.length * outputWeights[0].length + outputBiases.length;
	}

	public double[] getChromossome() {
		double[] chromossome = new double[getChromossomeSize()];
		int offset = 0;
		for (int i = 0; i < inputDim; i++) {
			for (int j = 0; j < hiddenDim; j++) {
				chromossome[i * hiddenDim + j] = inputWeights[i][j];
			}
		}
		offset = inputDim * hiddenDim;
		for (int i = 0; i < hiddenDim; i++) {
			chromossome[offset + i] = hiddenBiases[i];
		}
		offset += hiddenDim;
		
		
		for (int i = 0; i < hiddenDim; i++) {
			for (int j = 0; j < hiddenDim2; j++) {
				chromossome[i * hiddenDim2 + j] = hidden2Weights[i][j];
			}
		}
		offset +=hiddenDim * hiddenDim2;
		for (int i = 0; i < hiddenDim2; i++) {
			chromossome[offset + i] = hidden2Biases[i];
		}
		offset += hiddenDim2;
		
		for (int i = 0; i < hiddenDim2; i++) {
			for (int j = 0; j < outputDim; j++) {
				chromossome[offset + i * outputDim + j] = outputWeights[i][j];
			}
		}
		offset += hiddenDim2 * outputDim;
		for (int i = 0; i < outputDim; i++) {
			chromossome[offset + i] = outputBiases[i];
		}

		return chromossome;

	}

	public void initializeWeights() {
        Random random = new Random();
        for (int i = 0; i < inputDim; i++) {
            for (int j = 0; j < hiddenDim; j++) {
                inputWeights[i][j] = random.nextDouble() - 0.5;
            }
        }
        for (int i = 0; i < hiddenDim; i++) {
            hiddenBiases[i] = random.nextDouble() - 0.5;
            for (int j = 0; j < hiddenDim2; j++) {
            	hidden2Weights[i][j] = random.nextDouble() - 0.5;
            }
        }
        for (int i = 0; i < hiddenDim2; i++) {
            hidden2Biases[i] = random.nextDouble() - 0.5;
            for (int j = 0; j < outputDim; j++) {
                outputWeights[i][j] = random.nextDouble() - 0.5;
            }
        }
        for (int i = 0; i < outputDim; i++) {
            outputBiases[i] = random.nextDouble() - 0.5;
        }
    }
	
	public double[] nextMove(double[] d2) {
	    double[] normalizedData = normalize(d2);
	    double[] hidden1 = new double[hiddenDim];
	    double[] hidden2 = new double[hiddenDim2];
	    double[] output = new double[outputDim];

	    for (int i = 0; i < hiddenDim; i++) {
            double sum = 0.0;
            for (int j = 0; j < inputDim; j++) {
                double d = d2[j];
                sum += d * inputWeights[j][i]; 
            }
            hidden1[i] = sum + hiddenBiases[i]; 
            if(hidden1[i] < 0) {
                hidden1[i]*= 0.1;
            }
        }
	    for (int j = 0; j < hiddenDim2; j++) {
	        double z = hidden2Biases[j];
	        for (int i = 0; i < hiddenDim; i++) {
	            z += hidden2Weights[i][j] * hidden1[i];
	        }
	        hidden2[j] = relu(z);
	    }
	    for (int j = 0; j < outputDim; j++) {
	        double z = outputBiases[j];
	        for (int i = 0; i < hiddenDim2; i++) {
	            z += outputWeights[i][j] * hidden2[i];
	        }
	        output[j] = relu(z);
	    }
	    return output;
	}
	private double relu(double x) {
	    return Math.max(0, x);
	}

	public double[] normalize(double[] data) {
	    double mean = calculateMean(data);
	    double stdev = calculateStdev(data);
	    double[] normalizedData = new double[data.length];
	    for (int i = 0; i < data.length; i++) {
	        normalizedData[i] = (data[i] - mean) / stdev;
	    }
	    return normalizedData;
	}

	public double calculateMean(double[] data) {
	    double sum = 0;
	    for (int i = 0; i < data.length; i++) {
	        sum += data[i];
	    }
	    double mean = sum / data.length;
	    return mean;
	}

	public double calculateStdev(double[] data) {
	    double mean = calculateMean(data);
	    double sum = 0;
	    for (int i = 0; i < data.length; i++) {
	        sum += Math.pow(data[i] - mean, 2);
	    }
	    double stdev = Math.sqrt(sum / (data.length - 1));
	    return stdev;
	}

	public int getInputDim() {
		return inputDim;
	}

	public int getHiddenDim() {
		return hiddenDim;
	}

	public int getOutputDim() {
		return outputDim;
	}

	public double[][] getInputWeights() {
		return inputWeights;
	}

	public double[] getHiddenBiases() {
		return hiddenBiases;
	}

	public double[][] getOutputWeights() {
		return outputWeights;
	}

	public double[] getOutputBiases() {
		return outputBiases;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double f) {
		fitness=f;
	}

	public void setInputWheights(double[][] inputWhts) {
		this.inputWeights=inputWhts;
	}

	public void setOutputWeights(double[][] outputWhts) {
		this.outputWeights=outputWhts;
	}
	
	@Override
	public int compareTo(NeuralNetwork other) {
		if (this.fitness < other.fitness) {
			return -1;
		} else if (this.fitness > other.fitness) {
			return 1;
		} else {
			return 0;
		}
	}
	
	@Override
	
	public String toString() {
		return ""+getFitness();
	}

	public void setChromosome(double[] chromosome) {
		this.chromosome=chromosome;
		
	}
}
