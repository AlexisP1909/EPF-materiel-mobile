package fr.epf.mm.countrysearch.adapters

import android.content.Context
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool
import com.bumptech.glide.load.resource.SimpleResource
import com.caverock.androidsvg.SVG
import java.io.InputStream

class SvgDecoder : ResourceDecoder<InputStream, SVG> {
    override fun handles(source: InputStream, options: Options): Boolean = true

    override fun decode(source: InputStream, width: Int, height: Int, options: Options): SimpleResource<SVG>? {
        try {
            val svg = SVG.getFromInputStream(source)
            return SimpleResource(svg)
        } catch (ex: Exception) {
            throw ex
        }
    }
}