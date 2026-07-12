package com.example.testp.ViewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testp.Models.SkillforgeResponse
import com.example.testp.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {

    private val _courses = MutableLiveData<SkillforgeResponse>()
    val courses: LiveData<SkillforgeResponse> = _courses

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getCourses() {

        viewModelScope.launch {

            _loading.value = true

            try {

                val response = repository.getCourses()

                if (response.isSuccessful && response.body() != null) {

                    _courses.value = response.body()

                } else {

                    _error.value = response.message()
                }

            } catch (e: Exception) {

                _error.value = e.localizedMessage

            } finally {

                _loading.value = false
            }
        }
    }
}