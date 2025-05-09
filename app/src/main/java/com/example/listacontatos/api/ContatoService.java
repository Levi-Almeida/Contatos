package com.example.listacontatos.api;

import com.example.listacontatos.model.Contato;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ContatoService {
    @GET("contacts")
    Call<List<Contato>> listarContatos();

    @GET("contacts")
    Call<List<Contato>> listarContatosFavoritos(@Query("favorite") boolean favorite);

    @GET("contacts/{id}")
    Call<Contato> buscarContato(@Path("id") Integer id);

    @POST("contacts")
    Call<Contato> criarContato(@Body Contato contato);

    @PUT("contacts/{id}")
    Call<Contato> atualizarContato(@Path("id") String id, @Body Contato contato);

    @DELETE("contacts/{id}")
    Call<Void> excluirContato(@Path("id") String id);
}