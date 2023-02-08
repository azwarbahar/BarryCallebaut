package com.barrycallebaut.app.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.pedant.SweetAlert.SweetAlertDialog
import com.barrycallebaut.app.database.lokal.PreferencesHelper
import com.barrycallebaut.app.database.server.ApiClient
import com.barrycallebaut.app.databinding.ActivityUbahPasswordBinding
import com.barrycallebaut.app.models.Responses
import com.barrycallebaut.app.utils.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UbahPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUbahPasswordBinding

    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUbahPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constant.ID_USER).toString()


        binding.imgBack.setOnClickListener { finish() }

        binding.rlSimpan.setOnClickListener { clickSimpan() }
    }
    private fun clickSimpan() {

        var old_password = binding.tiePasswordLama.text.toString()
        var new_password = binding.tiePasswordBaru.text.toString()

        if (old_password.isEmpty() || old_password.equals("")) {
            SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Gagal...")
                .setContentText("Password lama tidak boleh kosong!")
                .show()
        } else if (new_password.isEmpty() || new_password.equals("")) {
            SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Gagal...")
                .setContentText("Password baru tidak boleh kosong!")
                .show()
        } else if (old_password.equals(new_password)) {
            SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Gagal...")
                .setContentText("Password baru dan lama tidak boleh sama!")
                .show()
        } else {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Ubah Password")
                .setContentText("Yakin ingin mengubah password ?")
                .setCancelButton(
                    "Tidak"
                ) { sweetAlertDialog -> sweetAlertDialog.dismiss() }
                .setConfirmButton(
                    "Ok"
                ) { sweetAlertDialog ->
                    sweetAlertDialog.dismiss()
                    startUpdatePassword(old_password, new_password)
                }
                .show()
        }

    }

    private fun startUpdatePassword(oldPassword: String, newPassword: String) {

        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.updatePasswrd(id, oldPassword, newPassword)
            ?.enqueue(object : Callback<Responses.ResponseKaryawan> {
                override fun onResponse(
                    call: Call<Responses.ResponseKaryawan>,
                    response: Response<Responses.ResponseKaryawan>
                ) {
                    dialogProgress.dismiss()
                    val pesanRespon = response.message()
                    if (response.isSuccessful) {
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode
                        if (kode.equals("1")) {
                            SweetAlertDialog(
                                this@UbahPasswordActivity,
                                SweetAlertDialog.SUCCESS_TYPE
                            )
                                .setTitleText("Success..")
                                .setContentText("Data berhasil diubah..")
                                .setConfirmButton(
                                    "Ok"
                                ) { sweetAlertDialog ->
                                    sweetAlertDialog.dismiss()
                                    finish()
                                }
                                .show()
                        } else {
                            SweetAlertDialog(
                                this@UbahPasswordActivity,
                                SweetAlertDialog.ERROR_TYPE
                            )
                                .setTitleText("Gagal..")
                                .setContentText(message)
                                .show()
                        }
                    } else {
                        SweetAlertDialog(
                            this@UbahPasswordActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText("Server tidak merespon")
                            .show()
                    }
                }

                override fun onFailure(call: Call<Responses.ResponseKaryawan>, t: Throwable) {
                    dialogProgress.dismiss()
                    SweetAlertDialog(
                        this@UbahPasswordActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Maaf..")
                        .setContentText("Terjadi Kesalahan Sistem")
                        .show()
                }

            })


    }
}