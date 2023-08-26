import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.style.ForegroundColorSpan
import android.text.style.LineBackgroundSpan
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.text.SimpleDateFormat
import java.util.*

class AttendanceDecorator(
    private val attendanceType: String,
    private var color: Int,
    private val attendanceData: Map<String, String>,
    context: Context
) : DayViewDecorator {

    private val calendar = Calendar.getInstance()
    init {
        this.color = ContextCompat.getColor(context,color)
    }
    override fun shouldDecorate(day: CalendarDay): Boolean {
        calendar.time = day.date
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateStr = sdf.format(calendar.time)
        return attendanceData[dateStr] == attendanceType
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(object : LineBackgroundSpan {
            override fun drawBackground(
                canvas: Canvas, paint: Paint,
                left: Int, right: Int, top: Int, baseline: Int, bottom: Int,
                charSequence: CharSequence,
                start: Int, end: Int, lineNum: Int
            ) {
                val oldColor = paint.color
                paint.color = color
                paint.setShadowLayer(5f, 0f, 2f, Color.argb(50, 0, 0, 0))
                val radius = Math.min((right - left), (bottom - top)) / 0.9f  // Adjust this divisor to control the circle's size
                canvas.drawCircle(
                    ((left + right) / 2).toFloat(),
                    ((top + bottom) / 2).toFloat(),
                    radius,
                    paint
                )
                paint.color = oldColor

            }
        })
    }
}

class DefaultTextColorDecorator(private val context: Context) : DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return true
    }

    override fun decorate(view: DayViewFacade) {
        val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        var color = when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_YES -> Color.WHITE
            Configuration.UI_MODE_NIGHT_NO -> Color.BLACK
            else -> Color.BLACK
        }

        view.addSpan(ForegroundColorSpan(color))
    }
}

