package caruso.nicholas.com.itm_database.QueryBuilder;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Nick:1/8/2018
 * WorkOrder.
 */

public class Query {
    private ProjectionList projection;
    @NonNull
    private JoinHelper tables;
    private WhereWrapper where;
    private OrderList sortby;

    public Query(JoinHelper tables) {
        this.tables = tables;
    }

    public Query(ProjectionList projection, JoinHelper joinHelper, WhereWrapper where, OrderList sortby) {
        this.projection = projection;
        this.tables = joinHelper;
        this.where = where;
        this.sortby = sortby;
    }

    public Query(Query query) {
        projection = ProjectionList.copy(query.projection);
        tables = query.tables;
        where = query.where;
        sortby = OrderList.copy(query.sortby);
    }

    public void replaceProjection(ProjectionList projectionList) {
        this.projection = projectionList;
    }

    public void replaceJoin(JoinHelper joinHelper) {
        this.tables = joinHelper;
    }

    public void replaceWhere(WhereWrapper where) {
        this.where = where;
    }

    public void replaceOrder(OrderList orderBy) {
        this.sortby = orderBy;
    }

    public WhereWrapper getWhere() {
        return where;
    }

    public ProjectionList getProjection() {
        return projection;
    }

    public OrderList getOrderList() {
        return sortby;
    }

    @NonNull
    public JoinHelper getTables() {
        return tables;
    }

    public QueryObject getQuery() {
        QueryObject obj = this.getQueryHelper();
        obj.query += ";";
        return obj;
    }

    public String getQueryAS(String asTable) {
        QueryObject obj = this.getQueryHelper();
        String query = obj.query;
        ArrayList<String> whereArgs = obj.whereArgs;
        for (String whereArg : whereArgs) {
            query = query.replaceFirst("\\?", "\"" + whereArg + "\"");
        }
        return "(" + query + ") as " + asTable;

    }

    private QueryObject getQueryHelper() {
        StringBuilder query = new StringBuilder("SELECT ");

        //SELECT
        if (projection == null) {
            query.append(" * ");
        } else {
            query.append(projection.getProjection());
        }

        //FROM
        query.append(" FROM ");
        query.append(tables.getHead().getFullJoin());

        //Where
        ArrayList<String> whereArgs = new ArrayList<>();
        if (where != null) {
            WhereWrapper.WhereObject wr = where.getWhereObject();
            if (wr != WhereWrapper.WhereObject.NULL) {
                query.append(" WHERE ");
                whereArgs = wr.getArgs();
                query.append(wr.getStatement());
            }
        }

        //ORDER BY
        if (sortby != null) {
            query.append(" ORDER BY ");
            query.append(sortby.getList());
        }

        //SEMICOLON
        return new QueryObject(query.toString(), whereArgs);
    }


    public static class QueryObject {
        private String query;
        private ArrayList<String> whereArgs;

        QueryObject(String query, ArrayList<String> whereArgs) {
            this.query = query;
            this.whereArgs = whereArgs;
        }

        public String getQuery() {
            return query;
        }

        public String[] getWhereArgs() {
            return whereArgs.toArray(new String[whereArgs.size()]);
        }

    }

}
