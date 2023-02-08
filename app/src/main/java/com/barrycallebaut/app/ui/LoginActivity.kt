package com.barrycallebaut.app.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.barrycallebaut.app.database.lokal.PreferencesHelper
import com.barrycallebaut.app.database.server.ApiClient
import com.barrycallebaut.app.databinding.ActivityLoginBinding
import com.barrycallebaut.app.models.Karyawan
import com.barrycallebaut.app.models.Responses
import com.barrycallebaut.app.utils.Constant
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var sharedPref: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = PreferencesHelper(this)

        if (sharedPref.getBoolean(Constant.IS_LOGIN)) {

            val role = sharedPref.getString(Constant.ROLE)
            if (role.equals("Koordinator")) {
                val menu_petugas = Intent(this, PetugasActivity::class.java)
                startActivity(menu_petugas)
                finish()
            } else if (role.equals("Petugas")) {
                val menu_petugas = Intent(this, PetugasActivity::class.java)
                startActivity(menu_petugas)
                finish()
            }

        }


        binding.rlLogin.setOnClickListener {
            val email = binding.tieUsername.text.toString()
            val password = binding.tiePassword.text.toString()

            if (email.isEmpty()) {
                val snack =
                    Snackbar.make(it, "Lengkapi Username anda!", Snackbar.LENGTH_LONG)
                snack.show()
            } else if (password.isEmpty()) {
                val snack = Snackbar.make(it, "Lengkapi Password anda!", Snackbar.LENGTH_LONG)
                snack.show()
            } else {
                setLogin(email, password)
            }

        }

    }

    private fun setLogin(email: String, password: String) {
        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading"
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.login(email, password)
            ?.enqueue(object : Callback<Responses.ResponseKaryawan> {
                override fun onResponse(
                    call: Call<Responses.ResponseKaryawan?>,
                    response: Response<Responses.ResponseKaryawan?>
                ) {
                    dialogProgress.dismiss()
                    val pesanRespon = response.message()

                    if (response.isSuccessful) {
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode
                        if (kode.equals("1")) {
                            val data = response.body()?.data
                            setLoginSuccess(data!!)
                        } else {

                            SweetAlertDialog(
                                this@LoginActivity,
                                SweetAlertDialog.ERROR_TYPE
                            )
                                .setTitleText("Uups..")
                                .setContentText(message)
                                .show()

                        }
                    } else {
                        SweetAlertDialog(this@LoginActivity, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Uups..")
                            .setContentText(pesanRespon)
                            .show()
                    }
                }

                override fun onFailure(
                    call: Call<Responses.ResponseKaryawan?>,
                    t: Throwable
                ) {
                    dialogProgress.dismiss()
                    SweetAlertDialog(this@LoginActivity, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Uups..")
                        .setContentText(t.localizedMessage)
                        .show()
                }

            })
    }

    private fun setLoginSuccess(data: Karyawan) {

        var posisi = data.posisi.toString()

        sharedPref.put(Constant.IS_LOGIN, true)
        sharedPref.put(Constant.ROLE, posisi)
        sharedPref.put(Constant.ID_USER, data.id.toString())

            startActivity(Intent(this, PetugasActivity::class.java))
            finish()

    }
}