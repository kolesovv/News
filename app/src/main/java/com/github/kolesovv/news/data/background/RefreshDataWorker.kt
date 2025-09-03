package com.github.kolesovv.news.data.background

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.kolesovv.news.domain.usecase.GetSettingsUseCase
import com.github.kolesovv.news.domain.usecase.UpdateArticlesForAllSubscriptionUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class RefreshDataWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val updateArticlesForAllSubscriptionUseCase: UpdateArticlesForAllSubscriptionUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(appContext = context, params = workerParameters) {

    override suspend fun doWork(): Result {
        Log.d("RefreshDataWorker", "Start refresh data")
        val settings = getSettingsUseCase().first()
        val updatedSubscriptions = updateArticlesForAllSubscriptionUseCase()

        if (updatedSubscriptions.isNotEmpty() && settings.notificationEnable) {
            notificationHelper.showNewArticlesNotification(updatedSubscriptions)
        }
        Log.d("RefreshDataWorker", "Finish refresh data")
        return Result.success()
    }
}
