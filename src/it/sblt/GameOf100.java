package it.sblt;


import it.sblt.ui.MainPanel;

import java.util.ArrayList;

import parser.flatzinc.ChocoFZN;
import solver.Solver;
import solver.constraints.IntConstraintFactory;
import solver.search.loop.monitors.SearchMonitorFactory;
import solver.search.strategy.IntStrategyFactory;
import solver.variables.IntVar;
import solver.variables.VariableFactory;


public class GameOf100 {


	public GameOf100() {
		// 1. Create a Solver 
		Solver solver = new Solver("100_Game");
		
		
		// 2. Create variables through the variable factory
		IntVar variableList[] = new IntVar[100];
		variableList[0] = VariableFactory.enumerated(Integer.toString(1), 0, 0, solver);//bounded(Integer.toString(1), 1, 1, solver);
		for (int i = 1; i < 100; i++) {
			variableList[i] = VariableFactory.enumerated(Integer.toString(i+1), 1, 99, solver);
		}
		
		
		// 3. Create and post constraints by using constraint factories
		solver.post(IntConstraintFactory.alldifferent(variableList, "BC"));//CONSISTENCY := AC, BoundConsistency, weak_BC, NEQS, DEFAULT
		
		for (int i = 0; i < 100; i++) {
			if (i != 3 && i != 22 && i != 30) {
				solver.post(IntConstraintFactory.arithm(variableList[1], "!=", i));
			}
		}
		SearchMonitorFactory.log(solver, true, true);
		
		
		// 4. Define the search strategy
		solver.set(IntStrategyFactory.inputOrder_InDomainMin(variableList)); 
		// 5. Launch the resolution process
		solver.findSolution();
		
//		for (int i = 0; i < variableList.length; i++) {
//			System.out.println("Var " + i + " = " + variableList[i].getValue() + " " + variableList[i].getDomainSize());
//		}
		
//		MainPanel m = new MainPanel();	
	}	 
	
	public static void main(String[] args) {
		new GameOf100();
		
	}
}
