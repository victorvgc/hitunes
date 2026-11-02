package com.skilltest.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.skilltest.domain.model.Track

@Entity(tableName = "recently_played_track")
internal data class RecentlyPlayedTrackEntity(
    @PrimaryKey val id: String,
    val name: String,
    @ColumnInfo(name = "artist_name") val artistName: String,
    @ColumnInfo(name = "album_name") val albumName: String,
    @ColumnInfo(name = "artwork_url") val artworkUrl: String,
    @ColumnInfo(name = "is_explicit") val isExplicit: Boolean,
    @ColumnInfo(name = "artist_id") val artistId: String,
    @ColumnInfo(name = "album_id") val albumId: String,
    @ColumnInfo(name = "stream_url") val streamUrl: String,
    @ColumnInfo(name = "last_played") val lastPlayed: Long,
) {
    fun toDomain() = Track(
        id = id,
        name = name,
        artistName = artistName,
        albumName = albumName,
        artworkUrl = artworkUrl,
        isExplicit = isExplicit,
        artistId = artistId,
        albumId = albumId,
        streamUrl = streamUrl,
    )

    companion object {
        fun Track.toEntity(lastPlayed: Long) = RecentlyPlayedTrackEntity(
            id = id,
            name = name,
            artistName = artistName,
            albumName = albumName,
            artworkUrl = artworkUrl,
            isExplicit = isExplicit,
            artistId = artistId,
            albumId = albumId,
            streamUrl = streamUrl,
            lastPlayed = lastPlayed,
        )
    }
}
