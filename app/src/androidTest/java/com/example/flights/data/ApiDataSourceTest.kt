package com.example.flights.data

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.json.JSONArray
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

class TestCallback(): VolleyCallback{
    override suspend fun onSuccess(flightList: JSONArray) {
        assertThat(flightList).isNotNull()
    }

    override fun onError() {
        TODO("Not yet implemented")
    }

}

@RunWith(AndroidJUnit4::class)
class ApiDataSourceTest {

    private lateinit var apiDataSource: ApiDataSource
    private lateinit var testCallback: TestCallback

    @Before
    fun setup(){
        apiDataSource = ApiDataSource(ApplicationProvider.getApplicationContext())
        testCallback = TestCallback()
    }

    @Test
    fun apiTestResponse() = runTest {

        apiDataSource.getFlightsList(testCallback)

    }

}