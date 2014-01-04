package it.sblt;


import it.sblt.ui.MainPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class GameOf100 {
	
	private static final int speed = 0;
	private static final long FAIL_BACKTRACK_TIME = 20*speed;
	private static final long POST_PAINT_CELL_TIME = 4*speed;//400
	private static final long WAIT_BEFORE_FIND_SOLUTION_TIME = 7*speed;//700
	private static final long WAIT_BEFORE_EXECUTE_SUBPROBLEM_TIME = 10*speed;//1000
	
	private final int dim = 10;
	
	private MainPanel ui = new MainPanel(dim);

	public GameOf100() { 
		Numbr variableList[] = new Numbr[1];
		variableList[0] = new Numbr(Integer.toString(1), 0);

		ui.printNumber("1", 0);
		if (executeSubProblem(variableList, 0)) {
			System.out.println("FUZIONA!!!!");
			ui.setStatusFieldLabel("Stop : TERMINATO");
		} else {
			ui.setStatusFieldLabel("Stop : FALLITO");
		}
	}

	public boolean executeSubProblem(Numbr[] old, int lastIndex) {

		int x = old[lastIndex].value;
		ArrayList<Integer> availablePositions = new ArrayList<Integer>();
		if (						(x + 3)-((x + 3)%dim) == x-(x%dim)) availablePositions.add(x + 3);
		if ((x - 3)>0    		 && (x - 3)-((x - 3)%dim) == x-(x%dim)) availablePositions.add(x - 3);
		if ((x + 3*dim)<dim*dim  && (x + 3*dim)%dim == x%dim) availablePositions.add(x + 3*dim);
		if ((x - 3*dim)>0		 && (x - 3*dim)%dim == x%dim) availablePositions.add(x - 3*dim);
		if (						((x + 2*dim-2)%(dim*dim))-((x + 2*dim-2)%dim) == (x + 2*dim)-((x + 2*dim)%dim)) availablePositions.add(x + 2*dim-2);
		if (						((x + 2*dim+2)%(dim*dim))-((x + 2*dim+2)%dim) == (x + 2*dim)-((x + 2*dim)%dim)) availablePositions.add(x + 2*dim+2);
		if ((x - 2*dim-2)>0   	 && ((x - 2*dim-2)%(dim*dim))-((x - 2*dim-2)%dim) == (x - 2*dim)-((Math.abs(x - 2*dim))%dim)) availablePositions.add(x - 2*dim-2);
		if ((x - 2*dim+2)>0   	 && ((x - 2*dim+2)%(dim*dim))-((x - 2*dim+2)%dim) == (x - 2*dim)-((Math.abs(x - 2*dim))%dim)) availablePositions.add(x - 2*dim+2);

		while (!availablePositions.isEmpty()) {
			Numbr[] variableList = new Numbr[lastIndex + 2];
			for (int i = 0; i < variableList.length-1; i++) {
				variableList[i] = new Numbr(old[i].name, old[i].value);
			}

			variableList[lastIndex+1] = new Numbr(Integer.toString(lastIndex+2), -1);

			System.out.println("Available Positions " + availablePositions.toString());
			for (int i = 1; i < dim*dim; i++) {
				boolean test = true;
				for (int j = 0; j < availablePositions.size() && test; j++) {
					if (availablePositions.get(j) == i){
						test = false;
						boolean test1 = true;
						for (int k = 0; k < lastIndex+1 && test1; k++) {
							if (variableList[k].value == i) {
								test1 = false;
							}
						}
						if (test1){
							ui.setAvailableCell(i);
							SLEEP(POST_PAINT_CELL_TIME);
						}			
					}
				}
//				if (test) {
//					solver.post(IntConstraintFactory.arithm(variableList[lastIndex+1], "!=", i));
//				}
			}

			SLEEP(WAIT_BEFORE_FIND_SOLUTION_TIME);

//			solver.set(IntStrategyFactory.inputOrder_InDomainMin(variableList)); 

			if (lastIndex+1<=dim*dim-1) {
				if (findSolution(variableList, availablePositions)) {

					ui.setSelectedCell(variableList[lastIndex+1].value);
					SLEEP(POST_PAINT_CELL_TIME);
					ui.printNumber(variableList[lastIndex+1].name, variableList[lastIndex+1].value);
					ui.setNextNumFieldLabel(String.valueOf(lastIndex+3));
					
					SLEEP(WAIT_BEFORE_EXECUTE_SUBPROBLEM_TIME);


					if (!executeSubProblem(variableList, lastIndex+1)) {
						ui.setFailCell(variableList[lastIndex+1].value);
						ui.printNumber("", variableList[lastIndex+1].value);
						for (int i = 0; i < availablePositions.size(); i++) {
							if (variableList[lastIndex+1].value == availablePositions.get(i).intValue()) {
								availablePositions.remove(i);
							}
						}
					} else {
						return true;
					}
				} else {

					System.out.println("ERRORE");
					ui.setStatusFieldLabel("Stop : BACKTRACK");
					return false;
				}
			} else {
				System.out.println("PROBLEMA RISOLTO");
				saveSolution(variableList);
				return true;
			}
			SLEEP(FAIL_BACKTRACK_TIME);
		}//END WHILE
		return false;
	}

	private boolean findSolution(Numbr[] variableList, ArrayList<Integer> availablePositions) {
		for (int i = 0; i < availablePositions.size(); i++) {
			boolean ck = true;
			for (int j = 0; j < variableList.length && ck; j++) {
				if (availablePositions.get(i) == variableList[j].value) {
					availablePositions.remove(i);
					i--;
					ck = false;
				}
			}
		}
		if (availablePositions.size()>0) {
			variableList[variableList.length-1].value = availablePositions.get(new Random().nextInt(availablePositions.size()));
			return true;
		}
		return false;
	}

	private void saveSolution(Numbr[] varList) {
		ui.printInLog(Arrays.toString(varList));
	}
	
	private void SLEEP(long time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new GameOf100();
//		new GD100FV();
	}
}
