package fr.epf.mm.countrysearch.adapters

import android.graphics.drawable.Drawable
import android.graphics.drawable.PictureDrawable
import android.widget.ImageView
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.transition.Transition

class SvgSoftwareLayerSetter(view: ImageView) : ImageViewTarget<PictureDrawable>(view) {

    override fun setResource(resource: PictureDrawable?) {
        view.setLayerType(ImageView.LAYER_TYPE_SOFTWARE, null)
        resource?.let { view.setImageDrawable(it) }
    }

    override fun setDrawable(drawable: Drawable?) {
        view.setLayerType(ImageView.LAYER_TYPE_SOFTWARE, null)
        super.setDrawable(drawable)
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        view.setLayerType(ImageView.LAYER_TYPE_NONE, null)
        super.onLoadFailed(errorDrawable)
    }

    override fun onResourceReady(resource: PictureDrawable, transition: Transition<in PictureDrawable>?) {
        view.setLayerType(ImageView.LAYER_TYPE_SOFTWARE, null)
        super.onResourceReady(resource, transition)
    }
}