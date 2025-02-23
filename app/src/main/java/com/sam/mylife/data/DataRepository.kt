package com.sam.mylife.data

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.sam.mylife.core.model.HomeResponse
import com.sam.mylife.core.model.MonthItemModel
import com.sam.mylife.utils.AppConstants
import com.sam.mylife.utils.AppConstants.DATE_KEY
import com.sam.mylife.utils.AppConstants.ITEM_KEY
import com.sam.mylife.utils.AppConstants.MONTH_KEY
import com.sam.mylife.utils.AppConstants.PRICE_KEY
import com.sam.mylife.utils.AppConstants.TOKEN_KEY
import com.sam.mylife.utils.CommonUtils.getMonthList
import com.sam.mylife.utils.FirebaseUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class DataRepository {

    suspend fun getUserYearData(year: String): Flow<ArrayList<HomeResponse>> = flow {
        try {
            val userDocRef = FirebaseUtils.fireStoreDb.collection(year)
            val monthsList = userDocRef.get().await().documents

            if (monthsList.isEmpty()) {
                emit(ArrayList())
                return@flow
            }

            Log.d("TAG", "Found ${monthsList.size} months.")

            // Sort the monthsList based on the correct month order
            val sortedMonthsList = monthsList.sortedBy { monthDoc ->
                getMonthList().indexOf(monthDoc.id.trim())  // Normalize the month string
            }

            val monthListItems = ArrayList<HomeResponse>()
            sortedMonthsList.forEach { monthDoc ->
                val monthId = monthDoc.id
                Log.d("TAG", "Processing month: $monthId")

                val itemsList = getItemsForMonth(monthDoc)
                if (itemsList.isEmpty()) return@forEach

                val monthItems = itemsList.map { itemDoc ->
                    val itemData = itemDoc.data ?: emptyMap<String, Any>()

                    // Create and return a MonthItemModel object
                    MonthItemModel(
                        id = itemDoc.id,
                        date = itemData[DATE_KEY] as? String ?: "",
                        item = itemData[ITEM_KEY] as? String ?: "",
                        month = itemData[MONTH_KEY] as? String ?: "",
                        token = itemData[TOKEN_KEY] as? String ?: "",
                    )
                }

                // Creating a HomeResponse object for each month
                val homeResult = HomeResponse(
                    month = monthId,
                    type = 1,
                    isExpanded = false,
                    list = monthItems
                )

                monthListItems.add(homeResult)
            }

            emit(monthListItems)

        } catch (e: Exception) {
            Log.e("TAG", "Error fetching year data: ${e.message}")
            emit(ArrayList())
        }
    }

    private suspend fun getItemsForMonth(monthDoc: DocumentSnapshot): List<DocumentSnapshot> {
        return try {
            val itemsCollectionRef = monthDoc.reference.collection(AppConstants.COLLECTION_ITEMS)
            val itemsSnapshot = itemsCollectionRef.get().await()
            itemsSnapshot.documents
        } catch (e: Exception) {
            Log.e("TAG", "Error fetching items for month ${monthDoc.id}: ${e.message}")
            emptyList()
        }
    }
}
