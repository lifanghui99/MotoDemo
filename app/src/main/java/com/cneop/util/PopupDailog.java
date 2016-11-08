package com.cneop.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

public class PopupDailog {
	private Context context;
	private PopupWindow mPopupWindow;

	public PopupDailog(final Context context) {
		this.context = context;
	}

	public void dimissPopup() {
		if (this.mPopupWindow != null && this.mPopupWindow.isShowing()) {
			this.mPopupWindow.dismiss();
		}
	}

	public void showPopuptWindow(final View view, final View view2) {
		(this.mPopupWindow = new PopupWindow(view,
				((Activity) this.context).getWindowManager().getDefaultDisplay().getWidth(),
				((Activity) this.context).getWindowManager().getDefaultDisplay().getHeight())).setFocusable(true);
		this.mPopupWindow.setOutsideTouchable(true);
		this.mPopupWindow.update();
		this.mPopupWindow.setBackgroundDrawable((Drawable) new BitmapDrawable());
		this.mPopupWindow.showAtLocation(view2, Gravity.CENTER, 0, 0);

	}
}
