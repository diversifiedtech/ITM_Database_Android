package caruso.nicholas.com.itm_database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import caruso.nicholas.com.itm_database.QueryBuilder.Delete;
import caruso.nicholas.com.itm_database.QueryBuilder.DropTable;
import caruso.nicholas.com.itm_database.QueryBuilder.Insert;
import caruso.nicholas.com.itm_database.QueryBuilder.JoinHelper;
import caruso.nicholas.com.itm_database.QueryBuilder.OrderList;
import caruso.nicholas.com.itm_database.QueryBuilder.ProjectionList;
import caruso.nicholas.com.itm_database.QueryBuilder.Query;
import caruso.nicholas.com.itm_database.QueryBuilder.Truncate;
import caruso.nicholas.com.itm_database.QueryBuilder.Union;
import caruso.nicholas.com.itm_database.QueryBuilder.Update;
import caruso.nicholas.com.itm_database.QueryBuilder.WhereWrapper;


/**
 * Nick: 9/5/2017
 * WorkOrder.
 */

public abstract class DatabaseHelper extends SQLiteOpenHelper {

    public final String REMOTE_DATABASE_LINK;
    private final String DATABASE_NAME = "LocalDatabase";
    private final int VERSION = 149;
    private Context context;

    protected abstract ArrayList<TableHelper> all_tables();

    protected abstract UpgradeHelper getUpgradeHelper(SQLiteDatabase db, int oldVersion, int newVersion);

    public DatabaseHelper(Context context, String remote_database_link, String database, SQLiteDatabase.CursorFactory cursorFactory, int version) {
        super(context, database, cursorFactory, version);
        this.REMOTE_DATABASE_LINK = remote_database_link;
        this.context = context;
    }

    public SQLiteDatabase Read() {
        return getReadableDatabase();
    }

    public SQLiteDatabase Write() {
        return getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (TableHelper t : all_tables()) {
            db.execSQL(t.CREATE_TABLE());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        UpgradeHelper upgradeHelper = getUpgradeHelper(db, oldVersion, newVersion);
        upgradeHelper.upgrade(context);
    }

    public void megaForceTruncate(Truncate truncate) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(truncate.getTruncate());
    }

    public boolean megaSafeTruncate(Truncate truncate) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (truncate.isSure()) {
            db.execSQL(truncate.getTruncate());
            return true;
        }
        return false;
    }

    public void megaForceDropTable(DropTable drop) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(drop.getTruncate());
    }

    public boolean megaSafeDropTable(DropTable drop) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (drop.isSure()) {
            db.execSQL(drop.getTruncate());
            return true;
        }
        return false;
    }

    public MegaCursor megaSelect(ProjectionList projection, @NonNull JoinHelper tables, WhereWrapper where, OrderList sortby) {
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
        ArrayList<String> whereArgs = null;
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
        query.append(";");

        //EXECUTE
        SQLiteDatabase db = this.getReadableDatabase();
        if (whereArgs != null) {
            return new MegaCursor(db.rawQuery(query.toString(), whereArgs.toArray(new String[whereArgs.size()])));
        }
        return new MegaCursor(db.rawQuery(query.toString(), null));
    }

    public MegaCursor megaSelect(Query query) {
        Query.QueryObject obj = query.getQuery();
        SQLiteDatabase db = this.getReadableDatabase();
        if (obj.getWhereArgs() != null) {
            return new MegaCursor(db.rawQuery(obj.getQuery(), obj.getWhereArgs()));
        }
        return new MegaCursor(db.rawQuery(obj.getQuery(), null));
    }

    public MegaCursor megaSelect(Union union) {
        Query.QueryObject obj = union.getQuery();
        SQLiteDatabase db = this.getReadableDatabase();
        if (obj.getWhereArgs() != null) {
            return new MegaCursor(db.rawQuery(obj.getQuery(), obj.getWhereArgs()));
        }
        return new MegaCursor(db.rawQuery(obj.getQuery(), null));
    }


    public boolean megaUpdate(Update update) {
        SQLiteDatabase db = this.getWritableDatabase();
        Update.UpdateObject updateObject = update.getWhere();
        int x = db.update(
                update.getTable(),
                update.getRecord(),
                updateObject.getStatement(),
                updateObject.getWhereArgs()
        );
        return x > 0;
    }

    public boolean megaUpdate(List<Update> update) {
        SQLiteDatabase db = this.getWritableDatabase();
//        Update.UpdateObject updateObject = update.getWhere();
//
//        db.beginTransaction();
//        db.update(
//                update.getTable(),
//                update.getRecord(),
//                updateObject.getStatement(),
//                updateObject.getWhereArgs()
//        );
//        db.setTransactionSuccessful();
//        db.endTransaction();
        for (Update u : update) {
            Update.UpdateObject updateObject = u.getWhere();
            db.update(
                    u.getTable(),
                    u.getRecord(),
                    updateObject.getStatement(),
                    updateObject.getWhereArgs()
            );
            db.setTransactionSuccessful();
        }
        return false;
    }


    public boolean megaInsert(Insert insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        long x = db.insert(insert.getTable(), null, insert.getRecord());
        return x > 0;
    }

    public boolean megaInsert(List<Insert> insertList) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean Successful = true;
        db.beginTransaction();
        for (Insert insert : insertList) {
            long x = db.insert(insert.getTable(), null, insert.getRecord());
            Successful = x > 0 && Successful;
        }
        if (Successful) {
            db.setTransactionSuccessful();
        }
        db.endTransaction();
        return Successful;
    }

    public boolean megaDelete(Delete delete) {
        SQLiteDatabase db = this.getWritableDatabase();
        Delete.DeleteObject where = delete.getWhere();
        long x = db.delete(delete.getTable(), where.getStatement(), where.getWhereArgs());
        return x > 0;
    }


}
