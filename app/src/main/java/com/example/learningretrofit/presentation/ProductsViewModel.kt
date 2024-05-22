package com.example.learningretrofit.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningretrofit.data.ProductsRepository
import com.example.learningretrofit.data.Result
import com.example.learningretrofit.data.model.ProductDescription
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val productsRepository: ProductsRepository
): ViewModel(){
    private val _productsStateFlow = MutableStateFlow<List<ProductDescription>>(emptyList())
    val products = _productsStateFlow.asStateFlow()

    private val _showErrorToastChannel = Channel<Boolean>()
    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()
    init {
        //Coroutines make asynchronous tasks so that the UI isn't blocked
        viewModelScope.launch {
            productsRepository.getProducts().collectLatest {
                result ->
                when (result){
                    is Result.Error -> {
                        _showErrorToastChannel.send(true)
                    }
                    is Result.Success -> {
                        result.data?.let {
                            products ->
                            _productsStateFlow.update { products }
                        }
                    }
                }
            }
        }
    }
}