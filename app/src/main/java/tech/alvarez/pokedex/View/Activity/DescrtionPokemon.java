package tech.alvarez.pokedex.View.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tech.alvarez.pokedex.Interface.PokemonService;
import tech.alvarez.pokedex.Model.PokemonDescription;
import tech.alvarez.pokedex.Model.Type;
import tech.alvarez.pokedex.Model.TypeX;
import tech.alvarez.pokedex.R;

public class DescrtionPokemon extends AppCompatActivity {
    private static final String TAG = "POKEMON";
    private Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descrtion_pokemon);
        Intent intent = getIntent();
        String pokemon = intent.getStringExtra("pokemon_name");
        TextView pokemonName = findViewById(R.id.pokemonName);
        pokemonName.setText(pokemon);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        obtenerDatos(pokemon);

    }
    private void obtenerDatos(String pokemon) {
        PokemonService service = retrofit.create(PokemonService.class);
        Call<PokemonDescription> pokemonRespuestaCall = service.obtenerDescriptionPokemon(pokemon);

        pokemonRespuestaCall.enqueue(new Callback<PokemonDescription>() {
            @Override
            public void onResponse(Call<PokemonDescription> call, Response<PokemonDescription> response) {
                if (response.isSuccessful()) {
                    PokemonDescription pokemonRespuesta = response.body();
                    Double pesoWeight = Double.valueOf(pokemonRespuesta.getWeight());
                    Double tamañoHeight = Double.valueOf(pokemonRespuesta.getHeight());
                    String imageId = String.valueOf(pokemonRespuesta.getId());
                    List<Type> type = pokemonRespuesta.getTypes();
                    Type typeInSlot1 = type.get(0);
                    String type_pokemon = typeInSlot1.getType().getName();
                    ImageView fotoImageView = findViewById(R.id.imagePokemon);
                    TextView typePokemon = findViewById(R.id.tipo_pokemon);
                    TextView peso = findViewById(R.id.peso_pokemon);
                    TextView tamaño =findViewById(R.id.tamaño_pokemon);
                    peso.setText(pesoWeight+"kg");
                    tamaño.setText(tamañoHeight+"m");
                    typePokemon.setText(type_pokemon);

                    Glide.with(DescrtionPokemon.this)
                            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + imageId + ".png")
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(fotoImageView);


                } else {
                    Log.e(TAG, " onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokemonDescription> call, Throwable t) {
             //   aptoParaCargar = true;
                Log.e(TAG, " onFailure: " + t.getMessage());
            }
        });
    }
}