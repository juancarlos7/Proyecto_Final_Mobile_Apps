package bo.edu.ucbcba.httpconnection.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import bo.edu.ucbcba.httpconnection.data.JobPostDbContract.JobPost;

import static bo.edu.ucbcba.httpconnection.data.JobPostDbContract.*;

public class JobPostDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "job_posts.db";
    private static int VERSION = 1;
    public JobPostDbHelper(Context context) {
        // Context: Contexto de la aplicacion
        // DB_NAME: Nombre de la base de datos
        // Cursor Factory: Por defecto en null
        // Numero de version
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // CREATE TABLE NombreTabla (p_key TIPO PRIMARY KEY ON CONFLICT REPLACE, attr TIPO, attr TIPO)
        String sqlCreateJobPost = "CREATE TABLE " + JobPost.TABLE_NAME + "(" +
                            JobPost._ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE," +
                            JobPost.TITLE_COLUMN + " TEXT NOT NULL," +
                            JobPost.DESCRIPTION_COLUMN + " TEXT NOT NULL," +
                            JobPost.POSTED_DATE_COLUMN + " TEXT NOT NULL);";

        String sqlCreateContact = "CREATE TABLE " + Contact.TABLE_NAME + "(" +
                            Contact._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                            Contact.NUMBER_COLUMN + " TEXT NOT NULL," +
                            Contact.JOB_POST_ID_COLUMN + " INTEGER NOT NULL," +
                            "FOREIGN KEY(" + Contact.JOB_POST_ID_COLUMN + ")" +
                            " REFERENCES " + JobPost.TABLE_NAME + "(" + JobPost._ID + ")," +
                            "UNIQUE (" + Contact.NUMBER_COLUMN + "," + Contact.JOB_POST_ID_COLUMN
                            + ") ON CONFLICT REPLACE);";

        // Ejecuta el SQL
        db.execSQL(sqlCreateJobPost);
        db.execSQL(sqlCreateContact);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + Contact.TABLE_NAME);
        db.execSQL("DROP TABLE " + JobPost.TABLE_NAME);
        onCreate(db);
    }
}
