package com.mycompany.hierarchyObjects;

public class Cooperators {
    private int parentId;
    private Integer[] coworkersIds;

    public Cooperators(){}

    public Cooperators(int parentId, Integer[] coworkersIds) {
        this.parentId = parentId;
        this.coworkersIds = coworkersIds;
    }
    
    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public Integer[] getCoworkersIds() {
        return coworkersIds;
    }

    public void setCooworkersIds(Integer[] coworkersIds) {
        this.coworkersIds = coworkersIds;
    }
}
