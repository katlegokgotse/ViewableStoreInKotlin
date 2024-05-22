package com.example.learningretrofit

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.learningretrofit.data.ProductImplementation
import com.example.learningretrofit.data.RetrofitInstance
import com.example.learningretrofit.data.model.ProductDescription
import com.example.learningretrofit.presentation.ProductsViewModel
import com.example.learningretrofit.ui.theme.LearningRetrofitTheme
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<ProductsViewModel>(
        factoryProducer = {
            object: ViewModelProvider.Factory{
                override fun <T: ViewModel> create(modelClass: Class<T>):T{
                    return ProductsViewModel(ProductImplementation(RetrofitInstance.api))
                    as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearningRetrofitTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                   Surface(
                       modifier = Modifier
                           .fillMaxSize()
                           .padding(innerPadding)
                   ){
                       val productList = viewModel.products.collectAsState().value
                        val context = LocalContext.current
                       LaunchedEffect(key1 = viewModel.showErrorToastChannel){
                           viewModel.showErrorToastChannel.collectLatest {
                                show -> if (show){
                                    Toast.makeText(
                                        context, "Error",
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                }
                           }
                       }
                       if (productList.isEmpty()){
                           Box(
                               modifier = Modifier.fillMaxSize(),
                               contentAlignment = Alignment.Center
                           ){
                               CircularProgressIndicator()
                           }
                       }else{
                           LazyColumn(
                               modifier = Modifier.fillMaxSize(),
                               horizontalAlignment = Alignment.CenterHorizontally,
                               contentPadding = PaddingValues(16.dp)
                           ){
                               items(productList.size){index ->
                                   Product(productList[index])
                                   Spacer(modifier = Modifier.height(16.dp))
                               }
                           }
                       }
                   }

                }
            }
        }
    }
}

@Composable
fun Product(productDescription: ProductDescription) {
    val imageState = rememberAsyncImagePainter( //Holds the state for the image using the coil dependency
        model =  ImageRequest.Builder(LocalContext.current).data(productDescription.thumbnail)
            .size(Size.ORIGINAL).build()
    ).state //ImageRequest.Builder.data.size.build
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .height(300.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
            if (imageState is AsyncImagePainter.State.Error){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator()
                }
            }
            if (imageState is AsyncImagePainter.State.Success){
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = imageState.painter,
                    contentDescription = productDescription.title,
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row() {
                Text(
                    text = productDescription.title,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "R ${productDescription.price}",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = productDescription.category,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        Text(
            text = productDescription.description,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        }
    }