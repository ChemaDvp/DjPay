package com.vedruna.djpay.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vedruna.djpay.R;
import com.vedruna.djpay.model.User;

import java.util.List;

public class UserAdapter extends BaseAdapter {
    private List<User> userList;
    private Context context;
    private User selectedUser;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_list, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.djNombre = convertView.findViewById(R.id.djNameText);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        User user = (User) getItem(position);

        viewHolder.djNombre.setText(user.getUsername());

        // Cambiar el color y el tamaño del texto según si el usuario está seleccionado
        if (user.equals(selectedUser)) {
            viewHolder.djNombre.setTextColor(Color.WHITE); // Color blanco
            viewHolder.djNombre.setTextSize(20); // Tamaño más grande
        } else {
            viewHolder.djNombre.setTextColor(Color.BLACK); // Color por defecto
            viewHolder.djNombre.setTextSize(14); // Tamaño por defecto
        }

        return convertView;
    }

    static class ViewHolder {
        TextView djNombre;
    }

    public void setSelectedUser(User user) {
        selectedUser = user;
        notifyDataSetChanged();
    }

    public User getSelectedUser() {
        return selectedUser;
    }
}
