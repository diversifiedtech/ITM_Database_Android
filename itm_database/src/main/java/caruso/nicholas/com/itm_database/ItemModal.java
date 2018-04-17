package caruso.nicholas.com.itm_database;

import android.content.Context;

/**
 * Nick:1/11/2018
 * WorkOrder.
 */

public abstract class ItemModal {
    public final String table;
    protected final String id;
    protected Context mContext;

    public ItemModal(Context context, String table, String id) {
        mContext = context;
        this.table = table;
        this.id = id;
    }

}
