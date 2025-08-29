import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Locale

object DateFormater {

    @SuppressLint("ConstantLocale")
    val dateFormat = SimpleDateFormat("d MMM, yyyy", Locale.getDefault())

    fun formatDateToString(timestamp: Long): String {

        return dateFormat.format(timestamp)
    }
}