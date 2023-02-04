package com.barrycallebaut.app.utils

import java.text.SimpleDateFormat

class Constant {

    companion object{

        const val IS_LOGIN = "IS_LOGIN"
        const val ROLE = "ROLE"
        const val ID_USER = "ID_USER"

        const val ID_PETANI_SELECTED = "ID_PETANI_SELECTED"

        const val URL_PHOTO = "https://barrycallebaut.cfg.my.id/assets/images/photo/"


        fun formatDate(input: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat = SimpleDateFormat("dd-MM-yyyy")
            val date = inputFormat.parse(input)
            return outputFormat.format(date)
        }

        fun formatDateCreated(input: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val outputFormat = SimpleDateFormat("dd-MM-yyyy")
            val date = inputFormat.parse(input)
            return outputFormat.format(date)
        }

    }

}