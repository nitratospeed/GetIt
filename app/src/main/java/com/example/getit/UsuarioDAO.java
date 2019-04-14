package com.example.getit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class UsuarioDAO {
    private DbHelper _dbHelper;

    public UsuarioDAO(Context c) {
        _dbHelper = new DbHelper(c);
    }

    public void SignUp(String email, String password) throws DAOException {
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        try {
            String[] args = new String[]{email, password};
            db.execSQL("INSERT INTO usuario(email, password) VALUES(?,?)", args);
        } catch (Exception e) {
            throw new DAOException("Error al registrar usuario: " + e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public String Login(String email, String password) throws DAOException {

        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        ArrayList<String> lista = new ArrayList<String>();
        String NombresApellidos = "";
        try {
            Cursor c = db.rawQuery("select email from usuario where email = '" + email + "' and password = '" + password + "'", null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    NombresApellidos = c.getString(c.getColumnIndex("email"));
                    lista.add(NombresApellidos);
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            throw new DAOException("Error al iniciar sesión: " + e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return NombresApellidos;
    }

}
