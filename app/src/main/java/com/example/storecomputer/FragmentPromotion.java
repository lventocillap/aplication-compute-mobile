package com.example.storecomputer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.storecomputer.EnumAPI.APIEnum;
import com.example.storecomputer.Model.Promotion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentPromotion extends Fragment {

    private RecyclerView recyclerView;
    private PromotionAdapter adapter;
    private List<Promotion> promotionsList;

    private static final String API_URL = APIEnum.DOMAIN.getUrl()+"/api/promotion"; //Reemplaza con la URL real

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promotion, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewPromotions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        promotionsList = new ArrayList<>();
        adapter = new PromotionAdapter(promotionsList, getContext());
        recyclerView.setAdapter(adapter);

        loadPromotionsFromAPI();

        return view;
    }

    private void loadPromotionsFromAPI() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        promotionsList.clear();
                        try {
                            JSONArray dataArray = response.getJSONArray("data");

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject obj = dataArray.getJSONObject(i);
                                String imageUrl = obj.getString("url");  // AHORA USAMOS "url"
                                String title = obj.getString("title");
                                String description = obj.getString("description");

                                promotionsList.add(new Promotion(imageUrl, title, description));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace(); // Imprime el error en Logcat
                        if (getActivity() != null) {
                            updateContend(new ErrorInternet());
                            Toast.makeText(getActivity(), "Compruebe su conexión a internet", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        queue.add(request);
    }
    private void updateContend(Fragment contentFragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_content, contentFragment)
                .addToBackStack(null) // Para que pueda volver atrás con el botón de retroceso
                .commit();
    }
}