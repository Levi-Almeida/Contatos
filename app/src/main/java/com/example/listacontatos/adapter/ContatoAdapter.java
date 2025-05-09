package com.example.listacontatos.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listacontatos.R;
import com.example.listacontatos.activity.FormContatoActivity;
import com.example.listacontatos.api.RetrofitConfig;
import com.example.listacontatos.model.Contato;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.ContatoViewHolder> {

    private List<Contato> contatos;
    private Context context;
    private OnContatoListener onContatoListener;

    public ContatoAdapter(Context context, List<Contato> contatos) {
        this.context = context;
        this.contatos = contatos;
    }

    public interface OnContatoListener {
        void onDeleteClick(int position);
        void onFavoriteClick(int position);
        void onContatoClick(int position);
    }

    public void setOnContatoListener(OnContatoListener listener) {
        this.onContatoListener = listener;
    }

    @NonNull
    @Override
    public ContatoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contato, parent, false);
        return new ContatoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContatoViewHolder holder, int position) {
        Contato contato = contatos.get(position);

        holder.tvNome.setText(contato.getNome());
        holder.tvTelefone.setText(contato.getTelefone());
        holder.tvEmail.setText(contato.getEmail());

        // Configurar ícone de favorito
        if (contato.isFavorito()) {
            holder.btnFavorito.setImageResource(R.drawable.ic_star_filled);
        } else {
            holder.btnFavorito.setImageResource(R.drawable.ic_star_border);
        }

        // Tratar clique no botão de ligação
        holder.btnLigar.setOnClickListener(v -> {
            String tel = "tel:" + contato.getTelefone();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(tel));
            context.startActivity(intent);
        });

        // Tratar clique no botão de edição
        holder.btnEditar.setOnClickListener(v -> {

            System.out.println("LOG DE ID DA INTENT-----------");
            System.out.println("LOG DE ID DA INTENT-----------"+ contato.getId());
            Intent intent = new Intent(context, FormContatoActivity.class);
            intent.putExtra("CONTATO_ID", contato.getId());
            context.startActivity(intent);
        });

        // Tratar clique no botão de exclusão
        holder.btnExcluir.setOnClickListener(v -> {
            System.out.println("LOG DE ID DA INTENT-----------");
            if (onContatoListener != null) {
                onContatoListener.onDeleteClick(position);
            }
        });

        // Tratar clique no botão de favorito
        holder.btnFavorito.setOnClickListener(v -> {
            if (onContatoListener != null) {
                onContatoListener.onFavoriteClick(position);
            }
        });

        // Tratar clique no item inteiro
        holder.itemView.setOnClickListener(v -> {
            if (onContatoListener != null) {
                onContatoListener.onContatoClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contatos != null ? contatos.size() : 0;
    }

    public void atualizarLista(List<Contato> novaLista) {
        this.contatos = novaLista;
        notifyDataSetChanged();
    }

    public Contato getItem(int position) {
        return contatos.get(position);
    }

    public void removerItem(int position) {
        contatos.remove(position);
        notifyItemRemoved(position);
    }

    public static class ContatoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvTelefone, tvEmail;
        ImageButton btnLigar, btnEditar, btnExcluir, btnFavorito;

        public ContatoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tv_nome);
            tvTelefone = itemView.findViewById(R.id.tv_telefone);
            tvEmail = itemView.findViewById(R.id.tv_email);
            btnLigar = itemView.findViewById(R.id.btn_ligar);
            btnEditar = itemView.findViewById(R.id.btn_editar);
            btnExcluir = itemView.findViewById(R.id.btn_excluir);
            btnFavorito = itemView.findViewById(R.id.btn_favorito);
        }
    }
}