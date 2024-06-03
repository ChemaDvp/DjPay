package com.vedruna.djpay.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vedruna.djpay.R;
import com.vedruna.djpay.model.Peticion;

import java.util.List;

public class PeticionAdapter extends BaseAdapter {

    private List<Peticion> peticionList;
    private Context context;
    private Peticion selectedPeticion;

    public PeticionAdapter(Context context, List<Peticion> peticionList) {
        this.context = context;
        this.peticionList = peticionList;
    }

    @Override
    public int getCount() {
        return peticionList.size();
    }

    @Override
    public Object getItem(int position) {
        return peticionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.peticion_list, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.peticionText = convertView.findViewById(R.id.peticionText);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Peticion peticion = (Peticion) getItem(position);

        viewHolder.peticionText.setText(peticion.getString());

        return convertView;
    }

    static class ViewHolder {
        TextView peticionText;
    }

    public void setSelectedPeticion(Peticion peticion) {
        selectedPeticion = peticion;
        notifyDataSetChanged();
    }

    public Peticion getSelectedPeticion() {
        return selectedPeticion;
    }
}
