package club.androidexpress.banners

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import java.io.InputStream

class BannerFragment : Fragment() {

    companion object {
        fun getInstance(filename: String): BannerFragment {
            return BannerFragment().apply {
                arguments = Bundle().apply {
                    putString("filename", filename)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_banner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val img: ImageView = view.findViewById<ImageView>(R.id.img)
        val fileName: String?  = requireArguments().getString("filename")
        val file: InputStream? = fileName?.let { resources.assets.open(it) }
        val bitmap = BitmapFactory.decodeStream(file)

        file?.close()
        img.setImageBitmap(bitmap)
    }
}