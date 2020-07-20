package com.app.view.ui.dialog

/**
import android.os.Bundle
import javax.inject.Inject
import android.content.Context
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager

class ExampleDialog : DialogFragment(), FooRecyclerViewAdapter.Listener {

 @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
 private val fooViewModel by activityViewModels<FooViewModel> { viewModelFactory }

 private val args: ExampleDialogArgs by navArgs()

 override fun onAttach(context: Context) {
 onAttachDiGraph().inject(this)
 super.onAttach(context)
 }

 override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
 return inflater.inflate(R.layout.fragment_dialog_foo, container, false)
 }

 override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
 super.onViewCreated(view, savedInstanceState)

 options_recyclerview.apply {
 layoutManager = LinearLayoutManager(activity)
 adapter = FooRecyclerViewAdapter(requireActivity()).apply {
 listener = this@ExampleDialog
 }
 }

 (options_recyclerview.adapter as FooRecyclerViewAdapter).data = args.options.toList()
 }

 override fun highlightOptionSelected(option: String) {
 fooViewModel.valueSelected(option)

 dismiss()
 }

}
 */
