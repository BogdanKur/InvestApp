package com.example.myapplication

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

class FavoritesDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val TABLE_ASSETS = "assets"
        private const val DATABASE_NAME = "favorites.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_FAVORITES = "favorites"
        private const val TABLE_CURRENCY_PAIRS = "currency_pairs"
        private const val COLUMN_ID = "id"
        private const val COLUMN_SYMBOL = "symbol"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_COUNTRY = "country"
        private const val COLUMN_FULL_NAME = "full_name"
        private const val COLUMN_ISIN = "isin"
        private const val COLUMN_CURRENCY = "currency"
        private const val COLUMN_BASE_CURRENCY = "base_currency"
        private const val COLUMN_CONVERT_CURRENCY = "convert_currency"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createFavoritesTable = """
            CREATE TABLE $TABLE_FAVORITES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SYMBOL TEXT NOT NULL,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_COUNTRY TEXT,                
                $COLUMN_FULL_NAME TEXT,              
                $COLUMN_ISIN TEXT,                  
                $COLUMN_CURRENCY TEXT                
            )
        """

        val createAssetsTable = """
            CREATE TABLE $TABLE_ASSETS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SYMBOL TEXT NOT NULL,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_COUNTRY TEXT,                
                $COLUMN_FULL_NAME TEXT,              
                $COLUMN_ISIN TEXT,                  
                $COLUMN_CURRENCY TEXT,
                UNIQUE($COLUMN_SYMBOL) ON CONFLICT IGNORE
            )
        """

        val createCurrencyPairsTable = """
            CREATE TABLE $TABLE_CURRENCY_PAIRS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_BASE_CURRENCY TEXT NOT NULL,
                $COLUMN_CONVERT_CURRENCY TEXT NOT NULL
            )
        """

        db.execSQL(createFavoritesTable)
        db.execSQL(createAssetsTable)
        db.execSQL(createCurrencyPairsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $TABLE_CURRENCY_PAIRS ADD COLUMN $COLUMN_BASE_CURRENCY TEXT")
            db.execSQL("ALTER TABLE $TABLE_CURRENCY_PAIRS ADD COLUMN $COLUMN_CONVERT_CURRENCY TEXT")
        }
    }
    fun addCryptoToFavorites(symbol: String, name: String): Boolean {
        if (isCryptoAlreadyInFavorites(name)) {
            return false
        }

        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_SYMBOL, symbol)
            put(COLUMN_NAME, name)
        }
        val result = db.insert(TABLE_FAVORITES, null, values)
        db.close()
        return result != -1L
    }

     fun isCryptoAlreadyInFavorites(name: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_FAVORITES WHERE $COLUMN_NAME = ?"
        val cursor = db.rawQuery(query, arrayOf(name))

        val exists = cursor.moveToFirst()
        cursor.close()
        db.close()

        return exists
    }

    fun getFavorites(): List<Crypto> {
        val favoritesList = mutableListOf<Crypto>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_FAVORITES"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val symbol = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SYMBOL))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val crypto = Crypto(symbol = symbol, name = name)
                favoritesList.add(crypto)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return favoritesList
    }



    fun addCurrencyPairToFavorites(baseCurrency: String, convertCurrency: String): Boolean {
        if (isCurrencyPairAlreadyInFavorites(baseCurrency, convertCurrency)) {
            return false
        }
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_BASE_CURRENCY, baseCurrency)
            put(COLUMN_CONVERT_CURRENCY, convertCurrency)
        }
        val result = db.insert(TABLE_CURRENCY_PAIRS, null, values)
        db.close()
        return result != -1L
    }

    fun isCurrencyPairAlreadyInFavorites(baseCurrency: String, convertCurrency: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_CURRENCY_PAIRS WHERE $COLUMN_BASE_CURRENCY = ? AND $COLUMN_CONVERT_CURRENCY = ?"
        val cursor = db.rawQuery(query, arrayOf(baseCurrency, convertCurrency))
        val exists = cursor.moveToFirst()
        cursor.close()
        db.close()

        return exists
    }

    fun getCurrencyPairs(): List<CurrencyPair> {
        val currencyPairsList = mutableListOf<CurrencyPair>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_CURRENCY_PAIRS"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val baseCurrency = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BASE_CURRENCY))
                val convertCurrency = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONVERT_CURRENCY))

                val baseName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BASE_CURRENCY))
                val second = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONVERT_CURRENCY))

                val currencyPair = CurrencyPair(
                    name = baseCurrency,
                    full_name = convertCurrency,
                    base = baseCurrency,
                    base_name = baseName,
                    second = second
                )
                currencyPairsList.add(currencyPair)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return currencyPairsList
    }




    fun addAssetToFavorites(symbol: String, name: String, country: String, fullName: String, isin: String, currency: String): Boolean {
        if (isAssetAlreadyInFavorites(symbol)) {
            return false
        }

        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_SYMBOL, symbol)
            put(COLUMN_NAME, name)
            put(COLUMN_COUNTRY, country)
            put(COLUMN_FULL_NAME, fullName)
            put(COLUMN_ISIN, isin)
            put(COLUMN_CURRENCY, currency)
        }
        val result = db.insert(TABLE_ASSETS, null, values)
        db.close()
        return result != -1L
    }

    fun isAssetAlreadyInFavorites(symbol: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_ASSETS WHERE $COLUMN_SYMBOL = ?"
        val cursor = db.rawQuery(query, arrayOf(symbol))

        val exists = cursor.moveToFirst()
        cursor.close()
        db.close()

        return exists
    }

    fun getFavoritesAsset(): List<Asset> {
        val favoritesList = mutableListOf<Asset>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_ASSETS"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val symbol = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SYMBOL))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val country = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY))
                val fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME))
                val isin = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ISIN))
                val currency = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CURRENCY))
                val asset = Asset(symbol = symbol, name = name, country = country, full_name = fullName, isin = isin, currency = currency)
                favoritesList.add(asset)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return favoritesList
    }

    fun removeCryptoFromFavorites(symbol: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_FAVORITES, "$COLUMN_SYMBOL = ?", arrayOf(symbol))
        db.close()
        return result > 0
    }

    fun removeCurrencyPairFromFavorites(baseCurrency: String, convertCurrency: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_CURRENCY_PAIRS, "$COLUMN_BASE_CURRENCY = ? AND $COLUMN_CONVERT_CURRENCY = ?", arrayOf(baseCurrency, convertCurrency))
        db.close()
        return result > 0
    }

    fun removeAssetFromFavorites(symbol: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_ASSETS, "$COLUMN_SYMBOL = ?", arrayOf(symbol))
        db.close()
        return result > 0
    }
}


