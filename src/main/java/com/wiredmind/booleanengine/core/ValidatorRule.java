package com.wiredmind.booleanengine.core;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * ValidatorRule.java
 * Created by Craig Earley on 4/12/13.
 * Copyright (c) 2013 Wired-Mind Labs, LLC.
 */
public class ValidatorRule implements Rule, Serializable {

    public static final long serialVersionUID = 1L;
    private String ruleName;
    private String ruleDescription;
    private DateTime updateTimestamp = new DateTime();
    private String relation;
    private String arguments;

    public ValidatorRule(String ruleName, String ruleDescription, DateTime updateTimestamp, String relation, String arguments) {
        this.ruleName = ruleName;
        this.ruleDescription = ruleDescription;
        this.updateTimestamp = updateTimestamp != null ? updateTimestamp : new DateTime();
        this.relation = relation;
        this.arguments = arguments;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ValidatorRule other = (ValidatorRule) obj;
        if ((this.ruleName == null) ? (other.ruleName != null) : !this.ruleName.equals(other.ruleName)) {
            return false;
        }
        if ((this.relation == null) ? (other.relation != null) : !this.relation.equals(other.relation)) {
            return false;
        }
        if ((this.arguments == null) ? (other.arguments != null) : !this.arguments.equals(other.arguments)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.ruleName != null ? this.ruleName.hashCode() : 0);
        hash = 37 * hash + (this.relation != null ? this.relation.hashCode() : 0);
        hash = 37 * hash + (this.arguments != null ? this.arguments.hashCode() : 0);
        return hash;
    }

    public String getRuleName() {
        return this.ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleDescription() {
        return this.ruleDescription;
    }

    public void setRuleDescription(String ruleDescription) {
        this.ruleDescription = ruleDescription;
    }

    public DateTime getUpdateTimestamp() {
        return this.updateTimestamp;
    }

    public void setUpdateTimestamp(DateTime updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getRelation() {
        return this.relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getArguments() {
        return this.arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }
}
