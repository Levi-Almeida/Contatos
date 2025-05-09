package com.example.listacontatos.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.listacontatos.R;
import com.example.listacontatos.api.RetrofitConfig;
import com.example.listacontatos.model.Contato;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormContatoActivity extends AppCompatActivity {

    private TextInputEditText editNome, editTelefone;
    private CheckBox checkFavorito;
    private Button btnSalvar;
    private ProgressBar progressBar;

    private Contato contato;
    private boolean modoEdicao = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_contato);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        editNome = findViewById(R.id.edit_nome);
        editTelefone = findViewById(R.id.edit_telefone);
        checkFavorito = findViewById(R.id.check_favorito);
        btnSalvar = findViewById(R.id.btn_salvar);
        progressBar = findViewById(R.id.progress_bar);

        if (getIntent().hasExtra("CONTATO")) {
            contato = (Contato) getIntent().getSerializableExtra("CONTATO");
            modoEdicao = true;

            getSupportActionBar().setTitle("Editar Contato");
            carregarDadosContato();
        } else {
            getSupportActionBar().setTitle("Novo Contato");
        }

        aplicarMascaraTelefone();
        btnSalvar.setOnClickListener(v -> {
            if (validarCampos()) {
                Contato contatoNv = new Contato();
                contatoNv.setNome(editNome.getText().toString().trim());
                contatoNv.setTelefone(editTelefone.getText().toString().trim());
                contatoNv.setFavorito(checkFavorito.isChecked());

                if (modoEdicao) {
                    contatoNv.setId(contato.getId());
                    atualizarContato(contatoNv);
                } else {
                    salvarNovoContato(contato);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void carregarDadosContato() {

        try {
            preencherFormulario(contato);

        }catch (Exception e){
            Toast.makeText(FormContatoActivity.this, "Erro ao carregar contato", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void preencherFormulario(Contato contato) {
        editNome.setText(contato.getNome());
        editTelefone.setText(contato.getTelefone());
        checkFavorito.setChecked(contato.isFavorito());
    }

    private boolean validarCampos() {
        boolean valido = true;

        String nome = editNome.getText().toString().trim();
        String telefone = editTelefone.getText().toString().trim();

        if (TextUtils.isEmpty(nome)) {
            editNome.setError("Campo obrigatório");
            valido = false;
        }

        if (TextUtils.isEmpty(telefone)) {
            editTelefone.setError("Campo obrigatório");
            valido = false;
        }

        return valido;
    }

    private void salvarNovoContato(Contato contato) {
        progressBar.setVisibility(View.VISIBLE);
        btnSalvar.setEnabled(false);

        RetrofitConfig.getContatoService().criarContato(contato).enqueue(new Callback<Contato>() {
            @Override
            public void onResponse(Call<Contato> call, Response<Contato> response) {
                progressBar.setVisibility(View.GONE);
                btnSalvar.setEnabled(true);

                if (response.isSuccessful()) {
                    Toast.makeText(FormContatoActivity.this, "Contato salvo com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(FormContatoActivity.this, "Erro ao salvar contato: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Contato> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnSalvar.setEnabled(true);
                Toast.makeText(FormContatoActivity.this, "Falha na comunicação: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void aplicarMascaraTelefone() {
        editTelefone.addTextChangedListener(new TextWatcher() {
            boolean isUpdating;
            String oldText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString().replaceAll("[^\\d]", "");

                String formatted = "";
                if (isUpdating) {
                    oldText = str;
                    isUpdating = false;
                    return;
                }

                int length = str.length();

                if (length > 11) {
                    str = str.substring(0, 11);
                    length = 11;
                }

                int i = 0;
                if (length >= 1) formatted += "(";
                if (length >= 2) formatted += str.substring(0, 2) + ") ";
                else if (length >= 1) formatted += str.substring(0, 1);
                if (length >= 7) formatted += str.substring(2, 7) + "-" + str.substring(7);
                else if (length > 2) formatted += str.substring(2);

                isUpdating = true;
                editTelefone.setText(formatted);
                editTelefone.setSelection(formatted.length());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }


    private void atualizarContato(Contato contato) {
        progressBar.setVisibility(View.VISIBLE);
        btnSalvar.setEnabled(false);

        RetrofitConfig.getContatoService().atualizarContato(contato.getId(), contato).enqueue(new Callback<Contato>() {
            @Override
            public void onResponse(Call<Contato> call, Response<Contato> response) {
                progressBar.setVisibility(View.GONE);
                btnSalvar.setEnabled(true);

                if (response.isSuccessful()) {
                    Toast.makeText(FormContatoActivity.this, "Contato atualizado com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(FormContatoActivity.this, "Erro ao atualizar contato: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Contato> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnSalvar.setEnabled(true);
                Toast.makeText(FormContatoActivity.this, "Falha na comunicação: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}