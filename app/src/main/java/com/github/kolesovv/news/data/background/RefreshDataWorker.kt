package com.github.kolesovv.news.data.background

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.kolesovv.news.domain.usecase.UpdateArticlesForAllSubscriptionUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class RefreshDataWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val updateArticlesForAllSubscriptionUseCase: UpdateArticlesForAllSubscriptionUseCase
) : CoroutineWorker(appContext = context, params = workerParameters) {

    override suspend fun doWork(): Result {
        Log.d("RefreshDataWorker", "Start refresh data")
        updateArticlesForAllSubscriptionUseCase()
        Log.d("RefreshDataWorker", "Finish refresh data")
        return Result.success()
    }
}
