package com.actiongroup.actionserver.models;

import java.lang.reflect.Field;

public class ObjectWithCopyableFields {
    //TODO remove try catch?
    public void copyFieldsTo(ObjectWithCopyableFields other) {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                if(!objContainsField(other, field.getName()))
                    continue;
                Field otherField = other.getClass().getDeclaredField(field.getName());
                if (field.getType().equals(otherField.getType())
                        && !java.lang.reflect.Modifier.isFinal(otherField.getModifiers())) {
                    field.setAccessible(true);
                    var v = field.get(this);
                    if(field.get(this) != null) {
                        otherField.setAccessible(true);
                        Object value = field.get(this);
                        otherField.set(other, value);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Boolean objContainsField(ObjectWithCopyableFields obj, String fieldName){
        for (Field field : obj.getClass().getDeclaredFields()){
            if(field.getName().equals(fieldName))
                return true;
        }
        return false;
    }
}
