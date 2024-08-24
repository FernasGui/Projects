package main;

import java.util.Collections;

import ga.GeneticAlgorithm;

import nn.NeuralNetwork;
import space.SpaceInvaders;

public class PlayGenetic {

	public static void main(String[] args) {
		GeneticAlgorithm g=new GeneticAlgorithm(150,0.01,new NeuralNetwork(112,140,40,4),20);
		g.run(10);
		
	}

}
