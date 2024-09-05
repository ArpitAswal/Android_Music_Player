package com.example.playmusic.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.playmusic.dataobject.DBMusicData
import com.example.playmusic.dataobject.DBPlaylistData
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@Database(entities = [DBPlaylistData::class, DBMusicData::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "playlist_database"
                )  .fallbackToDestructiveMigration()
                    .addCallback(AppDatabaseCallback(context)) // Add the callback here
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class AppDatabaseCallback(
            private val context: Context
        ) : Callback() {

            @OptIn(DelicateCoroutinesApi::class)
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Insert the "Liked Songs" playlist when the database is created
                Executors.newSingleThreadExecutor().execute {
                    GlobalScope.launch {
                        getDatabase(context).playlistDao().insertPlaylist(
                            DBPlaylistData(playlistId = -1, playlistName = "Liked Songs", playlistProfile = null)
                        )
                        populateDatabase(INSTANCE!!)
                    }
                }
            }
        }

        private fun populateDatabase(db: AppDatabase) {
            val playlistDao = db.playlistDao()

        }
    }
}

