package com.example.zlater.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lib.Deck;
import com.example.zlater.Model.Quote;
import com.example.zlater.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuotesFragment extends Fragment {
    private Deck deck_pager_quotes;
    private ArrayList<Quote> quotes;
    public QuotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quotes, container, false);
//        deck_pager_quotes = view.findViewById(R.id.deck_pager_quotes);
//        quotes = new ArrayList<>();
//        quotes.add(new Quote("""Có công mài sắt có ngày nên kim", "Quoc Phu"));
//        quotes.add(new Quote("", "Bàu ơi thương lấy bí cùng \n Tuy ràng khác giống nhưng chung 1 nồi", "Quoc Phu"));
//        quotes.add(new Quote("", "Cố gắng không ngừng nghỉ", "Quoc Phu 2"));
//        quotes.add(new Quote("", "Có công mài sắt có ngày nên kim", "Quoc Phu"));
//
//        QuotesDeckViewPager quotesDeckViewPager = new QuotesDeckViewPager(getChildFragmentManager(), quotes, getContext());
//        deck_pager_quotes.setAdapter(quotesDeckViewPager);
        return view;
    }

}

class QuotesDeckViewPager extends FragmentStatePagerAdapter {
    private ArrayList<Quote> quotes;
    private Context context;

    public QuotesDeckViewPager(FragmentManager fm, ArrayList<Quote> quotes, Context context) {
        super(fm);
        this.quotes = quotes;
        this.context = context;
    }

    public QuotesDeckViewPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Quote quote = quotes.get(position);
        return new CardQuoteFragment("", quote.getTitle(), quote.getImage_url());//TODO: Pass quote.getImage() to arg #1
    }

    @Override
    public int getCount() {
        return quotes.size();
    }
}
