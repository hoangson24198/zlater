package com.krahs.adminzlater.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.krahs.adminzlater.Utils.Constants;
import com.krahs.adminzlater.Fragments.QuotesFragment;
import com.krahs.adminzlater.Model.Quotes;
import com.krahs.adminzlater.R;
import com.krahs.adminzlater.Services.AdminZlaterServices;
import com.krahs.adminzlater.Services.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * Created by Hoang Son on 26,October,2019
 **/
public class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.ViewHolder> {

    List<Quotes> listQuotes;
    Context context;
    private AdminZlaterServices adminZlaterServices;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl(Constants.STORAGE_IMAGE);
    QuotesFragment quotesFragment;
    TextView dialogTitle;
    ImageView quotesImage;
    EditText quotesTitle;
    TextView tvButtonActionDialogQuotes;
    CardView saveQuotes;
    Dialog dialogUpload,dialog;

    public QuotesAdapter(List<Quotes> listQuotes, Context context, QuotesFragment quotesFragment) {
        this.listQuotes = listQuotes;
        this.context = context;
        this.quotesFragment = quotesFragment;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.one_item_quotes, parent, false);
        Retrofit retrofit = RetrofitClient.getInstance();
        adminZlaterServices = retrofit.create(AdminZlaterServices.class);
        dialogUpload = new Dialog(context);
        dialogUpload.setContentView(R.layout.dialog_upload);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Picasso.get().load(listQuotes.get(position).getImageUrl()).placeholder(R.drawable.loading).into(holder.imageQuotes);
        holder.titleQuotes.setText(listQuotes.get(position).getTitle());
        holder.imageQuotes.setClipToOutline(true);
        holder.layoutOptionQuotes.setVisibility(View.GONE);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int posittion) {
                if (holder.layoutOptionQuotes.getVisibility() == View.GONE) {
                    holder.layoutOptionQuotes.setVisibility(View.VISIBLE);
                } else {
                    holder.layoutOptionQuotes.setVisibility(View.GONE);
                }
            }
        });
        holder.itemEditQuotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_add_quotes);
                dialog.show();
                dialogTitle = dialog.findViewById(R.id.dialogQuotesTitle);
                dialogTitle.setText("Update quotes");
                quotesTitle = dialog.findViewById(R.id.edtTitleAddQuotes);
                quotesImage = dialog.findViewById(R.id.imvAddQuotes);
                Picasso.get().load(listQuotes.get(position).getImageUrl()).into(quotesImage);
                quotesTitle.setText(listQuotes.get(position).getTitle());
                tvButtonActionDialogQuotes = dialog.findViewById(R.id.tvButtonActionDialogQuotes);
                tvButtonActionDialogQuotes.setText("Update");
                saveQuotes = dialog.findViewById(R.id.saveNewQuotes);
                saveQuotes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("HS::", "Will update");
                        Quotes quotes=new Quotes();
                        quotes.setId(listQuotes.get(position).getId());
                        quotes.setTitle(quotesTitle.getText().toString());
                        quotes.setImageUrl(listQuotes.get(position).getImageUrl());
                        handleUpdateQuotes(quotes);
                    }
                });


            }
        });
        holder.itemDeleteQuotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUpload.show();
                handleDeleteQuotes(listQuotes.get(position).getId(), listQuotes.get(position).getImageUrl());
            }
        });
    }


    private void handleUpdateQuotes(Quotes quotes) {
        Call<Quotes> call = adminZlaterServices.updateQuotes(quotes.getId(), quotes.getTitle(), quotes.getImageUrl());
        call.enqueue(new Callback<Quotes>() {
            @Override
            public void onResponse(Call<Quotes> call, Response<Quotes> response) {
                if (response.isSuccessful()) {
                    Log.e("HS::", "Successfully");
                    Log.e("Body", response.code() + "");
                    dialogUpload.dismiss();
                    dialog.dismiss();
                    ((QuotesFragment) quotesFragment).getAllQuotes();


                }
                if (!response.isSuccessful()) {
                    Log.e("HS::ERROR", response.code() + "");
                }
                dialog.dismiss();
                dialogUpload.dismiss();
            }

            @Override
            public void onFailure(Call<Quotes> call, Throwable t) {
                Log.e("ERR", t.getMessage());
                dialog.dismiss();
                dialogUpload.dismiss();
            }
        });


    }

    @Override
    public int getItemCount() {
        return listQuotes.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        GifImageView imageQuotes, itemDeleteQuotes, itemEditQuotes;
        TextView titleQuotes;
        LinearLayout layoutOptionQuotes;

        private ItemClickListener itemClickListener;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageQuotes = itemView.findViewById(R.id.imageQuotes);
            itemDeleteQuotes = itemView.findViewById(R.id.itemDeleteQuotes);
            itemEditQuotes = itemView.findViewById(R.id.itemEditQuotes);
            layoutOptionQuotes = itemView.findViewById(R.id.layoutOptionQuotes);
            titleQuotes = itemView.findViewById(R.id.titleQuotes);

        }

        void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition());

        }
    }

    private void handleDeleteQuotes(final int id, final String imageLink) {
        adminZlaterServices.deleteQuotes(id).enqueue(new Callback<Quotes>() {
            @Override
            public void onResponse(Call<Quotes> call, Response<Quotes> response) {
                if (response.isSuccessful()) {
                    Log.e("HS::", String.valueOf(response.body()));
                    try {
                        handleDeleteImage(imageLink);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ((QuotesFragment) quotesFragment).getAllQuotes();
                        dialogUpload.dismiss();

                    }
                }
                if (!response.isSuccessful()) {
                    Log.e("phayTranERROR", "delete error");
                    Log.e("HS::", response.code() + "");
                    Toast.makeText(context, "Delete failed!!!", Toast.LENGTH_SHORT).show();
                    dialogUpload.dismiss();
                }

            }

            @Override
            public void onFailure(Call<Quotes> call, Throwable t) {
                Log.e("HS::", "Failed");
                Toast.makeText(context, "Delete failed!!!", Toast.LENGTH_SHORT).show();
                dialogUpload.dismiss();
            }
        });
    }

    private void handleDeleteImage(String urlImage) {
        StorageReference storageRef = storage.getReferenceFromUrl(urlImage);
        storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("HS::", "onSuccess: deleted file");
                ((QuotesFragment) quotesFragment).getAllQuotes();
                dialogUpload.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("HS::", "onFailure: did not delete file");
                dialogUpload.dismiss();
            }
        });


    }


}