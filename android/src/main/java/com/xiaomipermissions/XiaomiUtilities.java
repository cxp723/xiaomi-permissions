package com.xiaomipermissions;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;

public class XiaomiUtilities {

	// custom permissions
	public static final int OP_ACCESS_XIAOMI_ACCOUNT = 10015;
	public static final int OP_AUTO_START = 10008;
	public static final int OP_BACKGROUND_START_ACTIVITY = 10021;
	public static final int OP_BLUETOOTH_CHANGE = 10002;
	public static final int OP_BOOT_COMPLETED = 10007;
	public static final int OP_DATA_CONNECT_CHANGE = 10003;
	public static final int OP_DELETE_CALL_LOG = 10013;
	public static final int OP_DELETE_CONTACTS = 10012;
	public static final int OP_DELETE_MMS = 10011;
	public static final int OP_DELETE_SMS = 10010;
	public static final int OP_EXACT_ALARM = 10014;
	public static final int OP_GET_INSTALLED_APPS = 10022;
	public static final int OP_GET_TASKS = 10019;
	public static final int OP_INSTALL_SHORTCUT = 10017;
	public static final int OP_NFC = 10016;
	public static final int OP_NFC_CHANGE = 10009;
	public static final int OP_READ_MMS = 10005;
	public static final int OP_READ_NOTIFICATION_SMS = 10018;
	public static final int OP_SEND_MMS = 10004;
	public static final int OP_SERVICE_FOREGROUND = 10023;
	public static final int OP_SHOW_WHEN_LOCKED = 10020;
	public static final int OP_WIFI_CHANGE = 10001;
	public static final int OP_WRITE_MMS = 10006;

	public static boolean isMIUI() {
		return !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name"));
	}

	@SuppressLint("PrivateApi")
	private static String getSystemProperty(String key) {
		try {
			Class props = Class.forName("android.os.SystemProperties");
			return (String) props.getMethod("get", String.class).invoke(null, key);
		} catch (Exception ignore) {
		}
		return null;
	}

	@SuppressWarnings("JavaReflectionMemberAccess")
	@TargetApi(19)
	public static boolean isCustomPermissionGranted(Context context, int permission) {
		try {
			AppOpsManager mgr = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
			Method m = AppOpsManager.class.getMethod("checkOpNoThrow", int.class, int.class, String.class);
			int result = (int) m.invoke(mgr, permission, android.os.Process.myUid(), context.getPackageName());
			return result == AppOpsManager.MODE_ALLOWED;
		} catch (Exception e) {
		  Log.d("XiaomiUtils","Cannot open Miui Display Pop-up window while running in background" + e.toString());
		}
		return true;
	}

  private static boolean isIntentAvailable(Intent intent, Context context) {
     return intent != null && context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
  }

	public static void goToMiuiPermissionActivity_V8(Context context) {
      Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
      intent.putExtra("extra_package_uid", android.os.Process.myUid());
      intent.putExtra("extra_pkgname", context.getPackageName());
      intent.putExtra("extra_package_name", context.getPackageName());
      intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

      if (isIntentAvailable(intent, context)) {
          context.startActivity(intent);
      } else {
              Log.e("XiaomiUtils", "Intent is not available!");
      }
  }
}
