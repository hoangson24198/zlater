package com.krahs.adminzlater.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.krahs.adminzlater.Fragments.UsersFragment;
import com.krahs.adminzlater.Model.User;
import com.krahs.adminzlater.R;
import com.krahs.adminzlater.Services.AdminZlaterServices;
import com.krahs.adminzlater.Services.RetrofitClient;
import com.krahs.adminzlater.Utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    List<User> userList;
    Context context;
    Dialog dialog;
    UsersFragment usersFragment;
    int positionAdmin;
    private AdminZlaterServices adminZlaterServices;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl(Constants.STORAGE_IMAGE);
    public UserAdapter(List<User> userList, Context context, UsersFragment usersFragment) {
        this.userList = userList;
        this.context = context;
        this.usersFragment = usersFragment;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.one_item_user, parent, false);
        Retrofit retrofit = RetrofitClient.getInstance();
        adminZlaterServices = retrofit.create(AdminZlaterServices.class);
        Log.e("HSUser:::", "onCreateViewHolder: ");
        return new ViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (userList.get(position).getUserName().contentEquals("admin@zlater.com")) {
            holder.popupUser.setVisibility(View.INVISIBLE);
        }
        Picasso.get().load(userList.get(position).getAvt()).placeholder(R.drawable.ic_avatar).into(holder.avatar);
        holder.avatar.setClipToOutline(true);
        holder.displayname.setText(userList.get(position).getDisplayName());
        holder.useremail.setText(userList.get(position).getUserName());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int posittion) {

            }
        });
        holder.popupUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.popupUser);
                popupMenu.getMenuInflater().inflate(R.menu.user_option_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.delete:
                                Log.e("HS::", "Delete");
                                handleDeleteUser(userList.get(position).getId(), userList.get(position).getAvt());
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    private void handleDeleteUser(Integer userId,final String imageUrl) {
        Log.e("HSUser", "user id: "+ userId );
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_delete);
        CardView cancelDelete = dialog.findViewById(R.id.cancelDelete);
        TextView tvAccept = dialog.findViewById(R.id.tvDelete);
        tvAccept.setVisibility(View.GONE);
        GifImageView processDelete = dialog.findViewById(R.id.processDelete);
        processDelete.setVisibility(View.VISIBLE);
        cancelDelete.setVisibility(View.GONE);
        dialog.show();

        adminZlaterServices.deleteUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Log.e("HSDelete::", String.valueOf(response.body()));
                    try {
                        handleDeleteImage(imageUrl);
                        ((UsersFragment) usersFragment).handleGetAllUser();
                        notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                        ((UsersFragment) usersFragment).handleGetAllUser();
                        dialog.dismiss();
                    }

                }
                if (!response.isSuccessful()) {
                    Log.e("HS::", response.code() + "");
                    Toast.makeText(context, "Delete user failed!!!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("HS::", "Failed");
                Toast.makeText(context, "Delete failed!!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }

    private void handleDeleteImage(String urlImage) {
        StorageReference storageRef = storage.getReferenceFromUrl(urlImage);
        storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("HS::", "onSuccess: deleted file");
                ((UsersFragment) usersFragment).handleGetAllUser();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("HS::", "onFailure: did not delete file");
                dialog.dismiss();
            }
        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView avatar;
        private TextView displayname, useremail;
        private ItemClickListener itemClickListener;
        private ImageView popupUser;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            avatar = itemView.findViewById(R.id.avatar);
            displayname = itemView.findViewById(R.id.display_name);
            useremail = itemView.findViewById(R.id.email);
            popupUser = itemView.findViewById(R.id.popup_user);
        }

        void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition());

        }
    }
}
