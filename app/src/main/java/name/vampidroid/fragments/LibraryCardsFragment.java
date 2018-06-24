package name.vampidroid.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import name.vampidroid.CardsViewModel;
import name.vampidroid.LibraryCardsListViewAdapter;
import name.vampidroid.R;
import name.vampidroid.data.LibraryCard;
import name.vampidroid.utils.CardsEvent;

public class LibraryCardsFragment extends ViewLifecycleFragment {

    private static final String TAG = "LibraryCardsFragment";
    private final LibraryCardsListViewAdapter libraryCardsListViewAdapter = new LibraryCardsListViewAdapter();


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated: ");

        CardsViewModel cardsViewModel = ViewModelProviders.of(this.getActivity()).get(CardsViewModel.class);

        cardsViewModel.getLibraryCardsLiveData().observe(this.getViewLifecycleOwner(), new Observer<PagedList<LibraryCard>>() {
            @Override
            public void onChanged(@Nullable PagedList<LibraryCard> cryptCards) {
                libraryCardsListViewAdapter.submitList(cryptCards);
            }
        });

        cardsViewModel.getNeedRefreshCardImagesLiveData().observe(this.getViewLifecycleOwner(), new Observer<CardsEvent>() {
            @Override
            public void onChanged(@Nullable CardsEvent cardsEvent) {
                if (cardsEvent.hasLibraryEventToHandle()) {
                    libraryCardsListViewAdapter.notifyDataSetChanged();
                }
            }

        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_cards_list, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.my_recycler_view);



        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        Log.d(TAG, "CardsListFragment: " + this);
        // specify an adapter (see also next example)

        recyclerView.setAdapter(libraryCardsListViewAdapter);

        return v;

    }
}
