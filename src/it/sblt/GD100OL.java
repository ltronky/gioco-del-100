package it.sblt;

import it.sblt.ui.MainPanel;

import java.util.Arrays;

import solver.Solver;
import solver.constraints.Constraint;
import solver.constraints.ICF;
import solver.constraints.LogicalConstraintFactory;
import solver.search.loop.monitors.SearchMonitorFactory;
import solver.variables.IntVar;
import solver.variables.VariableFactory;
import util.tools.StringUtils;

public class GD100OL {

//	private static final int speed = 0;
//	private static final long FAIL_BACKTRACK_TIME = 20*speed;
//	private static final long POST_PAINT_CELL_TIME = 4*speed;//400
//	private static final long WAIT_BEFORE_FIND_SOLUTION_TIME = 7*speed;//700
//	private static final long WAIT_BEFORE_EXECUTE_SUBPROBLEM_TIME = 10*speed;//1000

	private final int dim = 10;

	private MainPanel ui = new MainPanel(dim);

	public GD100OL() {
		Solver solver = new Solver("100_Game");
		SearchMonitorFactory.log(solver, true, true);

		IntVar variableList[] = new IntVar[100];
		variableList[0] = VariableFactory.enumerated(Integer.toString(1), 0, 0, solver);
		ui.printNumber("1", 0);

		IntVar dimensionIV = VariableFactory.fixed(dim, solver);
		IntVar dimension2IV = VariableFactory.fixed(dim*dim, solver);
		
		
		for (int i = 0; i < variableList.length; i++) {
			variableList[i] = VariableFactory.enumerated(Integer.toString(i+1), 1, dim*dim-1, solver);
		}
		solver.post(ICF.alldifferent(variableList, "BC"));//CONSISTENCY := AC, BoundConsistency, weak_BC, NEQS, DEFAULT

		int[] c1 = new int[]{2*dim-2, 2*dim+2};
		int dueDim = 2*dim;
		
		for (int i = 0; i < variableList.length-1; i++) {
//(x + 3);
			IntVar xp3 = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
			solver.post(ICF.arithm(xp3, "-",variableList[i], "=", 3));

			IntVar xp3Mod = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
			solver.post(ICF.mod(xp3, dimensionIV, xp3Mod));
			
			IntVar xMod = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
			solver.post(ICF.mod(variableList[i], dimensionIV, xMod));
			
			IntVar xMinusXMod = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
			IntVar negXMod = VariableFactory.minus(xMod); 
			IntVar rSum = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
			solver.post(ICF.sum(new IntVar[]{variableList[i],negXMod}, rSum));
			solver.post(ICF.arithm(rSum, "=", xMinusXMod));
			
			IntVar negXp3Mod = VariableFactory.minus(xp3Mod);
			IntVar tSum = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
			solver.post(ICF.sum(new IntVar[]{xp3,negXp3Mod}, tSum));
			
			solver.post(LogicalConstraintFactory.ifThen(ICF.arithm(tSum ,"=", xMinusXMod), ICF.arithm(variableList[i+1], "-", variableList[i] ,"=", 3)));
			
//(x - 3);
			IntVar xm3 = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
			solver.post(ICF.arithm(xp3, "-",variableList[i], "=", -3));

			IntVar xm3Mod = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
			solver.post(ICF.mod(xm3, dimensionIV, xm3Mod));
			
			IntVar negXm3Mod = VariableFactory.minus(xm3Mod);
			IntVar tSumXM3 = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
			solver.post(ICF.sum(new IntVar[]{xm3,negXm3Mod}, tSumXM3));
			
			Constraint and1 = LogicalConstraintFactory.and(ICF.arithm(xm3, ">", 0), ICF.arithm(tSumXM3 ,"=", xMinusXMod));
			solver.post(LogicalConstraintFactory.ifThen(and1, ICF.arithm(variableList[i+1], "-", variableList[i] ,"=", -3)));
			

//(x + 3*dim)			
			IntVar xp3xDim = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
			solver.post(ICF.arithm(xp3xDim, "-",variableList[i], "=", 3*dim));
			
			IntVar xp3xDimMod = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
			solver.post(ICF.mod(xp3xDim, dimensionIV, xp3xDimMod));
			
			Constraint and2 = LogicalConstraintFactory.and(ICF.arithm(xp3xDim, "<", dim*dim), ICF.arithm(xp3xDimMod ,"=", xMod));
			solver.post(LogicalConstraintFactory.ifThen(and2, ICF.arithm(variableList[i+1], "-", variableList[i] ,"=", 3*dim)));
			
			
//(x - 3*dim)
			IntVar xm3xDim = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
			solver.post(ICF.arithm(xm3xDim, "-",variableList[i], "=", -3*dim));
			
			IntVar xm3xDimMod = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
			solver.post(ICF.mod(xm3xDim, dimensionIV, xm3xDimMod));
			
			Constraint and3 = LogicalConstraintFactory.and(ICF.arithm(xm3xDim, ">", 0), ICF.arithm(xm3xDimMod ,"=", xMod));
			solver.post(LogicalConstraintFactory.ifThen(and3, ICF.arithm(variableList[i+1], "-", variableList[i] ,"=", -3*dim)));
			
			
//(x + 2*dim-2) && (x + 2*dim+2)
			IntVar xp2Dim = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
			solver.post(ICF.arithm(xp2Dim, "-",variableList[i], "=", 2*dim));
			IntVar xp2DimMod = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
			solver.post(ICF.mod(xp2Dim, dimensionIV, xp2DimMod));
			
			IntVar negXp2DimMod = VariableFactory.minus(xp2DimMod);
			IntVar sum1 = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
			solver.post(ICF.sum(new IntVar[]{xp2Dim,negXp2DimMod}, sum1));
			
			for (int j = 0; j < c1.length; j++) {
				IntVar xpC1 = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
				solver.post(ICF.arithm(xpC1, "-",variableList[i], "=", c1[j]));
				
				IntVar xpC1Mod2 = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
				solver.post(ICF.mod(xpC1, dimension2IV, xpC1Mod2));
				
				IntVar xpC1Mod = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
				solver.post(ICF.mod(xpC1, dimensionIV, xpC1Mod));
				
				IntVar negXpC1Mod = VariableFactory.minus(xpC1Mod);
				IntVar sum2 = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
				solver.post(ICF.sum(new IntVar[]{xpC1Mod2,negXpC1Mod}, sum2));
				
				solver.post(LogicalConstraintFactory.ifThen(ICF.arithm(sum2, "=", sum1), ICF.arithm(variableList[i+1], "-", variableList[i] ,"=", c1[j])));
			}
			
//(x + 2*dim-2) && (x + 2*dim+2)
			IntVar xm2Dim = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
			solver.post(ICF.arithm(xm2Dim, "-",variableList[i], "=", -2*dim));
			
			IntVar xm2DimAbs = VariableFactory.abs(xm2Dim);
			IntVar xm2DimAbsMod = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
			solver.post(ICF.mod(xm2DimAbs, dimensionIV, xm2DimAbsMod));
			
			IntVar negXm2DimAbsMod = VariableFactory.minus(xm2DimAbsMod);
			IntVar sum4 = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
			solver.post(ICF.sum(new IntVar[]{xm2Dim,negXm2DimAbsMod}, sum4));
			
			for (int j = 0; j < c1.length; j++) {
				IntVar xmC1 = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
				solver.post(ICF.arithm(xmC1, "-",variableList[i], "=", -c1[j]));
				
				IntVar xmC1Mod2 = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
				solver.post(ICF.mod(xmC1, dimension2IV, xmC1Mod2));
				
				IntVar xmC1Mod = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
				solver.post(ICF.mod(xmC1, dimensionIV, xmC1Mod));
				
				IntVar negXmC1Mod = VariableFactory.minus(xmC1Mod);
				IntVar sum3 = VariableFactory.bounded(StringUtils.randomName(), 0, 200, solver);
				solver.post(ICF.sum(new IntVar[]{xmC1Mod2,negXmC1Mod}, sum3));
				
				Constraint and4 = LogicalConstraintFactory.and(ICF.arithm(xmC1, ">", 0), ICF.arithm(sum3, "=", sum4));
				solver.post(LogicalConstraintFactory.ifThen(and2, ICF.arithm(variableList[i+1], "-", variableList[i] ,"=", -c1[j])));
			}
		}
		
		
//		solver.set(IntStrategyFactory.inputOrder_InDomainMin(variableList));
		if (solver.findSolution()) {
			System.out.println("FUZIONA!!!!");
			ui.setStatusFieldLabel("Stop : TERMINATO");
			
			for (int i = 0; i < variableList.length; i++) {
				ui.printNumber(variableList[i].getName(), variableList[i].getValue());
			}
			
		} else {
			ui.setStatusFieldLabel("Stop : FALLITO");
		}
	}

	private void saveSolution(IntVar[] varList) {
		ui.printInLog(Arrays.toString(varList));
	}
	
//	private ArrayList<Integer> getAvailablePositions(int x) {
//		ArrayList<Integer> availablePositions = new ArrayList<Integer>();
//		if (						(x + 3)-((x + 3)%dim) == x-(x%dim)) availablePositions.add(x + 3);
//		if ((x - 3)>0    		 && (x - 3)-((x - 3)%dim) == x-(x%dim)) availablePositions.add(x - 3);
//		if ((x + 3*dim)<dim*dim  && (x + 3*dim)%dim == x%dim) availablePositions.add(x + 3*dim);
//		if ((x - 3*dim)>0		 && (x - 3*dim)%dim == x%dim) availablePositions.add(x - 3*dim);
//		if (						((x + 2*dim-2)%(dim*dim))-((x + 2*dim-2)%dim) == (x + 2*dim)-((x + 2*dim)%dim)) availablePositions.add(x + 2*dim-2);
//		if (						((x + 2*dim+2)%(dim*dim))-((x + 2*dim+2)%dim) == (x + 2*dim)-((x + 2*dim)%dim)) availablePositions.add(x + 2*dim+2);
//		if ((x - 2*dim-2)>0   	 && ((x - 2*dim-2)%(dim*dim))-((x - 2*dim-2)%dim) == (x - 2*dim)-((Math.abs(x - 2*dim))%dim)) availablePositions.add(x - 2*dim-2);
//		if ((x - 2*dim+2)>0   	 && ((x - 2*dim+2)%(dim*dim))-((x - 2*dim+2)%dim) == (x - 2*dim)-((Math.abs(x - 2*dim))%dim)) availablePositions.add(x - 2*dim+2);
//		return availablePositions;
//	}
	
	private void SLEEP(long time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
