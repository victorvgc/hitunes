package com.skilltest.domain.usecase

import com.skilltest.domain.model.Failure
import com.skilltest.domain.model.RecentlyPlayed
import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.model.SearchResult
import com.skilltest.domain.model.Track
import com.skilltest.domain.repository.SongRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class SongUseCasesTest {
    @Test
    fun searchPassesTermToRepositoryAndPreservesSuccess() = runTest {
        val expected = ResultWrapper.Success(SearchResult(listOf(track)))
        val repository = RecordingRepository(expected)

        val result = SearchSongsByTermUseCase(repository)(SearchSongsByTermUseCase.Request("jazz"))

        assertEquals("jazz", repository.lastSearchTerm)
        assertSame(expected, result)
    }

    @Test
    fun searchPreservesRepositoryFailure() = runTest {
        val expected = ResultWrapper.Error<SearchResult>(Failure(msg = "offline"))
        val repository = RecordingRepository(expected)

        val result = SearchSongsByTermUseCase(repository)(SearchSongsByTermUseCase.Request("rock"))

        assertEquals("rock", repository.lastSearchTerm)
        assertSame(expected, result)
    }

    @Test
    fun loadingStartsOnlyAfterTwoCharacters() = runTest {
        val useCase = ShouldShowSearchLoadingUseCase()

        for ((term, expected) in listOf("" to false, "a" to false, "ab" to false, "abc" to true)) {
            val request = ShouldShowSearchLoadingUseCase.Request(term, term, null)
            assertEquals("Loading for query '$term'", expected, useCase(request).data)
        }
    }

    @Test
    fun loadingDoesNotStartForOutdatedOrAlreadySearchedTerms() = runTest {
        val useCase = ShouldShowSearchLoadingUseCase()

        assertEquals(
            false,
            useCase(ShouldShowSearchLoadingUseCase.Request("abc", "abcd", null)).data
        )
        assertEquals(
            false,
            useCase(ShouldShowSearchLoadingUseCase.Request("abc", "abc", "abc")).data
        )
        assertEquals(
            true,
            useCase(ShouldShowSearchLoadingUseCase.Request("abc", "abc", "old")).data
        )
    }

    private class RecordingRepository(
        private val searchResult: ResultWrapper<SearchResult>,
    ) : SongRepository {
        var lastSearchTerm: String? = null

        override suspend fun searchSongsByTerm(term: String): ResultWrapper<SearchResult> {
            lastSearchTerm = term
            return searchResult
        }

        override suspend fun addToRecentlyPlayed(track: Track) = ResultWrapper.Success(Unit)
        override suspend fun getRecentlyPlayed() = ResultWrapper.Success(RecentlyPlayed(emptyList()))
    }

    private companion object {
        val track = Track("1", "Song", "Artist", "Album", "", false)
    }
}
