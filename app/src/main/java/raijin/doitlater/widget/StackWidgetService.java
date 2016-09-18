package raijin.doitlater.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import raijin.doitlater.R;
import raijin.doitlater.database.RealmHandle;
import raijin.doitlater.database.models.NoteModel;

/**
 * Created by 1918 on 12-Sep-16.
 */
public class StackWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context context;
        private List<NoteModel> noteModelList;

        public StackRemoteViewsFactory(Context context, Intent intent) {
            this.context = context;
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            RealmHandle realmHandle = RealmHandle.getInst();
            noteModelList = realmHandle.getToDoNotefromRealm();
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            RealmHandle realmHandle = RealmHandle.getInst();
            noteModelList = realmHandle.getToDoNotefromRealm();
            return noteModelList.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_item);
            RealmHandle realmHandle = RealmHandle.getInst();
            noteModelList = realmHandle.getToDoNotefromRealm();
            if (i < getCount()) {
                rv.setTextViewText(R.id.widget_item_title, noteModelList.get(i).getTitle());
                rv.setTextViewText(R.id.widget_item_detail, noteModelList.get(i).getDetail());
                Intent fillInIntent = new Intent();
                rv.setOnClickFillInIntent(R.id.widget_item_layout, fillInIntent);
            }
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
