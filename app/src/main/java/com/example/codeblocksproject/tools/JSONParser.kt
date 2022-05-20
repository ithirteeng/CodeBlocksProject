package com.example.codeblocksproject.tools

import android.content.Context
import android.widget.Toast
import com.example.codeblocksproject.R
import com.example.codeblocksproject.model.BlockData
import com.google.gson.Gson
import org.json.JSONObject
import java.io.InputStream
import java.nio.charset.Charset

class JSONParser {
    companion object {
        fun parseJSON(context: Context, jsonSeparator: String): List<BlockData> {
            val json = JSONObject(loadJSONFromAssets(context) ?: "").getJSONArray(jsonSeparator)

            return Gson().fromJson(json.toString(), Array<BlockData>::class.java).toList()
        }

        private fun loadJSONFromAssets(context: Context): String? {
            var json: String? = null

            try {
                val input: InputStream = context.assets.open("blocks.json")

                val amountOFBytes = input.available()
                val bufferArray = ByteArray(amountOFBytes)

                input.read(bufferArray)
                input.close()

                json = String(bufferArray, Charset.defaultCharset())
            } catch (exception: Exception) {
                Toast.makeText(
                    context,
                    context.resources.getString(R.string.JSON_error),
                    Toast.LENGTH_SHORT
                ).show()
            }

            return json
        }
    }
}