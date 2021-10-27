package dreamteam.tp2_grupo5;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Objects;

import dreamteam.tp2_grupo5.models.RankingItem;

public class CustomAdapter extends RecyclerView.Adapter {

    HashMap<Integer, RankingItem> stats;
    Context context;

    public CustomAdapter(HashMap<Integer, RankingItem> stats, Context context) {
        this.stats = stats;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder myViewHolder = (ViewHolder) holder;
        myViewHolder.element.setText(Objects.requireNonNull(stats.get(position)).elementString(position));
    }

    @Override
    public int getItemCount() {
        return stats.size();
    }


    public class ViewHolder  extends  RecyclerView.ViewHolder{

        TextView element;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            element = itemView.findViewById(R.id.textView);
        }

    }
}
