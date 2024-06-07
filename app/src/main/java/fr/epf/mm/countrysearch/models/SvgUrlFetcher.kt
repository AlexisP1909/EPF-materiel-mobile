package fr.epf.mm.countrysearch.models

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import com.caverock.androidsvg.SVG
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class SvgUrlFetcher(private val url: String) : DataFetcher<SVG> {

    private var stream: InputStream? = null

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in SVG>) {
        try {
            val urlConnection = URL(url).openConnection() as HttpURLConnection
            stream = urlConnection.inputStream
            val svg = SVG.getFromInputStream(stream)
            callback.onDataReady(svg)
        } catch (e: Exception) {
            callback.onLoadFailed(e)
        }
    }

    override fun cleanup() {
        stream?.close()
    }

    override fun cancel() {}

    override fun getDataClass(): Class<SVG> = SVG::class.java

    override fun getDataSource(): DataSource = DataSource.REMOTE
}