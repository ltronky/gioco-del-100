package it.sblt;


import it.sblt.ui.MainPanel;

import java.util.ArrayList;
import java.util.Random;


public class GameOf100 {

	private static final long FAIL_BACKTRACK_TIME = 20;
	private static final long POST_PAINT_CELL_TIME = 4;//400
	private static final long WAIT_BEFORE_FIND_SOLUTION_TIME = 7;//700
	private static final long WAIT_BEFORE_EXECUTE_SUBPROBLEM_TIME = 10;//1000

	private int dim;

	private int backTrack = 0;

	private MainPanel ui  = new MainPanel();

	public GameOf100() { 
		ui.recursiveAlgorithm = this;
	}

	public void startRecursiveAlgorithm(int d) throws Exception{
		dim = d;
		Numbr variableList[] = new Numbr[1];
		variableList[0] = new Numbr(Integer.toString(1), 0);
		backTrack = 0;
		ui.printNumber("1", 0);
		long startTime = System.currentTimeMillis();
		if (executeSubProblem(variableList, 0)) {
			System.out.println("ComputingTime= " + (System.currentTimeMillis() - startTime) + " backtrack= " + backTrack);
			ui.setStatusFieldLabel("Stop : TERMINATO");
		} else {
			ui.setStatusFieldLabel("Stop : FALLITO");
		}
		ui.resetRunThread();
	}

	public boolean executeSubProblem(Numbr[] old, int lastIndex) throws Exception{

		if (Thread.interrupted()) {
			throw new Exception("INTERRUZIONE MANUALE");
		}
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
							SLEEP(ui.speed * POST_PAINT_CELL_TIME);
						}			
					}
				}
			}

			SLEEP(ui.speed * WAIT_BEFORE_FIND_SOLUTION_TIME);

			if (lastIndex+1<=dim*dim-1) {
				if (findSolution(variableList, availablePositions)) {

					ui.setSelectedCell(variableList[lastIndex+1].value);
					SLEEP(ui.speed * POST_PAINT_CELL_TIME);
					ui.printNumber(variableList[lastIndex+1].name, variableList[lastIndex+1].value);
					ui.setNextNumFieldLabel(String.valueOf(lastIndex+3));

					SLEEP(ui.speed * WAIT_BEFORE_EXECUTE_SUBPROBLEM_TIME);


					if (!executeSubProblem(variableList, lastIndex+1)) {
						ui.setFailCell(variableList[lastIndex+1].value);
						ui.setNextNumFieldLabel(String.valueOf(lastIndex+2));
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

					backTrack++;
					ui.setBackTrakingLabel(String.valueOf(backTrack)); 
					return false;
				}
			} else {
				return true;
			}
			SLEEP(ui.speed * FAIL_BACKTRACK_TIME);
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


	private void SLEEP(long time){
		if (ui.speed != 0){
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	public static void main(String[] args) {
		GameOf100 gameOf100 = new GameOf100();
		new GD100OL(gameOf100.ui);
	}
}
