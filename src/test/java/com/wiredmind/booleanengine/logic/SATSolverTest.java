package com.wiredmind.booleanengine.logic;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.IVecInt;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.ModelIterator;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author Craig Earley
 */
public class SATSolverTest {

    Formula formula;
    List<IVecInt> clauses;

    public SATSolverTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws BadExpressionException, ContradictionException, TimeoutException {
        //c Formula: (a or b) and not (a and (b or c))
        //c Formula (encoded): (1 or 2) and !(1 and (2 or 3))
        //c Formula (cnf): (1 or 2) and (!1 or !3) and (!1 or !2)
        //c
        //p cnf 3 3
        //1 2 0
        //-1 -3 0
        //-1 -2 0

        // "((p or (q or r)) and ((p or not q) and ((q or not r) and ((r or not p) and (not p or (not q or not r))))))"
        // "a or b or c"

        FormulaBuilder formulaBuilder = new FormulaBuilder(
                "(a or b) and not(a and (b or c))");
        formula = formulaBuilder.build();

        clauses = formula.toCNFDimacsClauses();
        System.out.println("cnf 3 3");
        for (IVecInt literals : clauses) {
            System.out.println(literals);
        }
    }

    @Test
    public void testSATSolver() {
        final int MAXVAR = 1000;

        ISolver solver = new ModelIterator(SolverFactory.newLight());
        solver.newVar(MAXVAR);
        solver.setExpectedNumberOfClauses(clauses.size());

        try {
            for (IVecInt literals : clauses) {
                solver.addClause(literals);
            }

//            int[] partial = {1,2};
//            System.out.println("test isSatisfiable for assumps " + ArrayUtils.toString(partial) + ":");
//            boolean res = solver.isSatisfiable(new VecInt(partial),true);
//            System.out.println(res);

            System.out.println("All models:");
            while (solver.isSatisfiable()) {
                Model model = new Model(solver.model());
                System.out.println(model.toString());
                assertTrue(model.equals(new Model(new int[]{-1, 2, -3}))
                        || model.equals(new Model(new int[]{-1, 2, 3}))
                        || model.equals(new Model(new int[]{1, -2, -3})));
            }
        } catch (ContradictionException e) {
            System.out.println(e);
        } catch (TimeoutException e) {
            System.out.println(e);
        }
    }
}
