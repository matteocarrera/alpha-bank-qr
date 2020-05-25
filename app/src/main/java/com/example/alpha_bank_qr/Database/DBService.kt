package com.example.alpha_bank_qr.Database

import android.content.Context
import android.graphics.Bitmap
import com.example.alpha_bank_qr.Entities.User
import com.example.alpha_bank_qr.Utils.ImageUtils
import com.example.alpha_bank_qr.Utils.Json
import net.glxn.qrgen.android.QRCode

class DBService {
    companion object {
        fun addUser(context: Context, user: User) {
            val dbHelper = QRDatabaseHelper(context)
            dbHelper.addUser(user)
            dbHelper.close()
        }

        fun getUserById(context: Context, id : Int) : User {
            val dbHelper = QRDatabaseHelper(context)
            val cursor = dbHelper.getUser(id)
            val user = User()
            if (cursor!!.count != 0) {
                cursor.moveToFirst()
                user.id = cursor.getInt(cursor.getColumnIndex("id"))
                user.photo = cursor.getString(cursor.getColumnIndex("photo"))
                user.name = cursor.getString(cursor.getColumnIndex("name"))
                user.surname = cursor.getString(cursor.getColumnIndex("surname"))
                user.patronymic = cursor.getString(cursor.getColumnIndex("patronymic"))
                user.company = cursor.getString(cursor.getColumnIndex("company"))
                user.jobTitle = cursor.getString(cursor.getColumnIndex("job_title"))
                user.mobile = cursor.getString(cursor.getColumnIndex("mobile"))
                user.mobileSecond = cursor.getString(cursor.getColumnIndex("mobile_second"))
                user.email = cursor.getString(cursor.getColumnIndex("email"))
                user.emailSecond = cursor.getString(cursor.getColumnIndex("email_second"))
                user.address = cursor.getString(cursor.getColumnIndex("address"))
                user.addressSecond = cursor.getString(cursor.getColumnIndex("address_second"))
                user.sberbank = cursor.getString(cursor.getColumnIndex("sberbank"))
                user.vtb = cursor.getString(cursor.getColumnIndex("vtb"))
                user.alfabank = cursor.getString(cursor.getColumnIndex("alfabank"))
                user.vk = cursor.getString(cursor.getColumnIndex("vk"))
                user.facebook = cursor.getString(cursor.getColumnIndex("facebook"))
                user.instagram = cursor.getString(cursor.getColumnIndex("instagram"))
                user.twitter = cursor.getString(cursor.getColumnIndex("twitter"))
                user.notes = cursor.getString(cursor.getColumnIndex("notes"))
            }
            dbHelper.close()
            return user
        }

        fun getScannedUsers(context: Context) : ArrayList<User>? {
            val qrList = ArrayList<User>()
            val dbHelper = QRDatabaseHelper(context)
            val cursor = dbHelper.getScannedUsers()
            if (cursor!!.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    val user = getUserById(context, cursor.getInt(cursor.getColumnIndex("id")))
                    qrList.add(user)
                    cursor.moveToNext()
                }
            }
            dbHelper.close()
            return qrList
        }

        fun deleteUser(context: Context, id : Int) {
            val dbHelper = QRDatabaseHelper(context)
            dbHelper.deleteUser(id)
            dbHelper.close()
        }
    }
}