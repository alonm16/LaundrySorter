package com.example.laundrysorter;

import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class HistoryAdapter extends FirestoreRecyclerAdapter<History, HistoryAdapter.HistoryHolder> {


    public HistoryAdapter(@NonNull FirestoreRecyclerOptions<History> options) {
        super(options);
    }

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item,parent,false);
        return new HistoryHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull HistoryHolder historyHolder, int i, @NonNull final History history) {
        HomeActivity.ColorPick color1 = HomeActivity.ColorPick.fromInteger(history.getBasket1());
        HomeActivity.ColorPick color2 = HomeActivity.ColorPick.fromInteger(history.getBasket2());
        HomeActivity.ColorPick color3 = HomeActivity.ColorPick.fromInteger(history.getBasket3());

        Resources res = historyHolder.mView.getContext().getResources();

        int color1_res = convertColorToResourceColor(color1,res);
        int color2_res = convertColorToResourceColor(color2,res);
        int color3_res = convertColorToResourceColor(color3,res);

        historyHolder.basket1view.setBackgroundColor(color1_res);
        historyHolder.basket2view.setBackgroundColor(color2_res);
        historyHolder.basket3view.setBackgroundColor(color3_res);

        historyHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HomeActivity.class);
                intent.putExtra("basket1", history.getBasket1());
                intent.putExtra("basket2", history.getBasket2());
                intent.putExtra("basket3", history.getBasket3());
                v.getContext().startActivity(intent);

            }
        });

    }


    class HistoryHolder extends RecyclerView.ViewHolder{

        View mView, basket1view, basket2view,basket3view;

        public HistoryHolder (View itemView) {
            super(itemView);
            mView = itemView;
            basket1view = itemView.findViewById(R.id.tvBasketColor1);
            basket2view = itemView.findViewById(R.id.tvBasketColor2);
            basket3view = itemView.findViewById(R.id.tvBasketColor3);
     
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private int convertColorToResourceColor (HomeActivity.ColorPick color, Resources res){
        switch (color){
            case BLACK:
                return res.getColor(R.color.pickerBlack);
            case WHITE:
                return res.getColor(R.color.pickerWhite);
            case GRAY:
                return res.getColor(R.color.pickerLightGray);
            case BLUE:
                return res.getColor(R.color.pickerBlue);
            case ORANGE:
                return res.getColor(R.color.pickerOrange);
            case GREEN:
                return res.getColor(R.color.pickerGreen);
            case YELLOW:
                return res.getColor(R.color.pickerYellow);
            case LIGHTBLUE:
                return res.getColor(R.color.pickerLightBlue);
            case BROWN:
                return res.getColor(R.color.pickerBrown);
            case RED:
                return res.getColor(R.color.pickerRed);
            case PINK:
                return res.getColor(R.color.pickerLightPink);
            case PURPLE:
                return res.getColor(R.color.pickerPurple);
        }
        return res.getColor(R.color.pickerWhite);
    }

}
