import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dacs.Adapter.TVShowAdapter
import com.example.dacs.Data.Favorite
import com.example.dacs.Data.History
import com.example.dacs.Data.TVShowData
import com.example.dacs.R

class FavoriteAdapter(private val pmlist: List<Favorite>) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val poster: ImageView = itemView.findViewById(R.id.poster)
        private val name: TextView = itemView.findViewById(R.id.nameMovie)
        private val date: TextView = itemView.findViewById(R.id.textView12)
        fun bind(tvShowData: Favorite) {
            name.text = tvShowData.namePhim
//            date.text = tvShowData.date
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500/${tvShowData.poster}").into(poster)
        }
    }

    // co the view cua firebase
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.textView.setText(pmlist.get(position).Ntitle)
//        Glide.with(holder.itemView.context).load(pmlist.get(position).Nthumb).into(holder.imageView)
        val movie = pmlist[position]
        holder.bind(movie)

    }

    override fun getItemCount(): Int = pmlist.size
}