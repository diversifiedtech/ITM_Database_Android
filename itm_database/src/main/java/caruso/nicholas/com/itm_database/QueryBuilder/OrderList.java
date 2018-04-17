package caruso.nicholas.com.itm_database.QueryBuilder;

import android.os.Build;

import java.util.ArrayList;
import java.util.List;

/**
 * Nick:1/8/2018
 * WorkOrder.
 */

public class OrderList {
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";

    private ArrayList<OrderObject> sort;
    private ArrayList<Integer> sparseSpecial;

    public OrderList() {
        sort = new ArrayList<>();
        sparseSpecial = new ArrayList<>();
    }

    public static OrderList copy(OrderList orderList) {
        if (orderList == null) {
            return null;
        }
        OrderList copy = new OrderList();
        copy.sort.addAll(orderList.sort);
        copy.sparseSpecial.addAll(orderList.sparseSpecial);
        return copy;
    }

    public int size() {
        return sort.size();
    }

    public OrderObject get(int index) {
        return sort.get(index);
    }

    public boolean isSpecial(int index) {
        return sparseSpecial.contains(index);
    }

    public void addIndex(OrderObject o, int index, boolean special) {
        sort.add(index, o);
        if (special) {
            boolean found = false;
            for (int i = 0; i < sparseSpecial.size(); i++) {
                if (!found && index < sparseSpecial.get(i)) {
                    sparseSpecial.add(i, index);
                    found = true;
                }
            }
            if (!found) {
                sparseSpecial.add(index);
            }
        }
        for (int j = index; j < sparseSpecial.size(); j++) {
            sparseSpecial.set(j, sparseSpecial.get(j) + 1);
        }
    }

    public void removeBySpecial(int i) {
        int toRemove = sparseSpecial.remove(i);
        sort.set(toRemove, null);
    }

    public void addProjectionOrder(OrderObject orderObject) {
        sort.add(new OrderObject(orderObject.AS, orderObject.direction));
    }

    public void add(OrderObject orderObject) {
        sort.add(orderObject);
    }

    public void add(String field) {
        sort.add(new OrderObject(field));
    }

    public void addAS(String field, String AS) {
        sort.add(new OrderObject(field, AS));
        addSparse();

    }


    public void add(String field, boolean asc) {
        sort.add(new OrderObject(field, asc));
    }

    public void addAS(String field, boolean asc, String AS) {
        sort.add(new OrderObject(field, asc, AS));
        addSparse();
    }


    public void add(String field, String order) {
        sort.add(new OrderObject(field, order));
    }

    public void addAS(String field, String order, String AS) {
        sort.add(new OrderObject(field, order, AS));
        addSparse();
    }

    public void addCase(String field, boolean case_sensitive) {
        sort.add(new OrderObject(field).setCaseSensitivity(case_sensitive));
    }

    public void addCaseAS(String field, String AS, boolean case_sensitive) {
        sort.add(new OrderObject(field, AS).setCaseSensitivity(case_sensitive));
        addSparse();

    }


    public void addCase(String field, boolean asc, boolean case_sensitive) {
        sort.add(new OrderObject(field, asc).setCaseSensitivity(case_sensitive));
    }

    public void addCaseAS(String field, boolean asc, String AS, boolean case_sensitive) {
        sort.add(new OrderObject(field, asc, AS).setCaseSensitivity(case_sensitive));
        addSparse();
    }


    public void addCase(String field, String order, boolean case_sensitive) {
        sort.add(new OrderObject(field, order).setCaseSensitivity(case_sensitive));
    }

    public void addCaseAS(String field, String order, String AS, boolean case_sensitive) {
        sort.add(new OrderObject(field, order, AS).setCaseSensitivity(case_sensitive));
        addSparse();
    }


    public List<OrderObject> getSpecials() {
        List<OrderObject> objects = new ArrayList<>();
        for (Integer i : sparseSpecial) {
            objects.add(sort.get(i));
        }
        return objects;
    }


    public boolean isEmpty() {
        return sort.size() == 0;
    }

    private void addSparse() {
        sparseSpecial.add(sort.size() - 1);
    }

    public void addFunction(String function, boolean asc, String AS) {
        sort.add(new OrderObject(function, asc, AS));
        addSparse();
    }

    public void addFunction(String function, String order, String AS) {
        sort.add(new OrderObject(function, order, AS));
        addSparse();
    }

    public void addCaseFunction(String function, boolean asc, String AS, boolean case_sensitive) {
        sort.add(new OrderObject(function, asc, AS).setCaseSensitivity(case_sensitive));
        addSparse();
    }

    public void addCaseFunction(String function, String order, String AS, boolean case_sensitive) {
        sort.add(new OrderObject(function, order, AS).setCaseSensitivity(case_sensitive));
        addSparse();
    }

    public void addAll(OrderList orders) {
        if (orders != null) {
            int offset = this.sort.size();
            this.sort.addAll(orders.getOrderObjectList());
            for (int i = 0; i < orders.sparseSpecial.size(); i++) {
                this.sparseSpecial.add(orders.sparseSpecial.get(i) + offset);
            }
        }
    }

    private List<OrderObject> getOrderObjectList() {
        return sort;
    }

    public void addSplitOnFirst(String field, String separator, boolean left_first, boolean left_asc, boolean right_asc, boolean case_senitive) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            add(field, left_asc);
            return;
        }
        String left = "SUBSTR(" + field + ",1,INSTR(" + field + ",\"" + separator + "\")-1)";
        String right = "SUBSTR(" + field + ", INSTR(" + field + ",\"" + separator + "\"))";
        if (left_first) {
            addCaseFunction(left, left_asc, "LEFT_PRO", case_senitive);
            addCaseFunction(right, right_asc, "RIGHT_PRO", case_senitive);
        } else {
            addCaseFunction(right, right_asc, "LEFT_PRO", case_senitive);
            addCaseFunction(left, left_asc, "RIGHT_PRO", case_senitive);
        }


    }

    public String getList() {
        StringBuilder orderStatement = new StringBuilder("");
        boolean multi = false;
        for (OrderObject orderObj : sort) {
            if (multi) {
                orderStatement.append(",");
            }
            orderStatement.append(orderObj.getField());
            orderStatement.append(" ");
            if (orderObj.case_sensitive) {
                orderStatement.append(" COLLATE NOCASE ");
            }
            orderStatement.append(orderObj.getDirection());
            multi = true;
        }
        return orderStatement.toString();
    }

    public static class OrderObject {
        private String field;
        private boolean direction;
        private boolean case_sensitive;
        public String AS = null;

        public OrderObject(String field) {
            this.field = field;
            this.direction = true;
            this.case_sensitive = true;
        }

        public OrderObject(String field, boolean asc) {
            this.field = field;
            this.direction = asc;
            this.case_sensitive = true;
        }

        public OrderObject(String field, String order) {
            this.field = field;
            this.direction = !orderDirection[1].equals(order.toUpperCase());
            this.case_sensitive = true;

        }

        public OrderObject(String field, boolean asc, String AS) {
            this.field = field;
            this.direction = asc;
            this.AS = AS;
            this.case_sensitive = true;
        }

        public OrderObject(String field, String order, String AS) {
            this.field = field;
            this.direction = !orderDirection[1].equals(order.toUpperCase());
            this.AS = AS;
            this.case_sensitive = true;
        }

        public String getField() {
            return field;
        }

        public String getDirection() {
            return orderDirection[direction ? 0 : 1];
        }

        public boolean isCaseSensitive() {
            return case_sensitive;
        }

        public OrderObject setCaseSensitivity(boolean case_sensitive) {
            this.case_sensitive = case_sensitive;
            return this;
        }

        private String[] orderDirection = {"ASC", "DESC"};

    }
}
