package caruso.nicholas.com.itm_database.QueryBuilder;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Nick:1/8/2018
 * WorkOrder.
 */

public class ProjectionList {
    private ArrayList<Pair<String, String>> projection;

    public ProjectionList() {
        projection = new ArrayList<>();
    }

    public static ProjectionList copy(ProjectionList projectionList) {
        if (projectionList == null) {
            return null;
        }
        ProjectionList copy = new ProjectionList();
        copy.projection = new ArrayList<>();
        copy.projection.addAll(projectionList.projection);
        return copy;
    }

    public void selectAll() {
        this.projection.clear();
        this.projection.add(new Pair<String,String>("*", null));
    }

    public void add(String projection) {
        this.projection.add(new Pair<String,String>(projection, null));
    }


    public void add(String tableName, String field) {
        addAS(tableName, field, null);
    }

    public void addAll(ProjectionList projectionList) {
        this.projection.addAll(projectionList.projection);
    }

    public void addAll(String[] fieldList) {
        for (String field : fieldList) {
            add(field);
        }
    }
    public void addAll(List<String> fieldList) {
        for (String field : fieldList) {
            add(field);
        }
    }

    public void addNullAs(String AS) {
        this.projection.add(new Pair<>("NULL", AS));
    }

    public void addAS(String tableName, String field) {
        Pair<String, String> pair = new Pair<>(tableName + "." + field, field);
        this.projection.add(pair);
    }

    public void addAS(String tableName, String field, String AS) {
        Pair<String, String> pair = new Pair<>(tableName + "." + field, AS);
        this.projection.add(pair);
    }

    public void addAllFrom(String tableName) {
        this.projection.add(new Pair<String, String>(tableName + ".*", null));
    }


    public void addCount(String projection) {
        addCountAS(projection, null);
    }

    public void addCount(String tableName, String field) {
        addCountAS(tableName, field, null);
    }


    public void addCountAS(String projection, String AS) {
        Pair<String, String> pair = new Pair<>("COUNT(" + projection + ")", AS);
        this.projection.add(pair);
    }

    public void addCountAS(String tableName, String field, String AS) {
        Pair<String, String> pair = new Pair<>("COUNT(" + tableName + "." + field + ")", AS);
        this.projection.add(pair);
    }

    public void addSetFunction(String Function, String projection) {
        addSetFunctionAS(Function, projection, null);
    }

    public void addSetFunction(String Function, String tableName, String field) {
        addSetFunctionAS(Function, tableName, field, null);
    }

    public void addSetFunctionAS(String Function, String projection, String AS) {
        Pair<String, String> pair = new Pair<>(Function + "(" + projection + ")", AS);
        this.projection.add(pair);
    }

    public void addSetFunctionAS(String Function, String tableName, String field, String AS) {
        Pair<String, String> pair = new Pair<>(Function + "(" + tableName + "." + field + ")", AS);
        this.projection.add(pair);
    }

    public void addRawProjectionAS(String raw, String AS) {
        Pair<String, String> pair = new Pair<>(raw, AS);
        this.projection.add(pair);
    }


    public String getProjection() {
        StringBuilder select = new StringBuilder(" ");
        boolean mutli = false;
        for (Pair<String, String> pair : projection) {
            if (mutli) {
                select.append(",");
            }
            select.append(pair.first);
            if (pair.second != null) {
                select.append(" AS ");
                select.append(pair.second);
            }
            select.append(" ");
            mutli = true;
        }
        return select.toString();
    }

    public ArrayList<String> getColumnsList() {
        ArrayList<String> list = new ArrayList<>();
        for (Pair<String, String> pair : projection) {
            list.add(pair.first);
        }
        return list;
    }

    public String[] getColumns() {
        ArrayList<String> list = getColumnsList();
        return list.toArray(new String[list.size()]);
    }
}
