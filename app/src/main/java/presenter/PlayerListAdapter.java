package presenter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;  
import android.widget.TextView;
import hu.bme.aut.android.chainreaction.R;

import java.util.ArrayList;

/**
 * Adapter of the PlayerList RecyclerView
 */
public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.ViewHolder>{

    /**
     * the actual list of PlayerListData objects
     */
    private ArrayList<PlayerListData> list_data;

    /**
     * the context of the adapter
     */
    private Context context;

    /**
     * last position
     */
    private int lastPosition = -1;

    /**
     * PlayerListAdapter constructor
     *
     * @param   context 	    The context of the adapter
     * @param   list_data 	    The list to work on
     */
    public PlayerListAdapter(Context context, ArrayList<PlayerListData> list_data) {
        this.context = context;
        this.list_data = list_data;
    }

    @Override  
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {  
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());  
        View listItem= layoutInflater.inflate(R.layout.playerlist_item, parent, false);
        return new ViewHolder(listItem);
    }  
  
    @Override  
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameView.setText(list_data.get(position).getName());
        holder.typeView.setText(list_data.get(position).getType());
        holder.imageView.setImageResource(list_data.get(position).getImgId());
        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
  
    @Override  
    public int getItemCount() {
        return list_data.size();
    }

    /**
     * Nested class to hold PlayerList CardView's view
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * properties
         */
        private ImageView imageView;
        private TextView nameView;
        private TextView typeView;
        private CardView playerHolder;

        /**
         * constructor of ViewHolder
         */
        public ViewHolder(View itemView) {
            super(itemView);  
            this.imageView = itemView.findViewById(R.id.imageViewPlayer);
            this.nameView = itemView.findViewById(R.id.nameView);
            this.typeView = itemView.findViewById(R.id.typeView);
            this.playerHolder = itemView.findViewById(R.id.cvPlayerHolder);
        }

    }

    /**
     * Adds an item to the list
     *
     * @param   player 	    PlayerListData to add
     */
    public void addItem(PlayerListData player) {
        int size = list_data.size();
        list_data.add(player);
        notifyItemInserted(size);
    }

    /**
     * Clears the whole list
     * Sets lastPosition to -1 to enable animation to later added elements
     */
    public void Clear() {
        list_data.clear();
        notifyDataSetChanged();
        lastPosition = -1;
    }

    /**
     * Returns the String representation of the indexed list element
     *
     * @param   idx     Index of the list element
     * @return 	String	"<Player type>-<Player name>" format
     */
    public String StringElementAt(int idx) {
        PlayerListData data = list_data.get(idx);
        return data.getType() + "-" + data.getName();
    }

}  