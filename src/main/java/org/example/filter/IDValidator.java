package org.example.filter;

import java.util.*;

public class IDValidator {

    public static ValueValid validateID(String id) {
        ValueValid result = new ValueValid();
        result.setValue(id);

        if (id == null) {
            result.setValid(false);
            result.setNotes("ID cannot be null");
            return result;
        }

        if (id.length() < 5) {
            result.setValid(false);
            result.setNotes("Too short");
            return result;
        }

        if (id.length() > 5) {
            result.setValid(false);
            result.setNotes("Too long");
            return result;
        }

        if (!id.startsWith("Q")) {
            result.setValid(false);
            result.setNotes("Must begin with Q");
            return result;
        }

        result.setValid(true);
        result.setNotes("");
        return result;
    }

    public static List<ValueValid> validateIDs(String[] ids) {
        List<ValueValid> results = new ArrayList<>();
        for (String id : ids) {
            results.add(validateID(id));
        }
        return results;
    }
}