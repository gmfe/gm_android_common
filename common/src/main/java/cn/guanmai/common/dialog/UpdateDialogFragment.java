package cn.guanmai.common.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.pgyersdk.update.javabean.AppBean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class UpdateDialogFragment extends DialogFragment {
    private UpdateDialogListener mListener;
    private AppBean mAppBean;

    public UpdateDialogFragment() {
    }

    @SuppressLint("ValidFragment")
    public UpdateDialogFragment(AppBean appBean, UpdateDialogListener listener) {
        mListener = listener;
        this.mAppBean = appBean;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("更新提示")
                .setMessage("版本\n" + mAppBean.getVersionName() + "\n" + mAppBean.getReleaseNote())
                .setPositiveButton("安装更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mListener != null) {
                            mListener.onUpdate(mAppBean);
                        }
                    }
                })
                .setNegativeButton("暂不", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mListener != null) {
                            mListener.onCancel();
                        }
                    }
                });
        return builder.create();
    }

    public interface UpdateDialogListener {
        void onUpdate(AppBean appBean);
        void onCancel();
    }
}
