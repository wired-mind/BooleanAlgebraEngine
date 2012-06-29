package com.wiredmind.booleanengine.core;

import com.wiredmind.booleanengine.logic.Clause;
import com.wiredmind.booleanengine.logic.Formula;
import com.wiredmind.booleanengine.logic.Model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.tools.ModelIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract {@link com.wiredmind.booleanengine.core.ValidatorAlgebra} class.
 *
 * 
 */
public abstract class AbstractValidatorAlgebra implements ValidatorAlgebra {

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractValidatorAlgebra.class);
    private final static int MAXVAR = 1000; // maximum expected variables for SATsolver
    private final static Map<Formula, List<Model>> MODELCACHE = Collections.synchronizedMap(new HashMap<Formula, List<Model>>(100));

    // <editor-fold defaultstate="collapsed" desc="SAT-solver">
    public static List<Model> getSATModels(Formula formula) throws ContradictionException, org.sat4j.specs.TimeoutException {
        if (MODELCACHE.containsKey(formula)) {
            return MODELCACHE.get(formula);
        } else {
            List<Model> models = new ArrayList<Model>();
            ISolver solver = new ModelIterator(SolverFactory.newLight());
            solver.newVar(MAXVAR);
            List<Clause> clauses = formula.toCNFClauses();
            solver.setExpectedNumberOfClauses(clauses.size());
            // TODO: Cache the models for known formulas just as TreeStore objects are cached
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Formula: {}", formula.toString());
                LOGGER.debug("Formula (CNF): {}", formula.toCNF());
                LOGGER.debug("cnf clauses:");
                for (Clause clause : clauses) {
                    LOGGER.debug(ArrayUtils.toString(clause.toDimacsArray()));
                }
            }
            for (Clause clause : clauses) {
                solver.addClause(new VecInt(clause.toDimacsArray()));
            }
            LOGGER.debug("The following models satisfy this formula:");

            while (solver.isSatisfiable()) {
                Model model = new Model(solver.model());
                models.add(model);
                LOGGER.debug(model.toString());
            }
            MODELCACHE.put(formula, models);
            return models;
        }
    }// </editor-fold>

    public static boolean phrasePartiallySatisfiesAny(int[] dimacsPhrase, List<Model> models) {
        LOGGER.debug("Partial results of the variables are:");
        LOGGER.debug(ArrayUtils.toString(dimacsPhrase));
        for (Model model : models) {
            if (model.containsPartialPhrase(dimacsPhrase)) {
                LOGGER.debug("The model {} is partially satisfied", model.toString());
                return true;
            }
        }
        LOGGER.debug("No model can be satisfied - Proved False!");
        return false;
    }
}
