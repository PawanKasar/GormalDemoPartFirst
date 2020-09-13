package com.example.gormaldemofirstpart.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.gormaldemofirstpart.Interfaces.SubApiInterface;
import com.example.gormaldemofirstpart.R;
import com.example.gormaldemofirstpart.RetroClient.RetrofitApiUtils;
import com.example.gormaldemofirstpart.localstorage.DBAadapterClass;
import com.example.gormaldemofirstpart.models.ProductModel;
import com.example.gormaldemofirstpart.utilities.CallingImportantMethod;
import com.example.gormaldemofirstpart.utilities.Connectivity;
import com.example.gormaldemofirstpart.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private Button btn_add;
    private Button btn_sync;
    ProgressDialog pd;
    DBAadapterClass dbAadapterClass;
    JSONArray jsonArray;
    private String product_name = "", product_desc = "", product_quantity = "", product_price = "", user_mobile_no = "";
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    String substr="";
    private SubApiInterface iApiInterface;
    private RetrofitApiUtils retrofitApiUtils;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            Utility.checkPermission(getContext());
        }
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {
        btn_add = rootView.findViewById(R.id.btn_add);
        btn_sync = rootView.findViewById(R.id.btn_sync);
        btn_add.setOnClickListener(this);
        btn_sync.setOnClickListener(this);
        dbAadapterClass = new DBAadapterClass(getContext());
        try{
            TelephonyManager tMgr = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String user_mobile_no = tMgr.getLine1Number();
            Log.e("HomeFragment","MobileNumber "+user_mobile_no);
            substr=user_mobile_no.substring(2,12);
            Log.e("HomeFragment","substring "+substr);
        }catch (Exception ex){
            try {
                //pd.dismiss();
                throw new IOException(ex.toString());
            } catch (IOException e1) {
                e1.printStackTrace();
                //pd.dismiss();
            }
        }

        try{
            getDataFromSqlite();
        }catch (Exception ex){
            try {
                throw new IOException(ex.toString());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CODE:{
                // When request is cancelled, the results array are empty
                if(
                        (grantResults.length >0) &&
                                (grantResults[0]
                                        + grantResults[1]
                                        + grantResults[2]
                                        == PackageManager.PERMISSION_GRANTED
                                )
                ){
                    // Permissions are granted
                    CallingImportantMethod.showToast(getContext(),"Permissions granted.");
                }else {
                    // Permissions are denied
                    CallingImportantMethod.showToast(getContext(),"Permissions denied.");
                }
                return;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add:
                openProductFragment();
                break;

            case R.id.btn_sync:
                if (!jsonArray.isNull(0) && Connectivity.isConnected(getContext())){
                    pd = new ProgressDialog(getActivity(), R.style.MyTheme);
                    pd.setCancelable(false);
                    pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                    pd.show();
                    requestServerToAddNewProduct(product_name,product_desc,
                            product_quantity,product_price,substr);
                }else {
                    CallingImportantMethod.showToast(getContext(),"Please insert data into LocalDb");
                }
                break;
        }
    }

    private void openProductFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.mainContainer, new ProductFragment())
                .addToBackStack("HomeFragment")
                .commit();
    }


    // Call Below method in Actvity which include Async Task to fetch result as JSON
    private void getDataFromSqlite() {

        new AsyncTask<Object, Object, JSONArray>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(getActivity(), R.style.MyTheme);
                pd.setCancelable(false);
                pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                pd.show();
            }

            @Override
            protected JSONArray doInBackground(Object... params) {

                jsonArray = dbAadapterClass.getProductFromAllocationTable();      // roomNo : 26 , rackId : 203R001S0026
                Log.d("json",jsonArray+"");
                //sqLiteDatabase.close();

                return jsonArray;
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected void onPostExecute(JSONArray jsonArray) {
                super.onPostExecute(jsonArray);
                //Log.d"jsonArrayOne", jsonArray.toString());
                if (pd.isShowing()) {
                    pd.dismiss();

                    try {
                        //arrayModelList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Log.e("HomeFragment","getting jsonObject "+jsonObject.toString());
                            product_name = jsonObject.getString("productName");
                            Log.e("HomeFragment","productName "+product_name);

                            product_desc = jsonObject.getString("productDescription");
                            Log.e("HomeFragment","productDesc "+product_desc);

                            product_quantity = jsonObject.getString("quantity");
                            Log.e("HomeFragment","productQuantity "+product_quantity);

                            product_price = jsonObject.getString("price");
                            Log.e("HomeFragment","productPrice "+product_price);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }.execute();
    }

    private void requestServerToAddNewProduct(String productName,String productDesc,String productQty,
                                              String productPrice,String mobileNo){
        HashMap<String,String> requestBody = new HashMap<>();
        requestBody.put("product_name",productName);
        requestBody.put("product_desc",productDesc);
        requestBody.put("product_quantity",productQty);
        requestBody.put("product_price",productPrice);
        requestBody.put("user_mobile_no",mobileNo);
        Log.e("HomeFragment","requestBody "+requestBody);
        iApiInterface = retrofitApiUtils.getAPIService();
        iApiInterface.requestServerToAddProduct(requestBody).enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                try{
                    if (response.isSuccessful()){
                        if (response.body() != null && response.body().getResults().getSuccess().equals(1)){
                            CallingImportantMethod.showToast(getContext(),response.body().getResults().getMessage());
                            pd.dismiss();
                        }else {
                            CallingImportantMethod.showToast(getContext(),response.body().getResults().getMessage());
                            pd.dismiss();
                        }
                    }else {
                        CallingImportantMethod.showToastError(getContext());
                        pd.dismiss();
                    }
                }catch (Exception ex){
                    try {
                        pd.dismiss();
                        throw new IOException(ex.toString());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        pd.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                try {
                    CallingImportantMethod.showToast(getContext(),t.getMessage());
                    pd.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                    pd.dismiss();
                }
            }
        });
    }
}
