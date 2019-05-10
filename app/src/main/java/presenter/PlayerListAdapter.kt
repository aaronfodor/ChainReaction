package presenter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import hu.bme.aut.android.chainreaction.R

import java.util.ArrayList

/**
 * Adapter of the PlayerList RecyclerView
 */
class PlayerListAdapter
/**
 * PlayerListAdapter constructor
 *
 * @param   context        The context of the adapter
 * @param   list_data        The list to work on
 */
    (
    /**
     * the context of the adapter
     */
    private val context: Context,
    /**
     * the actual list of PlayerListData objects
     */
    private val list_data: ArrayList<PlayerListData>
) : RecyclerView.Adapter<PlayerListAdapter.ViewHolder>() {

    /**
     * last position
     */
    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.playerlist_item, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameView.text = list_data[position].name
        holder.typeView.text = list_data[position].type
        holder.imageView.setImageResource(list_data[position].imgId)
        setAnimation(holder.itemView, position)
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    override fun getItemCount(): Int {
        return list_data.size
    }

    /**
     * Nested class to hold PlayerList CardView's view
     */
    class ViewHolder
    /**
     * constructor of ViewHolder
     */
        (itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * properties
         */
        val imageView: ImageView
        val nameView: TextView
        val typeView: TextView
        private val playerHolder: CardView

        init {
            this.imageView = itemView.findViewById(R.id.imageViewPlayer)
            this.nameView = itemView.findViewById(R.id.nameView)
            this.typeView = itemView.findViewById(R.id.typeView)
            this.playerHolder = itemView.findViewById(R.id.cvPlayerHolder)
        }

    }

    /**
     * Adds an item to the list
     *
     * @param   player        PlayerListData to add
     */
    fun addItem(player: PlayerListData) {
        val size = list_data.size
        list_data.add(player)
        notifyItemInserted(size)
    }

    /**
     * Clears the whole list
     * Sets lastPosition to -1 to enable animation to later added elements
     */
    fun clear() {
        list_data.clear()
        notifyDataSetChanged()
        lastPosition = -1
    }

    /**
     * Returns the String representation of the indexed list element
     *
     * @param   idx     Index of the list element
     * @return    String	"<Player type>-<Player name>" format
     */
    fun stringElementAt(idx: Int): String {
        val data = list_data[idx]
        return data.type + "-" + data.name
    }

}  