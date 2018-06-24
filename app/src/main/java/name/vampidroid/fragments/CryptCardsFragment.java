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
import name.vampidroid.CryptCardsListViewAdapter;
import name.vampidroid.R;
import name.vampidroid.data.CryptCard;
import name.vampidroid.utils.CardsEvent;

public class CryptCardsFragment extends ViewLifecycleFragment {

    private static final String TAG = "CryptCardsFragment";
    private final CryptCardsListViewAdapter cryptCardsListViewAdapter = new CryptCardsListViewAdapter();


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        CardsViewModel cardsViewModel = ViewModelProviders.of(this.getActivity()).get(CardsViewModel.class);

        cardsViewModel.getCryptCardsLiveData().observe(this.getViewLifecycleOwner(), new Observer<PagedList<CryptCard>>() {
            @Override
            public void onChanged(@Nullable PagedList<CryptCard> cryptCards) {
                cryptCardsListViewAdapter.submitList(cryptCards);
            }
        });

        cardsViewModel.getNeedRefreshCardImagesLiveData().observe(this.getViewLifecycleOwner(), new Observer<CardsEvent>() {
            @Override
            public void onChanged(@Nullable CardsEvent cardsEvent) {
                if (cardsEvent.hasCryptEventToHandle()) {
                    cryptCardsListViewAdapter.notifyDataSetChanged();
                }
            }

        });


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: ");

        View v = inflater.inflate(R.layout.fragment_cards_list, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));


        // specify an adapter (see also next example)

        recyclerView.setAdapter(cryptCardsListViewAdapter);

        return v;

    }
}
