package com.example.nfc_gacor.APIService;

/**
 * Created by user on 1/10/2018.
 */






import com.example.nfc_gacor.model.audit.ModelAudit;
import com.example.nfc_gacor.model.audit.ModelPost;
import com.example.nfc_gacor.model.produk.ModelProduk;
import com.example.nfc_gacor.model.topup.ModelTopup;
import com.raizlabs.android.dbflow.annotation.Unique;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface APIInterfacesRest {


 @GET("produk/all")
 Call <ModelProduk> getProduk(@Query ("X-Api-Key") String apikey);

 @GET("topup/all")
 Call<ModelTopup> getTopup(@Query ("X-Api-Key") String apikey);

 @GET("audit/all")
 Call<ModelAudit> getAudit(@Query ("X-Api-Key") String apikey);




@FormUrlEncoded
 @POST("audit/add")
 Call<ModelPost> sendAudit(@Field("nama_user") String nama_user,
                         @Field("keterangan") String keterangan,
                         @Field("nominal") String nominal,
                         @Field("debet_credit") String debet_credit);

}




