package Models;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.task.digitalcurrency.R;
import java.util.ArrayList;

import Util.Utils;

public class Coinadaptor extends RecyclerView.Adapter<Coinadaptor.Viewholder> {

    private ArrayList<Coinsignature> arrayList;
    private Context context;

    public Coinadaptor(ArrayList<Coinsignature> arrayList, Context context) {

        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_displayview,parent,false);
        Viewholder viewholder = new Viewholder(v);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(Viewholder holder, final int position) {

        holder.rank.setText(arrayList.get(position).getRank());
        holder.change.setText(arrayList.get(position).getPercent_change_1h());
        holder.exchange.setText(arrayList.get(position).getPrice_usd());
        holder.name.setText(arrayList.get(position).getName()+"("+arrayList.get(position).getSymbol()+")");
        holder.firstletter.setText(""+arrayList.get(position).getName().charAt(0));
        holder.cardView.setCardBackgroundColor(Utils.getRandomColor());
        holder.sharerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.sharedata(arrayList.get(position).getName(),arrayList.get(position).getPrice_usd(),
                        arrayList.get(position).getPercent_change_1h(),arrayList.get(position).getRank(),
                        context);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
        }


    public static class Viewholder extends RecyclerView.ViewHolder{

        public TextView firstletter,name,exchange,change,rank;
        public CardView cardView;
        public RelativeLayout sharerate;

        public Viewholder(View itemView) {
            super(itemView);
            firstletter = itemView.findViewById(R.id.firstletter);
            name = itemView.findViewById(R.id.name);
            exchange = itemView.findViewById(R.id.exchange);
            change = itemView.findViewById(R.id.change);
            rank = itemView.findViewById(R.id.rank);
            cardView = itemView.findViewById(R.id.cardview);
            sharerate = itemView.findViewById(R.id.sharerate);
        }
    }

}
