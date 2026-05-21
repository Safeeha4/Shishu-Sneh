package com.shishusneh.app.data.remote.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shishusneh.app.data.local.entities.*
import kotlinx.coroutines.tasks.await

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val userId: String? get() = auth.currentUser?.uid

    suspend fun backupBabyData(
        baby: BabyProfile,
        growthEntries: List<GrowthEntry>,
        vaccines: List<VaccineRecord>,
        feedingSessions: List<FeedingSession>
    ): Boolean {
        val uid = userId ?: return false
        
        return try {
            val userRef = db.collection("users").document(uid)
            
            // 1. Backup Baby Profile
            userRef.collection("babies").document(baby.id).set(baby).await()
            
            // 2. Backup Growth Entries
            val growthRef = userRef.collection("babies").document(baby.id).collection("growth_entries")
            growthEntries.forEach { entry ->
                growthRef.document(entry.id).set(entry).await()
            }
            
            // 3. Backup Vaccines
            val vaccineRef = userRef.collection("babies").document(baby.id).collection("vaccine_records")
            vaccines.forEach { vaccine ->
                vaccineRef.document(vaccine.id).set(vaccine).await()
            }

            // 4. Backup Feeding Sessions
            val feedingRef = userRef.collection("babies").document(baby.id).collection("feeding_sessions")
            feedingSessions.forEach { session ->
                feedingRef.document(session.id).set(session).await()
            }
            
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
