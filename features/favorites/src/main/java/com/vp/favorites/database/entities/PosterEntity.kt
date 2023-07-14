package com.vp.favorites.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items_table")
data class PosterEntity(
    @PrimaryKey(autoGenerate = false)
    val poster: String
)