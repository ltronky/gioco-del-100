package it.sblt;


import it.sblt.ui.MainPanel;

import java.util.ArrayList;

import solver.Solver;
import solver.constraints.IntConstraintFactory;
import solver.search.loop.monitors.SearchMonitorFactory;
import solver.variables.IntVar;
import solver.variables.VariableFactory;


public class GameOf100 {

	MainPanel ui = new MainPanel();

	public GameOf100() { 
		Solver solver = new Solver("100_Game");
		IntVar variableList[] = new IntVar[1];
		variableList[0] = VariableFactory.enumerated(Integer.toString(1), 0, 0, solver);

		ui.printNumber("1", 0);
		if (executeSubProblem(variableList, 0)) {
			System.out.println("FUZIONA!!!!");
		}
	}
	
	public boolean executeSubProblem(IntVar[] old, int lastIndex) {
		
		int x = old[lastIndex].getValue();
		ArrayList<Integer> availablePositions = new ArrayList<Integer>();
		if (				(x + 3)-((x + 3)%10) == x-(x%10)) availablePositions.add(x + 3);
		if ((x - 3)>0    && (x - 3)-((x - 3)%10) == x-(x%10)) availablePositions.add(x - 3);
		if ((x + 30)<100 && (x + 30)%10 == x%10) availablePositions.add(x + 30);
		if ((x - 30)>0   && (x - 30)%10 == x%10) availablePositions.add(x - 30);
		if (				((x + 18)%100)-((x + 18)%10) == (x + 20)-((x + 20)%10)) availablePositions.add(x + 18);
		if (				((x + 22)%100)-((x + 22)%10) == (x + 20)-((x + 20)%10)) availablePositions.add(x + 22);
		if ((x - 18)>0   && ((x - 18)%100)-((x - 18)%10) == (x - 20)-((Math.abs(x - 20))%10)) availablePositions.add(x - 18);
		if ((x - 22)>0   && ((x - 22)%100)-((x - 22)%10) == (x - 20)-((Math.abs(x - 20))%10)) availablePositions.add(x - 22);
		
		while (!availablePositions.isEmpty()) {
			Solver solver = new Solver("subProblem " + (lastIndex+2));
			SearchMonitorFactory.log(solver, true, true);

			IntVar[] variableList = new IntVar[lastIndex + 2];
			for (int i = 0; i < variableList.length-1; i++) {
				variableList[i] = VariableFactory.enumerated(old[i].getName(), old[i].getValue(),  old[i].getValue(), solver);
			}

			variableList[lastIndex+1] = VariableFactory.enumerated(Integer.toString(lastIndex+2), 1, 99, solver);
			
			solver.post(IntConstraintFactory.alldifferent(variableList, "BC"));//CONSISTENCY := AC, BoundConsistency, weak_BC, NEQS, DEFAULT


			System.out.println("CANE " + availablePositions.toString());
			for (int i = 1; i < 100; i++) {
				boolean test = true;
				for (int j = 0; j < availablePositions.size(); j++) {
					if (availablePositions.get(j) == i){
						test = false;
					}
				}
				if (test) {
					solver.post(IntConstraintFactory.arithm(variableList[lastIndex+1], "!=", i));
				}
			}



//			solver.set(IntStrategyFactory.inputOrder_InDomainMin(variableList)); 
			boolean result = solver.findSolution();
			
			if (result && lastIndex+1<100) {
				ui.printNumber(variableList[lastIndex+1].getName(), variableList[lastIndex+1].getValue());
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				executeSubProblem(variableList, lastIndex+1);
				
			} else {
				if (result) {
					System.out.println("PROBLEMA RISOLTO");
					return true;
				} else {
					System.out.println("ERRORE");
					break;
//					for (int i = 0; i < availablePositions.size(); i++) {
//						if (variableList[lastIndex+1].getValue() == availablePositions.get(i).intValue()) {
//							availablePositions.remove(i);
//						}
//					}
				}
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//END WHILE
		return false;
	}

	public static void main(String[] args) {
		new GameOf100();

	}
}
