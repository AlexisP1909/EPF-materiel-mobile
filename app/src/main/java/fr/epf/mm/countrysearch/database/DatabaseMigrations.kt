package fr.epf.mm.countrysearch.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE countries ADD COLUMN population INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE countries ADD COLUMN language TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE countries ADD COLUMN currency TEXT NOT NULL DEFAULT ''")
    }
}