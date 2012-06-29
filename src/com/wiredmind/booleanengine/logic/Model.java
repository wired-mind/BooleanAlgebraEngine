package com.wiredmind.booleanengine.logic;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.lang.ArrayUtils;

/**
 * Encapsulates the common CNF Dimacs format
 * of a {@link Phrase}.
 *
 * 
 */
public class Model {

    private Set<Integer> _model;

    public Model(int[] model) {
        _model = new TreeSet<Integer>(Arrays.asList(ArrayUtils.toObject(model)));
    }

    public boolean containsPartialPhrase(int[] dimacsPhrase) {
        List<Integer> list = Arrays.asList(ArrayUtils.toObject(dimacsPhrase));
        return this._model.containsAll(list);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Model other = (Model) obj;
        if (this._model != other._model && (this._model == null || !this._model.equals(other._model))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this._model != null ? this._model.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return _model.toString();
    }
}
