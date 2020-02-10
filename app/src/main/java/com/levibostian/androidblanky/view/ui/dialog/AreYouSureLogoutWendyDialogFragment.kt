package com.levibostian.androidblanky.view.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.extensions.onAttachDiGraph
import com.levibostian.androidblanky.service.util.ConnectivityUtil
import com.levibostian.wendy.WendyConfig
import com.levibostian.wendy.listeners.TaskRunnerListener
import com.levibostian.wendy.service.PendingTask
import com.levibostian.wendy.service.Wendy
import com.levibostian.wendy.types.ReasonPendingTaskSkipped
import javax.inject.Inject

class AreYouSureLogoutWendyDialogFragment: DialogFragment(), TaskRunnerListener {

    companion object {
        fun getInstance(): AreYouSureLogoutWendyDialogFragment = AreYouSureLogoutWendyDialogFragment()
    }

    interface Listener {
        fun logout()
        fun cancel()
    }

    override fun onAttach(context: Context) {
        ((activity as? Listener) ?: (parentFragment as? Listener))?.let { listener = it }
        onAttachDiGraph().inject(this)
        super.onAttach(context)
    }

    @Inject lateinit var connectivityUtil: ConnectivityUtil

    private lateinit var listener: Listener
    private var dialogView: View? = null

    private var state: State? = null
        set(value) {
            field = value

            if (!isAdded) return

            when (value) {
                State.SYNCING -> {
                    dialogView?.apply {
                        findViewById<ImageView>(R.id.are_you_sure_done_imageview)?.visibility = View.GONE
                        findViewById<LinearLayout>(R.id.are_you_sure_progressbar)?.visibility = View.VISIBLE
                        findViewById<TextView>(R.id.are_you_sure_message)?.text = getString(R.string.are_you_sure_dialog_message)
                        findViewById<TextView>(R.id.are_you_sure_logout_anyway)?.visibility = View.VISIBLE
                    }
                }
                State.DONE_SYNCING -> {
                    dialogView?.apply {
                        findViewById<ImageView>(R.id.are_you_sure_done_imageview)?.setImageResource(R.drawable.ic_check_circle_green_24dp)
                        findViewById<ImageView>(R.id.are_you_sure_done_imageview)?.visibility = View.VISIBLE
                        findViewById<LinearLayout>(R.id.are_you_sure_progressbar)?.visibility = View.GONE
                        findViewById<TextView>(R.id.are_you_sure_message)?.text = getString(R.string.are_you_sure_data_safe_dialog_message)
                        findViewById<TextView>(R.id.are_you_sure_logout_anyway)?.visibility = View.GONE
                    }
                }
                State.NO_NETWORK_CONNECTION -> {
                    dialogView?.apply {
                        findViewById<ImageView>(R.id.are_you_sure_done_imageview)?.setImageResource(R.drawable.ic_error_red_24dp)
                        findViewById<ImageView>(R.id.are_you_sure_done_imageview)?.visibility = View.VISIBLE
                        findViewById<LinearLayout>(R.id.are_you_sure_progressbar)?.visibility = View.GONE
                        findViewById<TextView>(R.id.are_you_sure_message)?.text = getString(R.string.are_you_sure_no_network_connection)
                        findViewById<TextView>(R.id.are_you_sure_logout_anyway)?.visibility = View.VISIBLE
                    }
                }
                State.FINISH_SYNC_ERRORS -> {
                    dialogView?.apply {
                        findViewById<ImageView>(R.id.are_you_sure_done_imageview)?.setImageResource(R.drawable.ic_error_yellow_24dp)
                        findViewById<ImageView>(R.id.are_you_sure_done_imageview)?.visibility = View.VISIBLE
                        findViewById<LinearLayout>(R.id.are_you_sure_progressbar)?.visibility = View.GONE
                        findViewById<TextView>(R.id.are_you_sure_message)?.text = getString(R.string.are_you_sure_finished_sync_errors_message)
                        findViewById<TextView>(R.id.are_you_sure_logout_anyway)?.visibility = View.VISIBLE
                    }
                }
            }
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (Wendy.shared.getAllTasks().isEmpty()) { // If no tasks, just logout the user.
            listener.logout()
            this.dismiss()
        }

        dialogView = activity!!.layoutInflater.inflate(R.layout.dialog_fragment_are_you_sure_logout_wendy, null)
        refreshView()

        WendyConfig.addTaskRunnerListener(this)
        Wendy.shared.runTasks(null)

        return AlertDialog.Builder(activity!!)
                .setTitle(R.string.warning)
                .setView(dialogView)
                .setPositiveButton(R.string.log_out) { _, _ ->
                    listener.logout()
                    this.dismiss()
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                    listener.cancel()
                    this.dismiss()
                }
                .create()
    }

    private fun refreshView() {
        state = if (!connectivityUtil.isNetworkAvailable()) State.NO_NETWORK_CONNECTION
        else if (Wendy.shared.getAllTasks().isEmpty()) State.DONE_SYNCING else State.SYNCING
    }

    override fun allTasksComplete() {
        state = if (Wendy.shared.getAllTasks().isNotEmpty()) State.FINISH_SYNC_ERRORS else State.DONE_SYNCING
    }

    override fun errorRecorded(task: PendingTask, errorMessage: String?, errorId: String?) {
        refreshView()
    }

    override fun errorResolved(task: PendingTask) {
        refreshView()
    }

    override fun newTaskAdded(task: PendingTask) {
        refreshView()
    }

    override fun runningTask(task: PendingTask) {
        refreshView()
    }

    override fun taskComplete(success: Boolean, task: PendingTask) {
        refreshView()
    }

    override fun taskSkipped(reason: ReasonPendingTaskSkipped, task: PendingTask) {
        refreshView()
    }

    private enum class State {
        DONE_SYNCING,
        SYNCING,
        NO_NETWORK_CONNECTION,
        FINISH_SYNC_ERRORS
    }

}