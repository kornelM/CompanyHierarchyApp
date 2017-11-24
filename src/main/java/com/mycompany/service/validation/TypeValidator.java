package com.mycompany.service.validation;

import java.util.HashSet;
import java.util.Set;

public class TypeValidator {
    private Set<String> types;

    public TypeValidator(){
        initSet();
    }

    private void initSet() {
        types = new HashSet<>();
        types.add("team");
        types.add("location");
        types.add("department");
    }


    public boolean checkType(String type){
        return types.contains(type.toLowerCase());
    }
}
