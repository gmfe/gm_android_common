package cn.guanmai.common.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.blankj.utilcode.util.SPUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import cn.guanmai.common.R;
import cn.guanmai.common.view.X5WebViewActivity;

public class EnvDialogFragment extends DialogFragment {
    private EnvDialogListener mListener;
    private EditText mEditText;

    public EnvDialogFragment() {
    }

    @SuppressLint("ValidFragment")
    public EnvDialogFragment(EnvDialogListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_env, null);
        mEditText = view.findViewById(R.id.env);
        mEditText.setText(SPUtils.getInstance().getString(X5WebViewActivity.HOME_PAGE_KEY, ""));
        builder.setTitle("修改测试环境")
                .setView(view)
                .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        if (mListener != null) {
                            mListener.onSubmit(mEditText.getText().toString());
                        }
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        if (mListener != null) {
                            mListener.onCancel();
                        }
                    }
                });
        return builder.create();
    }

    public interface EnvDialogListener {
        void onSubmit(String url);
        void onCancel();
    }
}
