import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ohc.ohcrop.R

class hydroponicSystemAdapter(private val slides: List<HydroponicSystemSlide>) :
    RecyclerView.Adapter<hydroponicSystemAdapter.SliderViewHolder>() {

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val title: TextView = itemView.findViewById(R.id.texttitle)
        private val textView: TextView = itemView.findViewById(R.id.textView)
        private val imageView1: ImageView = itemView.findViewById(R.id.imageView1)
        private val imageView2: ImageView = itemView.findViewById(R.id.imageView2)

        fun bind(slide: HydroponicSystemSlide) {
            imageView.setImageResource(slide.imageRes)
            title.text = slide.title
            textView.text = slide.text
            imageView1.setImageResource(slide.imageRes1)
            imageView2.setImageResource(slide.imageRes2)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_slide, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(slides[position])
    }

    override fun getItemCount(): Int {
        return slides.size
    }
}
