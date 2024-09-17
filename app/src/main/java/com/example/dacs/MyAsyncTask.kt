package com.example.dacs

import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MyAsyncTask : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg params: String?): String {
        val url = URL(params[0])
        val connection = url.openConnection() as HttpURLConnection
        try {
            val inputStream = connection.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String? = null
            while ({ line = reader.readLine(); line }() != null) {
                stringBuilder.append(line)
            }
            reader.close()
            inputStream.close()
            return stringBuilder.toString()
        } catch (e: Exception) {
            Log.e("TAG", "Error fetching data: ${e.message}")
        } finally {
            connection.disconnect()
        }
        return ""
    }

    override fun onPostExecute(result: String?) {
        // Parse the result using Gson on the main thread
        val data = Gson().fromJson(result, DataModel::class.java)
    }
}
