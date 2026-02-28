package com.example.expansetracker.data


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.expansetracker.data.dao.ExpanseDao
import com.example.expansetracker.data.module.ExpanseEntity
import com.example.expansetracker.data.module.ListEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.expansetracker.data.module.ListTitle


annotation class Inject

class ExpanseRepository @Inject constructor(private val expanseDao: ExpanseDao){

    suspend fun insertListTitle(title: ListTitle){
        expanseDao.insertListTitle(title)
    }

    fun getAllListTitles(): Flow<List<String>> {
        return expanseDao.getAllListTitles()
    }

    suspend fun deleteListTitle(title: String){
        expanseDao.deleteListTitle(title)
    }
    suspend fun insertExpanse(expanseEntity: ExpanseEntity){
        expanseDao.insertExpanse(expanseEntity)
    }
    fun getAllExpanses(): Flow<List<ExpanseEntity>>{
        return expanseDao.getAllExpanses()
    }
    suspend fun deleteAllExpense(){
        expanseDao.deleteAll()
    }
    suspend fun deleteExpense(expense: ExpanseEntity) {
        expanseDao.delete(expense)
    }
    suspend fun insertEntity(entityList: ListEntity) {
        expanseDao.insertEntity(entityList)
    }
    fun getAllEntities(): Flow<List<ListEntity>>{
        return  expanseDao.getALLEntities()
    }
    suspend fun deleteEntity(entity: ListEntity){
        expanseDao.deleteEntity(entity)
    }
}


@Database(entities = [ExpanseEntity::class, ListEntity::class, ListTitle::class], version = 6)
abstract class ExpanseDataBase : RoomDatabase() {
    abstract fun expanseDao(): ExpanseDao


    companion object {

        @Volatile
        private var INSTANCE: ExpanseDataBase? = null

        fun getDatabase(context: Context): ExpanseDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpanseDataBase::class.java,
                    "expanse_database"
                )
                    .addMigrations(MIGRATION_1_2,Migration_2_3,Migration_3_4,Migration_4_5,Migration_5_6)
                    .build()
                INSTANCE = instance
                instance
            }
        }
        // Migration from version 1 to 2 (fixing the list_entity table)
        private val MIGRATION_1_2 =object : Migration(1,2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS 'list_entity'(" +
                    "'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "'entity' TEXT NOT NULL," +
                    "'quantity' REAL NOT NULL)"

                )
            }
        }
         //Migration from version 2 to 3 (adding list_title table)
        private val Migration_2_3 = object : Migration(2,3){
            override fun migrate(db: SupportSQLiteDatabase) {
               db.execSQL(
                    "ALTER TABLE 'list_entity' ADD COLUMN 'listTitle' TEXT NOT NULL DEFAULT ''"
                )
            }
        }
        private val Migration_3_4 = object : Migration(3,4){
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS 'list_titles'(" +
                            "'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "'title' TEXT NOT NULL)"
                )
            }
        }
        private val Migration_4_5 = object : Migration(4,5){
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE 'list_entity' ADD COLUMN 'quantityType' TEXT NOT NULL DEFAULT ''"
                )
            }
        }
        private val Migration_5_6 = object : Migration(5,6){
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE 'list_entity' ADD COLUMN 'price' REAL NOT NULL DEFAULT 0.0"
                )
            }
        }
    }
}
