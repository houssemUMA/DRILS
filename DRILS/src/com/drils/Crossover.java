package com.drils;

import java.util.List;

public interface Crossover {

	List<Integer> crossover(List<Integer> pSolutionOne, List<Integer> pSolutionTwo);

}