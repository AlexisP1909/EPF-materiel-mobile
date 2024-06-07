package fr.epf.mm.countrysearch.models

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey
import com.caverock.androidsvg.SVG
import java.io.InputStream

class SvgUrlLoader : ModelLoader<String, SVG> {

    override fun handles(s: String): Boolean = true

    override fun buildLoadData(model: String, width: Int, height: Int, options: Options): ModelLoader.LoadData<SVG>? {
        return ModelLoader.LoadData(ObjectKey(model), SvgUrlFetcher(model))
    }

    class Factory : ModelLoaderFactory<String, SVG> {
        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<String, SVG> {
            return SvgUrlLoader()
        }

        override fun teardown() {}
    }
}