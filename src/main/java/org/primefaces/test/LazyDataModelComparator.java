package org.primefaces.test;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.jboss.logging.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class LazyDataModelComparator<T> implements Comparator<T> {

    private Logger log = Logger.getLogger(LazyDataModelComparator.class);

    private static final Map<String, Field> fieldCache = new HashMap<>();

    private String sortField;
    private boolean ascending;

    public LazyDataModelComparator() {

    }

    public LazyDataModelComparator(String sortField, boolean ascending) {
        this.sortField = sortField;
        this.ascending = ascending;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compare(T object1, T object2) {
        int value = 0;

        try {
            Comparable<Object> fieldValue1 = getFieldComparableValue(object1, sortField);
            Comparable<Object> fieldValue2 = getFieldComparableValue(object2, sortField);

            // Compare the field values based on the specified sorting order
            if (fieldValue1 != null && fieldValue2 != null) {
                value = fieldValue1.compareTo(fieldValue2);
            } else {
                if (fieldValue1 == null && fieldValue2 == null) {
                    value = 0;
                } else if (fieldValue1 == null) {
                    value = -1;
                } else {
                    value = 1;
                }
            }

            return ascending ? value : -1 * value;
        } catch (NestedNullException e) {
            log.debug(e.getMessage());
        } catch (Exception e) {
            //log.error(e);
            throw new RuntimeException(e);
        }

        return value;
    }

    private Comparable<Object> getFieldComparableValue(Object object, String fieldPath) throws IllegalAccessException {
        Object fieldValue = null;
        if (object == null) {
            return null;
        }
        try {
            fieldValue = PropertyUtils.getProperty(object, fieldPath);
        } catch (NestedNullException e) {
            //Ignore
            return null;
        } catch (InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        if (fieldValue instanceof Iterable<?>) {
            Iterable<?> col = (Iterable<?>) fieldValue;
            if (col.iterator().hasNext()) {
                return (Comparable<Object>) col.iterator().next();
            } else {
                return null;
            }
        }
        return (Comparable<Object>) fieldValue;
    }
}
