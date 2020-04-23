package com.sindhuja.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sindhuja.Model.City;
import com.sindhuja.R;
import com.sindhuja.listener.OnClickListener;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class AdapterCityListingLandScape extends RecyclerView.Adapter<AdapterCityListingLandScape.MyViewHolder> implements Filterable {

    private List<City> cityList;
    private List<City> mlist;
    private Context context;
    private OnClickListener onItemClickListener;

        //Search Filter
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    cityList = mlist;
                } else {
                    ArrayList<City> filteredList = new ArrayList<>();
                    for (City city : mlist) {
                        if (city.getName().concat(city.getCountry()).toLowerCase().startsWith(charString.toLowerCase()) ||  city.getName().concat(city.getCountry()).toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(city);
                        }
                    }
                    cityList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = cityList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                cityList = (ArrayList<City>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView countryName, coordinates;
        LinearLayout linMain;

        MyViewHolder(View view) {
            super(view);
            countryName = (TextView) view.findViewById(R.id.tvCountryWithName);
            coordinates = (TextView) view.findViewById(R.id.tvCoordinates);
            linMain = (LinearLayout) view.findViewById(R.id.linMain);
        }
    }


    public AdapterCityListingLandScape(List<City> cityList, Context context) {
        this.cityList = cityList;
        mlist = cityList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_city, parent, false);

        return new MyViewHolder(itemView);
    }

        //Setting details along with color
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final City city = cityList.get(position);
        holder.countryName.setText(String.format("%s, %s", city.getName(), city.getCountry()));
        holder.coordinates.setText(String.format("%s, %s", city.getCoord().getLat(), city.getCoord().getLon()));
        if (city.isSelected())
            holder.linMain.setBackgroundColor(context.getResources().getColor(R.color.colorBlue));
        else
            holder.linMain.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));

        holder.linMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectedPos(position);
                onItemClickListener.onItemClicked(position, city);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    private void showSelectedPos(int position) {
        for (int i = 0; i < cityList.size(); i++) {
            if (position == i)
                cityList.get(i).setSelected(true);
            else
                cityList.get(i).setSelected(false);
        }
        notifyDataSetChanged();
    }

    /**
     * @param onItemClickListener item click listener
     */
    public void setOnItemClickListener(OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}