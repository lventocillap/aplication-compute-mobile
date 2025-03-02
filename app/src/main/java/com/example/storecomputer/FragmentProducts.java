package com.example.storecomputer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.storecomputer.Model.Product;
import com.example.storecomputer.Model.Promotion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentProducts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentProducts extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private String category;
    private static final String API_URL = "http://192.168.100.80:8000/api/category/product/?category=";

    public FragmentProducts() {
        // Required empty public constructor
    }

    public static FragmentProducts newInstance(String category) {
        FragmentProducts fragment = new FragmentProducts();
        Bundle args = new Bundle();
        args.putString("category", category); // Pasar la categoría normalizada
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString("category"); // Obtener la categoría de los argumentos
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_products_section, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewProductsCategory);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, getContext());
        recyclerView.setAdapter(productAdapter);
        loadProductFromAPI();
        return view;
    }
    private void loadProductFromAPI() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_URL+category, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        productList.clear();
                        try {
                            JSONArray dataArray = response.getJSONArray("data");

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject categoryObject = dataArray.getJSONObject(i);
                                JSONArray productArray = categoryObject.getJSONArray("product_id"); // Acceder a los productos dentro de "product_id"

                                for (int j = 0; j < productArray.length(); j++) {
                                    JSONObject productObject = productArray.getJSONObject(j);

                                    int id = productObject.getInt("id");
                                    String name = productObject.getString("name");
                                    String brand = productObject.getString("brand");
                                    String status = productObject.getString("status");
                                    int stock = productObject.getInt("stock");
                                    String manufacturerUrl = productObject.getString("manufacturer_information_url");
                                    String description = productObject.getString("description");
                                    double price = productObject.getDouble("price");
                                    String image = productObject.getString("image");

                                    productList.add(new Product(id, name, brand, status, stock, manufacturerUrl, description, price, image));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        productAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace(); // Imprime el error en Logcat
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(), "Compruebe su conexión a internet", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        queue.add(request);
    }
}