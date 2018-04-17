package caruso.nicholas.com.itm_database.QueryBuilder;

/**
 * Nick:4/17/2018
 * android_itm_database.
 */
public class DropTable {
    private boolean AreYouSure = false;
    private String table_name;

    public DropTable(String table_name) {
        this.table_name = table_name;
    }

    public DropTable(String table_name, boolean areYouSure) {
        this.table_name = table_name;
        this.AreYouSure = areYouSure;
    }

    public DropTable ImSure() {
        AreYouSure = true;
        return this;
    }

    public DropTable ImNotSure() {
        AreYouSure = false;
        return this;
    }

    public String getTruncate() {
        return "DROP FROM " + table_name + " ;";
    }

    public boolean isSure() {
        return AreYouSure;
    }



}
