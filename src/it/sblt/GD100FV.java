package it.sblt;

import it.sblt.ui.MainPanel;

import java.util.ArrayList;
import java.util.Arrays;

import solver.Solver;
import solver.constraints.IntConstraintFactory;
import solver.search.loop.monitors.SearchMonitorFactory;
import solver.variables.IntVar;
import solver.variables.VariableFactory;

public class GD100FV {

	private static final long FAIL_BACKTRACK_TIME = 2000;
	private static final long POST_PAINT_CELL_TIME = 400;//400
	private static final long WAIT_BEFORE_FIND_SOLUTION_TIME = 700;//700
	private static final long WAIT_BEFORE_EXECUTE_SUBPROBLEM_TIME = 1000;//1000
	
	private final int dim = 6;
	
	private MainPanel ui = new MainPanel(dim);

	public GD100FV() { 
		Solver solver = new Solver("100_Game");
		IntVar variableList[] = new IntVar[1];
		variableList[0] = VariableFactory.enumerated(Integer.toString(1), 0, 0, solver);

		ui.printNumber("1", 0);
		if (executeSubProblem(variableList, 0)) {
			System.out.println("FUZIONA!!!!");
			ui.setStatusFieldLabel("Stop : TERMINATO");
		} else {
			ui.setStatusFieldLabel("Stop : FALLITO");
		}
	}

	public boolean executeSubProblem(IntVar[] old, int lastIndex) {

		int x = old[lastIndex].getValue();
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
			Solver solver = new Solver("subProblem " + (lastIndex+2));
			SearchMonitorFactory.log(solver, true, true);

			IntVar[] variableList = new IntVar[lastIndex + 2];
			for (int i = 0; i < variableList.length-1; i++) {
				variableList[i] = VariableFactory.enumerated(old[i].getName(), old[i].getValue(),  old[i].getValue(), solver);
			}

			variableList[lastIndex+1] = VariableFactory.enumerated(Integer.toString(lastIndex+2), 1, dim*dim-1, solver);

			solver.post(IntConstraintFactory.alldifferent(variableList, "BC"));//CONSISTENCY := AC, BoundConsistency, weak_BC, NEQS, DEFAULT


			System.out.println("Available Positions " + availablePositions.toString());
			for (int i = 1; i < dim*dim; i++) {
				boolean test = true;
				for (int j = 0; j < availablePositions.size() && test; j++) {
					if (availablePositions.get(j) == i){
						test = false;
						boolean test1 = true;
						for (int k = 0; k < lastIndex+1 && test1; k++) {
							if (variableList[k].getValue() == i) {
								test1 = false;
							}
						}
						if (test1){
							ui.setAvailableCell(i);
							SLEEP(POST_PAINT_CELL_TIME);
						}			
					}
				}
				if (test) {

					solver.post(IntConstraintFactory.arithm(variableList[lastIndex+1], "!=", i));
				}
			}

			SLEEP(WAIT_BEFORE_FIND_SOLUTION_TIME);

//			solver.set(IntStrategyFactory.inputOrder_InDomainMin(variableList)); 

			if (lastIndex+1<=dim*dim-1) {
				if (solver.findSolution()) {

					ui.setSelectedCell(variableList[lastIndex+1].getValue());
					SLEEP(POST_PAINT_CELL_TIME);
					ui.printNumber(variableList[lastIndex+1].getName(), variableList[lastIndex+1].getValue());
					ui.setNextNumFieldLabel(String.valueOf(lastIndex+3));
					
					SLEEP(WAIT_BEFORE_EXECUTE_SUBPROBLEM_TIME);


					if (!executeSubProblem(variableList, lastIndex+1)) {
						ui.setFailCell(variableList[lastIndex+1].getValue());
						ui.printNumber("", variableList[lastIndex+1].getValue());
						for (int i = 0; i < availablePositions.size(); i++) {
							if (variableList[lastIndex+1].getValue() == availablePositions.get(i).intValue()) {
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

	private void saveSolution(IntVar[] varList) {
		ui.printInLog(Arrays.toString(varList));
	}
	
	private void SLEEP(long time){
//		try {
//			Thread.sleep(time);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}
	
}