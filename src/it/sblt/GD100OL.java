package it.sblt;

import it.sblt.ui.MainPanel;

import java.util.ArrayList;
import java.util.Arrays;

import solver.Solver;
import solver.constraints.Constraint;
import solver.constraints.ICF;
import solver.constraints.LCF;
import solver.search.loop.monitors.SearchMonitorFactory;
import solver.search.strategy.IntStrategyFactory;
import solver.variables.IntVar;
import solver.variables.VariableFactory;
import util.tools.StringUtils;

public class GD100OL {

	private MainPanel ui;

	public GD100OL(MainPanel main) {
		ui = main; 
		ui.cspAlgorithm = this;
	}

	public void startAlgorithm(int dim) {

		System.out.println("start CHOCO");
		Solver solver = new Solver("100_Game");
		SearchMonitorFactory.log(solver, true, true);

		IntVar variableList[] = new IntVar[dim*dim];
		variableList[0] = VariableFactory.enumerated(Integer.toString(1), 0, 0, solver);
		ui.printNumber("1", 0);

		IntVar dimensionIV = VariableFactory.fixed(dim, solver);
		IntVar dimension2IV = VariableFactory.fixed(dim*dim, solver);


		for (int i = 1; i < dim*dim; i++) {
			variableList[i] = VariableFactory.enumerated(Integer.toString(i+1), 1, dim*dim-1, solver);
		}
		solver.post(ICF.alldifferent(variableList, "BC"));

		int[] c1 = new int[]{2*dim-2, 2*dim+2};
		int dueDim = 2*dim;

		for (int i = 0; i < variableList.length-1; i++) {
			ArrayList<Constraint> clauses = new ArrayList<Constraint>();
			//(x + 3);
			{
				IntVar xp3 = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.arithm(xp3, "-",variableList[i], "=", 3));

				IntVar xp3Mod = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.mod(xp3, dimensionIV, xp3Mod));

				IntVar xMod = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.mod(variableList[i], dimensionIV, xMod));

				IntVar xMinusXMod = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				IntVar negXMod = VariableFactory.minus(xMod); 
				IntVar rSum = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.sum(new IntVar[]{variableList[i],negXMod}, rSum));
				solver.post(ICF.arithm(rSum, "=", xMinusXMod));

				IntVar negXp3Mod = VariableFactory.minus(xp3Mod);
				IntVar tSum = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.sum(new IntVar[]{xp3,negXp3Mod}, tSum));

				clauses.add(LCF.and(ICF.arithm(tSum ,"=", xMinusXMod), ICF.arithm(variableList[i+1], "-", variableList[i] ,"=", 3)));
			}
			//(x - 3);
			{
				IntVar xm3 = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.arithm(xm3, "-",variableList[i], "=", -3));

				IntVar xm3Mod = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.mod(xm3, dimensionIV, xm3Mod));

				IntVar xMod = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.mod(variableList[i], dimensionIV, xMod));

				IntVar xMinusXMod = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				IntVar negXMod = VariableFactory.minus(xMod); 
				IntVar rSum = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.sum(new IntVar[]{variableList[i],negXMod}, rSum));
				solver.post(ICF.arithm(rSum, "=", xMinusXMod));

				IntVar negXm3Mod = VariableFactory.minus(xm3Mod);
				IntVar tSumXM3 = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.sum(new IntVar[]{xm3,negXm3Mod}, tSumXM3));

				Constraint and1 = LCF.and(ICF.arithm(xm3, ">", -100), ICF.arithm(tSumXM3 ,"=", xMinusXMod));
				clauses.add(LCF.and(and1, ICF.arithm(variableList[i+1], "-", variableList[i] ,"=", -3)));
			}

			//(x + 3*dim)
			{
				IntVar xp3xDim = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.arithm(xp3xDim, "-",variableList[i], "=", 3*dim));

				IntVar xp3xDimMod = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.mod(xp3xDim, dimensionIV, xp3xDimMod));

				IntVar xMod = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.mod(variableList[i], dimensionIV, xMod));

				Constraint and2 = LCF.and(ICF.arithm(xp3xDim, "<", dim*dim), ICF.arithm(xp3xDimMod ,"=", xMod));
				clauses.add(LCF.and(and2, ICF.arithm(variableList[i+1], "-", variableList[i] ,"=", 3*dim)));
			}

			//(x - 3*dim)
			{
				IntVar xm3xDim = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.arithm(xm3xDim, "-",variableList[i], "=", -3*dim));

				IntVar xm3xDimMod = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.mod(xm3xDim, dimensionIV, xm3xDimMod));

				IntVar xMod = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.mod(variableList[i], dimensionIV, xMod));

				Constraint and3 = LCF.and(ICF.arithm(xm3xDim, ">", 0), ICF.arithm(xm3xDimMod ,"=", xMod));
				clauses.add(LCF.and(and3, ICF.arithm(variableList[i+1], "-", variableList[i] ,"=", -3*dim)));
			}

			//(x + 2*dim-2) && (x + 2*dim+2)
			for (int j = 0; j < c1.length; j++) {
				IntVar xp2Dim = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.arithm(xp2Dim, "-",variableList[i], "=", dueDim));
				IntVar xp2DimMod = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.mod(xp2Dim, dimensionIV, xp2DimMod));

				IntVar negXp2DimMod = VariableFactory.minus(xp2DimMod);
				IntVar sum1 = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.sum(new IntVar[]{xp2Dim,negXp2DimMod}, sum1));

				IntVar xpC1 = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.arithm(xpC1, "-",variableList[i], "=", c1[j]));

				IntVar xpC1Mod2 = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.mod(xpC1, dimension2IV, xpC1Mod2));

				IntVar xpC1Mod = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.mod(xpC1, dimensionIV, xpC1Mod));

				IntVar negXpC1Mod = VariableFactory.minus(xpC1Mod);
				IntVar sum2 = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.sum(new IntVar[]{xpC1Mod2,negXpC1Mod}, sum2));

				clauses.add(LCF.and(ICF.arithm(sum2, "=", sum1), ICF.arithm(variableList[i+1], "-", variableList[i] ,"=", c1[j])));
			}

			//(x - 2*dim-2) && (x - 2*dim+2)
			for (int j = 0; j < c1.length; j++) {
				IntVar xm2Dim = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.arithm(xm2Dim, "-",variableList[i], "=", -dueDim));

				IntVar xm2DimAbs = VariableFactory.abs(xm2Dim);
				IntVar xm2DimAbsMod = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.mod(xm2DimAbs, dimensionIV, xm2DimAbsMod));

				IntVar negXm2DimAbsMod = VariableFactory.minus(xm2DimAbsMod);
				IntVar sum4 = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.sum(new IntVar[]{xm2Dim,negXm2DimAbsMod}, sum4));

				IntVar xmC1 = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.arithm(xmC1, "-",variableList[i], "=", -c1[j]));

				IntVar xmC1Mod2 = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.mod(xmC1, dimension2IV, xmC1Mod2));

				IntVar xmC1Mod = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.mod(xmC1, dimensionIV, xmC1Mod));

				IntVar negXmC1Mod = VariableFactory.minus(xmC1Mod);
				IntVar sum3 = VariableFactory.bounded(StringUtils.randomName(), -1000, 2000, solver);
				solver.post(ICF.sum(new IntVar[]{xmC1Mod2,negXmC1Mod}, sum3));

				Constraint and4 = LCF.and(ICF.arithm(xmC1, ">", 0), ICF.arithm(sum3, "=", sum4));
				clauses.add(LCF.and(and4, ICF.arithm(variableList[i+1], "-", variableList[i] ,"=", -c1[j])));
			}

			ArrayList<Constraint> refactorClauses = new ArrayList<Constraint>();
			for (int j = 0; j < clauses.size(); j++) {
				ArrayList<Constraint> tmpClauses = new ArrayList<Constraint>();
				for (int j2 = 0; j2 < clauses.size(); j2++) {
					if (j2==j) {
						tmpClauses.add(clauses.get(j2));
					} else {
						tmpClauses.add(LCF.not(clauses.get(j2)));
					}
				}
				refactorClauses.add(LCF.and(tmpClauses.toArray(new Constraint[tmpClauses.size()])));
			}
			solver.post(LCF.or(refactorClauses.toArray(new Constraint[refactorClauses.size()])));
		}

		solver.set(IntStrategyFactory.inputOrder_InDomainMin(variableList));
		if (solver.findSolution()) {
			ui.setStatusFieldLabel("Stop: TERMINATO");

			for (int i = 0; i < variableList.length; i++) {
				ui.printNumber(variableList[i].getName(), variableList[i].getValue());
			}
		} else {
			ui.setStatusFieldLabel("Stop: FALLITO");
		}
	}

}
