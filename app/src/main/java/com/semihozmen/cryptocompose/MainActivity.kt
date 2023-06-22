package com.semihozmen.cryptocompose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semihozmen.cryptocompose.model.CryptoModel
import com.semihozmen.cryptocompose.service.CryptoAPI
import com.semihozmen.cryptocompose.ui.theme.CryptoComposeTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoComposeTheme {
                MainScreen()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(){
    var cryptoList = remember { mutableStateListOf<CryptoModel>()  }

    val BASE_URL = "https://raw.githubusercontent.com/"
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(CryptoAPI::class.java)

    val call = retrofit.gelAll()

    call.enqueue(object:Callback<List<CryptoModel>>{
        override fun onResponse(call: Call<List<CryptoModel>>, response: Response<List<CryptoModel>>) {
            if(response.isSuccessful){
                response.body()?.let {
                    cryptoList.addAll(it)
                }
            }
        }

        override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
            t.printStackTrace()
        }

    })

    Scaffold( topBar = { AppBar() }){
        Spacer(modifier = Modifier.padding(5.dp))
        CryptoList(cryptos = cryptoList)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(){
    TopAppBar(
        title = {
            Text("Retrofit Compose", maxLines = 1, fontWeight = FontWeight.Bold)
        },colors= TopAppBarDefaults.centerAlignedTopAppBarColors(Color.Magenta), modifier = Modifier.padding(10.dp))

}

@Composable
fun CryptoList(cryptos:List<CryptoModel>){
    LazyColumn(contentPadding = PaddingValues(5.dp) ){
        items (cryptos) {crypto ->
            CryptoRow(crypto = crypto)
        }
    }
}

@Composable
fun CryptoRow(crypto:CryptoModel){
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.surface)) {
        Text(
            text = crypto.currency,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(2.dp),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.padding(2.dp))
        Text(
            text = crypto.price,
            modifier = Modifier.padding(2.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CryptoComposeTheme {
        MainScreen()
    }
}