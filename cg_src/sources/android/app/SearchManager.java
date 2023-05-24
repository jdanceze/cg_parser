package android.app;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.os.Bundle;
import java.util.List;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/SearchManager.class */
public class SearchManager implements DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {
    public static final char MENU_KEY = 's';
    public static final int MENU_KEYCODE = 47;
    public static final String QUERY = "query";
    public static final String USER_QUERY = "user_query";
    public static final String APP_DATA = "app_data";
    public static final String ACTION_KEY = "action_key";
    public static final String EXTRA_DATA_KEY = "intent_extra_data_key";
    public static final String EXTRA_SELECT_QUERY = "select_query";
    public static final String EXTRA_NEW_SEARCH = "new_search";
    public static final String EXTRA_WEB_SEARCH_PENDINGINTENT = "web_search_pendingintent";
    public static final String CURSOR_EXTRA_KEY_IN_PROGRESS = "in_progress";
    public static final String ACTION_MSG = "action_msg";
    public static final int FLAG_QUERY_REFINEMENT = 1;
    public static final String SUGGEST_URI_PATH_QUERY = "search_suggest_query";
    public static final String SUGGEST_MIME_TYPE = "vnd.android.cursor.dir/vnd.android.search.suggest";
    public static final String SUGGEST_URI_PATH_SHORTCUT = "search_suggest_shortcut";
    public static final String SHORTCUT_MIME_TYPE = "vnd.android.cursor.item/vnd.android.search.suggest";
    public static final String SUGGEST_COLUMN_FORMAT = "suggest_format";
    public static final String SUGGEST_COLUMN_TEXT_1 = "suggest_text_1";
    public static final String SUGGEST_COLUMN_TEXT_2 = "suggest_text_2";
    public static final String SUGGEST_COLUMN_TEXT_2_URL = "suggest_text_2_url";
    public static final String SUGGEST_COLUMN_ICON_1 = "suggest_icon_1";
    public static final String SUGGEST_COLUMN_ICON_2 = "suggest_icon_2";
    public static final String SUGGEST_COLUMN_INTENT_ACTION = "suggest_intent_action";
    public static final String SUGGEST_COLUMN_INTENT_DATA = "suggest_intent_data";
    public static final String SUGGEST_COLUMN_INTENT_EXTRA_DATA = "suggest_intent_extra_data";
    public static final String SUGGEST_COLUMN_INTENT_DATA_ID = "suggest_intent_data_id";
    public static final String SUGGEST_COLUMN_QUERY = "suggest_intent_query";
    public static final String SUGGEST_COLUMN_SHORTCUT_ID = "suggest_shortcut_id";
    public static final String SUGGEST_COLUMN_SPINNER_WHILE_REFRESHING = "suggest_spinner_while_refreshing";
    public static final String SUGGEST_COLUMN_FLAGS = "suggest_flags";
    public static final String SUGGEST_COLUMN_LAST_ACCESS_HINT = "suggest_last_access_hint";
    public static final String SUGGEST_NEVER_MAKE_SHORTCUT = "_-1";
    public static final String SUGGEST_PARAMETER_LIMIT = "limit";
    public static final String INTENT_ACTION_GLOBAL_SEARCH = "android.search.action.GLOBAL_SEARCH";
    public static final String INTENT_ACTION_SEARCH_SETTINGS = "android.search.action.SEARCH_SETTINGS";
    public static final String INTENT_ACTION_WEB_SEARCH_SETTINGS = "android.search.action.WEB_SEARCH_SETTINGS";
    public static final String INTENT_ACTION_SEARCHABLES_CHANGED = "android.search.action.SEARCHABLES_CHANGED";
    public static final String INTENT_GLOBAL_SEARCH_ACTIVITY_CHANGED = "android.search.action.GLOBAL_SEARCH_ACTIVITY_CHANGED";
    public static final String INTENT_ACTION_SEARCH_SETTINGS_CHANGED = "android.search.action.SETTINGS_CHANGED";

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/SearchManager$OnCancelListener.class */
    public interface OnCancelListener {
        void onCancel();
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/SearchManager$OnDismissListener.class */
    public interface OnDismissListener {
        void onDismiss();
    }

    SearchManager() {
        throw new RuntimeException("Stub!");
    }

    public void startSearch(String initialQuery, boolean selectInitialQuery, ComponentName launchActivity, Bundle appSearchData, boolean globalSearch) {
        throw new RuntimeException("Stub!");
    }

    public ComponentName getGlobalSearchActivity() {
        throw new RuntimeException("Stub!");
    }

    public void triggerSearch(String query, ComponentName launchActivity, Bundle appSearchData) {
        throw new RuntimeException("Stub!");
    }

    public void stopSearch() {
        throw new RuntimeException("Stub!");
    }

    public void setOnDismissListener(OnDismissListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setOnCancelListener(OnCancelListener listener) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.DialogInterface.OnCancelListener
    @Deprecated
    public void onCancel(DialogInterface dialog) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.DialogInterface.OnDismissListener
    @Deprecated
    public void onDismiss(DialogInterface dialog) {
        throw new RuntimeException("Stub!");
    }

    public SearchableInfo getSearchableInfo(ComponentName componentName) {
        throw new RuntimeException("Stub!");
    }

    public List<SearchableInfo> getSearchablesInGlobalSearch() {
        throw new RuntimeException("Stub!");
    }
}
