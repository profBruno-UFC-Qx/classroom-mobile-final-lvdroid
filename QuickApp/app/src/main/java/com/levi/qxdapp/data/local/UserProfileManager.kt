package com.levi.qxdapp.data.local

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object UserProfileManager {
    private const val PREFS_NAME = "quickqxd_profile_prefs"
    
    private const val KEY_FIRST_NAME = "first_name"
    private const val KEY_LAST_NAME = "last_name"
    private const val KEY_PHONE = "phone"
    private const val KEY_PHOTO_PATH = "photo_path"
    private const val KEY_ADDRESSES = "addresses"
    
    private const val KEY_REG_EMAIL = "reg_email"
    private const val KEY_REG_PASSWORD = "reg_password"
    
    private const val DEFAULT_ADDRESS = "Rua Basílio Emiliano Pinto, 711 - Centro, Quixadá - CE"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getFirstName(context: Context): String {
        return getPrefs(context).getString(KEY_FIRST_NAME, "João") ?: "João"
    }

    fun setFirstName(context: Context, value: String) {
        getPrefs(context).edit().putString(KEY_FIRST_NAME, value).apply()
    }

    fun getLastName(context: Context): String {
        return getPrefs(context).getString(KEY_LAST_NAME, "Silva") ?: "Silva"
    }

    fun setLastName(context: Context, value: String) {
        getPrefs(context).edit().putString(KEY_LAST_NAME, value).apply()
    }

    fun getPhone(context: Context): String {
        return getPrefs(context).getString(KEY_PHONE, "(88) 99999-9999") ?: "(88) 99999-9999"
    }

    fun setPhone(context: Context, value: String) {
        getPrefs(context).edit().putString(KEY_PHONE, value).apply()
    }

    fun getPhotoPath(context: Context): String? {
        val path = getPrefs(context).getString(KEY_PHOTO_PATH, null)
        if (path != null) {
            val file = File(path)
            if (file.exists()) {
                return path
            }
        }
        return null
    }

    fun setPhotoPath(context: Context, value: String?) {
        getPrefs(context).edit().putString(KEY_PHOTO_PATH, value).apply()
    }

    fun getAddresses(context: Context): List<String> {
        val raw = getPrefs(context).getString(KEY_ADDRESSES, null)
        return if (raw == null) {
            listOf(DEFAULT_ADDRESS)
        } else if (raw.isEmpty()) {
            emptyList()
        } else {
            raw.split("||")
        }
    }

    fun setAddresses(context: Context, list: List<String>) {
        val raw = if (list.isEmpty()) "" else list.joinToString("||")
        getPrefs(context).edit().putString(KEY_ADDRESSES, raw).apply()
    }

    fun addAddress(context: Context, address: String) {
        val list = getAddresses(context).toMutableList()
        if (address.isNotBlank() && !list.contains(address)) {
            list.add(address)
            setAddresses(context, list)
        }
    }

    fun removeAddress(context: Context, index: Int) {
        val list = getAddresses(context).toMutableList()
        if (index in list.indices) {
            list.removeAt(index)
            setAddresses(context, list)
        }
    }

    fun registerUser(context: Context, fullName: String, email: String, password: String) {
        val nameParts = fullName.trim().split("\\s+".toRegex())
        val first = nameParts.firstOrNull() ?: "Usuário"
        val last = if (nameParts.size > 1) {
            nameParts.drop(1).joinToString(" ")
        } else {
            ""
        }
        
        val editor = getPrefs(context).edit()
        editor.putString(KEY_FIRST_NAME, first)
        editor.putString(KEY_LAST_NAME, last)
        editor.putString(KEY_REG_EMAIL, email.trim())
        editor.putString(KEY_REG_PASSWORD, password)
        editor.apply()
    }

    fun getRegisteredEmail(context: Context): String? {
        return getPrefs(context).getString(KEY_REG_EMAIL, null)
    }

    fun authenticate(context: Context, email: String, password: String): Boolean {
        val savedEmail = getPrefs(context).getString(KEY_REG_EMAIL, null)
        val savedPassword = getPrefs(context).getString(KEY_REG_PASSWORD, null)
        

        if (savedEmail == null) {
            return email.trim() == "joao@example.com" && password == "123456"
        }
        
        return savedEmail.equals(email.trim(), ignoreCase = true) && savedPassword == password
    }

    fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            if (inputStream != null) {
                val directory = File(context.filesDir, "profile_photos")
                if (!directory.exists()) {
                    directory.mkdirs()
                }
                val file = File(directory, "profile_photo_${System.currentTimeMillis()}.jpg")
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
                

                getPhotoPath(context)?.let { oldPath ->
                    val oldFile = File(oldPath)
                    if (oldFile.exists()) {
                        oldFile.delete()
                    }
                }
                
                file.absolutePath
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
