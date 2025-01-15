package com.example.firebase.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase.model.Mahasiswa
import com.example.firebase.repository.MahasiswaRepository
import kotlinx.coroutines.launch

class InsertViewModel(
    private val mhs: MahasiswaRepository
): ViewModel() {

    var uiEvent: InsertUiState by mutableStateOf(InsertUiState())
        private set

    var uiState: FormState by mutableStateOf(FormState.Idle)
        private set

    fun updateState(mahasiswaEvent: MahasiswaEvent){
        uiEvent = uiEvent.copy(
            insertUiEvent = mahasiswaEvent,
        )
    }

    fun validateFields(): Boolean {
        val event = uiEvent.insertUiEvent
        val errorState = FormErrorState(
            nim = if (event.nim.isNotEmpty()) null else "NIM tidak boleh kosong",
            nama = if (event.nama.isNotEmpty()) null else "Nama tidak boleh kosong",
            jenis_kelamin = if (event.jenis_kelamin.isNotEmpty()) null else "Jenis Kelamin tidak boleh kosong",
            alamat = if (event.alamat.isNotEmpty()) null else "Alamat tidak boleh kosong",
            kelas = if (event.kelas.isNotEmpty()) null else "Kelas tidak boleh kosong",
            judulskripsi = if (event.judulskripsi.isNotEmpty()) null else "Judul Skripsi tidak boleh kosong",
            dospem1 = if (event.dospem1.isNotEmpty()) null else "Dosen Pembimbing 1 tidak boleh kosong",
            dospem2 = if (event.dospem2.isNotEmpty()) null else "Dosen Pembimbing 2 tidak boleh kosong"
        )

        uiEvent = uiEvent.copy(isEntryValid = errorState)
        return errorState.isValid()
    }

    fun insertMhs(){
        if (validateFields()) {
            viewModelScope.launch {
                uiState = FormState.Loading
                try {
                    mhs.insertMahasiswa(uiEvent.insertUiEvent.toMhsModel())
                    uiState = FormState.Success("Data Berhasil Disimpan")
                } catch (e: Exception) {
                    uiState = FormState.Error("Data Gagal Disimpan")
                }
            }
        } else {
            uiState = FormState.Error("Data Tidak Valid")
        }
    }

    fun resetForm() {
        uiEvent = InsertUiState()
        uiState = FormState.Idle
    }

    fun resetSnackBarMessage(){
        uiState = FormState.Idle
    }
}


sealed class FormState{
    object Idle : FormState()
    object Loading : FormState()
    data class Success(val message: String) : FormState()
    data class Error(val message: String) : FormState()
}

data class InsertUiState(
    val insertUiEvent: MahasiswaEvent = MahasiswaEvent(),
    val isEntryValid: FormErrorState = FormErrorState(),
)

data class FormErrorState(
    val nim: String? = null,
    val nama: String? = null,
    val jenis_kelamin: String? = null,
    val alamat: String? = null,
    val kelas: String? = null,
    val angkatan: String? = "2022",
    val judulskripsi: String? = null,
    val dospem1: String? = null,
    val dospem2: String? = null
){
    fun isValid() : Boolean {
        return nim == null && nama == null && jenis_kelamin == null &&
                alamat == null && kelas == null && angkatan == "2022" && judulskripsi == null &&
                dospem1 == null && dospem2 == null
    }
}

data class MahasiswaEvent(

    val nim: String = "",
    val nama: String = "",
    val jenis_kelamin: String = "",
    val alamat: String = "",
    val kelas: String = "",
    val angkatan: String = "2022",
    val judulskripsi: String = "",
    val dospem1: String = "",
    val dospem2: String = ""
)

fun MahasiswaEvent.toMhsModel(): Mahasiswa = Mahasiswa(
    nim = nim,
    nama = nama,
    jenis_kelamin = jenis_kelamin,
    alamat = alamat,
    kelas = kelas,
    angkatan = angkatan,
    judulskripsi = judulskripsi,
    dospem1 = dospem1,
    dospem2 = dospem2

)