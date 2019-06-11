package com.dmrjkj.remotecontroller.dialog;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.view.View;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmrjkj.remotecontroller.R;
import com.dmrjkj.remotecontroller.adapter.BaseListItemAdapter;
import com.dmrjkj.remotecontroller.entity.BaseListItem;
import com.dmrjkj.remotecontroller.utils.ToolUtils;

import java.util.List;

import rx.functions.Action2;

/**
 * Created by xinchen on 18-11-12.
 */

public class DialogUtils {

    public static MaterialDialog showLoadingDialog(Context context) {
        return showLoadingDialog(context, null);
    }

    public static MaterialDialog showLoadingDialog(Context context, String content) {
        if (content == null) {
            content = "Please wait while loading!";
        }
        return new MaterialDialog.Builder(context)// 标题
                .title("")
                .titleGravity(GravityEnum.CENTER)
                .contentGravity(GravityEnum.CENTER)
                .content(content)// 内容
                .progress(true, 0)// 不确定进度条
                .canceledOnTouchOutside(false).build();
    }

    public static MaterialDialog showWarnConfirmDialog(Context context, String content, MaterialDialog.SingleButtonCallback listener) {
        return new MaterialDialog.Builder(context)
                .title("Warm prompt")
                .content(content)
                .positiveText("OK")
                .positiveColor(context.getResources().getColor(R.color.main))
                .positiveColor(context.getResources().getColor(R.color.warn))
                .onPositive(listener)
                .neutralText("Cancel")
                .build();
    }

    public static MaterialDialog showAdapterDialogWithPositiveBtn(Context context, String title, List<? extends BaseListItem> baseListItems, final BaseQuickAdapter.OnItemClickListener onItemClickListener) {
        BaseListItemAdapter adapter = new BaseListItemAdapter<>(baseListItems);
        final MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(title)
                .adapter(adapter, null)
                .positiveText("Cancel")
                .show();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                dialog.cancel();
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(adapter, view, position);
                }
            }
        });
        return dialog;
    }

    public static MaterialDialog showAdapterDialog(Context context, String title, List<? extends BaseListItem> baseListItems, final BaseQuickAdapter.OnItemClickListener onItemClickListener) {
        return showAdapterDialog(context, title, baseListItems, true, onItemClickListener);
    }

    //展示列表项的dialog
    public static MaterialDialog showAdapterDialog(Context context, String title, List<? extends BaseListItem> baseListItems, final boolean cancle, final BaseQuickAdapter.OnItemClickListener onItemClickListener) {
        BaseListItemAdapter adapter = new BaseListItemAdapter<>(baseListItems);
        final MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(title)
                .titleGravity(GravityEnum.CENTER)
                .adapter(adapter, null)
                .show();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (cancle) {
                    dialog.dismiss();
                }
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(adapter, view, position);
                }
            }
        });
        return dialog;
    }

    /**
     * 输入对话框
     */
    public static void showInputDialog(Activity context, String title, String content, String preFill, int inputType, int minLength, int maxLength, MaterialDialog.InputCallback inputCallback) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!ToolUtils.isEmpty(content)) {
            stringBuilder.append(content);
        }
        new MaterialDialog.Builder(context)
                .title(title)
                .content(stringBuilder.toString())
                .input("", preFill, false, inputCallback)
                .inputType(inputType)
                .inputRangeRes(minLength, maxLength, R.color.link)
                .show();
    }

    public static void showInputDialog(Activity context, String title, String content, int inputType, int minLength, int maxLength, MaterialDialog.InputCallback inputCallback) {
        showInputDialog(context, title, content, "", inputType, minLength, maxLength, inputCallback);
    }

    public static void showInputDialog(Activity context, String title, String content,  String preFill, int minLength, int maxLength, MaterialDialog.InputCallback inputCallback) {
        showInputDialog(context, title, content, preFill, InputType.TYPE_CLASS_TEXT, minLength, maxLength, inputCallback);
    }

    public static void showInputDialog(Activity context, String title, String content,  String preFill, MaterialDialog.InputCallback inputCallback) {
        showInputDialog(context, title, content, preFill, 0, -1, inputCallback);
    }

    /**
     * 普通的dialog
     * @param context
     * @param title
     * @return
     */
    public static MaterialDialog showListAdapterDialog(Context context, String title, List<String> items, final Action2<Integer, String> callback) {
        final MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(title)
                .titleGravity(GravityEnum.CENTER)
                .items(items.toArray(new String[]{}))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        if (callback != null) {
                            callback.call(position, text.toString());
                        }
                    }
                })
                .show();
        return dialog;
    }


    /**
     * 单选对话框
     */
    public static MaterialDialog showSinglerChoiceDialog(Context context, String title, int choiceIndex, List<String> items, final Action2<Integer, String> callback) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(title)
                .titleGravity(GravityEnum.CENTER)
                .items(items)
                .itemsCallbackSingleChoice(choiceIndex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        if (callback != null) {
                            callback.call(which, text.toString());
                        }
                        return true;
                    }
                })
                .alwaysCallSingleChoiceCallback()
                .show();
        return dialog;
    }
}
