package org.primefaces.test;

import jakarta.faces.context.FacesContext;
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

public class CustomLazyDataModel<T> extends LazyDataModel<T> {
    protected List<T> datasource = new ArrayList<>();

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
        var data = datasource.stream()
                .skip(first)
                .filter(o -> filter(FacesContext.getCurrentInstance(), filters.values(), o))
                .limit(pageSize)
                .collect(Collectors.toList());
        return data;
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
            } catch (ReflectiveOperationException e) {
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

    public List<T> getDatasource() {
        return datasource;
    }

    public void setDatasource(List<T> datasource) {
        this.datasource = datasource;
    }
}
