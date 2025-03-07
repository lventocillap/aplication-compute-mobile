package com.example.storecomputer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.storecomputer.Api.GenerateSaleEmailPDF;
import com.example.storecomputer.Api.SaleProduct;
import com.example.storecomputer.Api.VerificationUser;
import com.example.storecomputer.EnumAPI.APIEnum;
import com.example.storecomputer.Model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentListWish extends Fragment {

    private RecyclerView recyclerView;
    private ListWishAdapter listWishAdapter;
    private List<Product> productList;
    private TextView amount;
    private static final String API_URL = APIEnum.DOMAIN.getUrl()+"/api/wishlist";
    private OkHttpClient client;
    private ExecutorService executorService;

    public FragmentListWish() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new OkHttpClient();
        executorService = Executors.newSingleThreadExecutor(); // Hilo para llamadas HTTP
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_list_wish, container, false);
        amount = view.findViewById(R.id.txtAmountProduct);

        recyclerView = view.findViewById(R.id.recyclerViewListWisht);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        productList = new ArrayList<>();
        listWishAdapter = new ListWishAdapter(productList, getContext());
        recyclerView.setAdapter(listWishAdapter);
        Context context = getContext();
        Button btnSale = view.findViewById(R.id.btnGenerateSale);
            btnSale.setOnClickListener(v -> {
                new Thread(() -> {
                    try {

                        GenerateSaleEmailPDF generateSaleEmailPDF = new GenerateSaleEmailPDF();
                        generateSaleEmailPDF.postGenerateSale(context);
                        Header headerGenerate = new Header();
                        updateContent(new AlertSale(), headerGenerate.setTitle("Compra exitosa"));
                    } catch (IOException e) {
                        Header headerLogin = new Header();
                        //updateContent(new Login(), headerLogin.setTitle("Login"));
                        e.printStackTrace();
                    }
                }).start();
            });

        loadProductAPI();
        return view;
    }

    private void loadProductAPI() {
        String url = API_URL;
        String token = VerificationUser.getAuthToken(getContext());

        if (token == null) {
            Log.e("API_ERROR", "Token de autenticación es nulo");
            return;
        }

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("API_ERROR", "Error en la API: " + e.getMessage());
                Header header = new Header();
                updateContent(new ErrorInternet(),  header.setTitle("Error"));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.e("API_ERROR", "Respuesta no exitosa o cuerpo vacío");
                    return;
                }

                try {
                    String responseData = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseData);

                    // Obtener el monto total
                    double totalAmount = jsonResponse.optDouble("total_amount", 0.0);

                    // Verificar si "duplicated_products" existe
                    if (!jsonResponse.has("duplicated_products") || jsonResponse.isNull("duplicated_products")) {
                        Log.e("API_ERROR", "No se encontró la clave 'duplicated_products' en la respuesta");
                        return;
                    }

                    JSONArray dataArray = jsonResponse.getJSONArray("duplicated_products");
                    List<Product> tempList = new ArrayList<>();

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject productObject = dataArray.getJSONObject(i);

                        int id = productObject.optInt("id", -1);
                        String name = productObject.optString("name", "Desconocido");
                        String brand = productObject.optString("brand", "Desconocida");
                        String status = productObject.optString("status", "Desconocido");
                        int stock = productObject.optInt("stock", 0);
                        String manufacturerInfoUrl = productObject.optString("manufacturer_information_url", "");
                        String description = productObject.optString("description", "Sin descripción");
                        double price = productObject.optDouble("price", 0.0);
                        int duplicateCount = productObject.optInt("duplicate_count", 0); // Ajustado
                        double total = jsonResponse.optDouble("total_amount", 0.0);
                        String imageUrl = "";
                        if (productObject.has("image") && !productObject.isNull("image")) {
                            JSONObject imageObject = productObject.getJSONObject("image");
                            imageUrl = imageObject.optString("url", "");
                        }

                        // Agregar el producto con la cantidad correcta
                        Product product = new Product(id, name, brand, status, stock, manufacturerInfoUrl, description, price, imageUrl, duplicateCount);
                        tempList.add(product);
                    }
                    new Handler(Looper.getMainLooper()).post(() -> {
                        productList.clear();
                        productList.addAll(tempList);
                        listWishAdapter.notifyDataSetChanged();

                        if (amount != null) {
                            amount.setText("Total: S/" + totalAmount); // 🔹 Ahora se actualiza en el hilo principal
                        } else {
                            Log.e("UI_ERROR", "El TextView amount es nulo. Verifica su inicialización.");
                        }
                    });


                } catch (JSONException e) {
                    Log.e("API_ERROR", "Error al parsear JSON: " + e.getMessage());
                }
            }
        });
    }



    private void updateContent(Fragment contentFragment, Fragment header) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_content, contentFragment)
                .replace(R.id.fragment_header, header)
                .addToBackStack(null)
                .commit();
    }
    public static String getAuthToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("auth_token", null); // Devuelve null si no hay token guardado
    }
}
