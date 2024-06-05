import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.annethomestay.DataListPenginapan
import com.example.annethomestay.R

class DataListPenginapanAdapter(private val context: Context, private val arrayList: ArrayList<DataListPenginapan>) :
    ArrayAdapter<DataListPenginapan>(context, R.layout.list_home, arrayList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.list_home, parent, false)
            viewHolder = ViewHolder()
            viewHolder.imageView = view.findViewById(R.id.img_list_home)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val item = arrayList[position]

        // Mengambil ID gambar dari nama gambar
        val drawableId = context.resources.getIdentifier(item.img, "drawable", context.packageName)

        // Memuat gambar ke ImageView menggunakan Drawable
        viewHolder.imageView.setImageDrawable(ContextCompat.getDrawable(context, drawableId))

        return view!!
    }

    private class ViewHolder {
        lateinit var imageView: ImageView
    }
}
