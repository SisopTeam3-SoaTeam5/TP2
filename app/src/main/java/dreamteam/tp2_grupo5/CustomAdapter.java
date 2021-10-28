package dreamteam.tp2_grupo5;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
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
    ViewHolder holder;

    public CustomAdapter(HashMap<Integer, RankingItem> stats, Context context) {
        this.stats = stats;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        holder = new ViewHolder(view);
        return holder;
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

    public void changeTextColor(int color) {
        if (holder != null)
            holder.changeTextColor(color);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView element;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            element = itemView.findViewById(R.id.textView);
        }

        public void changeTextColor(int color) {
            String hexColor = Integer.toHexString((int) color);
            if (hexColor.length() == 1)
                hexColor += hexColor;         //transforma, por ejemplo, F en FF. parseColor requiere 6 caracteres;
            hexColor = "#" + hexColor + hexColor + hexColor;
            element.setTextColor(Color.parseColor(hexColor));
        }

    }
}
