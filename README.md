# ITM_Database_Android
A series of classes that make it easier to create Querys and objects based on database tables. There are three main classes Items, Modals, and Tables. Items are Object classes that can be used with your activitys to get data. Modal Classes are Used to generate these Item classes, Lists of Item Classes or write up any other querys you want to write. Table classes are used for the inital build of the database and to set values for what does and doesn't sync up to your server when you request

### Prerequisites

minSdkVersion 19

## Getting Started
Add dependencies

```
implementation 'com.github.nmc9:ITM_Database_Android:v1.3.2-beta'
```
Note*: This code can be seen in the exmaple app that comes along

It's recommended to create a database folder in your project and inside that folder add three more
1. Item
2. Table
3. Modal
These will hold all your ITM classes.

Next create a subclass of DatabaseHelper and UpgradeHelper
### Database
```
public final class Database extends DatabaseHelper {
    public static final String REMOTE_DATABASE_LINK = "{yourserver.com}";
    
    //Optional Authentication Table
    public static final String AuthTable = UsersTable.TABLE_NAME;
    
    //Name of local Database
    public static final String DATABASE_NAME = "local_database";
    
    //Version Number
    private static final int VERSION = 3;
    
    public Database(Context context) {
        super(context, REMOTE_DATABASE_LINK, DATABASE_NAME, null, VERSION);
    }

    //List of the tables in your project
    @Override
    public ArrayList<TableHelper> all_tables() {
        ArrayList<TableHelper> TABLES = new ArrayList<>();
        TABLES.add(new UsersTable(this));
        return TABLES;
    }
    
    // Create a new instance of UpgradeHelper Class
    @Override
    protected UpgradeHelper getUpgradeHelper(SQLiteDatabase db, int oldVersion, int newVersion) {
        return new Upgrade( db, oldVersion, newVersion);
    }


}
```

### Upgrade 

```
public class Upgrade extends UpgradeHelper {
    public Upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super(db, oldVersion, newVersion);
    }
    //Logic for what to do when the app is upgraded
    @Override
    public void upgrade(DatabaseHelper database) {
        switch (oldVersion) {
            case 2:
            case 3:
                break;
            default:
                for (TableHelper t : database.all_tables()) {
                    DropTable dropTable = new DropTable(t.table_name());
                    Database.megaSafeDropTable(dropTable.ImSure().ifExists(), db);
                    Database.megaCreateTable(t.CREATE_TABLE(), db);
                }
        }
    }
}
```
Now you can create ITM classes
 
### Example Table Class
The table class should contain a list of all the columns of the table.
It should also extend the TableHelper class. This class has the following methods and uses them to determine what columns and records that should sync when syncing occurs.
The CreateTable method below will be described in more detail in the QueryBuilder section

```public class UsersTable extends TableHelper {

    //Name of the table It should match the server table's name
    public static final String TABLE_NAME = "users";
    
    //List of columns. Column names should match server names
    public static final String COL_ID = "userid";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";
    
    // List of fields
    public static final String[] FIELDS = {
            COL_ID,
            COL_USERNAME,
            COL_PASSWORD
    };

    //List of fields that should be sent up on a server sync
    //If all should be synced return FIELDS;
    @Override
    public String[] sync_up_filter_fields() {
        return new String[]{COL_ID};
    }

    //Constructor
    public UsersTable(DatabaseHelper databaseHelper) {
        super(databaseHelper);
    }
  
    //If true this table is synced up
    @Override
    public boolean sync_up() {
        return false;
    }
    
    //If true this table is synced down
    @Override
    public boolean sync_down() {
        return false;
    }

    //What conditions need to be met for a record to be added to sync_up
    @Override
    public Where sync_up_condition() {
        return SYNC_NONE;
    }

    //Returns the name of the table
    @Override
    public String table_name() {
        return TABLE_NAME;
    }

    //Returns the id column of the table
    @Override
    public String id_column() {
        return COL_ID;
    }

    //Returns list of columns that make up the table
    @Override
    public String[] fields() {
        return FIELDS;
    }
    
    /*
    * Using the Create Table class build the query for Creating this table.
    * CreateTable has a lot of shortcut methods that can be used to remove hardcoded strings
    *
    * Building the CreateTable is similar to writing a create table statement
    * Create a new instance using the TABLE_NAME;
    * add columns with CreateTable.put();
    * put accepts a varaible number of strings so
    *   COLUMN_NAME TYPE(#) ATTRUBUTES looks like
    *   COLUMN_NAME, TYPE(#), PRIMARY_KEY
    *When the table is built use .get() to return the String used to create the table
    */
    public String CREATE_TABLE() {
        CreateTable user = new CreateTable(TABLE_NAME);
        user.put(COL_ID, INT(11), PRIMARY_KEY);
        user.put(COL_USERNAME, VARCHAR(20));
        user.put(COL_PASSWORD, VARCHAR(20));
        return user.get();
    }
}
}
```

### Example Item Class
The item class should look like the table class. It should have all the varaibles you want to use in your activities.
It's usefull to add a static ```getModal(Context context)``` method so you can access the related modal from Item classes.
There are two constructors. One that builds a blank Item and one that builds an Item from a MegaCursor (More info in queryBuilder). In this constructor set the Item class varaibles with this cursor using the stings from your Table class.
It should look something like this ``` varaible = cursor.getIntByField(ClassTable.FIELD_NAME); ```

The getRecord method is used for making Insert/Update Operations. This should do the reverse of the constructor. Create a ContentValue object and fill it with the variables of this class. It should look something like this 
```contentvalue.put(ClassTable.COLUMN_NAME, variable); ```
The dump method is mainly used for testing. Plug values in to return a String to Log data

There are three other methods Insert,Update,Delete. They do what the name implies.
Use these methods to call helper methods that will do the work of these methods for you.
Follow the example for an idea but each of these methods should have the same flow.
1. Create DatabaseHelper Object
2. Pass data to helper function
3. close DatabaseHelper object;
4. return result.

There are 4 helper functions insert,update,delete,insertWithId
#### insert() parameters
1. DatabaseHelper object
2. The name of the table being inserted
3. The name of the id column
4. Content Values retrieved by getRecord();

#### update() parameters
1. DatabaseHelper object
2. The name of the table being inserted
3. The name of the id column
4. Content Values retrieved by getRecord();
5. the id column value of this record

#### delete() parameters
1. DatabaseHelper object
2. The name of the table being inserted
3. The name of the id column
5. the id column value of this record

#### insertWithId() parameters
Use insert if table has AUTO_INCREMENT id insertWithId if not
1. DatabaseHelper object
2. The name of the table being inserted
3. The name of the id column
4. Content Values retrieved by getRecord();
```
public class User extends Item {

    public Integer userid;
    public String username;
    public String password;

    public static UserModal getModal(Context mContext) {
        return new UserModal(mContext);
    }

    public User() {

    }
     
    public User(MegaCursor cursor) {
        super();
        userid = cursor.getIntByField(UsersTable.COL_ID);
        username = cursor.getStringByField(UsersTable.COL_USERNAME);
        password = cursor.getStringByField(UsersTable.COL_PASSWORD);
        blob = cursor.getStringByField(UsersTable.FIELD_BLOB);
        dub = cursor.getDoubleByField(UsersTable.FIELD_DOUBLE);
        date = DateTimeConversion.StringToDate(cursor.getStringByField(UsersTable.FIELD_DATETIME));
    }

    @Override
    public String dump() {
        String dump = "User dump:\n";
        dump += "userid" + userid + "\n";
        dump += "username" + username + "\n";
        dump += "password" + password + "\n";
        return dump;
    }

    @Override
    public ContentValues getRecord() {
        ContentValues cv = new ContentValues();
        cv.put(UsersTable.COL_ID, userid);
        cv.put(UsersTable.COL_USERNAME, username);
        cv.put(UsersTable.COL_PASSWORD, password);
        return cv;
    }

    @Override
    public boolean insert(Context context) {
        DatabaseHelper db = new Database(context);
        boolean i = insertWithId(db, UsersTable.TABLE_NAME, UsersTable.COL_ID, getRecord());
        db.close();
        return i;
    }

    @Override
    public boolean update(Context context) {
        DatabaseHelper db = new Database(context);
        boolean i = update(db, UsersTable.TABLE_NAME, UsersTable.COL_ID, getRecord(), userid);
        db.close();
        return i;
    }

    @Override
    public boolean delete(Context context, int mode) {
        DatabaseHelper db = new Database(context);
        boolean i = delete(db, UsersTable.TABLE_NAME, UsersTable.COL_ID, userid);
        db.close();
        return i;
    }
}
```
### Example Alternative Item class
Not Every item class needs to be an exact clone of a database table



## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone who's code was used
* Inspiration
* etc
