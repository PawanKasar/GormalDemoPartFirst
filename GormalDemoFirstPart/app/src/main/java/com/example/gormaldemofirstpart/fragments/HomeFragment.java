package com.example.gormaldemofirstpart.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.gormaldemofirstpart.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{

    private EditText edtProductName;
    private EditText edtProductDescription;
    private EditText edtQuantity;
    private EditText edtPrice;
    private Button btnSubmit;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        initViews(rootView);

        return rootView;
    }

    private void initViews(View rootView){
        edtProductName = rootView.findViewById(R.id.edt_productName);
        edtProductDescription = rootView.findViewById(R.id.edt_productDescription);
        edtQuantity = rootView.findViewById(R.id.edt_quantity);
        edtPrice = rootView.findViewById(R.id.edt_price);
        btnSubmit = rootView.findViewById(R.id.btn_submit);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_submit:

                break;
        }
    }
}
