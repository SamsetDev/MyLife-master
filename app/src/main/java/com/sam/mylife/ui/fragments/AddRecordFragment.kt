package com.sam.mylife.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.SetOptions
import com.sam.mylife.R
import com.sam.mylife.core.model.MonthItemModel
import com.sam.mylife.databinding.FragmentAddRecordBinding
import com.sam.mylife.ui.adapter.AutoSuggestAdapter
import com.sam.mylife.ui.adapter.MonthSpinAdapter
import com.sam.mylife.ui.base.BaseBottomSheetFragment
import com.sam.mylife.utils.AppConstants.BASE_USER_ID
import com.sam.mylife.utils.AppConstants.COLLECTION_ITEMS
import com.sam.mylife.utils.CommonUtils.getCurrentDateTime
import com.sam.mylife.utils.FirebaseUtils
import com.sam.mylife.utils.ext.hide
import com.sam.mylife.utils.listeners.OnClickItemListeners
import java.util.Calendar
import java.util.Locale


class AddRecordFragment :
    BaseBottomSheetFragment<FragmentAddRecordBinding>(FragmentAddRecordBinding::inflate) {

    private lateinit var month: String
    private  lateinit var searchList:ArrayList<String>
    private lateinit var token :String
    private var currentMonth:Int=0
    var currentYear: Int = Calendar.getInstance()[Calendar.YEAR]
    private val TAG: String=AddRecordFragment::class.java.simpleName

    override fun initView() {

        if (arguments!=null){
            val id=arguments?.getString("id").toString()
            val item=arguments?.getString("item").toString()
            val date=arguments?.getString("date").toString()
            val month=arguments?.getString("month").toString()
            val price=arguments?.getString("price").toString()
            token=arguments?.getString("token").toString()
            val docId=arguments?.getString("docId").toString()

            binding.etItem.setText(item)
            binding.etDate.setText(date)
            currentMonth = resources.getStringArray(R.array.month).indexOf(month)
            //binding.fabAddstory.text=getString(R.string.update_record)

        }else{
            currentMonth = Calendar.getInstance().get(Calendar.MONTH)
            binding.etDate.setText(getCurrentDateTime())
            token=getPrefmanager().userData?.token.toString()
           // binding.fabAddstory.text=getString(R.string.add_record)
        }


        binding.fabAddstory.setOnClickListener {
            if (validate()) {
                addItemToMonth()
            }
        }

        setupSpinnerAdapter()

    }


    fun addItemToMonth() {
        val date = binding.etDate.getText().toString()
        val item = binding.etItem.getText().toString()

        if (!getPrefmanager().userData?.token.isNullOrEmpty()) {
            binding.recordProgress.visibility=View.VISIBLE

            //generateNewItemId { newItemId ->   //for our custom id generate

                val itemCollection = FirebaseUtils.fireStoreDb.collection(currentYear.toString()).document(month)
                val itemData = MonthItemModel(getFirebaseUserId(), date, item, month,token)

                val temp = hashMapOf("itemId" to month)
                itemCollection.set(temp,SetOptions.merge())

                val newRef=itemCollection.collection(COLLECTION_ITEMS)
               // val newItemRef = newRef.document(newItemId.toString())

                newRef.add(itemData).addOnCompleteListenerWithFeedback()
           // }
        }
    }

    private fun Task<Void>.addSuccessListener()  // use with document data
    {
        addOnSuccessListener {
            context?.let { safeContext ->
                binding.recordProgress.hide()
                Toast.makeText(safeContext, getString(R.string.record_added_successfully), Toast.LENGTH_SHORT).show()
                dialog?.dismiss()
            }
        }.addOnFailureListener {
            context?.let { safeContext ->
                binding.recordProgress.hide()
                Toast.makeText(safeContext, getString(R.string.something_went_error), Toast.LENGTH_SHORT).show()
                dialog?.dismiss()
            }
        }
    }

    private fun <T> Task<T>.addOnCompleteListenerWithFeedback() // use with collection data
    {
        this.addOnSuccessListener {
            context?.let { safeContext ->
                binding.recordProgress.hide()
                Toast.makeText(safeContext, safeContext.getString(R.string.record_added_successfully), Toast.LENGTH_SHORT).show()
                dialog?.dismiss()
                Log.e(TAG, "Success: addOnCompleteListenerWithFeedback ")
            }
        }.addOnFailureListener { exception ->
            context?.let { safeContext ->
                binding.recordProgress.hide()
                Toast.makeText(safeContext, safeContext.getString(R.string.something_went_error), Toast.LENGTH_SHORT).show()
                dialog?.dismiss()
            }
            Log.e(TAG, "Error: addOnCompleteListenerWithFeedback ${exception.message}")
        }
    }



    private fun generateNewItemId(callback: (Long) -> Unit) {
        val itemsCollection = FirebaseUtils.fireStoreDb.collection(currentYear.toString()).document(month).collection(COLLECTION_ITEMS)
        itemsCollection.get().addOnSuccessListener { result ->
            if (result.isEmpty) callback(BASE_USER_ID.toLong())
            for (doc in result){
                val itemId = doc.id
                val newItemId = itemId.toInt() + 1
                callback(newItemId.toLong())
            }
        }
    }

    private fun setupSpinnerAdapter() {
        val months = resources.getStringArray(R.array.month)
        val adaptermonth = MonthSpinAdapter(requireContext(), months)
        binding.spinMonth.adapter = adaptermonth
        binding.spinMonth.setSelection(currentMonth)
        binding.spinMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.e(" tag","  Spinner un selected ")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                month =months[position].toString()
            }

        }

    }

    private fun validate(): Boolean {
        val date = binding.etDate.getText().toString()
        val item = binding.etItem.getText().toString()

        if (getFirebaseUserId().isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please again login", Toast.LENGTH_SHORT).show()
            return false
        } else if (date.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.empty_date), Toast.LENGTH_SHORT).show()
            return false
        } else if (date.isNotEmpty() && month.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.enter_month), Toast.LENGTH_SHORT).show()
            return false
        } else if (date.isNotEmpty() && month.isNotEmpty() && item.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.empty_item), Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout = bottomSheetDialog.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            )
            parentLayout?.let { bottomSheet ->
                val behaviour = BottomSheetBehavior.from(bottomSheet)
                val layoutParams = bottomSheet.layoutParams
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                bottomSheet.layoutParams = layoutParams
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog

    }


}