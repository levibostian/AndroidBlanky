package com.levibostian.view.ui.dialog

import android.os.Bundle
import javax.inject.Inject
import android.content.Context
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_select_special_value.*

class SelectHighlightValueDialog : DialogFragment(), SelectHighlightOptionsRecyclerViewAdapter.Listener {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val workoutsViewModel by activityViewModels<WorkoutsViewModel> { viewModelFactory }
    @Inject lateinit var logger: Logger

    private val args: SelectHighlightValueDialogArgs by navArgs()

    override fun onAttach(context: Context) {
        onAttachDiGraph().inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_select_highlight_value, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        options_recyclerview.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = SelectHighlightOptionsRecyclerViewAdapter(requireActivity()).apply {
                listener = this@SelectHighlightValueDialog
            }
        }

        (options_recyclerview.adapter as SelectHighlightOptionsRecyclerViewAdapter).data = args.options.toList()
    }

    override fun highlightOptionSelected(option: String) {
        workoutsViewModel.highlightScheduleSelected(option)

        dismiss()
    }

}