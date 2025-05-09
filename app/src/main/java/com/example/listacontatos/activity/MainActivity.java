package com.example.listacontatos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listacontatos.R;
import com.example.listacontatos.adapter.ContatoAdapter;
import com.example.listacontatos.api.RetrofitConfig;
import com.example.listacontatos.model.Contato;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// DUPLA:
// Levi Almeida     - CV3064409
// Mariana Novaes   - CV3063224


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerContatos;
    private ContatoAdapter adapter;
    private List<Contato> listaContatos;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private FloatingActionButton fabAddContato;
    private boolean mostrarSomenteOsFavoritos = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Lista de Contatos");

        recyclerContatos = findViewById(R.id.recycler_contatos);
        progressBar = findViewById(R.id.progress_bar);
        tvEmpty = findViewById(R.id.tv_empty);
        fabAddContato = findViewById(R.id.fab_add_contato);

        listaContatos = new ArrayList<>();
        adapter = new ContatoAdapter(this, listaContatos);
        recyclerContatos.setLayoutManager(new LinearLayoutManager(this));
        recyclerContatos.setAdapter(adapter);

        fabAddContato.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FormContatoActivity.class);
            startActivity(intent);
        });

        adapter.setOnContatoListener(new ContatoAdapter.OnContatoListener() {
            @Override
            public void onDeleteClick(int position) {
                confirmarExclusao(position);
            }

            @Override
            public void onFavoriteClick(int position) {
                atualizarFavorito(position);
            }

            @Override
            public void onContatoClick(int position) {
                abrirDetalheContato(position);
            }
        });
    }

    private void verificarListaVazia() {
        if (listaContatos == null || listaContatos.isEmpty()) {
            recyclerContatos.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerContatos.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }
    }

    private void exibirLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerContatos.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);
    }

    private void ocultarLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void confirmarExclusao(int position) {
        Contato contato = adapter.getItem(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Excluir Contato");
        builder.setMessage("Deseja realmente excluir o contato " + contato.getNome() + "?");
        builder.setPositiveButton("Sim", (dialog, which) -> {
            excluirContato(contato.getId(), position);
        });
        builder.setNegativeButton("Não", null);
        builder.show();
    }

    private void excluirContato(String id, int position) {
        progressBar.setVisibility(View.VISIBLE);

        RetrofitConfig.getContatoService().excluirContato(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    adapter.removerItem(position);
                    Toast.makeText(MainActivity.this, "Contato excluído com sucesso", Toast.LENGTH_SHORT).show();
                    verificarListaVazia();
                } else {
                    Toast.makeText(MainActivity.this, "Erro ao excluir contato: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Falha na comunicação: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void atualizarFavorito(int position) {
        Contato contato = adapter.getItem(position);
        contato.setFavorito(!contato.isFavorito());

        progressBar.setVisibility(View.VISIBLE);

        RetrofitConfig.getContatoService().atualizarContato(contato.getId(), contato).enqueue(new Callback<Contato>() {
            @Override
            public void onResponse(Call<Contato> call, Response<Contato> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    adapter.notifyItemChanged(position);
                    Toast.makeText(MainActivity.this,
                            contato.isFavorito() ? "Adicionado aos favoritos" : "Removido dos favoritos",
                            Toast.LENGTH_SHORT).show();

                    if (mostrarSomenteOsFavoritos && !contato.isFavorito()) {
                        adapter.removerItem(position);
                        verificarListaVazia();
                    }
                } else {
                    contato.setFavorito(!contato.isFavorito());
                    Toast.makeText(MainActivity.this, "Erro ao atualizar favorito: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Contato> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                contato.setFavorito(!contato.isFavorito());
                Toast.makeText(MainActivity.this, "Falha na comunicação: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void abrirDetalheContato(int position) {
        Contato contato = adapter.getItem(position);
        Intent intent = new Intent(MainActivity.this, FormContatoActivity.class);
        intent.putExtra("CONTATO", contato);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarContatos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_todos) {
            mostrarSomenteOsFavoritos = false;
            carregarContatos();
            return true;
        } else if (id == R.id.menu_favoritos) {
            mostrarSomenteOsFavoritos = true;
            carregarContatos();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void carregarContatos() {
        exibirLoading();

        Call<List<Contato>> call;
        if (mostrarSomenteOsFavoritos) {
            call = RetrofitConfig.getContatoService().listarContatosFavoritos(true);
        } else {
            call = RetrofitConfig.getContatoService().listarContatos();
        }

        call.enqueue(new Callback<List<Contato>>() {
            @Override
            public void onResponse(Call<List<Contato>> call, Response<List<Contato>> response) {
                if (response.isSuccessful()) {
                    listaContatos = response.body();
                    adapter.atualizarLista(listaContatos);

                    verificarListaVazia();
                } else {
                    Toast.makeText(MainActivity.this, "Erro ao carregar contatos: " + response.code(), Toast.LENGTH_SHORT).show();
                }
                ocultarLoading();
            }

            @Override
            public void onFailure(Call<List<Contato>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Falha na comunicação: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                ocultarLoading();
            }
        });
    }
}