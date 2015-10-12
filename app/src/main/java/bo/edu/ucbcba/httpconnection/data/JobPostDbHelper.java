package bo.edu.ucbcba.httpconnection.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import bo.edu.ucbcba.httpconnection.data.JobPostDbContract.JobPost;

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
                            JobPost.POSTED_DATE_COLUMN + " TEXT NOT NULL)";

        // Ejecuta el SQL
        db.execSQL(sqlCreateJobPost);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + JobPost.TABLE_NAME);
        onCreate(db);
    }
}
