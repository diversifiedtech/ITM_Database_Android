package caruso.nicholas.com.itm_database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import caruso.nicholas.com.itm_database.QueryBuilder.CreateTable;
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
    protected Context context;

    public abstract ArrayList<TableHelper> all_tables();

    protected abstract UpgradeHelper getUpgradeHelper(SQLiteDatabase db, int oldVersion, int newVersion);

    public DatabaseHelper(Context context, String remote_database_link, String database, SQLiteDatabase.CursorFactory cursorFactory, int version) {
        super(context, database, cursorFactory, version);
        this.REMOTE_DATABASE_LINK = remote_database_link;
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (TableHelper t : all_tables()) {
            megaCreateTable(t.CREATE_TABLE(), db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        UpgradeHelper upgradeHelper = getUpgradeHelper(db, oldVersion, newVersion);
        upgradeHelper.upgrade(this);
    }

    /*
        Static Methods that use their own SQLite Database
     */
    public static void execSQL(String sql, SQLiteDatabase db) {
        db.execSQL(sql);
    }

    public static void execSQL(String sql, Object[] bindArgs, SQLiteDatabase db) {
        db.execSQL(sql, bindArgs);
    }

    public static MegaCursor megaSelect(Query query, SQLiteDatabase db) {
        Query.QueryObject obj = query.getQuery();
        if (obj.getWhereArgs() != null) {
            return new MegaCursor(db.rawQuery(obj.getQuery(), obj.getWhereArgs()));
        }
        return new MegaCursor(db.rawQuery(obj.getQuery(), null));
    }

    public static MegaCursor megaSelect(Union union, SQLiteDatabase db) {
        Query.QueryObject obj = union.getQuery();
        if (obj.getWhereArgs() != null) {
            return new MegaCursor(db.rawQuery(obj.getQuery(), obj.getWhereArgs()));
        }
        return new MegaCursor(db.rawQuery(obj.getQuery(), null));
    }

    public static MegaCursor megaSelect(ProjectionList projection, @NonNull JoinHelper tables, WhereWrapper where, OrderList sortby, SQLiteDatabase db) {
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
        if (whereArgs != null) {
            return new MegaCursor(db.rawQuery(query.toString(), whereArgs.toArray(new String[whereArgs.size()])));
        }
        return new MegaCursor(db.rawQuery(query.toString(), null));
    }

    public static boolean megaInsert(Insert insert, SQLiteDatabase db) {
        long x = db.insert(insert.getTable(), null, insert.getRecord());
        return x > 0;
    }

    public static boolean megaInsert(List<Insert> insertList, SQLiteDatabase db) {
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

    public static boolean megaUpdate(Update update, SQLiteDatabase db) {
        Update.UpdateObject updateObject = update.getWhere();
        int x = db.update(
                update.getTable(),
                update.getRecord(),
                updateObject.getStatement(),
                updateObject.getWhereArgs()
        );
        return x > 0;
    }

    public static boolean megaUpdate(List<Update> update, SQLiteDatabase db) {
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

    public static boolean megaDelete(Delete delete, SQLiteDatabase db) {
        Delete.DeleteObject where = delete.getWhere();
        long x = db.delete(delete.getTable(), where.getStatement(), where.getWhereArgs());
        return x > 0;
    }

    public static boolean megaSafeTruncate(Truncate truncate, SQLiteDatabase db) {
        if (truncate.isSure()) {
            db.execSQL(truncate.getTruncate());
            return true;
        }
        return false;
    }

    public static void megaForceTruncate(Truncate truncate, SQLiteDatabase db) {
        db.execSQL(truncate.getTruncate());
    }

    public static void megaCreateTable(CreateTable createTable, SQLiteDatabase db) {
        db.execSQL(createTable.get());
    }

    public static void megaCreateTable(String createTableString, SQLiteDatabase db) {
        db.execSQL(createTableString);
    }

    public static boolean megaSafeDropTable(DropTable drop, SQLiteDatabase db) {
        if (drop.isSure()) {
            db.execSQL(drop.getTruncate());
            return true;
        }
        return false;
    }

    public static void megaForceDropTable(DropTable drop, SQLiteDatabase db) {
        db.execSQL(drop.getTruncate());
    }

    /*
        Methods Using Databases SQLite database
     */
    public MegaCursor megaSelect(Query query) {
        return megaSelect(query, this.getReadableDatabase());
    }

    public MegaCursor megaSelect(Union union) {
        return megaSelect(union, this.getReadableDatabase());
    }

    public MegaCursor megaSelect(ProjectionList projection, @NonNull JoinHelper tables, WhereWrapper where, OrderList sortby) {
        return megaSelect(projection, tables, where, sortby, this.getReadableDatabase());
    }

    public boolean megaInsert(Insert insert) {
        return megaInsert(insert, this.getWritableDatabase());
    }

    public boolean megaInsert(List<Insert> insertList) {
        return megaInsert(insertList, this.getWritableDatabase());
    }

    public boolean megaUpdate(Update update) {
        return megaUpdate(update, this.getWritableDatabase());
    }

    public boolean megaUpdate(List<Update> update) {
        return megaUpdate(update, this.getWritableDatabase());
    }

    public boolean megaDelete(Delete delete) {
        return megaDelete(delete, this.getWritableDatabase());
    }

    public boolean megaSafeTruncate(Truncate truncate) {
        return megaSafeTruncate(truncate, this.getWritableDatabase());
    }

    public void megaForceTruncate(Truncate truncate) {
        megaForceTruncate(truncate, this.getWritableDatabase());
    }

    public void megaCreateTable(CreateTable createTable) {
        megaCreateTable(createTable, this.getWritableDatabase());
    }

    public void megaCreateTable(String createTableString) {
        megaCreateTable(createTableString, this.getWritableDatabase());
    }

    public boolean megaSafeDropTable(DropTable drop) {
        return megaSafeDropTable(drop, this.getWritableDatabase());
    }

    public void megaForceDropTable(DropTable drop) {
        megaForceDropTable(drop, this.getWritableDatabase());
    }

}
