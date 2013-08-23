package com.wiredmind.booleanengine.relations.comparators;

/**
 * Comparator.java
 * <p/>
 * Interface for comparing objects to determine their ordering with respect
 * to each other. For a Comparator to be consistent with equals, its
 * {code #compare(Object, Object)} method has to return zero for each pair of
 * elements (a,b) where a.equals(b) holds true.
 * <p/>
 * It is recommended that a Comparator implements Serializable.
 * <p/>
 * Created by Craig Earley on 8/5/13.
 * Copyright (c) 2013 Wired-Mind Labs, LLC.
 */
public interface Comparator<T> {

    /**
     * Compares the two specified objects to determine their relative ordering.
     * The ordering implied by the return value of this method for all possible
     * pairs of (lhs, rhs) should form an equivalence relation. This means that
     * <p/>
     * 1) compare(a,a) returns zero for all a
     * 2) the sign of compare(a,b) must be the opposite of the sign of compare(b,a) for all pairs of (a,b)
     * 3) From compare(a,b) > 0 and compare(b,c) > 0 it must follow compare(a,c) > 0 for all possible combinations of (a,b,c)
     *
     * @param lhs an Object.
     * @param rhs a second Object to compare with lhs..
     * @return an integer < 0 if lhs is less than rhs, 0 if they are equal,
     *         and > 0 if lhs is greater than rhs.
     * @throws ClassCastException if objects are not of the correct type.
     */
    int compare(T lhs, T rhs) throws Exception;

    /**
     * Compares lhs object to rhs as represented by a character sequence
     * and determines their relative ordering.
     * <p/>
     * How the rhs is converted to a type T is left to implementers.
     *
     * @param lhs an Object.
     * @param rhs a character sequence representing an object to comparators with lhs.
     * @return an integer < 0 if lhs is less than rhs, 0 if they are equal,
     *         and > 0 if lhs is greater than rhs.
     * @throws ClassCastException if objects are not of the correct type.
     */
    int compare(T lhs, CharSequence rhs) throws Exception;

    /**
     * Determines if the string representation of an object contains a character sequence.
     * <p/>
     * How obj T is properly represented as a string is left to implementers.
     *
     * @param obj an Object.
     * @param cs  the character sequence to search for.
     * @return true if the sequence of characters are contained in the string representation
     *         of the object, otherwise false.
     */
    boolean contains(T obj, CharSequence cs) throws Exception;

    /**
     * Tests whether the string representation of an object matches the given regularExpression.
     * This method returns true only if the regular expression matches the entire input string.
     * A common mistake is to assume that this method behaves like contains(CharSequence); if you
     * want to match anywhere within the input string, you need to add .* to the beginning and end
     * of your regular expression.
     * <p/>
     * How obj T is properly represented as a string is left to implementers.
     *
     * @param obj               an Object.
     * @param regularExpression a regularExpression.
     * @return true if the regular expression matches the string representation of the object.
     */
    boolean matches(T obj, String regularExpression) throws Exception;
}
