package name.vampidroid;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import name.vampidroid.data.Card;

/**
 * Created by francisco on 11/09/17.
 */

public abstract class CardsListViewAdapter<VH extends RecyclerView.ViewHolder, C extends Card>
        extends RecyclerView.Adapter<VH> {

    protected List<C> cardList = null;


    @Override
    public int getItemCount() {
        return cardList == null ? 0 : cardList.size();
    }

    public void setCardList(final List<C> cardList) {
        this.cardList = cardList;
    }

    public List<C> getCardList() {
        return cardList;
    }


    public static class CardsListViewAdapterDiffCallback<C extends Card> extends DiffUtil.Callback {

        private final List<C> oldList;
        private final List<C> newList;

        public CardsListViewAdapterDiffCallback(List<C> oldList, List<C> newList) {

            this.oldList = oldList == null ? Collections.<C>emptyList() : oldList;
            this.newList = newList == null ? Collections.<C>emptyList() : newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getName().equals(newList.get(newItemPosition).getName());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getName().equals(newList.get(newItemPosition).getName());
        }
    }
}
