package com.example.gormaldemofirstpart.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gormaldemofirstpart.Activities.MainActivity;
import com.example.gormaldemofirstpart.R;
import com.example.gormaldemofirstpart.localstorage.DBAadapterClass;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFragment extends Fragment implements View.OnClickListener{

    private EditText edtProductName;
    private EditText edtProductDescription;
    private EditText edtQuantity;
    private EditText edtPrice;
    private Button btnSubmit;
    private DBAadapterClass dbAadapterClass;

    public ProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_product, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView){
        MainActivity.titleMain.setText("Add New Product");
        edtProductName = rootView.findViewById(R.id.edt_productName);
        edtProductDescription = rootView.findViewById(R.id.edt_productDescription);
        edtQuantity = rootView.findViewById(R.id.edt_quantity);
        edtPrice = rootView.findViewById(R.id.edt_price);
        btnSubmit = rootView.findViewById(R.id.btn_submit);

        btnSubmit.setOnClickListener(this);
        dbAadapterClass = new DBAadapterClass(getContext());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_submit:
                if (edtProductName.getText().toString().equals("") && edtProductDescription.getText().toString().equals("")
                        && edtQuantity.getText().toString().equals("") && edtPrice.getText().toString().equals("")){
                    edtProductName.setError("Please Enter value");
                    edtProductDescription.setError("Please Enter value");
                    edtQuantity.setError("Please Enter value");
                    edtPrice.setError("Please Enter value");
                }else {
                    insertIntoProductMasterTable(edtProductName.getText().toString(),edtProductDescription.getText().toString(),
                            edtQuantity.getText().toString(),edtPrice.getText().toString());
                }
                break;

            default:
                break;
        }
    }

    private void insertIntoProductMasterTable(String productName,String productdescription,String quantity,String price){
        long lastid = dbAadapterClass.insertTagsIntoTagMaster(productName,productdescription,quantity,price);
        Log.e("ProductFragment","lastid "+lastid);
        Toast.makeText(getContext(),"Product inserted successfully",Toast.LENGTH_SHORT).show();
        clearField();
        dbAadapterClass.close();
    }

    private void clearField(){
        edtProductName.getText().clear();
        edtProductDescription.getText().clear();
        edtQuantity.getText().clear();
        edtPrice.getText().clear();
    }
}
