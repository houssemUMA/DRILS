package com.drils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class Main {
	private int set_number;
	private int pointsNumber = 52;
	private double tMax = 1616;
	private int clusters_used;
	private double total_path_distance;
	private double total_path_profit;
	private double best_score_solution=0.0;
	private int[] set_profit;
	public int[] points_profit = new int[pointsNumber];
	public int[] points_cluster = new int[pointsNumber];
	public double[][] points = new double[pointsNumber][2];
	private int set_data[][];
	public double m_Distance[][] = new double[pointsNumber][pointsNumber];
	private int[][] set_cluster;
	//randomSet is the first path of clusters indexes
		List<Integer> randomSet= new ArrayList<Integer>();
		/*
		 * randomPath is the first path of vertices indexes. In this path we have one
		 * vertex from each cluster
		 */
		List<Integer> randomPath = new ArrayList<Integer>();
		// this path is the first solution and respect the time budget
		List<Integer> firstPath = new ArrayList<Integer>();
		// this path is the solution that is being optimized
		List<Integer> solution = new ArrayList<Integer>();
		List<Integer> secondSolution = new ArrayList<Integer>();
		List<Integer> pX = new ArrayList<Integer>();
		List<Integer> best_solution = new ArrayList<Integer>();
		List<Integer> clusters = new ArrayList<Integer>();
		List<Integer> clustersOfPx = new ArrayList<Integer>();



	public static void main(String[] args) {
		
		Main test= new Main();
        test.run_one_tine();
		test.generateRandomSet();
        test.allStepsToMakeRandomSoltuion();
        test.ntime();
               
	}
	
	public void run_one_tine() {
		read_data();
		fillDistanceMatrix();
		prepare_data();
	}
	
	public void allStepsToMakeRandomSoltuion() {

        generateInitialSolution();
        generate_first_path();
       
	}
	
	public void ntime() {
		
//		float sec;
//		long start = System.currentTimeMillis();
//
//		do {
//			System.out.println("******************************");
		
		for (int i=0;i<100;i++) {
			 algorithm();
		}
		
//		long end = System.currentTimeMillis();
//		sec = (end - start) / 1000F;
//	} while (sec < 100);
	}
	
	public void algorithm() {
		local_search (solution);
		escape(solution);
		System.out.println("*******************************");
		allStepsToMakeRandomSoltuion();
		local_search (secondSolution);
		display();
		 crossover(solution,secondSolution);
		 randomSet.clear();
		 randomSet.addAll(clustersOfPx);
		 allStepsToMakeRandomSoltuion();
		 local_search (pX);
		 System.out.println("The best score is "+best_score_solution);
	}
	
	
	public void display () {
		System.out.println();
		System.out.println(solution);
		System.out.println(secondSolution);
	}
	
	
	
	
	public void crossover(List<Integer> pSolutionOne,List<Integer> pSolutionTwo) {
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
		
		
		generatePx(uniqueElementsWinnerOne,uniqueElementsWinnerTwo,commonlElementsOrderOne);
	}
	
	public void generatePx(List<Integer> pathOne,List<Integer> pathTwo,List<Integer> pathOnecommonPoint) {
		List<Integer> one = new ArrayList<Integer>();
		List<Integer> two = new ArrayList<Integer>();
		List<Integer> three = new ArrayList<Integer>();

		verify_clear_list(clustersOfPx);
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
		for (int i =1;i<set_number;i++) {
			if(!clustersOfPx.contains(i)) {
				clustersOfPx.add(1,i);
			}
		}
		System.out.println("new clustersOfPx");
		System.out.println(clustersOfPx);
		
	}
	
	
	public void local_search (List<Integer> pSolution) {
		double delta = 0;
		verify_clear_list(pSolution);
		pSolution.addAll(firstPath);	
		
		do {
//			System.out.println("three");
			delta = move(pSolution);
		} while (delta < 0);
		System.out.println("Here is our first solution");
		System.out.println(pSolution);
		total_path_distance= calculate_path_distance(pSolution);
		total_path_profit= calculate_path_profit(pSolution);
		compare_results(pSolution );
		}
	
	public void escape(List<Integer> pSolution) {
	int a;
	int b;
	List<Integer> removed_list = new ArrayList<Integer>();
	clusters_path(pSolution)	;
//	stop_condition();
	a=random_first_index();
	b=random_second_index( )+1;
	removed_list.addAll(clusters.subList(a,b ));
//	System.out.println(removed_list);
	Collections.shuffle(removed_list);
//	System.out.println(removed_list);
	clusters.subList(a, b).clear();
//	System.out.println(clusters);
	clusters.addAll(1, removed_list);
//	System.out.println(clusters);
	randomSet.clear();
    randomSet.addAll(clusters);
    System.out.println(randomSet);
    clusters.clear();
	
	}
	
	   private int random_first_index() {
			int selected;
			Random rand = new Random();
			selected = rand.nextInt(clusters_used-1)+1;
			return selected;
		}
		    
		    private int random_second_index( ) {
		    	int selected;
		    	Random rand = new Random();
		    	// this condition if we have already visited all the clusters
		    	if (set_number == clusters_used) {
		    		return clusters_used-1;
		    	} else {
		    		selected = rand.nextInt(set_number - clusters_used - 1) + clusters_used + 1;
		    	}

		    	return selected;
		    }
	
	public void clusters_path(List<Integer> pSolution) {
		verify_clear_list(clusters);
		int a;
		for (int i = 0; i < pSolution.size()-1; i++) {
			a = points_cluster[pSolution.get(i)];
			clusters.add(a);
		}
		clusters_used=clusters.size();
		
		for (int i=clusters.size();i<randomSet.size();i++) {
			a = randomSet.get(i);
			clusters.add(a);
		}
	}
	
	public double move(List<Integer> solution) {
		
		double local_delta;
		
		for (int i = 2; i < solution.size() - 2; i++) {
			for (int j = i + 1; j < solution.size() - 1; j++) {
				
				local_delta = delta(solution, i, j);

				if (local_delta < 0) {
					
					swapElements(solution, j, i);

					valid_insert(solution.size() - 2);
//					System.out.println(solution);
//					System.out.println("local_delta"+local_delta);
					return local_delta;
				}

			}
		}
			
		return 0;
	}
	
	protected void swapElements(List<Integer> path, int j, int i) {
		int temp = path.get(j);
		path.set(j, path.get(i));
		path.set(i, temp);
	}
	
	private void valid_insert(int last_index) {
		double timeBudget;
		double nextDistance;
		double lastDistance;
//		System.out.println("one");
		timeBudget = tMax - calculate_path_distance(solution);
		if (last_index < set_number - 1) {
			nextDistance = m_Distance[randomPath.get(last_index)][randomPath.get(last_index + 1)];
			lastDistance = m_Distance[randomPath.get(last_index + 1)][0];
			timeBudget = timeBudget - nextDistance - lastDistance;
			
			if (timeBudget > 0) {
//				System.out.println("we insert");
	            solution.add(solution.size()-1,randomPath.get(last_index + 1));
//	            System.out.println();
//	            System.out.println("solution after the add ");
//	            System.out.println(solution);
//	            System.out.println();
			}
			
			
		}

	}
	
	public double delta(List<Integer> path, int pI, int pJ) {
		double delta = 0;
		double scoreOne = 0;
		double scoreTwo = 0;
		int i = pI;
		int j = pJ;
		
		if (j<path.size()-1) {
		
		if ((j - i) == 1 ||(j - i) == 2) {
			scoreOne = m_Distance[path.get(i - 1)][path.get(i)] + m_Distance[path.get(j)][path.get(j+1)];
			scoreTwo = m_Distance[path.get(i - 1)][path.get(j)] + m_Distance[path.get(i)][path.get(j+1)];		
} else {
	
			scoreOne = m_Distance[path.get(i - 1)][path.get(i)] + m_Distance[path.get(i)][path.get(i + 1)]
					+ m_Distance[path.get(j - 1)][path.get(j)] + m_Distance[path.get(j)][path.get(j+1)];

			scoreTwo = m_Distance[path.get(i - 1)][path.get(j)] + m_Distance[path.get(j)][path.get(i + 1)]
					+ m_Distance[path.get(j - 1)][path.get(i)] + m_Distance[path.get(i)][path.get(j+1)];
			
			
		}
		}
		
		
		delta = scoreTwo - scoreOne;
		return delta;
	}
	

	
	public void read_data() {
		read_txt_file();
		readCSVfile("one.csv");
	}
	
	public void prepare_data() {
		fill_set_profit();
		fill_set_cluster();
		fill_points_profit();
		fill_points_cluster();
	}
	
	public void generate_first_path() {
		double timeBudget;
		double lastDistance;
		double nextDistance;
		verify_clear_list(firstPath);
		firstPath.add(0);
		firstPath.add(randomPath.get(1));
		timeBudget = tMax - (m_Distance[randomPath.get(0)][randomPath.get(1)]);

		for (int i = 1; i < randomPath.size() - 1; i++) {
			nextDistance = m_Distance[randomPath.get(i)][randomPath.get(i + 1)];
			lastDistance = m_Distance[randomPath.get(i + 1)][0];
			timeBudget = timeBudget - nextDistance - lastDistance;

			if (timeBudget > 0) {

				firstPath.add(randomPath.get(i + 1));
			}

		}
		firstPath.add(0);
		System.out.println(firstPath);
		total_path_distance= calculate_path_distance(firstPath);
		total_path_profit= calculate_path_profit(firstPath);

		
	}
	
	
	private double calculate_path_distance(List<Integer> pPath) {
		double distance = 0;
		for (int i = 0; i < pPath.size() - 1; i++) {
			distance = distance + m_Distance[pPath.get(i)][pPath.get(i + 1)];
		}
		System.out.println("path distance is  " + distance);
		return distance;
	}
	
	private int calculate_path_profit(List<Integer> path) {
		int profit_path = 0;
		for (int i = 0; i < path.size() ; i++) {
			profit_path = profit_path + points_profit[path.get(i)] ;
		}
		System.out.println("The new profit is " + profit_path);
		return profit_path;
	}

	
//  This function is created only to generate the first random path	

	public void generateInitialSolution() {
		verify_clear_list(randomPath);
		randomPath.add(0);
		int index = 0;
		for (int i = 1; i < set_number; i++) {
			index = findNearstPoint(index, randomSet.get(i));
			randomPath.add(index);
		}
		randomPath.add(0);
		System.out.println(randomPath);

	}
	
	private void verify_clear_list(List<Integer> list) {
		if (list.size() > 0) {
			list.clear();
		}
	}
	
	private int findNearstPoint(int pointA, int clusterB) {
		double distance;
//			int currentProfit;
		int index = set_cluster[clusterB][0];
		distance = m_Distance[pointA][index];
		for (int i = 1; i < set_cluster[clusterB].length; i++) {
			if (distance > m_Distance[pointA][set_cluster[clusterB][i]]) {
				distance = m_Distance[pointA][set_cluster[clusterB][i]];
				index = set_cluster[clusterB][i];
			}
		}
//			// We add the new distance to the total distance
//			distanceTraveled = distanceTraveled + distance;

//			currentProfit = set_profit[clusterB];
//			// we add the new profit to the total profit
//			totalProfit = totalProfit + currentProfit;
		return index;
	}
	
	//Generate randomly the first path of clusters indexes starts and return to the origin.		
		public void generateRandomSet() {
			verify_clear_list(randomSet);
			initialisePath();
			Collections.shuffle(randomSet.subList(1, set_number));
			System.out.println(randomSet);
		}

		private void initialisePath() {
			for (int i = 0; i < set_number; i++) {
				randomSet.add(i);
			}
			randomSet.add(0);
		}
	
	// fill_set_profit is : each cluster has its profit ****set_profit
		private void fill_set_profit() {
			set_profit = new int[set_number];
			for (int i = 0; i < set_number; i++) {
				set_profit[i] = set_data[i][1];
//					System.out.println(set_profit[i]);
			}
		}

		// fill_set_cluster is : each cluster has its vertices****set_cluster

		private void fill_set_cluster() {
			set_cluster = new int[set_number][];
			for (int i = 0; i < set_number; i++) {
				set_cluster[i] = new int[set_data[i].length - 2];
				for (int j = 0; j < (set_data[i].length - 2); j++) {
					set_cluster[i][j] = set_data[i][j + 2] - 1;

				}
			}
		}
		
		/*
		 * the index of the array is the id point and the value of each case is the
		 * profit****points_profit
		 */

		private void fill_points_profit() {
			int index;
			for (int i = 0; i < set_data.length; i++) {
				for (int j = 2; j < set_data[i].length; j++) {
					index = set_data[i][j] - 1;
					points_profit[index] = set_data[i][1];
				}

			}

		}
		
		/*
		 * the index of the array is the id point and the value of each case is the
		 * cluster id****points_cluster
		 */

		private void fill_points_cluster() {
			int index;
			for (int i = 0; i < set_data.length; i++) {
				for (int j = 2; j < set_data[i].length; j++) {
					index = set_data[i][j] - 1;
					points_cluster[index] = i;

//						System.out.println("index " + index + "  " + points_cluster[index]);
				}

			}

		}
	
	private void fillDistanceMatrix() {
		for (int i = 0; i < pointsNumber; i++) {
			for (int j = 0; j < pointsNumber; j++) {
				m_Distance[i][j] = calculateDistanceBetweenPoints(i, j);
			}

		}
	}
		private double calculateDistanceBetweenPoints(int a, int b) {
			double x1 = points[a][0];
			double x2 = points[b][0];
			double y1 = points[a][1];
			double y2 = points[b][1];
			return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
		}
	
	public void read_txt_file() {
		Read_data_set fileReader = new Read_data_set();
		set_data = fileReader.read("file.txt");
		set_number = set_data.length;
	}
	
	public void readCSVfile(String fileName) {
		int i = 0;
		File file = new File(fileName);
		try {
			Scanner inputStream = new Scanner(file);
			inputStream.next();
			while (inputStream.hasNext()) {
				String data = inputStream.next();
				String values[] = data.split(",");
				for (int j = 0; j < 2; j++) {
					points[i][j] = Double.parseDouble(values[j + 1]);
				}
				i++;
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public List<Integer> get_clusters_from_path(List<Integer> pSolution) {
		List<Integer> path_of_clusters = new ArrayList<Integer>();
		int a;
		for (int i = 0; i < pSolution.size(); i++) {
			a = points_cluster[pSolution.get(i)];
			path_of_clusters.add(a);
		}
		System.out.println(path_of_clusters);
		return path_of_clusters;
	}
	
	public void compare_results(List<Integer> pSolution) {
		double score =calculate_path_profit(pSolution);
//		System.out.println("the score before comparision "+ score);
//		System.out.println("the best_score before comparision "+ best_score_solution);
		if (score > best_score_solution) {
			best_score_solution = score;
			verify_clear_list(best_solution);
			best_solution.addAll(pSolution);

		}
//		solution.clear();

	}
	

	
}
