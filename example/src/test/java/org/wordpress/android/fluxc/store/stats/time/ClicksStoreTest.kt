package org.wordpress.android.fluxc.store.stats.time

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.experimental.Dispatchers.Unconfined
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.wordpress.android.fluxc.model.SiteModel
import org.wordpress.android.fluxc.model.stats.time.ClicksModel
import org.wordpress.android.fluxc.model.stats.time.TimeStatsMapper
import org.wordpress.android.fluxc.network.rest.wpcom.stats.time.ClicksRestClient
import org.wordpress.android.fluxc.network.rest.wpcom.stats.time.ClicksRestClient.ClicksResponse
import org.wordpress.android.fluxc.network.utils.StatsGranularity.DAYS
import org.wordpress.android.fluxc.persistence.TimeStatsSqlUtils
import org.wordpress.android.fluxc.store.StatsStore.FetchStatsPayload
import org.wordpress.android.fluxc.store.StatsStore.StatsError
import org.wordpress.android.fluxc.store.StatsStore.StatsErrorType.API_ERROR
import org.wordpress.android.fluxc.test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private const val PAGE_SIZE = 8

@RunWith(MockitoJUnitRunner::class)
class ClicksStoreTest {
    @Mock lateinit var site: SiteModel
    @Mock lateinit var restClient: ClicksRestClient
    @Mock lateinit var sqlUtils: TimeStatsSqlUtils
    @Mock lateinit var mapper: TimeStatsMapper
    private lateinit var store: ClicksStore
    @Before
    fun setUp() {
        store = ClicksStore(
                restClient,
                sqlUtils,
                mapper,
                Unconfined
        )
    }

    @Test
    fun `returns clicks per site`() = test {
        val fetchInsightsPayload = FetchStatsPayload(
                CLICKS_RESPONSE
        )
        val forced = true
        whenever(restClient.fetchClicks(site, DAYS, PAGE_SIZE + 1, forced)).thenReturn(
                fetchInsightsPayload
        )
        val model = mock<ClicksModel>()
        whenever(mapper.map(CLICKS_RESPONSE, PAGE_SIZE)).thenReturn(model)

        val responseModel = store.fetchClicks(site, PAGE_SIZE, DAYS, forced)

        assertThat(responseModel.model).isEqualTo(model)
        verify(sqlUtils).insert(site, CLICKS_RESPONSE, DAYS)
    }

    @Test
    fun `returns error when clicks call fail`() = test {
        val type = API_ERROR
        val message = "message"
        val errorPayload = FetchStatsPayload<ClicksResponse>(StatsError(type, message))
        val forced = true
        whenever(restClient.fetchClicks(site, DAYS, PAGE_SIZE + 1, forced)).thenReturn(errorPayload)

        val responseModel = store.fetchClicks(site, PAGE_SIZE, DAYS, forced)

        assertNotNull(responseModel.error)
        val error = responseModel.error!!
        assertEquals(type, error.type)
        assertEquals(message, error.message)
    }

    @Test
    fun `returns clicks from db`() {
        whenever(sqlUtils.selectClicks(site, DAYS)).thenReturn(CLICKS_RESPONSE)
        val model = mock<ClicksModel>()
        whenever(mapper.map(CLICKS_RESPONSE, PAGE_SIZE)).thenReturn(model)

        val result = store.getClicks(site, DAYS, PAGE_SIZE)

        assertThat(result).isEqualTo(model)
    }
}