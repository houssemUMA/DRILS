package com.drils;

import java.util.ArrayList;
import java.util.List;

public class ClusterCrossover implements Crossover {
	
	private Main main;
	
	public ClusterCrossover (Main main) {
		this.main = main;
	}
	

	@Override
	public List<Integer> crossover(List<Integer> pSolutionOne,List<Integer> pSolutionTwo) {
		List<Integer> uniqueElementsWinnerOne = new ArrayList<Integer>();
		List<Integer> uniqueElementsWinnerTwo = new ArrayList<Integer>();
		List<Integer> commonlElementsOrderOne = new ArrayList<Integer>();
		//		List<Integer> commonlElementsOrderTwo = new ArrayList<Integer>();

		uniqueElementsWinnerOne.addAll(pSolutionOne);
		uniqueElementsWinnerOne.removeAll(pSolutionTwo);
		System.out.println("uniqueElementsWinnerOne are "+uniqueElementsWinnerOne);

		System.out.println();

		uniqueElementsWinnerTwo.addAll(pSolutionTwo);
		uniqueElementsWinnerTwo.removeAll(pSolutionOne);
		System.out.println("uniqueElementsWinnerTwo are "+uniqueElementsWinnerTwo);

		System.out.println();

		commonlElementsOrderOne.addAll(pSolutionOne);		
		commonlElementsOrderOne.retainAll(pSolutionTwo);
		System.out.println("commonlElements order one "+commonlElementsOrderOne);

		//		System.out.println();

		//		commonlElementsOrderTwo.addAll(pSolutionTwo);		
		//		commonlElementsOrderTwo.retainAll(pSolutionOne);
		//		System.out.println("commonlElements order two "+commonlElementsOrderTwo);


		//		/*
		//		 *  if we have only 4 common elements or less, we don't need to do the crossover, 
		//		 *  we just add the unique elements in the genome .
		//		 *  we consider the origin as a two common elements
		//		 */
		//		if (commonlElementsOrderOne.size()>=3) {
		//			
		//			if (!commonlElementsOrderOne.equals(commonlElementsOrderTwo )) {
		//				
		//				Random r = new Random();
		//				int crosspoint = r.nextInt(commonlElementsOrderOne.size()-2)+1;
		//				System.out.println("crosspoint "+crosspoint);
		//				
		//			}
		//		}
		//		


		return generatePx(uniqueElementsWinnerOne,uniqueElementsWinnerTwo,commonlElementsOrderOne);
	}
	
	private List<Integer> generatePx(List<Integer> pathOne,List<Integer> pathTwo,List<Integer> pathOnecommonPoint) {
		List<Integer> one = new ArrayList<Integer>();
		List<Integer> two = new ArrayList<Integer>();
		List<Integer> three = new ArrayList<Integer>();
		List<Integer>  clustersOfPx = new ArrayList<>();
		
		one.addAll(get_clusters_from_path(pathOne));
		two.addAll(get_clusters_from_path(pathTwo));
		three.addAll(get_clusters_from_path(pathOnecommonPoint));

		clustersOfPx.addAll(three);
		clustersOfPx.addAll(1,one);
		for (int i=0; i<two.size();i++) {
			if(!clustersOfPx.contains(two.get(i))) {
				clustersOfPx.add(1,two.get(i) );
			}
		}
		System.out.println("clustersOfPx");
		System.out.println(clustersOfPx);
		for (int i =1;i<main.getSet_number();i++) {
			if(!clustersOfPx.contains(i)) {
				clustersOfPx.add(1,i);
			}
		}
		System.out.println("new clustersOfPx");
		System.out.println(clustersOfPx);
		
		return clustersOfPx;

	}
	
	private List<Integer> get_clusters_from_path(List<Integer> pSolution) {
		List<Integer> path_of_clusters = new ArrayList<Integer>();
		int a;
		for (int i = 0; i < pSolution.size(); i++) {
			a = main.getPoints_cluster()[pSolution.get(i)];
			path_of_clusters.add(a);
		}
		System.out.println(path_of_clusters);
		return path_of_clusters;
	}
	
}
