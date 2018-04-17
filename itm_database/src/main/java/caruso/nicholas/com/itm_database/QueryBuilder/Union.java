package caruso.nicholas.com.itm_database.QueryBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Nick:4/3/2018
 * WorkOrder.
 */
public class Union {
    private List<Query> queryList;
    private boolean all;
    private boolean data_on = true;
    private OrderList orderList;

    public Union(Query... queries) {
        queryList = new ArrayList<>();
        for (Query query : queries) {
            queryList.add(new Query(query));
        }
        this.all = true;
        specialToProjection();
    }

    public boolean all() {
        return all;
    }

    public void all(boolean all) {
        this.all = all;
    }

    public void specialToProjection() {
        int longest = 0;
        for (Query query : queryList) {
            int size = 0;
            OrderList ol = query.getOrderList();
            if (ol != null) {
                size = ol.size();
            }
            if (size > longest) {
                longest = size;
            }
        }

        OrderList masterOrderList = new OrderList();
        for (int index = 0; index <= longest; index++) {
            for (int qIndex = 0; qIndex < queryList.size(); qIndex++) {
                OrderList queryOrderList = queryList.get(qIndex).getOrderList();
                if (queryOrderList != null && queryOrderList.size() > index) {
                    OrderList.OrderObject o = queryOrderList.get(index);
                    if (o != null) {
                        if (!queryOrderList.isSpecial(index)) {
                            masterOrderList.add(o);
                        } else {
                            masterOrderList.addProjectionOrder(o);

                            for (int y = 0; y < queryList.size(); y++) {
                                Query innerQuery = queryList.get(y);
                                OrderList innerOrderList = innerQuery.getOrderList();
                                boolean found = false;

                                if (innerOrderList != null) {
                                    List<OrderList.OrderObject> innerSpecialOrderList = innerOrderList.getSpecials();
                                    for (int i = 0; i < innerSpecialOrderList.size(); i++) {
                                        OrderList.OrderObject oo = innerSpecialOrderList.get(i);
                                        if (o.AS.equals(oo.AS)) {
                                            if (!found) {
                                                ProjectionList projectionList = innerQuery.getProjection();
                                                projectionList.addRawProjectionAS(oo.getField(), oo.AS);
                                                innerQuery.replaceProjection(projectionList);
                                            }
                                            innerOrderList.removeBySpecial(i);
                                            found = true;
                                        }
                                    }
                                }
                                if (!found) {
                                    ProjectionList projectionList = innerQuery.getProjection();
                                    projectionList.addNullAs(o.AS);
                                    innerQuery.replaceProjection(projectionList);
                                }
                            }
                        }
                    }
                }
            }
        }

        this.orderList = masterOrderList;
        for (Query query : queryList) {
            query.replaceOrder(null);
        }
    }


    //    public void setUnionProjection() {
//        int i = 0;
//        if (storedProjectionList == null) {
//            storedProjectionList = new ArrayList<>();
//            for (Query query : queryList) {
//                ProjectionList pl = query.getProjection();
//                storedProjectionList.add(pl);
//                Log.d("dtc", "This happened " + i + " times");
//                pl.add("\"" + query.getTables().getTable() + "\"" + " AS " + "TABLE_NAME");
//                pl.add(i + " AS " + "QUERY_NUMBER");
//                query.replaceProjection(pl);
//                i++;
//            }
//        }
//
//    }
//
//    public void unsetUnionProjection() {
//        if (storedProjectionList != null) {
////            int i = 0;
//            for (Query query : queryList) {
//                ProjectionList pl = query.getProjection();
//                pl
//                query.replaceProjection(storedProjectionList.remove(0));
////                i++;
//            }
//            storedProjectionList = null;
//        }
//    }
    public void toggleQueryDataFields(boolean data_on) {
        this.data_on = data_on;
    }

    public Query.QueryObject getQuery() {
        StringBuilder queryString = new StringBuilder();
        ArrayList<String> whereArgs = new ArrayList<>();
//        boolean multi = false;
        int i = 0;
        for (Query query : queryList) {
            if (data_on) {
                ProjectionList pl = query.getProjection();
                pl.add("\"" + query.getTables().getTable() + "\"" + " AS " + "TABLE_NAME");
                pl.add(i + " AS " + "QUERY_NUMBER");
                query.replaceProjection(pl);
            }


            Query.QueryObject obj = query.getQuery();
            if (i > 0) {
                queryString.append(" UNION ");
            }
            queryString.append(obj.getQuery());


            whereArgs.addAll(Arrays.asList(obj.getWhereArgs()));
            queryString.deleteCharAt(queryString.length() - 1);
            i++;
        }
        if (!orderList.isEmpty()) {
            queryString.append(" ORDER BY ").append(orderList.getList());
        }
        queryString.append(";");
//        String[] both = DatabaseHelper.concatenate(obj.getWhereArgs(), obj2.getWhereArgs());

        return new Query.QueryObject(queryString.toString(), whereArgs);
    }
}
