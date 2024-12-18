package org.primefaces.test;

import jakarta.faces.context.FacesContext;
import lombok.Setter;
import lombok.extern.java.Log;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.ComparatorUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.filter.FilterConstraint;
import org.primefaces.util.LocaleUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log
public class CustomLazyDataModel<T> extends LazyDataModel<T> {

    @Setter
    protected List<T> datasource = new ArrayList<>();

    protected List<T> filteredDatasource = new ArrayList<>();

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        return (int) datasource.stream().filter(o -> filter(FacesContext.getCurrentInstance(), filterBy.values(), o)).count();
    }

    @Override
    public List<T> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filters) {
        if (!sortBy.isEmpty()) {
            var comparators = sortBy.values().stream()
                    .map(o -> new LazyDataModelComparator<T>(o.getField(), o.getOrder().isAscending()))
                    .collect(Collectors.toList());
            var cp = ComparatorUtils.chainedComparator(comparators); // from apache
            datasource.sort(cp);
        }

        //filter
        // apply offset & filters
        filteredDatasource = datasource.stream()
                .filter(o -> filter(FacesContext.getCurrentInstance(), filters.values(), o))
                .collect(Collectors.toList());

        return filteredDatasource.stream()
                .skip(first)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    @Override
    public T getRowData(String rowKey) {
        for (T t : datasource) {
            if (getRowKey(t).equals(rowKey))
                return t;
        }
        return super.getRowData(rowKey);
    }

    @Override
    public String getRowKey(T object) {
        try {
            Method getId = object.getClass().getMethod("getId");
            return getId.invoke(object).toString();
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean filter(FacesContext context, Collection<FilterMeta> filterBy, Object o) {
        boolean matching = true;

        for (FilterMeta filter : filterBy) {
            FilterConstraint constraint = filter.getConstraint();
            Object filterValue = filter.getFilterValue();

            try {
                matching = constraint.isMatching(context, PropertyUtils.getProperty(o, filter.getField()), filterValue, LocaleUtils.getCurrentLocale());
            } catch (ReflectiveOperationException | IllegalArgumentException e) {
                matching = false;
            }

            if (!matching) {
                break;
            }
        }
        return matching;
    }

    public Integer size() {
        return getRowCount();
    }

    public void update(T oldObject, T newObject) {
        if (oldObject == null) {
            datasource.add(newObject);
        } else {
            int index = datasource.indexOf(oldObject);
            if (index != -1) {
                datasource.set(index, newObject);
            } else {
                log.info("Object not found in datatable:" + oldObject);
            }
        }
    }

    public void update(Integer id, T newObject) {
        T oldObject = datasource.stream().filter(o -> getRowKey(o).equals(id.toString())).findFirst().orElse(null);
        if (oldObject != null) {
            int index = datasource.indexOf(oldObject);
            if (index != -1) {
                datasource.set(index, newObject);
            } else {
                log.info("Object not found in datatable:" + oldObject);
            }
        } else {
            log.info("Object not found in datatable:" + oldObject);
        }

    }

    public void remove(T object) {
        datasource.remove(object);
    }

    public void add(T object) {
        datasource.add(object);
    }

    public void clear() {
        datasource.clear();
    }

    public boolean isEmpty() {
        return datasource.isEmpty();
    }

    public void addAll(List<T> objects) {
        datasource.addAll(objects);
    }

    public boolean contains(T object) {
        return datasource.contains(object);
    }
}