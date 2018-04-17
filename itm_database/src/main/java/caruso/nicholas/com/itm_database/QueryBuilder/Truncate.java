package caruso.nicholas.com.itm_database.QueryBuilder;

/**
 * Nick:4/16/2018
 * WorkOrder.
 */
public class Truncate {
    private boolean AreYouSure = false;
    private String table_name;

    public Truncate(String table_name) {
        this.table_name = table_name;
    }

    public Truncate(String table_name, boolean areYouSure) {
        this.table_name = table_name;
        this.AreYouSure = areYouSure;
    }

    public Truncate ImSure() {
        AreYouSure = true;
        return this;
    }

    public Truncate ImNotSure() {
        AreYouSure = false;
        return this;
    }

    public String getTruncate() {
        return "DELETE FROM " + table_name + " ;";
    }

    public boolean isSure() {
        return AreYouSure;
    }


}
