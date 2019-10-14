package cn.guanmai.common.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import cn.guanmai.common.R;

public class RebootDialogFragment extends DialogFragment {
    private RebootDialogListener mListener;

    public RebootDialogFragment() {
    }

    @SuppressLint("ValidFragment")
    public RebootDialogFragment(RebootDialogListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.tip))
                .setMessage(getString(R.string.rebootMsg))
                .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mListener != null) {
                            mListener.onReboot();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mListener != null) {
                            mListener.onCancel();
                        }
                    }
                });
        return builder.create();
    }

    public interface RebootDialogListener {
        void onReboot();
        void onCancel();
    }
}
