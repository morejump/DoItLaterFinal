package raijin.doitlater.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import raijin.doitlater.R;
import raijin.doitlater.adapters.ListNoteAdapter;
import raijin.doitlater.database.RealmHandle;
import raijin.doitlater.database.models.NoteModel;
import raijin.doitlater.managers.ScreenManager;

/**
 * A simple {@link Fragment} subclass.
 */

/**
 * Created by Qk Lahpita on 7/23/2016.
 */
public class CompletedFragment extends Fragment implements FragmentWithSearch {
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private ListNoteAdapter completedNoteAdapter;
    private RealmHandle realmHandle;
    private boolean undoClicked = false;

    public static CompletedFragment create(ScreenManager screenManager) {
        CompletedFragment listNoteFragment = new CompletedFragment();
        return listNoteFragment;
    }

    public CompletedFragment() {
        realmHandle = RealmHandle.getInst();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list_note);
        setUpRecyclerView();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("Completed Tasks");


        ItemTouchHelper.Callback callback = new ListNoteTouchHelper();
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        return view;
    }

    private void setUpRecyclerView() {
        recyclerView.hasFixedSize();
        completedNoteAdapter = new ListNoteAdapter(realmHandle.getCompletedNoteFromRealm());
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
        recyclerView.setAdapter(completedNoteAdapter);
    }

    @Override
    public void doSearch(String searchString) {
        List<NoteModel> foundNoteModelList = realmHandle.findNoteByTitleOrDetail(searchString, true);
        if (this.completedNoteAdapter != null) {
            this.completedNoteAdapter.reloadData(foundNoteModelList);
        }
    }

    public class ListNoteTouchHelper extends ItemTouchHelper.SimpleCallback {

        public ListNoteTouchHelper() {
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //Not implemented here
            return false;
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            int ID = realmHandle.getCompletedNoteFromRealm().get(position).getID();
            String pathOfAudio = realmHandle.getCompletedNoteFromRealm().get(position).getPathOfAudio();
            String title = realmHandle.getCompletedNoteFromRealm().get(position).getTitle();
            String detail = realmHandle.getCompletedNoteFromRealm().get(position).getDetail();
            String location = realmHandle.getCompletedNoteFromRealm().get(position).getLocation();
            String timeReminder = realmHandle.getCompletedNoteFromRealm().get(position).getTimeReminder();
            String imagePath = realmHandle.getCompletedNoteFromRealm().get(position).getImagePath();
            boolean hasAudio = realmHandle.getCompletedNoteFromRealm().get(position).isHasAudio();
            String dateReminder = realmHandle.getCompletedNoteFromRealm().get(position).getDateReminder();
            boolean isCompleted = realmHandle.getCompletedNoteFromRealm().get(position).isCompleted();
            int requestCodeAlarm = realmHandle.getCompletedNoteFromRealm().get(position).getRequestCodeAlarm();
            String priority = realmHandle.getCompletedNoteFromRealm().get(position).getPriority();
            final NoteModel noteModel = new NoteModel(ID, pathOfAudio, title, detail, location, timeReminder, imagePath,
                    hasAudio, dateReminder, requestCodeAlarm, isCompleted, priority);
            realmHandle.deleteNoteFromRealm(realmHandle.getCompletedNoteFromRealm().get(position));
            completedNoteAdapter.notifyItemRemoved(position);
            Snackbar snackbar = Snackbar.make(getView(), "Deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            realmHandle.addNotetoRealm(noteModel);
                            recyclerView.removeAllViews();
                            setUpRecyclerView();
                        }
                    })
                    .setActionTextColor(Color.WHITE);
            snackbar.show();
        }
    }
}
