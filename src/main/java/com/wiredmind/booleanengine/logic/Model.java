package com.wiredmind.booleanengine.logic;

import java.io.Serializable;
import java.util.*;

/**
 * Encapsulates an array of literals (in the common CNF Dimacs format)
 * that satisfy a {@link Formula}.
 * <p/>
 * <p>Conforms to the java {@link Set} interface definition with the exception
 * that zero is not allowed as an element and a model may contain no pair of
 * elements <code>e1</code> and <code>e2</code> such that <code>e1</code> is
 * the negation of <code>e2</code>. Thus, {1, 2} is a model but {1, 2, -2} is
 * not.</p>
 */
public final class Model implements Set<Integer>, Serializable {

    private static final long serialVersionUID = 2L;
    private List<Integer> internalModel = new ArrayList<Integer>();

    public Model() {
    }

    public Model(int[] literals) {
        for (int i = 0; i < literals.length; i++) {
            this.add(literals[i]);
        }
    }

    public Model(Integer[] literals) {
        this.addAll(Arrays.asList(literals));
    }

    @Override
    public int size() {
        return internalModel.size();
    }

    @Override
    public boolean isEmpty() {
        return internalModel.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return internalModel.contains(o);
    }

    @Override
    public Iterator<Integer> iterator() {
        return internalModel.iterator();
    }

    @Override
    public Object[] toArray() {
        return internalModel.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return internalModel.toArray(a);
    }

    @Override
    public boolean add(Integer e) {
        if (e == 0) {
            throw new IllegalArgumentException("A model cannot have a literal value of 0");
        }
        if (internalModel.contains(-e)) {
            throw new IllegalArgumentException("A model cannot contain a literal and its negation");
        }
        if (!internalModel.contains(e)) {
            return internalModel.add(e);
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return internalModel.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return internalModel.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Integer> c) {
        for (Integer integer : c) {
            this.add(integer);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return internalModel.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return internalModel.removeAll(c);
    }

    @Override
    public void clear() {
        internalModel.clear();
    }

    /**
     * The difference of A and B, denoted A - B, which is
     * the model containing those literals that are in this
     * model (A) but not in the other model (B).
     *
     * @param other the other model
     * @return a new model containing those literals that are
     *         in this model (A) but not in the other model (B).
     */
    public Model differenceOf(Model other) {
        Model difference = new Model();

        for (int i = 0; i < internalModel.size(); i++) {
            if (!other.contains(internalModel.get(i))) {
                difference.add(internalModel.get(i));
            }
        }

        return difference;
    }

    public boolean isSubsetOf(Model other) {
        for (int i = 0; i < this.size(); i++) {
            if (!other.contains(internalModel.get(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean isSupersetOf(Model other) {
        for (int i = 0; i < other.size(); i++) {
            if (!this.contains(other.internalModel.get(i))) {
                return false;
            }
        }
        return true;
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
        if (!(this.isSubsetOf(other) && other.isSubsetOf(this))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (internalModel != null ? internalModel.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return Arrays.toString(internalModel.toArray());
    }
}
