package presenter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  
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
     * PlayerListAdapter constructor
     *
     * @param   list_data 	    The list to work on
     */
    public PlayerListAdapter(ArrayList<PlayerListData> list_data) {

        this.list_data = list_data;

    }

    @Override  
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {  
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());  
        View listItem= layoutInflater.inflate(R.layout.playerlist_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);  
        return viewHolder;  
    }  
  
    @Override  
    public void onBindViewHolder(ViewHolder holder, int position) {  
        final PlayerListData PlayerListData = list_data.get(position);
        holder.nameView.setText(list_data.get(position).getName());
        holder.typeView.setText(list_data.get(position).getType());
        holder.imageView.setImageResource(list_data.get(position).getImgId());
    }
  
    @Override  
    public int getItemCount() {  

        return list_data.size();

    }

    /**
     * Nested class to hold PlayerList RecyclerView's view
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * properties
         */
        public ImageView imageView;  
        public TextView nameView;
        public TextView typeView;
        public RelativeLayout relativeLayout;

        /**
         * constructor of ViewHolder
         */
        public ViewHolder(View itemView) {

            super(itemView);  
            this.imageView = itemView.findViewById(R.id.imageViewPlayer);
            this.nameView = itemView.findViewById(R.id.nameView);
            this.typeView = itemView.findViewById(R.id.typeView);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);

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
     */
    public void Clear() {

        list_data.clear();
        notifyDataSetChanged();

    }

    /**
     * Returns the String representation of the indexed list element
     *
     * @param   idx     Index of the list element
     * @return 	String	"<Player type>-<Player name>" format
     */
    public String StringElementAt(int idx) {

        PlayerListData data = list_data.get(idx);
        String value = data.getType() + "-" + data.getName();

        return value;

    }

}  