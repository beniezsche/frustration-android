package i.am.frustrated.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import i.am.frustrated.R
import kotlinx.android.synthetic.main.fragment_bottomsheet_mood_selector.*

class MoodSelectorBottomSheet(private var moodSelectorBottomSheetListener: MoodSelectorBottomSheetListener) :
    BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottomsheet_mood_selector,container,false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        ll__mood_all.setOnClickListener {
            moodSelectorBottomSheetListener.onClick(resources.getInteger(R.integer.mood_filter_all))
            dismiss()
        }

        ll_mood_sad.setOnClickListener {
            moodSelectorBottomSheetListener.onClick(resources.getInteger(R.integer.mood_filter_sad))
            dismiss()
        }

        ll_mood_ennui.setOnClickListener {
            moodSelectorBottomSheetListener.onClick(resources.getInteger(R.integer.mood_filter_ennui))
            dismiss()
        }

        ll_mood_happy.setOnClickListener {
            moodSelectorBottomSheetListener.onClick(resources.getInteger(R.integer.mood_filter_happy))
            dismiss()
        }

        ll_mood_raging.setOnClickListener {
            moodSelectorBottomSheetListener.onClick(resources.getInteger(R.integer.mood_filter_raging))
            dismiss()
        }

    }


    interface MoodSelectorBottomSheetListener {

        fun onClick(mood: Int)

    }
}