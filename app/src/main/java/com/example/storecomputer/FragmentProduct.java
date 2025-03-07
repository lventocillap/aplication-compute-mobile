package com.example.storecomputer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.storecomputer.Api.SaleProduct;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentProduct#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentProduct extends Fragment {

    private static final String PRODUCT_ID = "productId";
    private TextView txtName, txtPrice, txtDescription, txtBrand, txtStock, txtManufacturer, txtStatus, txtId;
    private ImageView imgProduct;
    private int productId;
    private static final String API_URL = "http://192.168.100.80:8000/api/product/";
    private String urlManufacturer = "";

    public FragmentProduct() {
        // Required empty public constructor
    }

    public static FragmentProduct newInstance(int productId) {
        FragmentProduct fragment = new FragmentProduct();
        Bundle args = new Bundle();
        args.putInt(PRODUCT_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getInt(PRODUCT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        Button btnInformation = view.findViewById(R.id.btnInformation);
        Button btnSaleProduct = view.findViewById(R.id.btnSale);

        btnSaleProduct.setOnClickListener(v -> {
            new Thread(() -> {
                try {
                    Header headerSale = new Header();
                    SaleProduct saleProduct = new SaleProduct();
                    String response = saleProduct.postWishlistItem(Integer.parseInt(txtId.getText().toString()), getContext());
                    updateContent(new FragmentListWish(), headerSale.setTitle("Compras"));
                } catch (IOException e) {
                    Header headerLogin = new Header();
                    updateContent(new Login(), headerLogin.setTitle("Login"));
                    e.printStackTrace();
                }
            }).start();
        });

        btnInformation.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlManufacturer));
            startActivity(intent);
        });

        txtPrice = view.findViewById(R.id.txtPrice);
        txtBrand = view.findViewById(R.id.txtBrand);
        txtDescription = view.findViewById(R.id.txtDescription);
        txtName = view.findViewById(R.id.txtName);
        txtStock = view.findViewById(R.id.txtStock);
        imgProduct = view.findViewById(R.id.imgProduct);
        txtStatus = view.findViewById(R.id.txtStatus);
        txtId = view.findViewById(R.id.txtProductId);
        loadProductFromAPI();
        return view;
    }
    private void loadProductFromAPI() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_URL + productId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("data")) {
                                JSONObject productObject = response.getJSONObject("data");

                                // Extraer valores de la API
                                int id = productObject.getInt("id");
                                String name = productObject.getString("name");
                                double price = productObject.getDouble("price");
                                String brand = productObject.getString("brand");
                                String status = productObject.getString("status");
                                String manufacturer = productObject.getString("manufacturer_information_url");
                                String stock = productObject.getString("stock");
                                String description = productObject.getString("description");
                                String imageUrl = productObject.getJSONObject("image").getString("url");

                                urlManufacturer = manufacturer;

                                String[] descriptionDetail = description.split(",");
                                StringBuilder concatDesc = new StringBuilder("");
                                for (String desc : descriptionDetail) {
                                    concatDesc.append(desc).append("\n");
                                }
                                txtId.setText(String.valueOf(id));
                                txtName.setText(name);
                                txtBrand.setText("Marca: "+brand);
                                txtStock.setText("Stock: "+stock);
                                txtStatus.setText("Estado: "+status);
                                txtPrice.setText(String.format("S/ %.2f", price));
                                txtDescription.setText(concatDesc.toString());
                                Glide.with(requireContext()).load(imageUrl).into(imgProduct);

                            } else {
                                Toast.makeText(getActivity(), "Error: No se encontraron datos", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getActivity(), "Error al cargar el producto", Toast.LENGTH_LONG).show();
                    }
                });

        queue.add(request);

    }
    private void updateContent(Fragment contentFragment, Fragment header) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_content, contentFragment)
                .replace(R.id.fragment_header, header)
                .addToBackStack(null)
                .commit();
    }

}