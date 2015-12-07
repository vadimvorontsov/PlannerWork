package ru.anroidapp.plannerwork.record.contact_choose.intface;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.smena.clientbase.procedures.Clients;
import com.example.smena.clientbase.procedures.Procedures;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.anroidapp.plannerwork.record.contact_choose.PinnedHeaderListView;
import ru.anroidapp.plannerwork.CircularImageView;
import ru.anroidapp.plannerwork.R;

public class PinnedHeaderAdapter extends BaseAdapter implements AbsListView.OnScrollListener, IPinnedHeader, Filterable {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SECTION = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SECTION + 1;

    LayoutInflater mLayoutInflater;
    int mCurrentSectionPosition = 0, mNextSectionPosition = 0;

    // array list to store section positions
    ArrayList<Integer> mListSectionPos;

    // array list to store list view data
    List<String> mListItems;

    // context object
    Context mContext;

    //private Clients clients;
//    private ArrayList<String> allClients;
//    private String lastChooseName;
//    int visits = 0;


    public PinnedHeaderAdapter(Context context, List<String> listItems,
                               ArrayList<Integer> listSectionPos) {
        this.mContext = context;
        this.mListItems = listItems;
        this.mListSectionPos = listSectionPos;
        //this.lastChooseName = lastChoose;

        //mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutInflater = LayoutInflater.from(mContext);

//        Clients clients = new Clients(mContext);
//        allClients = clients.getAllClientsNames();
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return !mListSectionPos.contains(position);
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return mListSectionPos.contains(position) ? TYPE_SECTION : TYPE_ITEM;
    }

    @Override
    public Object getItem(int position) {
        return mListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mListItems.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            int type = getItemViewType(position);

            switch (type) {
                case TYPE_ITEM:
                    convertView = mLayoutInflater.inflate(R.layout.contact_row_view, null);

                    holder.contactPhoto = (CircularImageView) convertView.findViewById(R.id.contact_circle);
                    Drawable drawable = mContext.getDrawable(R.drawable.ic_launcher);
                    holder.contactPhoto.setImageDrawable(drawable);
                    break;
                case TYPE_SECTION:
                    convertView = mLayoutInflater.inflate(R.layout.section_row_view, null);
                    break;
            }
            holder.textView = (TextView) convertView.findViewById(R.id.row_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.contactRow = convertView;
        String name = mListItems.get(position);
        holder.textView.setText(name);
        if (name.length() > 1) {
//            if (name == lastChooseName) {
//
////            } else {
////                holder.contactRow.setBackgroundColor(Color.WHITE);
//            }
            holder.visitsTextView = (TextView) convertView.findViewById(R.id.contact_status);

//            if (allClients.contains(name)) {
                int visits = getClientsVisits(name);
                if (visits > 0)
                    holder.visitsTextView.setText("Количество посещений " + visits);
                else
                    holder.visitsTextView.setText("Посещений не было");
//                allClients.remove(name);
//            }
        }
        return convertView;
    }

    @Override
    public int getPinnedHeaderState(int position) {
        // hide pinned header when items count is zero OR position is less than
        // zero OR
        // there is already a header in list view
        if (getCount() == 0 || position < 0 || mListSectionPos.indexOf(position) != -1) {
            return PINNED_HEADER_GONE;
        }

        // the header should get pushed up if the top item shown
        // is the last item in a section for a particular letter.
        mCurrentSectionPosition = getCurrentSectionPosition(position);
        mNextSectionPosition = getNextSectionPosition(mCurrentSectionPosition);
        if (mNextSectionPosition != -1 && position == mNextSectionPosition - 1) {
            return PINNED_HEADER_PUSHED_UP;
        }

        return PINNED_HEADER_VISIBLE;
    }

    public int getCurrentSectionPosition(int position) {
        String listChar = mListItems.get(position).toString().substring(0, 1).toUpperCase(Locale.getDefault());
        return mListItems.indexOf(listChar);
    }

    public int getNextSectionPosition(int currentSectionPosition) {
        int index = mListSectionPos.indexOf(currentSectionPosition);
        if ((index + 1) < mListSectionPos.size()) {
            return mListSectionPos.get(index + 1);
        }
        return mListSectionPos.get(index);
    }

    @Override
    public void configurePinnedHeader(View v, int position) {
        // set text in pinned header
        TextView header = (TextView) v;
        mCurrentSectionPosition = getCurrentSectionPosition(position);
        header.setText(mListItems.get(mCurrentSectionPosition));
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (view instanceof PinnedHeaderListView) {
            ((PinnedHeaderListView) view).configureHeaderView(firstVisibleItem);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public static class ViewHolder {
        public View contactRow;
        public TextView textView;
        public CircularImageView contactPhoto;
        public TextView visitsTextView;
    }

    private int getClientsVisits (String name) {
        Clients clients = new Clients(mContext);
        int visit = clients.getClientVisits(name);
        return visit;
    }

}