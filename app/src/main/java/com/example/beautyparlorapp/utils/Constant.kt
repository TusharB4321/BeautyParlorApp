package com.example.beautyparlorapp.utils

import androidx.navigation.NavOptions
import com.example.beautyparlorapp.R


object Constant {

    private const val REGEX = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    fun isNumberValid(number:String,callback:(Resources<String>)->Unit): Boolean {
        return if (number.length == 10) {
            true
        } else {
            callback(Resources.Error("Number should contain 10 numbers"))
            false
        }
    }
    fun isNameValid(name:String,callback:(Resources<String>)->Unit): Boolean {
        return if (name.isNotEmpty()) {
            true
        } else {
            callback(Resources.Error("Your name field is empty"))
            false
        }
    }
    fun isPasswordValid(password:String,callback:(Resources<String>)->Unit):Boolean{
        return if (password.length>= 8){
            true
        }else{
            callback(Resources.Error("Password should contain at least 8 characters"))
            false
        }
    }

    fun isPasswordConfirmValid(password:String,confirmPass:String,callback:(Resources<String>)->Unit):Boolean{
        return if (password==confirmPass){
            true
        }else{
            callback(Resources.Error("Password is not match please check"))
            false
        }
    }

     fun isEmailValid(email:String,callback: (Resources<String>) -> Unit): Boolean {
        return if (email.isNotEmpty() && email.matches(Regex(REGEX))
        ) {
            true
        } else {
            callback(Resources.Error("Enter a valid email"))
            false
        }
    }


    val slideRightLeftNavOptions: NavOptions by lazy {
        NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left)
            .setPopExitAnim(R.anim.slide_out_right)
            .build()
    }


}