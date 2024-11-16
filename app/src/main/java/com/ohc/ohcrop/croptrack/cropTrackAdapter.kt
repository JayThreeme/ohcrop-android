package com.ohc.ohcrop.croptrack
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ohc.ohcrop.R
import com.ohc.ohcrop.calculator.nutrientsolution.NutrientSolutionMixing
import com.ohc.ohcrop.croptrack.croptrackerview.cropTrackerView
import com.ohc.ohcrop.utils.FirebaseUtils

class cropTrackAdapter : RecyclerView.Adapter<cropTrackAdapter.ViewHolder>() {
    private lateinit var userID: String
    private val refIds: ArrayList<String> = ArrayList()
    private val titleList: ArrayList<String> = ArrayList()
    private val detailList: ArrayList<String> = ArrayList()
    private var isLoading = true // Add a variable to track loading state


    init {
        fetchDataFromFirestore()
    }

    private fun fetchDataFromFirestore() {
        userID = FirebaseUtils.firebaseAuth.currentUser!!.uid
        val db = FirebaseUtils.firestore
        val collectionRef = db.collection("user").document(userID).collection("croptrack")

        collectionRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val title = document.getString("nodeName") ?: ""
                    val detail = document.getString("typeofplant") ?: ""
                    val refids = document.getString("referenceID") ?: ""
                    titleList.add(title)
                    detailList.add(detail)
                    refIds.add(refids)
                }
                notifyDataSetChanged()
                hideProgressBar() // Data fetching is complete, hide the progress bar
            }
            .addOnFailureListener { exception ->
                // Handle errors
                isLoading = false // Data fetching failed
                hideProgressBar()
            }
    }

    fun showProgressBar() {
        isLoading = true
        notifyDataSetChanged()
    }

    fun hideProgressBar() {
        isLoading = false
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.croptracker_card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(titleList[position], detailList[position])

        // Check if it's loading and show the progress bar accordingly
        if (isLoading && position == titleList.size - 1) {
            holder.progressBar.visibility = View.VISIBLE
        } else {
            holder.progressBar.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return titleList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val titleTextView: TextView = itemView.findViewById(R.id.item_title)
        private val detailTextView: TextView = itemView.findViewById(R.id.item_detail)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position: Int = absoluteAdapterPosition
            val context = itemView.context
            //Toast.makeText(context, "You clicked ${titleList[position]} at position $position", Toast.LENGTH_SHORT).show()

            val intent = Intent(context, cropTrackerView::class.java)
            intent.putExtra("referenceID", refIds[position])
            context.startActivity(intent)
        }

        fun bind(title: String, detail: String) {
            titleTextView.text = title
            detailTextView.text = detail
        }
    }
}