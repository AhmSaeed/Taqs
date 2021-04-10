package com.iti.mad41.taqs.favourites

import android.util.Log
import androidx.lifecycle.*
import com.iti.mad41.taqs.R
import com.iti.mad41.taqs.data.model.LocationDetails
import com.iti.mad41.taqs.data.model.WeatherNode
import com.iti.mad41.taqs.data.repo.IDefaultWeatherRepository
import com.iti.mad41.taqs.data.source.Result
import com.iti.mad41.taqs.data.source.Result.Success
import com.iti.mad41.taqs.util.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class FavouritesViewModel(
    private val defaultWeatherRepository: IDefaultWeatherRepository
) : ViewModel() {
    private val _favouriteListResult: LiveData<Result<List<WeatherNode>>> = defaultWeatherRepository.observeFavouriteWeatherList()

    private var _favouriteList: MutableLiveData<List<WeatherNode>?> = _favouriteListResult.map {
        if (it is Success) {
            _dataEmpty.value = it.data.count() == 0
            it.data
        } else {
            null
        }
    } as MutableLiveData<List<WeatherNode>?>
    val favouriteList: LiveData<List<WeatherNode>?> = _favouriteList

    private val _searchQuery = MutableStateFlow("")

    private val _searchFavouriteListFlow = _searchQuery.flatMapLatest {
        defaultWeatherRepository.searchForWeatherNodeByAddress(it)
    }

    private val _searchFavouriteList: MutableLiveData<List<WeatherNode>?> = _searchFavouriteListFlow.asLiveData() as MutableLiveData<List<WeatherNode>?>
    val searchFavouriteList: LiveData<List<WeatherNode>?> = _searchFavouriteList

    private var _deletedItemId = MutableLiveData<Int>()

    private val _dataEmpty = MutableLiveData<Boolean>()
    val dataEmpty: LiveData<Boolean> = _dataEmpty

    private val _openFavouriteItemDetailsEvent = MutableLiveData<Event<Int>>()
    val openFavouriteItemDetailsEvent: LiveData<Event<Int>> = _openFavouriteItemDetailsEvent

    private var _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    var isSearching = MutableLiveData<Boolean>(false)

    fun searchFavourites(searchQuery: String) {
        setSearchQuery(searchQuery)
        isSearching.value = !searchQuery.isEmpty()
    }

    fun setSearchQuery(searchQuery: String){
        _searchQuery.value = searchQuery
    }

    fun saveWeatherNode(locationDetails: LocationDetails){
        viewModelScope.launch {
            defaultWeatherRepository.getPlaceWeatherToAddToFavouriteLocalDataSource(locationDetails)
        }
    }

    fun deleteWeatherNode(){
        viewModelScope.launch {
            defaultWeatherRepository.deleteWeatherNodeFromLocalDataSource(_deletedItemId.value ?: 0)
        }
    }

    fun onDeleteClick(id: Int){
        _snackbarText.value = Event(R.string.delete_confirmation_message)
        _deletedItemId.value = id
    }

    fun openFavouriteItemDetails(id: Int) {
        _openFavouriteItemDetailsEvent.value = Event(id)
    }
}

class FavouritesViewModelFactory(
        val defaultWeatherRepository: IDefaultWeatherRepository
) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavouritesViewModel(defaultWeatherRepository)  as T
    }
}