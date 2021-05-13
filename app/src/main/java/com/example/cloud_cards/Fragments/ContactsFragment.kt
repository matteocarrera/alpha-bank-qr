package com.example.cloud_cards.Fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cloud_cards.Activities.MainActivity
import com.example.cloud_cards.Adapters.ContactsAdapter
import com.example.cloud_cards.Adapters.RecyclerItemClickListener
import com.example.cloud_cards.Adapters.SelectedContactsAdapter
import com.example.cloud_cards.Database.AppDatabase
import com.example.cloud_cards.Entities.User
import com.example.cloud_cards.R
import com.example.cloud_cards.Utils.ProgramUtils
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.selected_saved_card_list_item.view.*
import net.glxn.qrgen.android.QRCode

class ContactsFragment : Fragment() {

    private lateinit var db : AppDatabase
    private var multipleSelectionMode = false
    private val selectedItems = ArrayList<String>()
    private var actionMode : ActionMode? = null
    private var users = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)
        val toolbar = view.findViewById(R.id.contacts_toolbar) as MaterialToolbar
        toolbar.inflateMenu(R.menu.main_menu)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.sort -> {
                    Toast.makeText(requireContext(), "SORT", Toast.LENGTH_SHORT).show()
                }
                R.id.search -> {
                    Toast.makeText(requireContext(), "SEARCH", Toast.LENGTH_SHORT).show()
                }
                R.id.camera -> {
                    Toast.makeText(requireContext(), "CAMERA", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        return view
    }

   /*  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getInstance(requireContext())

        setUsersToList {
            contact_list.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = ContactsAdapter(it)
            }
        }

        contact_list.addOnItemTouchListener(
            RecyclerItemClickListener(context, contact_list, object :
                RecyclerItemClickListener.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    if (multipleSelectionMode) {
                        view.checkbox.isChecked = !view.checkbox.isChecked
                        val contactId = view.contact_id.text.toString()
                        if (selectedItems.contains(contactId)) selectedItems.remove(contactId)
                        else selectedItems.add(contactId)
                    } else {
                        val cardViewFragment = CardViewFragment.newInstance(view.contact_id.text.toString())
                        val tx: FragmentTransaction = requireParentFragment().parentFragmentManager.beginTransaction()
                        tx.replace(R.id.nav_host_fragment, cardViewFragment).addToBackStack(null).commit()
                    }
                }

                override fun onLongItemClick(view: View, position: Int) {
                    if (!multipleSelectionMode) {
                        setUsersToList {
                            contact_list.adapter = null
                            contact_list.adapter = SelectedContactsAdapter(it)
                        }
                        multipleSelectionMode = true
                        selectedItems.clear()

                        setStandardToolbarVisibility(View.GONE)

                        actionMode = (parentFragment?.requireActivity() as AppCompatActivity).startSupportActionMode(actionModeCallback)
                    }
                }
            })
        )
    }
    */

    /* private fun setUsersToList(callback: (list: List<User>) -> Unit){
        users.clear()
        users = ArrayList(db.userDao().getScannedUsers())

        val list : MutableList<User> = mutableListOf()

        users.forEach {
            val databaseRef = FirebaseDatabase.getInstance().getReference(it.id)
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val jsonUser = dataSnapshot.value.toString()
                    val userFromDB = Gson().fromJson(jsonUser, User::class.java)
                    list.add(userFromDB)
                    callback(list)
                    //if (userFromDB.toString() != it.toString()) db.userDao().updateUser(userFromDB)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    println("Ошибка считывания: " + databaseError.code)
                }
            })
        }
    }

     */

    /* private val actionModeCallback: ActionMode.Callback = object : ActionMode.Callback {
        override fun onActionItemClicked(
            mode: ActionMode?,
            item: MenuItem?
        ): Boolean {
            if (item != null) {
                if (item.itemId == R.id.share) {
                    shareCards()
                    onDestroyActionMode(mode)
                }
                else if (item.itemId == R.id.delete) {
                    deleteCards()
                    onDestroyActionMode(mode)
                }
                return true
            }
            return false
        }

        override fun onCreateActionMode(
            mode: ActionMode?,
            menu: Menu?
        ): Boolean {
            mode?.menuInflater?.inflate(R.menu.selection_menu, menu)
            mode?.title = "Выберите визитки"
            setNavEnabled(false)
            return true
        }

        override fun onPrepareActionMode(
            mode: ActionMode?,
            menu: Menu?
        ): Boolean {
            return false
        }

        /* override fun onDestroyActionMode(mode: ActionMode?) {
            actionMode = null
            mode?.finish()
            setStandardToolbarVisibility(View.VISIBLE)

            contact_list.adapter = null
            setUsersToList {
                contact_list.adapter = ContactsAdapter(it)
            }

            setNavEnabled(true)
            multipleSelectionMode = false
        }

         */
    }
    */

    private fun shareCards() {
        val qrList = ArrayList<Bitmap>()
        if (selectedItems.count() == 0) Toast.makeText(requireContext(), "Вы не выбрали ни одной визитки!", Toast.LENGTH_SHORT).show()
        else {
            selectedItems.forEach {
                var bitmap = QRCode.from(it).withCharset("utf-8").withSize(1000, 1000).bitmap()
                bitmap = Bitmap.createScaledBitmap(bitmap, 1000, 1000, true)
                qrList.add(bitmap)
            }
            ProgramUtils.saveImage(requireContext(), qrList)
        }
    }

    /* private fun deleteCards() {
        if (selectedItems.count() == 0) Toast.makeText(requireContext(), "Вы не выбрали ни одной визитки!", Toast.LENGTH_SHORT).show()
        else {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Удаление визиток")
            builder.setMessage("Вы действительно хотите удалить выбранные визитки?")
            builder.setPositiveButton("Да"){ _, _ ->
                selectedItems.forEach {
                    val user = db.userDao().getUserById(it)
                    db.userDao().deleteUser(user)
                }
                Toast.makeText(requireContext(), "Выбранные визитки успешно удалены!", Toast.LENGTH_SHORT).show()
                selectedItems.clear()
                contact_list.adapter = null
                setUsersToList {
                    contact_list.adapter = ContactsAdapter(it)
                }
            }
            builder.setNegativeButton("Нет"){ _, _ -> }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

     */

    companion object {
        @JvmStatic
        fun newInstance() =
            ContactsFragment()
    }
}