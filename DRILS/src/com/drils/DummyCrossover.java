package com.drils;

import java.util.ArrayList;
import java.util.List;

public class DummyCrossover implements Crossover {

	@Override
	public List<Integer> crossover(List<Integer> pSolutionOne, List<Integer> pSolutionTwo) {
		return new ArrayList<>();
	}

}
